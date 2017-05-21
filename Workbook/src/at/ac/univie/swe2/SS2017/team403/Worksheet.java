package at.ac.univie.swe2.SS2017.team403;

import java.util.TreeMap;

public class Worksheet {

	private transient int worksheetId;
	private TreeMap<Long, Cell> worksheetCells = new TreeMap<Long, Cell>();
	private String worksheetName;
	private Workbook parentWorkbook;
	private WorksheetRenameCallback worksheetRenameCallback = null;
	private int furthestRowUsed = 1;
	private int furthestColumnUsed = 1;

	Worksheet(String name, Workbook workbook, WorksheetRenameCallback worksheetRenameCallback) {
		this.worksheetId = WorkbookMainGui.getActiveWorkbook().getNewId();
		this.worksheetName = name;
		this.parentWorkbook = workbook;
		this.worksheetRenameCallback = worksheetRenameCallback;
	}

	public int getId() {
		return worksheetId;
	}

	public String getWorksheetName() {
		return worksheetName;
	}

	public void setWorksheetName(String name) throws IllegalArgumentException {
		
		if (parentWorkbook.getSheets().containsKey(name)) {
			throw new IllegalArgumentException("Sheet with name " + name + " already exists");			
		}

		String oldName = this.worksheetName;
		this.worksheetName = name;

		if (worksheetRenameCallback != null) {
			worksheetRenameCallback.AfterWorksheetRenamed(oldName, this);			
		}

	}

	public Workbook getParentWorkbook() {
		return parentWorkbook;
	}

	public Area getMaxUsedRangeArea() {
		return new Area(getCell(1, 1), getCell(furthestRowUsed, furthestColumnUsed));
	}
	
	public Cell getCell(int row, int column) {
		return getCell(row, column, true);
	}

	/**
	 * This method checks the availability of a cell by a cell-key check.
	 * If there is a similar key it returns the existing cell, otherwise
	 * it returns the new cell.
	 * @param row
	 * @param column
	 * @param isCellExisting
	 * @return existing/new cell
	 */
	public Cell getCell(int row, int column, boolean isCellExisting) {
		
		long key = getUniqueCellKey(row,column);
		
		if (!isCellExisting) {
			if (row > furthestRowUsed){
				furthestRowUsed = row;			
			}
			if (row > furthestColumnUsed){
				furthestColumnUsed = column;				
			}
		}

		if (!worksheetCells.containsKey(key)) {
			if (!isCellExisting) {
				return null;				
			}
			worksheetCells.put(key, new Cell(this, row, column));
		}
		
		return worksheetCells.get(key);
		
	}
	
	public long getUniqueCellKey(int row, int column){
		return (long) row << 32 | column & 0xFFFFFFFFL;
	}

	public Cell getCell(String cellReferences, Cell cellContext) {
		
		int row, column;
		
		String[] cellRefUpperCase = cellReferences.toUpperCase().split("C");
		// System.out.println(cellReferences);
		org.junit.Assert.assertEquals('R', cellRefUpperCase[0].charAt(0));

		// relative row address without offset
		if (cellRefUpperCase[0].length() == 1) {
			row = cellContext.getRow();
		}
		// relative row address
		else if (cellRefUpperCase[0].charAt(1) == '[') {
			row = cellContext.getRow() + Integer.valueOf(cellRefUpperCase[0].substring(2, cellRefUpperCase[0].length() - 1));
		}
		else {
			row = Integer.valueOf(cellRefUpperCase[0].substring(1));			
		}


		// relative column address without offset
		if (cellRefUpperCase.length == 1 || cellRefUpperCase[1].length() == 0) {
			column = cellContext.getColumn();
		}
		// relative column address
		else if (cellRefUpperCase[1].charAt(0) == '[') {
			column = cellContext.getColumn() + Integer.valueOf(cellRefUpperCase[1].substring(1, cellRefUpperCase[1].length() - 1));
		}
		else {
			column = Integer.valueOf(cellRefUpperCase[1]);			
		}

		return getCell(row, column);
	}

}
