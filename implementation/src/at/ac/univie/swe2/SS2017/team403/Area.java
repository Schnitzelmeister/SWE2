package at.ac.univie.swe2.SS2017.team403;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Comparator;

/**
 * This class is used to handle all operations connected to Area.
 * An Area can be either one cell or more cells. 
 */
public class Area {
	private transient Worksheet parentWorksheet;

	public Worksheet getParent() {
		return parentWorksheet;
	}

	private int firstRow, lastRow, firstColumn, lastColumn;

	
	/**
	 * 
	 * @return an int that gives the number of the first row
	 */
	public int getFirstRow() {
		return firstRow;
	}

	/**
	 * 
	 * @return an int value that returns the number of the last row
	 */
	public int getLastRow() {
		return lastRow;
	}

	/**
	 * 
	 * @return an int value that returns the number of the first column
	 */
	public int getFirstColumn() {
		return firstColumn;
	}

	/**
	 * 
	 * @return an int value that returns the number of the last column
	 */
	public int getLastColumn() {
		return lastColumn;
	}

	/**
	 * This constructor is used to set a range which contains different cells.
	 * We need only two cells to build a rectangle to define the area.
	 * 
	 * @param cellTopLeft the top left cell of an area
	 * @param cellBottomRight the bottom right cell of an area
	 */
	public Area(Cell cellTopLeft, Cell cellBottomRight) {
		parentWorksheet = cellTopLeft.getParentWorksheet();
		firstRow = cellTopLeft.getCellRow();
		firstColumn = cellTopLeft.getCellColumn();
		lastRow = cellBottomRight.getCellRow();
		lastColumn = cellBottomRight.getCellColumn();
		correctCoordinates();
	}

	/**
	 * This constructor is used to set a specific cell.
	 * 
	 * @param cell a cell which is only one cell of an area
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
	 * @param cellReferences  references of the selected cell
	 * @param worksheet  selected worksheet
	 * @param cellContext  selected cell
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
		correctCoordinates();
	}
	
    private void correctCoordinates()
    {
    	if (lastRow < firstRow) {
    		int tmp = lastRow; lastRow = firstRow; firstRow =tmp;
    	}
    	
    	if (lastColumn < firstColumn) {
    		int tmp = lastColumn; lastColumn = firstColumn; firstColumn =tmp;
    	}		
    }

	/**
	 * A cell contains multiple referenced cells as a string.
	 * Here you get the cell content as a String. 
	 * 
	 * @return  referenced area as a String
	 */
	public String getCellReferences() {
		return "'" + parentWorksheet.getWorksheetName() + "'!R" + firstRow + "C" + firstColumn + ":" + "R" + lastRow
				+ "C" + lastColumn;
	}

	/** We use Comperator to order Area-objects and accelerate the search. */
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
