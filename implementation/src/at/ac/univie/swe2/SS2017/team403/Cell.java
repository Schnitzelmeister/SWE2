package at.ac.univie.swe2.SS2017.team403;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Comparator;

/**
 * This class is used to handle all operations connected to a Cell.
 * A cell-object is part of a worksheet and contains either a cellValue 
 * which is also an object or a formula.
 * 
 */
public class Cell implements Externalizable {

	private transient Worksheet parentWorksheet;
	private int cellRow, cellColumn;

	private CellInputDataType cellInputDataType = CellInputDataType.General;
	private Object cellValue = null;
	private String cellFormula = null;
	private ExpressionTree cellExpression = null;

	/**
	 * Use setNumericValue to store a double-value in a Cell.
	 * 
	 * @param value  cell-input-double-value
	 */
	public void setNumericValue(double value) {
		Application.getActiveWorkbook().removeReferenceDependencies(this);

		this.cellExpression = null;
		this.cellFormula = null;
		this.cellValue = value;
		this.cellInputDataType = CellInputDataType.Number;
		parentWorksheet.getParentWorkbook().calculateReferenceDependencies(this);
	}

	/**
	 * Use setTextValue to store a String-value in a Cell.
	 * 
	 * @param value   cell-input-value
	 */
	public void setTextValue(String value) {
		Application.getActiveWorkbook().removeReferenceDependencies(this);

		this.cellExpression = null;
		this.cellFormula = null;
		this.cellValue = value;
		this.cellInputDataType = CellInputDataType.String;
		parentWorksheet.getParentWorkbook().calculateReferenceDependencies(this);
	}

	/**
	 * Use setFormula to store a formula in a Cell.
	 * 
	 * @param formula  cell-input-formula-value
	 * @throws IllegalArgumentException  Exception will be thrown if the formula doesn't begin with "="
	 */
	public void setFormula(String formula) throws IllegalArgumentException {
		if (!formula.startsWith("=")) {
			throw new IllegalArgumentException(formula + " Formula must begin with = symbol");
		}

		if (!this.parentWorksheet.getParentWorkbook().getAutoCalculate()) {
			cellFormula = formula;
			cellExpression = null;
			return;
		}
		
		String previous = this.cellFormula;
		try {
			cellFormula = formula;
			cellExpression = ExpressionTree.parse(this, formula);
			cellInputDataType = cellExpression.getDataType();
			calculateCellExpression();
		}
		catch (IllegalArgumentException e) { this.cellFormula = previous; cellExpression = null; throw e; }
		
		try {
			parentWorksheet.getParentWorkbook().calculateReferenceDependencies(this);
		} catch (IllegalArgumentException e) {
			cellFormula = null;
			cellExpression = null;
	    	Application.getActiveWorkbook().removeReferenceDependencies(this);
			throw e;
		}
	}

	public void calculateCellExpression() {
		if (cellExpression != null) {
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
	 * A cell contains a formula which is a reference of other cells.
	 * 
	 * @return  referenced cell
	 */
	public String getCellReferences() {
		return "'" + parentWorksheet.getWorksheetName() + "'!R" + cellRow + "C" + cellColumn;
	}

	public Object getCellValue() {
		return cellValue;
	}

	/**
	 * @return 0d  double or double value
	 */
	public double getNumericValue() throws ClassCastException {
		if (cellValue == null) {
			return 0d;
		}

		if (cellInputDataType == CellInputDataType.Number)
			return (double)cellValue;
		
		if (cellInputDataType == CellInputDataType.General) {
			try {
				return Double.parseDouble(cellValue.toString());
			}
			catch (NumberFormatException e) {
				return 0d;
			}
		}
		
    	throw new ClassCastException (this.getCellReferences() + " Incompatible DataType");

	}

	public String getTextValue() {
		if (cellValue == null) {
			return "";
		}
		return cellValue.toString();
	}

	public Cell(Worksheet parent, int row, int column) {
		this.parentWorksheet = parent;
		this.cellRow = row;
		this.cellColumn = column;
	}
	
	
	Cell(Cell cell, Worksheet parent) {
		this.parentWorksheet = parent;
		this.cellRow = cell.cellRow;
		this.cellColumn = cell.cellColumn;
		this.cellInputDataType = cell.cellInputDataType;
		this.cellValue = cell.cellValue;
		if (this.cellFormula != null)
			this.setFormula(cell.cellFormula);
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

	/** We use Comperator to order Cell-objects and accelerate the search. */
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
	
	
	//Externalizable
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(cellRow);
		out.writeInt(cellColumn);
		out.writeUTF(cellFormula); 
		out.writeInt(cellInputDataType.getNumVal());
		switch(cellInputDataType) {
		case Boolean:
			out.writeBoolean((boolean)cellValue);
			break;
		case Number:
			out.writeDouble((double)cellValue);
			break;
		default:
			out.writeUTF(cellValue.toString());
			break;
		}
	}

	//Externalizable
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		cellRow = in.readInt();
		cellColumn = in.readInt();
		cellFormula = in.readUTF();
		cellInputDataType = CellInputDataType.values()[in.readInt()];
		switch(cellInputDataType) {
		case Boolean:
			cellValue = in.readBoolean();
			break;
		case Number:
			cellValue = in.readDouble();
			break;
		default:
			cellValue = in.readUTF();
			break;
		}
	}

}
