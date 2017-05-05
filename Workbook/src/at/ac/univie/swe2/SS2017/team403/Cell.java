package at.ac.univie.swe2.SS2017.team403;

public class Cell {
	private Worksheet parent;
	private long id;

	private DataType dataType = DataType.General;
	private Object value = 0d;
	private Expression expr = null;
	private String formula;

	public void setNumericValue(double value)
	{ this.value = value; this.dataType = DataType.Number; }

	public void setTextValue(String value)
	{ this.value = value; this.dataType = DataType.String; }
	
	public void setFormula(String formula)
	{
		this.formula = formula; 
		try {
			expr = Expression.Parse(formula);
			dataType = expr.getDataType();
		}
		catch(Exception e) {
			
		}
	}

	public DataType getDataType()
	{ return dataType; }

	public void setDataType(DataType dataType)
	{ this.dataType = dataType; }
	
	public String getFormula()
	{ return formula; }

	public Object getValue()
	{ return value; }

	public double getNumericValue()
	{ return (double)value; }

	public String getTextValue()
	{ return (String)value; }

	
	public Cell(Worksheet parent, long id) {
		this.parent = parent;
		this.id = id;
	}

}
