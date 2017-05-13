package at.ac.univie.swe2.SS2017.team403;

import java.util.Comparator;

public class Cell {
	private Worksheet parent;
	private int r, c;

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
			expr = Expression.Parse(this, formula);
			dataType = expr.getDataType();
		}
		catch(Exception e) {
			
		}
		
		//look for observers
		//for (Dependency dep : Application.activeWorkbook.(this)) {
			//proverit est li zikl
		//	dep.Cell.Recalculate
		//}
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
