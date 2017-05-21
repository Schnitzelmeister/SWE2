package at.ac.univie.swe2.SS2017.team403;

import java.util.Comparator;

public class Area {
	
	private Worksheet parentWorksheet;

	public Worksheet getParent() {
		return parentWorksheet;
	}

	private int firstRow, lastRow, firstColumn, lastColumn;

	public int getFirstRow() {
		return firstRow;
	}

	public int getLastRow() {
		return lastRow;
	}

	public int getFirstColumn() {
		return firstColumn;
	}

	public int getLastColumn() {
		return lastColumn;
	}

	/**
	 * This constructor is used to set a range which contains different cells.
	 * We need only two cells to build a rectangle to define the range.
	 * @param cellTopLeft
	 * @param cellBottomRight
	 */
	public Area(Cell cellTopLeft, Cell cellBottomRight) {
		parentWorksheet = cellTopLeft.getParentWorksheet();
		firstRow = cellTopLeft.getRow();
		firstColumn = cellTopLeft.getColumn();
		lastRow = cellBottomRight.getRow();
		lastColumn = cellBottomRight.getColumn();
	}

	/**
	 * This constructor is used to set a specific cell.
	 * @param cell
	 */
	public Area(Cell cell) {
		parentWorksheet = cell.getParentWorksheet();
		firstRow = cell.getRow();
		firstColumn = cell.getColumn();
		lastRow = firstRow;
		lastColumn = firstColumn;
	}

	/**
	 * This constructor is used to set a referenced range of a cell.
	 * @param cellReferences -> references of the selected cell
	 * @param worksheet -> selected worksheet
	 * @param cellContext -> selected cell
	 */
	public Area(String cellReferences, Worksheet worksheet, Cell cellContext) {
		parentWorksheet = worksheet;	
		String[] splittedCellReferences = cellReferences.split(":");
		Cell cellTopLeft = worksheet.getCell(splittedCellReferences[0], cellContext);
		Cell cellBottomRight = worksheet.getCell(splittedCellReferences[1], cellContext);
		firstRow = cellTopLeft.getRow();
		firstColumn = cellTopLeft.getColumn();
		lastRow = cellBottomRight.getRow();
		lastColumn = cellBottomRight.getColumn();
	}

	public String getCellReferences() {
		return "'" + parentWorksheet.getName() + "'!R" + firstRow + "C" + firstColumn + ":" + "R" + lastRow + "C" + lastColumn;
	}

	/**
	 * We use Comperator to order Area-objects and accelerate the search.
	 */
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
