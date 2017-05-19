package at.ac.univie.swe2.SS2017.team403;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {
	
	private enum TOKEN {
        CELL,
        RANGE,

        EQUAL,
        NOT_EQUAL,
        NOT,
        BIGGER,
        BIGGER_EQUAL,
        SMALLER,
        SMALLER_EQUAL,
        AND,
        OR,
        IN,
        NOT_IN,
        LIKE,
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        BETWEEN,
        FUNCTION,
        POWER,

        NUMBER,
        //INTEGER,
        STRING,
        //DATE,
        TRUE,
        FALSE,

        RIGHT_BRACKET,
        LEFT_BRACKET,
        UNDEF,
        DELIMITER,
        CONCATENATE,
        COMMA,
        //REF,
        //LIST,
        ERROR,
        REMAINDER,
        PERIOD,
        EXPRESSION
	}
	
	private enum FUNCTION {
		SUM,
		MEAN,
		COUNT,
		UNDEF
	}

	private static class ExpressionParser {
	
	    private char[] charFormula;
	    private String formula;
	    private int pos;
	    private int len;
	    private Object data;
	    private TOKEN token = TOKEN.UNDEF;
	    private Cell cell;
	    
	    
        private static final String regExCellPattern = "(((\\w+)|('([\\w\\s]+)'))!)?"
	       		 + "R((\\d+)|(\\[([+-]?\\d+)\\]))?"
	       		 + "C((\\d+)|(\\[([+-]?\\d+)\\]))?";
        //"(\\[[\\w\\-. ]+\\])?(([?\\w+]?'?\\w+'?)!)?[\\$]?[A-Z]+[\\$]?[0-9]+($|(?=[\\s\\.\\#\\+\\-\\*\\/\\=\\~\\>\\<\\!\\|\\(\\)\\,\\;]))";
        private static final String regExRangePattern = "(((\\w+)|('([\\w\\s]+)'))!)?"
	       		 + "R((\\d+)|(\\[([+-]?\\d+)\\]))?"
	       		 + "C((\\d+)|(\\[([+-]?\\d+)\\]))?"
	       		 + ":R((\\d+)|(\\[([+-]?\\d+)\\]))?"
	       		 + "C((\\d+)|(\\[([+-]?\\d+)\\]))?"; 
        //"(\\[[\\w\\-. ]+\\])?(([?\\w+]?'?\\w+'?)!)?[\\$]?[A-Z]+[\\$]?[0-9]+:[\\$]?[A-Z]+[\\$]?[0-9]+($|(?=[\\s\\.\\#\\+\\-\\*\\/\\=\\~\\>\\<\\!\\|\\(\\)\\,\\;]))";

        private static final String regExBooleanPattern = "(TRUE|FALSE)";
        //"\\b(TRUE|FALSE)\\b($|(?=[\\s\\.\\#\\+\\-\\*\\/\\=\\~\\>\\<\\!\\|\\(\\)\\,\\;]))";

        private static final Pattern regExCell = Pattern.compile(regExCellPattern, Pattern.CASE_INSENSITIVE);
        private static final Pattern regExRange = Pattern.compile(regExRangePattern, Pattern.CASE_INSENSITIVE);
        private static final Pattern regExBool = Pattern.compile(regExBooleanPattern, Pattern.CASE_INSENSITIVE);

	
	    public ExpressionParser(String formula, Cell cell)
	    {
	    	this.cell = cell;
	        this.formula = formula;
	        this.charFormula = this.formula.toCharArray();
	        this.len = this.formula.length();
	        this.pos = 1;
	    }
	    
	    private void _readToken()
	    {
	        if (pos >= len)
	        {
	            token = TOKEN.UNDEF;
	            return;
	        }
	
	        while (pos < len && charFormula[pos] == ' ')
	            pos++;
	
	        data = null;
	        token = TOKEN.UNDEF;
	
	        {
		        Matcher matcher = regExRange.matcher(formula.substring(pos));
		        if (matcher.find() && (matcher.start() == 0))
	            {
	                token = TOKEN.RANGE;	
	                pos += matcher.end();
	                data = Range.getRangeByAddress(matcher.group(0), cell);
	            }
	        }


            if (token == TOKEN.UNDEF)
            {
            	Matcher matcher = regExCell.matcher(formula.substring(pos));
            	if (matcher.find() && (matcher.start() == 0))
                {
                    token = TOKEN.CELL;

                    pos += matcher.end();
	                data = Range.getRangeByAddress(matcher.group(0), cell).getCells().first();
                }
            }

            
	        if (token == TOKEN.UNDEF && Character.isDigit(charFormula[pos]))
	        {
	            token = TOKEN.NUMBER;
	        }
	        else if (token == TOKEN.UNDEF)
	        {
	            switch (charFormula[pos])
	            {
	                case '"': pos++; token = TOKEN.STRING; break;
	                case '.': pos++; token = TOKEN.NUMBER; break;
	                //case '#': pos++; token = TOKEN.DATE; break;
	                case ',': pos++; token = TOKEN.DELIMITER; break;
	                case '+': pos++; token = TOKEN.PLUS; break;
	                case '-': pos++; token = TOKEN.MINUS; break;
	                case '&':
	                    if (charFormula[pos + 1] != '&')
	                    { pos++; token = TOKEN.CONCATENATE; }
	                    break;
	                case '*': pos++; token = TOKEN.MULTIPLY; break;
	                case '/': pos++; token = TOKEN.DIVIDE; break;
	                case '=': pos++; token = TOKEN.EQUAL; break;
	                case '(': pos++; token = TOKEN.LEFT_BRACKET; break;
	                case ')': pos++; token = TOKEN.RIGHT_BRACKET; break;
	                case '^': pos++; token = TOKEN.POWER; break;
	                case '%': pos++; token = TOKEN.REMAINDER; break;
	                case '~': pos++; token = TOKEN.NOT; break;
	                case '>':
	                    if (pos + 1 < len && charFormula[pos + 1] == '=')
	                    {
	                        pos += 2; token = TOKEN.BIGGER_EQUAL;
	                    }
	                    else
	                    {
	                        pos++; token = TOKEN.BIGGER;
	                    }
	                    break;
	                case '<':
	                    if (pos + 1 < len && (charFormula[pos + 1] == '=') || (charFormula[pos + 1] == '>'))
	                    {
	                        if (charFormula[pos + 1] != '>')
	                        { pos += 2; token = TOKEN.SMALLER_EQUAL; }
	                    }
	                    else
	                    {
	                        pos++; token = TOKEN.SMALLER;
	                    }
	                    break;
	            }
	
	            if (pos + 1 < len && token == TOKEN.UNDEF)
	            {
	                switch (formula.substring(pos, pos + 2))
	                {
	                    case "||": pos += 2; token = TOKEN.OR; break;
	                    case "&&": pos += 2; token = TOKEN.AND; break;
	                    case "<>":
	                    case "!=": pos += 2; token = TOKEN.NOT_EQUAL; break;
	                }
	
	                if (token == TOKEN.UNDEF && pos + 8 < len && (formula.substring(pos, pos + 8).toUpperCase() == "BETWEEN " || formula.substring(pos, pos + 8).toUpperCase() == "BETWEEN("))
	                {
	                    pos += 7; token = TOKEN.BETWEEN;
	                }
	
	
	                if (token == TOKEN.UNDEF && pos + 3 < len && (formula.substring(pos, pos + 3).toUpperCase() == "IN(" || formula.substring(pos, pos + 3).toUpperCase() == "IN "))
	                {
	                    pos += 2; token = TOKEN.IN;
	                }
	
	                if (token == TOKEN.UNDEF && pos + 5 < len && (formula.substring(pos, pos + 5).toUpperCase() == "LIKE " || formula.substring(pos, pos + 5).toUpperCase() == "LIKE\""))
	                {
	                    pos += 4; token = TOKEN.LIKE;
	                }
	
	                if (token == TOKEN.UNDEF && pos + 4 < len && (formula.substring(pos, pos + 5).toUpperCase() == "NOT " || formula.substring(pos, pos + 5).toUpperCase() == "NOT("))
	                {
	                    pos += 3; token = TOKEN.NOT;
	                }
	            }
	        }
	
	        if (token == TOKEN.UNDEF)
	        {
	        	Matcher matcher = regExBool.matcher(formula.substring(pos));
	            if (matcher.find() && (matcher.start() == 0) )
                {
                    pos += matcher.end();
                    int tmpPos = pos;
                    _readToken();
                    if (token == TOKEN.LEFT_BRACKET)
                    {
                        _readToken();
                        _readThis(TOKEN.RIGHT_BRACKET, "TRUE/FALSE-function, expected )");
                    }
                    else
                        pos = tmpPos;
                    token = (matcher.group(0).toUpperCase() == "TRUE") ? TOKEN.TRUE : TOKEN.FALSE;
                }
	        }
	
	        if (token == TOKEN.NUMBER || token == TOKEN.STRING)
	        {
	            switch (token)
	            {
	                case NUMBER:
	                {
	                    double d = _readNumber();
                        token = TOKEN.NUMBER;
                        data = d;
	                    break;
	            	}
	                case STRING:
	                {
	                    data = _readString();
	                    break;
	                }
	            }
	        }
	
	        if (token == TOKEN.UNDEF)
	        {
	
	        	FUNCTION _func = _readFunctionToken();
	            if (_func != FUNCTION.UNDEF)
	            {
	                data = _func;
	                token = TOKEN.FUNCTION;
	            }
	        }
	    }
	    
	    private void _readThis(TOKEN nextToken, String err) throws IllegalArgumentException
	    {
	        if (token != nextToken)
	        	throw new IllegalArgumentException(err + ": " + this.token.toString() + "," + this.pos + "," + this.formula);
	    }
	
	    private double _readNumber()
	    {
	        int _start = pos;
	        while (pos < len && (Character.isDigit(this.charFormula[pos]) || this.charFormula[pos] == '.'))
	            pos++;
	
	        String _s = new String(this.charFormula, _start, pos - _start);
	
	        return Double.valueOf(_s);
	    }
	    
        private String _readString()
        {
            int _p = pos;
            int _p2 = formula.indexOf('"', _p);
            while (_p2 == formula.indexOf("\"\"", _p))
            {
                _p = _p2 + 2;
                _p2 = formula.indexOf('"', _p);
            }

            _p = pos;
            pos = _p2 + 1;

            return formula.substring(_p, _p2).replace("\"\"", "\"");
        }
	
        private FUNCTION _readFunctionToken()
        {
            if (pos + 5 < len)
                switch (formula.substring(pos, pos + 5))
                {
                    case "COUNT": pos += 5; return FUNCTION.COUNT;
                }

            if (pos + 4 < len)
                switch (formula.substring(pos, pos + 4))
                {
                    case "MEAN": pos += 4; return FUNCTION.MEAN;
                }

            if (pos + 3 < len)
                switch (formula.substring(pos, pos + 3))
                {
                    case "SUM": pos += 3; return FUNCTION.SUM;
                }

            return FUNCTION.UNDEF;
        }

        private List<Expression> _readFunction()
        {
        	FUNCTION _func = (FUNCTION)data;
            _readToken();
            _readThis(TOKEN.LEFT_BRACKET, "must be ( after function name");

            List<Expression> ret = new ArrayList<Expression>();

            _readToken();

            switch (_func)
            {
                case COUNT: 
                case SUM:
                case MEAN:

                	_readThis(TOKEN.RANGE, "param for functions must be range");
                    ret.add(_readOr());
                    while (token != TOKEN.RIGHT_BRACKET)
                    {
                        _readThis(TOKEN.DELIMITER, "must be , after function param");
                        _readToken();

                        _readThis(TOKEN.RANGE, "param must be range");
                        ret.add(_readOr());                                        
                    }

                    break;
            }

            _readThis(TOKEN.RIGHT_BRACKET, "must be ) after function params");
            return ret;
        }

	    private Expression _readOr()
	    {
	        Expression r = _readAnd();
	
	        while (token == TOKEN.OR)
	        {
	            TOKEN _t = token;
	            Expression a = r;
	
	            _readToken();
	
	            r = new Expression(_t, a, _readAnd());
	        }
	
	        return r;
	    }
	
	    private Expression _readAnd()
	    {
	        Expression r = _readCondition();
	
	        while (token == TOKEN.AND)
	        {
	            TOKEN _t = token;
	            Expression a = r;
	
	            _readToken();
	
	            r = new Expression(_t, a, _readCondition());
	        }
	
	        return r;
	    }
	
	    private Expression _readCondition() throws IllegalArgumentException
	    {
	        if (token == TOKEN.NOT)
	        {
	            _readToken();
	
	            Expression ret = _readCondition();
	
	            if (ret.token == TOKEN.IN)
	                ret.token = TOKEN.NOT_IN;
	            else
	                ret = new Expression(TOKEN.NOT, ret, null);
	
	            return ret;
	        }
	        else
	        {
	            Expression a = _readExpConcat();
	            boolean not = false;
	
	            if (token == TOKEN.NOT)
	            {
	                not = true;
	
	                _readToken();
	            }
	
	            if (token == TOKEN.LIKE)
	            {
	                Expression b = _readExpConcat();
	
	                a = new Expression(TOKEN.LIKE, a, b);
	            }
	            else if (token == TOKEN.BETWEEN)
	            {
	                Expression l = new Expression(TOKEN.BIGGER_EQUAL, a,
	                    _readExpConcat());
	
	                _readThis(TOKEN.AND, "must be AND in BETWEEN");
	
	                Expression h = new Expression(TOKEN.SMALLER_EQUAL, a,
	                    _readExpConcat());
	
	                a = new Expression(TOKEN.AND, l, h);
	            }
	            else if (token == TOKEN.IN || token == TOKEN.NOT_IN)
	            {
	                TOKEN baseToken = token;
	                _readToken();
	                _readThis(TOKEN.LEFT_BRACKET, "after IN must be (");
	
	                Expression b = new Expression();
	
	                while (true)
	                {
	                    _readToken();
	                    b.expressions.add(_readExpConcat());
	
	                    if (token != TOKEN.DELIMITER)
	                        break;
	                }
	
	                _readThis(TOKEN.RIGHT_BRACKET, "after IN and ( must be )");
	
	                a = new Expression(baseToken, a, b);
	            }
	            else if (token == TOKEN.EQUAL || token == TOKEN.BIGGER_EQUAL || token == TOKEN.BIGGER || token == TOKEN.SMALLER || token == TOKEN.SMALLER_EQUAL || token == TOKEN.NOT_EQUAL)
	            {
	                if (not)
	                    throw new IllegalArgumentException ("not can not be used with =,<>,!=,>,<,<=,>=" + this.token.toString() + "," + this.pos + "," + this.formula);
	
	                TOKEN _t = token;
	
	                _readToken();
	
	                return new Expression(_t, a, _readExpConcat());
	            }
	
	            if (not)
	                a = new Expression(TOKEN.NOT, a, null);
	
	            return a;
	        }
	    }
	
	    private Expression _readExpConcat()
	    {
	        Expression r = _readSum();
	
	        if (token == TOKEN.REMAINDER)
	        {
	            Expression a = r;
	
	            _readToken();
	
	            r = new Expression(TOKEN.REMAINDER, a, _readOr());
	        }
	        else
	        {
	            while (token == TOKEN.CONCATENATE || token == TOKEN.POWER)
	            {
	                TOKEN _tok = token;
	                Expression a = r;
	
	                _readToken();
	
	                r = new Expression(_tok, a, _readOr());
	            }
	        }
	
	        return r;
	    }
	
	    private Expression _readSum()
	    {
	        Expression r = _readFactor();
	
	        while (true)
	        {
	            if (token != TOKEN.PLUS && token != TOKEN.MINUS)
	                break;
	
	            TOKEN _t = token;
	            Expression a = r;
	
	            _readToken();
	
	            r = new Expression(_t, a, _readFactor());
	        }
	
	        return r;
	    }
	
	    private Expression _readFactor()
	    {
	        Expression r = _readTerm();
	
	        while (token == TOKEN.MULTIPLY || token == TOKEN.DIVIDE)
	        {
	            TOKEN _t = token;
	            Expression a = r;
	
	            _readToken();
	
	            r = new Expression(_t, a, _readTerm());
	        }
	
	        return r;
	    }
	
	    private Expression _readTerm() throws IllegalArgumentException
	    {
	        Expression r = null;
	
	        switch (token)
	        {
	            case MINUS:
	                _readToken();
	                r = new Expression(TOKEN.MINUS, _readTerm());
	                break;
	            case PLUS:
	                _readToken();
	                r = _readTerm();
	                break;
	            case LEFT_BRACKET:
	                _readToken();
	
	                r = _readOr();
	
	                if (token != TOKEN.RIGHT_BRACKET)
	                    throw new IllegalArgumentException("unexpected element, must be )" + this.token.toString() + "," + this.pos + "," + this.formula);
	
	                break;
	            case NUMBER:
	                r = new Expression((double)data); _readToken(); break;
	            case STRING:
	                r = new Expression((String)data); _readToken(); break;
	            case TRUE:
	                r = new Expression(true); _readToken(); break;
	            case FALSE:
	                r = new Expression(false); _readToken(); break;
	            case FUNCTION:
	                r = new Expression((FUNCTION)data); r.expressions.addAll(_readFunction()); _readToken(); break;
	            case CELL:
	                r = new Expression(token, (Cell)data); _readToken(); break;
	            case RANGE:
	                r = new Expression(token, (Range)data); _readToken(); break;
	            default:
	                throw new IllegalArgumentException("unexpected element " + token.toString() + ": " + this.token.toString() + "," + this.pos + "," + this.formula);
	        }
	
	        return r;
	    }
	};

    public Expression()
    {
        dataType = DataType.General;
    }
    
    public Expression(TOKEN token, Object value)
    {
        this.token = token;
        if (token != TOKEN.CELL && token != TOKEN.ERROR && token != TOKEN.EXPRESSION && token != TOKEN.UNDEF)
        {
            switch (token)
            {
            	case RANGE: data = (Range)value; this.dataType = DataType.RANGE; break;
                case NUMBER: data = Double.valueOf(value.toString()); this.dataType = DataType.Number; break;
                case STRING: data = value.toString(); this.dataType = DataType.String; break;
                default:
                	this.dataType = DataType.General;
            }
        }
    }

    //copy constructor
    public Expression(Expression e1)
    {
        this.token = e1.token;
        this.dataType = e1.dataType;
        this.data = e1.data;
        if (e1.expressions != null)
        {
            expressions = new ArrayList<Expression>();
            for (Expression ex : e1.expressions)
                expressions.add(new Expression(ex));
        }
    }
    
    //unary operation constructor
    public Expression(TOKEN token, Expression e)
    {
        this.token = token;
        dataType = DataType.General;
        expressions = new ArrayList<Expression>();
        expressions.add(e);
    }

    //binary operation constructor
    public Expression(TOKEN token, Expression e1, Expression e2)
    {
        this.token = token;
        expressions = new ArrayList<Expression>();
        expressions.add(e1);
        expressions.add(e2);
        dataType = DataType.General;
    }

    //string constant constructor
    public Expression(String v)
    {
        token = TOKEN.STRING;
        dataType = DataType.String;
        data = v;
    }

    //boolean constant constructor
    public Expression(boolean v)
    {
        token = (v) ? TOKEN.TRUE : TOKEN.FALSE;
        dataType = DataType.Boolean;
    }

    //double constant constructor
    public Expression(double v)
    {
        token = TOKEN.NUMBER;
        dataType = DataType.Number;
        data = v;
    }

    //function constructor
    public Expression(FUNCTION v)
    {
        token = TOKEN.FUNCTION;
        data = v;
        //dataType = ExpressionParser._defineFunctionTypes(this, false); ;
    }
    
    
    
	public Object getValue()
	{
        switch (this.token)
        {
            case NUMBER:
                return (double)this.data;

            case PLUS:
                return this.expressions.get(0).getNumericValue() + this.expressions.get(1).getNumericValue();

            case MINUS:
                return this.expressions.get(0).getNumericValue() - this.expressions.get(1).getNumericValue();

            case MULTIPLY:
                return this.expressions.get(0).getNumericValue() * this.expressions.get(1).getNumericValue();

            case DIVIDE:
                double ex2 = this.expressions.get(1).getNumericValue();
                if (ex2 == 0)
        			throw new IllegalArgumentException("divide by 0");
                return this.expressions.get(0).getNumericValue() / ex2;

            case POWER:
                return Math.pow(this.expressions.get(0).getNumericValue(), this.expressions.get(1).getNumericValue());

            case REMAINDER:
                return this.expressions.get(0).getNumericValue() % this.expressions.get(1).getNumericValue();

            case FUNCTION:
            	
            	FunctionRangeStrategy strategy;
                switch ((FUNCTION)this.data)
                {
	                case SUM:
	                	strategy = new FunctionSUM();
	                	break;
	
	                case COUNT:
	                	strategy = new FunctionCOUNT();
	                	break;
	
	                case MEAN:
	                	strategy = new FunctionMEAN();
	                	break;
                	
                	default:
                		strategy = null;
                		break;
                }
                
                return strategy.calculate((Range)this.expressions.get(0).data);
        }
        
        throw new RuntimeException("Expression token " + this.token.toString() + " is not defined");
	}

	public double getNumericValue()
	{ return (double)getValue(); }

	public String getTextValue()
	{ return (String)getValue(); }

	
	private TOKEN token = TOKEN.UNDEF;
    private DataType dataType;
    private ArrayList<Expression> expressions = new ArrayList<Expression>();
    private Object data = null;
    
    public DataType getDataType() {
    	return dataType;
    }

    public void setDataType(DataType value) {
    	dataType = value;
    }
    
    public static Expression parse(Cell cell, String formula) throws IllegalArgumentException
    {
    	Expression.ExpressionParser parser = new Expression.ExpressionParser(formula, cell);
    	parser._readToken();
    	Expression ret = parser._readOr();
    	if (parser.pos != formula.length() || parser.token != TOKEN.UNDEF)
        	throw new IllegalArgumentException(formula + " Formula has false structure");

    	
    	//remove dependency, if exists
    	Application.getActiveWorkbook().removeDependancy(cell);
    	
    	//add new dependency
    	addDependency(cell, ret);
    	
    	return ret;
    }
    
    private static void addDependency(Cell cell, Expression ex)
    {
    	if (ex.token == TOKEN.RANGE) {
    		Range r = (Range)ex.data;
    		for (Area a : r.getAreas())
    			Application.getActiveWorkbook().addDependency(cell, ex, a);

    		for (Cell c : r.getCells())
    			Application.getActiveWorkbook().addDependency(cell, ex, c);
    	}
    	else if (ex.token == TOKEN.CELL) { 
    		Application.getActiveWorkbook().addDependency(cell, ex, (Cell)ex.data);
    	}
    	
    	if (ex.expressions != null) {
    		for(Expression e : ex.expressions)
    			addDependency(cell, e);
    	}
    }
    
}
