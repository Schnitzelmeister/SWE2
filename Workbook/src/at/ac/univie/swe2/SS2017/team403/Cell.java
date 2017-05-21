package at.ac.univie.swe2.SS2017.team403;

import java.util.Comparator;

public class Cell {
	private Worksheet parentWorksheet;
	private int cellRow, cellColumn;

	private CellInputDataType cellInputDataType = CellInputDataType.General;

	// current value
	private Object value = null;
	private String formula = null;

	// expression tree for a formula
	private Expression expr = null;

	public void setNumericValue(double value) {
		// remove dependency, if exists
		Application.getActiveWorkbook().removeDependancy(this);

		this.expr = null;
		this.formula = null;
		this.value = value;
		this.cellInputDataType = CellInputDataType.Number;
		parentWorksheet.getParentWorkbook().calculateDependencies(this);
	}

	public void setTextValue(String value) {
		// remove dependency, if exists
		Application.getActiveWorkbook().removeDependancy(this);

		this.expr = null;
		this.formula = null;
		this.value = value;
		this.cellInputDataType = CellInputDataType.String;
		parentWorksheet.getParentWorkbook().calculateDependencies(this);
	}

	public void setFormula(String formula) throws IllegalArgumentException {
		if (!formula.startsWith("="))
			throw new IllegalArgumentException(formula + " Formula must begin with = symbol");

		this.formula = formula;
		expr = Expression.parse(this, formula);
		cellInputDataType = expr.getDataType();

		this.value = expr.getValue();

		parentWorksheet.getParentWorkbook().calculateDependencies(this);
	}

	public void calculate() {
		if (expr != null)
			this.value = expr.getValue();
	}

	public CellInputDataType getDataType() {
		return cellInputDataType;
	}

	public void setDataType(CellInputDataType dataType) {
		this.cellInputDataType = dataType;
	}

	public String getFormula() {
		return formula;
	}

	public String getAddress() {
		return "'" + parentWorksheet.getWorksheetName() + "'!R" + cellRow + "C" + cellColumn;
	}

	public Object getValue() {
		return value;
	}

	public double getNumericValue() {
		if (value == null)
			return 0d;
		return (double) value;
	}

	public String getTextValue() {
		if (value == null)
			return "";
		return (String) value;
	}

	public Cell(Worksheet parent, int row, int column) {
		this.parentWorksheet = parent;
		this.cellRow = row;
		this.cellColumn = column;
	}

	public Worksheet getParentWorksheet() {
		return parentWorksheet;
	}

	public int getRow() {
		return cellRow;
	}

	public int getColumn() {
		return cellColumn;
	}

	// Comparator to Cell class, we can order Cells
	static class CellComparator implements Comparator<Cell> {
		public int compare(Cell c1, Cell c2) {
			int res = c1.parentWorksheet.getId() - c2.parentWorksheet.getId();
			if (res == 0) {
				res = c1.cellRow - c2.cellRow;
				if (res == 0) {
					return c1.cellColumn - c2.cellColumn;
				} else {
					return res;
				}
			} else {
				return res;
			}
		}
	}
}
