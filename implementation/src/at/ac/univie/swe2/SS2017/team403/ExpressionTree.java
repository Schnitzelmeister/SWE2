package at.ac.univie.swe2.SS2017.team403;



import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.ac.univie.swe2.SS2017.team403.Cell;
import at.ac.univie.swe2.SS2017.team403.Range;


/**
 * 
 *The class will used for the expression in a cell
 *
 */
public class ExpressionTree {
	
	/**
	 * 
	 * The class Token is for the labeling
	 *
	 */
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
		private String cellFormula;
		private int charPosition;
		private int formulaLength;
		private Object data;
		private TOKEN token = TOKEN.UNDEF;
		private Cell cell;

		private static final String regExCellPattern = "(((\\w+)|('([\\w\\s]+)'))!)?" + "R((\\d+)|(\\[([+-]?\\d+)\\]))?"
				+ "C((\\d+)|(\\[([+-]?\\d+)\\]))?";
			// "(\\[[\\w\\-.
				// ]+\\])?(([?\\w+]?'?\\w+'?)!)?[\\$]?[A-Z]+[\\$]?[0-9]+($|(?=[\\s\\.\\#\\+\\-\\*\\/\\=\\~\\>\\<\\!\\|\\(\\)\\,\\;]))";
		private static final String regExRangePattern = "(((\\w+)|('([\\w\\s]+)'))!)?"
				+ "R((\\d+)|(\\[([+-]?\\d+)\\]))?" + "C((\\d+)|(\\[([+-]?\\d+)\\]))?"
				+ ":R((\\d+)|(\\[([+-]?\\d+)\\]))?" + "C((\\d+)|(\\[([+-]?\\d+)\\]))?"

				+ "(;(((\\w+)|('([\\w\\s]+)'))!)?" + "R((\\d+)|(\\[([+-]?\\d+)\\]))?" + "C((\\d+)|(\\[([+-]?\\d+)\\]))?"
				+ ":R((\\d+)|(\\[([+-]?\\d+)\\]))?" + "C((\\d+)|(\\[([+-]?\\d+)\\]))?)?";
			// "(\\[[\\w\\-.
				// ]+\\])?(([?\\w+]?'?\\w+'?)!)?[\\$]?[A-Z]+[\\$]?[0-9]+:[\\$]?[A-Z]+[\\$]?[0-9]+($|(?=[\\s\\.\\#\\+\\-\\*\\/\\=\\~\\>\\<\\!\\|\\(\\)\\,\\;]))";

		private static final String regExBooleanPattern = "(TRUE|FALSE)";
			// "\\b(TRUE|FALSE)\\b($|(?=[\\s\\.\\#\\+\\-\\*\\/\\=\\~\\>\\<\\!\\|\\(\\)\\,\\;]))";

		private static final Pattern regExCell = Pattern.compile(regExCellPattern, Pattern.CASE_INSENSITIVE);
		private static final Pattern regExRange = Pattern.compile(regExRangePattern, Pattern.CASE_INSENSITIVE);
		private static final Pattern regExBool = Pattern.compile(regExBooleanPattern, Pattern.CASE_INSENSITIVE);

		/**
		 * the constructor has a call and the contains of the cell(formula) as
		 * input parameters and initializes the instances.
		 * 
		 * @param formula
		 *            the contains of a cell
		 * @param cell
		 *            the cell with formula
		 */
		public ExpressionParser(String formula, Cell cell) {
			this.cell = cell;
			this.cellFormula = formula;
			this.charFormula = this.cellFormula.toCharArray();
			this.formulaLength = this.cellFormula.length();
			this.charPosition = 1;
		}

		/**
		 * At the beginning a Token is undefined if charPoition is same or
		 * greater than formulaLength. If charPosition is smaller than
		 * formulaLength, charPosition is incremented by every loop cycle.
		 * 
		 */
		private void _readToken() {
			if (charPosition >= formulaLength) {
				token = TOKEN.UNDEF;
				return;
			}

			while (charPosition < formulaLength && charFormula[charPosition] == ' ')
				charPosition++;

			data = null;
			token = TOKEN.UNDEF;

			{
				Matcher matcher = regExRange.matcher(cellFormula.substring(charPosition));
				if (matcher.find() && (matcher.start() == 0)) {
					token = TOKEN.RANGE;
					charPosition += matcher.end();
					data = Range.getRangeByAddress(matcher.group(0), cell, Application.getActiveWorkbook());
				}
			}

			if (token == TOKEN.UNDEF) {
				Matcher matcher = regExCell.matcher(cellFormula.substring(charPosition));
				if (matcher.find() && (matcher.start() == 0)) {
					token = TOKEN.CELL;
					charPosition += matcher.end();
					data = Range.getRangeByAddress(matcher.group(0), cell, Application.getActiveWorkbook()).getWorksheetCells().firstKey();
				}
			}

			if (token == TOKEN.UNDEF && Character.isDigit(charFormula[charPosition])) {
				token = TOKEN.NUMBER;
			} else if (token == TOKEN.UNDEF) {
				switch (charFormula[charPosition]) {
					case '"':
						charPosition++;
						token = TOKEN.STRING;
						break;
					case '.':
						charPosition++;
						token = TOKEN.NUMBER;
						break;
					// case '#': pos++; token = TOKEN.DATE; break;
					case ',':
						charPosition++;
						token = TOKEN.DELIMITER;
						break;
					case '+':
						charPosition++;
						token = TOKEN.PLUS;
						break;
					case '-':
						charPosition++;
						token = TOKEN.MINUS;
						break;
					case '&':
						if (charFormula[charPosition + 1] != '&') {
							charPosition++;
							token = TOKEN.CONCATENATE;
						}
						break;
					case '*':
						charPosition++;
						token = TOKEN.MULTIPLY;
						break;
					case '/':
						charPosition++;
						token = TOKEN.DIVIDE;
						break;
					case '=':
						charPosition++;
						token = TOKEN.EQUAL;
						break;
					case '(':
						charPosition++;
						token = TOKEN.LEFT_BRACKET;
						break;
					case ')':
						charPosition++;
						token = TOKEN.RIGHT_BRACKET;
						break;
					case '^':
						charPosition++;
						token = TOKEN.POWER;
						break;
					case '%':
						charPosition++;
						token = TOKEN.REMAINDER;
						break;
					case '~':
						charPosition++;
						token = TOKEN.NOT;
						break;
					case '>':
						if (charPosition + 1 < formulaLength && charFormula[charPosition + 1] == '=') {
							charPosition += 2;
							token = TOKEN.BIGGER_EQUAL;
						} else {
							charPosition++;
							token = TOKEN.BIGGER;
						}
						break;
					case '<':
						if (charPosition + 1 < formulaLength && (charFormula[charPosition + 1] == '=')
								|| (charFormula[charPosition + 1] == '>')) {
							if (charFormula[charPosition + 1] != '>') {
								charPosition += 2;
								token = TOKEN.SMALLER_EQUAL;
							}
						} else {
							charPosition++;
							token = TOKEN.SMALLER;
						}
						break;
				}

				if (charPosition + 1 < formulaLength && token == TOKEN.UNDEF) {
					switch (cellFormula.substring(charPosition, charPosition + 2)) {
						case "||":
							charPosition += 2;
							token = TOKEN.OR;
							break;
						case "&&":
							charPosition += 2;
							token = TOKEN.AND;
							break;
						case "<>":
						case "!=":
							charPosition += 2;
							token = TOKEN.NOT_EQUAL;
							break;
						}

					if (token == TOKEN.UNDEF && charPosition + 8 < formulaLength
							&& (cellFormula.substring(charPosition, charPosition + 8).toUpperCase() == "BETWEEN "
									|| cellFormula.substring(charPosition, charPosition + 8)
											.toUpperCase() == "BETWEEN(")) {
						charPosition += 7;
						token = TOKEN.BETWEEN;
					}

					if (token == TOKEN.UNDEF && charPosition + 3 < formulaLength
							&& (cellFormula.substring(charPosition, charPosition + 3).toUpperCase() == "IN("
									|| cellFormula.substring(charPosition, charPosition + 3).toUpperCase() == "IN ")) {
						charPosition += 2;
						token = TOKEN.IN;
					}

					if (token == TOKEN.UNDEF && charPosition + 5 < formulaLength
							&& (cellFormula.substring(charPosition, charPosition + 5).toUpperCase() == "LIKE "
									|| cellFormula.substring(charPosition, charPosition + 5)
											.toUpperCase() == "LIKE\"")) {
						charPosition += 4;
						token = TOKEN.LIKE;
					}

					if (token == TOKEN.UNDEF && charPosition + 4 < formulaLength
							&& (cellFormula.substring(charPosition, charPosition + 5).toUpperCase() == "NOT "
									|| cellFormula.substring(charPosition, charPosition + 5).toUpperCase() == "NOT(")) {
						charPosition += 3;
						token = TOKEN.NOT;
					}
				}
			}

			if (token == TOKEN.UNDEF) {
				Matcher matcher = regExBool.matcher(cellFormula.substring(charPosition));
				if (matcher.find() && (matcher.start() == 0)) {
					charPosition += matcher.end();
					int tmpPos = charPosition;
					_readToken();
					
					if (token == TOKEN.LEFT_BRACKET) {
						_readToken();
						_readThis(TOKEN.RIGHT_BRACKET, "TRUE/FALSE-function, expected )");
					} else
						charPosition = tmpPos;
					token = (matcher.group(0).toUpperCase() == "TRUE") ? TOKEN.TRUE : TOKEN.FALSE;
				}
			}

			if (token == TOKEN.NUMBER || token == TOKEN.STRING) {
				switch (token) {
					case NUMBER: {
						double d = _readNumber();
						token = TOKEN.NUMBER;
						data = d;
						break;
					}
					case STRING: {
						data = _readString();
						break;
					}
				}
			}

			if (token == TOKEN.UNDEF) {

				FUNCTION _func = _readFunctionToken();
				if (_func != FUNCTION.UNDEF) {
					data = _func;
					token = TOKEN.FUNCTION;
				}
			}
		}

		private void _readThis(TOKEN nextToken, String err) throws IllegalArgumentException {
			if (token != nextToken)
				throw new IllegalArgumentException(
						err + ": " + this.token.toString() + "," + this.charPosition + "," + this.cellFormula);
		}

		private double _readNumber() {
			int _start = charPosition;
			while (charPosition < formulaLength
					&& (Character.isDigit(this.charFormula[charPosition]) || this.charFormula[charPosition] == '.'))
				charPosition++;

			String _s = new String(this.charFormula, _start, charPosition - _start);

			return Double.valueOf(_s);
		}

		private String _readString() {
			int _p = charPosition;
			int _p2 = cellFormula.indexOf('"', _p);
			while (_p2 == cellFormula.indexOf("\"\"", _p)) {
				_p = _p2 + 2;
				_p2 = cellFormula.indexOf('"', _p);
			}

			_p = charPosition;
			charPosition = _p2 + 1;

			return cellFormula.substring(_p, _p2).replace("\"\"", "\"");
		}

		private FUNCTION _readFunctionToken() {
			if (charPosition + 5 < formulaLength)
				switch (cellFormula.substring(charPosition, charPosition + 5).toUpperCase()) {
					case "COUNT":
						charPosition += 5;
						return FUNCTION.COUNT;
					}

			if (charPosition + 4 < formulaLength)
				switch (cellFormula.substring(charPosition, charPosition + 4).toUpperCase()) {
					case "MEAN":
						charPosition += 4;
						return FUNCTION.MEAN;
					}

			if (charPosition + 3 < formulaLength)
				switch (cellFormula.substring(charPosition, charPosition + 3).toUpperCase()) {
					case "SUM":
						charPosition += 3;
						return FUNCTION.SUM;
					}

			return FUNCTION.UNDEF;
		}

		private List<ExpressionTree> _readFunction() {
			FUNCTION _func = (FUNCTION) data;
			_readToken();
			_readThis(TOKEN.LEFT_BRACKET, "must be ( after function name");

			List<ExpressionTree> ret = new ArrayList<ExpressionTree>();

			_readToken();

			switch (_func) {
				case COUNT:
				case SUM:
				case MEAN:
	
					_readThis(TOKEN.RANGE, "param for functions must be range");
					ret.add(_readOr());
					while (token != TOKEN.RIGHT_BRACKET) {
						_readThis(TOKEN.DELIMITER, "must be , after function param");
						_readToken();
	
						_readThis(TOKEN.RANGE, "param must be range");
						ret.add(_readOr());
					}
	
					break;
				default:
					throw new IllegalArgumentException("Undefined formula " + _func);

			}

			_readThis(TOKEN.RIGHT_BRACKET, "must be ) after function params");
			return ret;
		}

		private ExpressionTree _readOr() {
			ExpressionTree r = _readAnd();

			while (token == TOKEN.OR) {
				TOKEN _t = token;
				ExpressionTree a = r;

				_readToken();

				r = new ExpressionTree(_t, a, _readAnd());
			}

			return r;
		}

		private ExpressionTree _readAnd() {
			ExpressionTree r = _readCondition();

			while (token == TOKEN.AND) {
				TOKEN _t = token;
				ExpressionTree a = r;

				_readToken();

				r = new ExpressionTree(_t, a, _readCondition());
			}

			return r;
		}

		private ExpressionTree _readCondition() throws IllegalArgumentException {
			if (token == TOKEN.NOT) {
				_readToken();

				ExpressionTree ret = _readCondition();

				if (ret.token == TOKEN.IN)
					ret.token = TOKEN.NOT_IN;
				else
					ret = new ExpressionTree(TOKEN.NOT, ret, null);

				return ret;
			} else {
				ExpressionTree a = _readExpConcat();
				boolean not = false;

				if (token == TOKEN.NOT) {
					not = true;

					_readToken();
				}

				if (token == TOKEN.LIKE) {
					ExpressionTree b = _readExpConcat();

					a = new ExpressionTree(TOKEN.LIKE, a, b);
				} else if (token == TOKEN.BETWEEN) {
					ExpressionTree l = new ExpressionTree(TOKEN.BIGGER_EQUAL, a, _readExpConcat());

					_readThis(TOKEN.AND, "must be AND in BETWEEN");

					ExpressionTree h = new ExpressionTree(TOKEN.SMALLER_EQUAL, a, _readExpConcat());

					a = new ExpressionTree(TOKEN.AND, l, h);
				} else if (token == TOKEN.IN || token == TOKEN.NOT_IN) {
					TOKEN baseToken = token;
					_readToken();
					_readThis(TOKEN.LEFT_BRACKET, "after IN must be (");

					ExpressionTree b = new ExpressionTree();

					while (true) {
						_readToken();
						b.expressions.add(_readExpConcat());

						if (token != TOKEN.DELIMITER)
							break;
					}

					_readThis(TOKEN.RIGHT_BRACKET, "after IN and ( must be )");

					a = new ExpressionTree(baseToken, a, b);
				} else if (token == TOKEN.EQUAL || token == TOKEN.BIGGER_EQUAL || token == TOKEN.BIGGER
						|| token == TOKEN.SMALLER || token == TOKEN.SMALLER_EQUAL || token == TOKEN.NOT_EQUAL) {
					if (not)
						throw new IllegalArgumentException("not can not be used with =,<>,!=,>,<,<=,>="
								+ this.token.toString() + "," + this.charPosition + "," + this.cellFormula);

					TOKEN _t = token;

					_readToken();

					return new ExpressionTree(_t, a, _readExpConcat());
				}

				if (not)
					a = new ExpressionTree(TOKEN.NOT, a, null);

				return a;
			}
		}

		private ExpressionTree _readExpConcat() {
			ExpressionTree r = _readSum();

			if (token == TOKEN.REMAINDER) {
				ExpressionTree a = r;

				_readToken();

				r = new ExpressionTree(TOKEN.REMAINDER, a, _readOr());
			} else {
				while (token == TOKEN.CONCATENATE || token == TOKEN.POWER) {
					TOKEN _tok = token;
					ExpressionTree a = r;

					_readToken();

					r = new ExpressionTree(_tok, a, _readOr());
				}
			}

			return r;
		}

		private ExpressionTree _readSum() {
			ExpressionTree r = _readFactor();

			while (true) {
				if (token != TOKEN.PLUS && token != TOKEN.MINUS)
					break;

				TOKEN _t = token;
				ExpressionTree a = r;

				_readToken();

				r = new ExpressionTree(_t, a, _readFactor());
			}

			return r;
		}

		private ExpressionTree _readFactor() {
			ExpressionTree r = _readTerm();

			while (token == TOKEN.MULTIPLY || token == TOKEN.DIVIDE) {
				TOKEN _t = token;
				ExpressionTree a = r;

				_readToken();

				r = new ExpressionTree(_t, a, _readTerm());
			}

			return r;
		}

		private ExpressionTree _readTerm() throws IllegalArgumentException {
			ExpressionTree r = null;

			switch (token) {
				case MINUS:
					_readToken();
					r = new ExpressionTree(TOKEN.MINUS, _readTerm());
					break;
				case PLUS:
					_readToken();
					r = _readTerm();
					break;
				case LEFT_BRACKET:
					_readToken();
	
					r = _readOr();
	
					if (token != TOKEN.RIGHT_BRACKET)
						throw new IllegalArgumentException("unexpected element, must be )" + this.token.toString() + ","
								+ this.charPosition + "," + this.cellFormula);
					
					_readToken();
	
					break;
				case NUMBER:
					r = new ExpressionTree((double) data);
					_readToken();
					break;
				case STRING:
					r = new ExpressionTree((String) data);
					_readToken();
					break;
				case TRUE:
					r = new ExpressionTree(true);
					_readToken();
					break;
				case FALSE:
					r = new ExpressionTree(false);
					_readToken();
					break;
				case FUNCTION:
					r = new ExpressionTree((FUNCTION) data);
					r.expressions.addAll(_readFunction());
					_readToken();
					break;
				case CELL:
					r = new ExpressionTree(token, (Cell) data);
					_readToken();
					break;
				case RANGE:
					r = new ExpressionTree(token, (Range) data);
					_readToken();
					break;
				default:
					throw new IllegalArgumentException("unexpected element " + token.toString() + ": "
							+ this.token.toString() + "," + this.charPosition + "," + this.cellFormula);
				}

			return r;
		}
	};

	public ExpressionTree() {
		dataType = CellInputDataType.General;
	}

	public ExpressionTree(TOKEN token, Object value) {
		this.token = token;
		if (token != TOKEN.ERROR && token != TOKEN.EXPRESSION && token != TOKEN.UNDEF) {
			switch (token) {
				case CELL:
					Cell c = (Cell) value;
					data = c;
					this.dataType = c.getCellDataType();
					break;
				case RANGE:
					data = (Range) value;
					this.dataType = CellInputDataType.RANGE;
					break;
				case NUMBER:
					data = Double.valueOf(value.toString());
					this.dataType = CellInputDataType.Number;
					break;
				case STRING:
					data = value.toString();
					this.dataType = CellInputDataType.String;
					break;
				default:
					this.dataType = CellInputDataType.General;
			}
		}
	}

	// copy constructor
	public ExpressionTree(ExpressionTree e1) {
		this.token = e1.token;
		this.dataType = e1.dataType;
		this.data = e1.data;
		if (e1.expressions != null) {
			expressions = new ArrayList<ExpressionTree>();
			for (ExpressionTree ex : e1.expressions)
				expressions.add(new ExpressionTree(ex));
		}
	}

	// unary operation constructor
	public ExpressionTree(TOKEN token, ExpressionTree e) {
		this.token = token;
		dataType = CellInputDataType.General;
		expressions = new ArrayList<ExpressionTree>();
		expressions.add(e);
	}

	// binary operation constructor
	public ExpressionTree(TOKEN token, ExpressionTree e1, ExpressionTree e2) {
		this.token = token;
		expressions = new ArrayList<ExpressionTree>();
		expressions.add(e1);
		expressions.add(e2);
		dataType = CellInputDataType.General;
	}

	// string constant constructor
	public ExpressionTree(String v) {
		token = TOKEN.STRING;
		dataType = CellInputDataType.String;
		data = v;
	}

	// boolean constant constructor
	public ExpressionTree(boolean v) {
		token = (v) ? TOKEN.TRUE : TOKEN.FALSE;
		dataType = CellInputDataType.Boolean;
	}

	// double constant constructor
	public ExpressionTree(double v) {
		token = TOKEN.NUMBER;
		dataType = CellInputDataType.Number;
		data = v;
	}

	// function constructor
	public ExpressionTree(FUNCTION v) {
		token = TOKEN.FUNCTION;
		data = v;
		// dataType = ExpressionParser._defineFunctionTypes(this, false); ;
	}

	public Object getValue() {
		switch (this.token) {
			case NUMBER:
				return (double) this.data;
	
			case PLUS:
				if (this.expressions.size() == 1)
					return this.expressions.get(0).getNumericValue();
				else
					return this.expressions.get(0).getNumericValue() + this.expressions.get(1).getNumericValue();
	
			case MINUS:
				if (this.expressions.size() == 1)
					return -this.expressions.get(0).getNumericValue();
				else
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
	
			case CELL:
				return ((Cell) this.data).getCellValue();
	
			case FUNCTION:

			FunctionRangeStrategy strategy;
			switch ((FUNCTION) this.data) {
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

			return strategy.calculate((Range) this.expressions.get(0).data);
		}

		throw new RuntimeException("Expression token " + this.token.toString() + " is not defined");
	}

	public double getNumericValue() {
		Object o = getValue();
		if (o == null)
			return 0d;
		return Double.parseDouble(o.toString());
	}

	public String getTextValue() {
		Object o = getValue();
		if (o == null)
			return "";
		return (String) o;
	}

	private TOKEN token = TOKEN.UNDEF;
	private CellInputDataType dataType = CellInputDataType.General;
	private ArrayList<ExpressionTree> expressions = new ArrayList<ExpressionTree>();
	private Object data = null;

	public CellInputDataType getDataType() {
		return dataType;
	}

	public void setDataType(CellInputDataType value) {
		dataType = value;
	}

	public static ExpressionTree parse(Cell cell, String cellformula) throws IllegalArgumentException {
		ExpressionTree.ExpressionParser parser = new ExpressionTree.ExpressionParser(cellformula, cell);
		parser._readToken();
		ExpressionTree ret = parser._readOr();
		if (parser.charPosition != cellformula.length() || parser.token != TOKEN.UNDEF)
			throw new IllegalArgumentException(cellformula + " Formula has false structure");

		// remove dependency, if exists
		Application.getActiveWorkbook().removeReferenceDependencies(cell);

		// add new dependency
		addDependency(cell, ret);

		return ret;
	}

	private static void addDependency(Cell cell, ExpressionTree ex) {
		if (ex.token == TOKEN.RANGE) {
			Range r = (Range) ex.data;
			for (Area a : r.getWorksheetAreas().keySet())
				Application.getActiveWorkbook().addDependency(cell, a);

			for (Cell c : r.getWorksheetCells().keySet())
				Application.getActiveWorkbook().addDependency(cell, c);
		} else if (ex.token == TOKEN.CELL) {
			Application.getActiveWorkbook().addDependency(cell, (Cell) ex.data);
		}

		if (ex.expressions != null) {
			for (ExpressionTree e : ex.expressions)
				addDependency(cell, e);
		}
	}

}
