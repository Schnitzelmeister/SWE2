package at.ac.univie.swe2.SS2017.team403;

import java.util.TreeMap;

public class Worksheet {

	private transient int worksheetId;
	private TreeMap<Long, Cell> worksheetCells = new TreeMap<Long, Cell>();
	private String worksheetName;
	private Workbook parentWorkbook;
	private WorksheetRenameCallback worksheetRenameCallback = null;
<<<<<<< HEAD
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
=======
	private int usedAreaR = 1;
	private int usedAreaC = 1;
		
	Worksheet(String name, Workbook wbk, WorksheetRenameCallback worksheetRenameCallback) {
		this.id = wbk.getNewId();
		this.name = name;
		this.parent = wbk;
		this.worksheetRenameCallback = worksheetRenameCallback;
	}
	
	int getId() { return id; }
	public String getName() { return name; }
	public void setName(String name) throws IllegalArgumentException
	{
		//check if sheet with new name exists
		if (parent.getSheets().containsKey(name))
			throw new IllegalArgumentException("Sheet with name " + name + " already exists");
		
		String oldName = this.name;
		this.name = name;
		
		if (worksheetRenameCallback != null)
			worksheetRenameCallback.afterWorksheetRenamed(oldName, this);
		
>>>>>>> 74d1ba7ec3738bcada746293a10eb3296363d500
	}
	
	public Workbook getParent() { return parent; }
		
	//maximal Used range on this Worksheet
	public Area getUsedArea() { return new Area( getCell(1,1), getCell(usedAreaR, usedAreaC) ); }
	
	public Cell getCell(int row, int column) throws IllegalArgumentException
	{
		return getCell(row, column, true);
	}
<<<<<<< HEAD

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
=======
	
	public Cell getCell(int row, int column, boolean createIfNotExist) throws IllegalArgumentException
	{
		if (row <= 0 || column <=0)
			throw new IllegalArgumentException("Row/Column must be >= 1");
		
		if (createIfNotExist)
		{
			if (row > usedAreaR)
				usedAreaR = row;
			if (row > usedAreaC)
				usedAreaC = column;
		}
		
		//convert two Integers to one Long
		long key = (long)row << 32 | column & 0xFFFFFFFFL;
		//int row = (int)(key >> 32);
		//int column = (int)key;
		
		if (!cells.containsKey(key)) {
			if (!createIfNotExist)
				return null;
			cells.put(key, new Cell(this, row, column));
		}
		
		return cells.get(key);
	}
	
    public Cell getCell(String address, Cell contextCell)
    {
    	int r, c;
    	String[] els = address.toUpperCase().split("C");
    	//System.out.println(address);
    	org.junit.Assert.assertEquals('R', els[0].charAt(0));
    	
    	//relative row address without offset
    	if (els[0].length() == 1) {
    		r = contextCell.getRow();
    	}
    	//relative row address
    	else if (els[0].charAt(1) == '[')
    		r = contextCell.getRow() + Integer.valueOf(els[0].substring(2, els[0].length() - 1));
    	else
    		r = Integer.valueOf(els[0].substring(1));
    	
    	//relative column address without offset
    	if (els.length == 1 || els[1].length() == 0) {
    		c = contextCell.getColumn();
    	}
    	//relative column address
    	else if (els[1].charAt(0) == '[')
    		c = contextCell.getColumn() + Integer.valueOf(els[1].substring(1, els[1].length() - 1));
    	else
    		c = Integer.valueOf(els[1]);
    	
    	return getCell(r, c);
    }
>>>>>>> 74d1ba7ec3738bcada746293a10eb3296363d500

}
