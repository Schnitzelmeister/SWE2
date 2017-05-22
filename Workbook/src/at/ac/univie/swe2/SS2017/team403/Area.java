package at.ac.univie.swe2.SS2017.team403;

import java.util.Comparator;

/**
 * This class is used to handle all operations connected to Area.
 * An Area can be either one cell or multiple cells. 
 */
public class Area {

	private Worksheet parentWorksheet;

	public Worksheet getParent() {
		return parentWorksheet;
	}

	private int firstRow, lastRow, firstColumn, lastColumn;

	/** firstRow -> top row. */
	public int getFirstRow() {
		return firstRow;
	}

	/** lastRow -> below row. */
	public int getLastRow() {
		return lastRow;
	}

	/** firstColumn -> left-side column. */
	public int getFirstColumn() {
		return firstColumn;
	}

	/** lastColumn -> right-side column. */
	public int getLastColumn() {
		return lastColumn;
	}

	/**
	 * This constructor is used to set a range which contains different cells.
	 * We need only two cells to build a rectangle to define the range.
	 * 
	 * @param cellTopLeft
	 * @param cellBottomRight
	 */
	public Area(Cell cellTopLeft, Cell cellBottomRight) {
		parentWorksheet = cellTopLeft.getParentWorksheet();
		firstRow = cellTopLeft.getCellRow();
		firstColumn = cellTopLeft.getCellColumn();
		lastRow = cellBottomRight.getCellRow();
		lastColumn = cellBottomRight.getCellColumn();
	}

	/**
	 * This constructor is used to set a specific cell.
	 * 
	 * @param cell
	 */
	public Area(Cell cell) {
		parentWorksheet = cell.getParentWorksheet();
		firstRow = cell.getCellRow();
		firstColumn = cell.getCellColumn();
		lastRow = firstRow;
		lastColumn = firstColumn;
	}

	/**
	 * This constructor is used to set a referenced area of a cell.
	 * 
	 * @param cellReferences -> references of the selected cell
	 * @param worksheet -> selected worksheet
	 * @param cellContext -> selected cell
	 */
	public Area(String cellReferences, Worksheet worksheet, Cell cellContext) {
		parentWorksheet = worksheet;
		String[] splittedCellReferences = cellReferences.split(":");
		Cell cellTopLeft = worksheet.getCell(splittedCellReferences[0], cellContext);
		Cell cellBottomRight = worksheet.getCell(splittedCellReferences[1], cellContext);
		firstRow = cellTopLeft.getCellRow();
		firstColumn = cellTopLeft.getCellColumn();
		lastRow = cellBottomRight.getCellRow();
		lastColumn = cellBottomRight.getCellColumn();
	}

	/**
	 * A cell contains multiple referenced cells
	 * 
	 * @return -> referenced area as a String
	 */
	public String getCellReferences() {
		return "'" + parentWorksheet.getWorksheetName() + "'!R" + firstRow + "C" + firstColumn + ":" + "R" + lastRow
				+ "C" + lastColumn;
	}

	/** We use Comperator to order Area-objects and accelerate the search. 
	 * The class compares two areas.
	 * a negative integer, zero, or a positive integer 
	 * as the first argument is less than, equal to, or greater than the second.
	 * */
	static class AreaComparator implements Comparator<Area> {
		public int compare(Area firstArea, Area secondArea) {
			int var = firstArea.parentWorksheet.getId() - secondArea.parentWorksheet.getId();
			if (var == 0) {
				var = firstArea.firstRow - secondArea.firstRow;
				if (var == 0) {
					var = firstArea.firstColumn - secondArea.firstColumn;
					if (var == 0) {
						var = secondArea.lastRow - firstArea.lastRow;
						if (var == 0) {
							return secondArea.lastColumn - firstArea.lastColumn;
						} else {
							return var;
						}

					} else {
						return var;
					}
				} else {
					return var;
				}
			} else {
				return var;
			}
		}
	}

}
