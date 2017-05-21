package at.ac.univie.swe2.SS2017.team403;

import java.util.TreeMap;

public class Worksheet {

	private transient int id;
	private TreeMap<Long, Cell> cells = new TreeMap<Long, Cell>();
	private String name;
	private Workbook parent;
	private WorksheetRenameCallback worksheetRenameCallback = null;
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
		
	}
	
	public Workbook getParent() { return parent; }
		
	//maximal Used range on this Worksheet
	public Area getUsedArea() { return new Area( getCell(1,1), getCell(usedAreaR, usedAreaC) ); }
	
	public Cell getCell(int row, int column) throws IllegalArgumentException
	{
		return getCell(row, column, true);
	}
	
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

}
