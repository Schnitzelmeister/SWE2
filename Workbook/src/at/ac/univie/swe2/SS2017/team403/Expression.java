package at.ac.univie.swe2.SS2017.team403;

import java.util.ArrayList;;

public class Expression {
	
	private enum TOKEN {
		FUNCTION_VALUE,
        CELL_VALUE,
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
        INTEGER,
        STRING,
        DATE,
        TRUE,
        FALSE,

        RIGHT_BRACKET,
        LEFT_BRACKET,
        UNDEF,
        DELIMITER,
        CONCATENATE,
        COMMA,
        REF,
        //LIST,
        ERROR,
        REMAINDER,
        PERIOD,
        EXPRESSION
	}
	
	private enum FUNCTION {
		SUM,
		MEAN,
		COUNT
	}

	
	
	private TOKEN token = TOKEN.UNDEF;
    private DataType dataType;
    private ArrayList<Expression> expressions;
    private Object data = null;
    private boolean dirty = true;
    
    public DataType getDataType() {
    	return dataType;
    }
    
    public static Expression Parse(String formula) throws IllegalArgumentException
    {
    	return null;
    }
}
