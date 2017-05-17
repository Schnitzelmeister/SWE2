package at.ac.univie.swe2.SS2017.team403;

import java.util.Comparator;

public class Cell {
	private Worksheet parent;
	//row, column
	private int r, c;

	private DataType dataType = DataType.General;
	
	//current value
	private Object value = null;
	private String formula;

	//expression tree for a formula
	private Expression expr = null;

	public void setNumericValue(double value)
	{ 
		this.expr = null;
		this.value = value; 
		this.dataType = DataType.Number; 
		parent.getParent().calculateDependencies(this);
	}

	public void setTextValue(String value)
	{
		this.expr = null;
		this.value = value;
		this.dataType = DataType.String;
		parent.getParent().calculateDependencies(this);
	}
	
	public void setFormula(String formula) throws IllegalArgumentException
	{
		if (!formula.startsWith("="))
        	throw new IllegalArgumentException(formula + " Formula must begin with = symbol");
		
		this.formula = formula; 
		expr = Expression.parse(this, formula);
		dataType = expr.getDataType();
		
		this.value = expr.getValue();

		parent.getParent().calculateDependencies(this);
	}

	public void calculate()
	{
		if (expr != null)
			this.value = expr.getValue(); 
	}

	public DataType getDataType()
	{ return dataType; }

	public void setDataType(DataType dataType)
	{ this.dataType = dataType; }
	
	public String getFormula()
	{ return formula; }

	public Object getValue()
	{ return value; }

	public double getNumericValue()	{
		if (value == null)
			return 0d;
		return (double)value; 
	}

	public String getTextValue() {
		if (value == null)
			return "";
		return (String)value;
	}

	
	public Cell(Worksheet parent, int row, int column) {
		this.parent = parent;
		this.r = row;
		this.c = column;
	}

	public Worksheet getParent()
	{ return parent; }

	public int getRow()
	{ return r; }

	public int getColumn()
	{ return c; }
	
	//Comparator to Cell class, we can order Cells
	static class CellComparator implements Comparator<Cell>
	{
		public int compare(Cell c1, Cell c2)
	    {
			int res = c1.parent.getId() - c2.parent.getId();
			if (res == 0) {
				res = c1.r - c2.r;
				if (res == 0) {
					return c1.c - c2.c;
				}
				else {
					return res;
				}
			}
			else {
				return res;
			}
		}
	}
}
