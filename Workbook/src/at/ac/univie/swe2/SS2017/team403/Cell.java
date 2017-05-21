package at.ac.univie.swe2.SS2017.team403;

import java.util.Comparator;

public class Cell {
	
	private Worksheet parentWorksheet;
	private int cellRow, cellColumn;

	private CellInputDataType cellInputDataType = CellInputDataType.General;
	private Object cellValue = null;
	private String cellFormula = null;
	private ExpressionTree cellExpression = null;

	/**
	 * @param value -> cell-input-double-value
	 */
	public void setNumericValue(double value) {
		WorkbookMainGui.getActiveWorkbook().removeReferenceDependencies(this);

		this.cellExpression = null;
		this.cellFormula = null;
		this.cellValue = value;
		this.cellInputDataType = CellInputDataType.Number;
		parentWorksheet.getParentWorkbook().calculateReferenceDependencies(this);
	}

	/**
	 * @param value -> cell-input-value
	 */
	public void setTextValue(String value) {
		WorkbookMainGui.getActiveWorkbook().removeReferenceDependencies(this);

		this.cellExpression = null;
		this.cellFormula = null;
		this.cellValue = value;
		this.cellInputDataType = CellInputDataType.String;
		parentWorksheet.getParentWorkbook().calculateReferenceDependencies(this);
	}

	/**
	 * @param formula -> cell-input-formula-value
	 * @throws IllegalArgumentException
	 */
	public void setFormula(String formula) throws IllegalArgumentException {
		if (!formula.startsWith("=")){
			throw new IllegalArgumentException(formula + " Formula must begin with = symbol");			
		}

		this.cellFormula = formula;
		cellExpression = ExpressionTree.parse(this, formula);
		cellInputDataType = cellExpression.getExpressionDataType();

		this.cellValue = cellExpression.getValue();

		parentWorksheet.getParentWorkbook().calculateReferenceDependencies(this);
	}

	public void calculateCellExpression() {
		if (cellExpression != null){
			this.cellValue = cellExpression.getValue();			
		}
	}

	public CellInputDataType getCellDataType() {
		return cellInputDataType;
	}

	public void setCellDataType(CellInputDataType dataType) {
		this.cellInputDataType = dataType;
	}

	public String getFormula() {
		return cellFormula;
	}

	/**
	 * A cell contains a referenced cell.
	 * @return -> referenced cell
	 */
	public String getCellReferences() {
		return "'" + parentWorksheet.getWorksheetName() + "'!R" + cellRow + "C" + cellColumn;
	}

	public Object getCellValue() {
		return cellValue;
	}

	/**
	 * @return 0d -> double or double value
	 */
	public double getNumericValue() {
		if (cellValue == null) {
			return 0d;			
		}
		return (double) cellValue;
	}

	public String getTextValue() {
		if (cellValue == null) {
			return "";			
		}
		return (String) cellValue;
	}

	public Cell(Worksheet parent, int row, int column) {
		this.parentWorksheet = parent;
		this.cellRow = row;
		this.cellColumn = column;
	}

	public Worksheet getParentWorksheet() {
		return parentWorksheet;
	}

	public int getCellRow() {
		return cellRow;
	}

	public int getCellColumn() {
		return cellColumn;
	}

	/**
	 * We use Comperator to order Cell-objects and accelerate the search.
	 */
	static class CellComparator implements Comparator<Cell> {
		public int compare(Cell firstCell, Cell secondCell) {
			int var = firstCell.parentWorksheet.getId() - secondCell.parentWorksheet.getId();
			if (var == 0) {
				var = firstCell.cellRow - secondCell.cellRow;
				if (var == 0) {
					return firstCell.cellColumn - secondCell.cellColumn;
				} else {
					return var;
				}
			} else {
				return var;
			}
		}
	}
	
}
