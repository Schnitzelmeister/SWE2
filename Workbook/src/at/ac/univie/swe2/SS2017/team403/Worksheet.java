package at.ac.univie.swe2.SS2017.team403;

import java.util.TreeMap;

public class Worksheet {

	private transient int id;
	private TreeMap<Long, Cell> cells;
	private String name;
	private Workbook parent;
	private int usedRangeR = 1;
	private int usedRangeC = 1;
	
	
	Worksheet(String name, Workbook wbk) {
		this.id = Application.activeWorkbook.getNewId();
		this.name = name;
		this.parent = wbk;
	}
	
	int getId() { return id; }
	public String getName() { return name; }
	public Workbook getParent() { return parent; }
		
	//maximal Used range on this Worksheet
	public Range getUsedRange() { return new Range( getCell(1,1), getCell(usedRangeR, usedRangeC) ); }
	
	public Cell getCell(int row, int column)
	{
		if (row > usedRangeR)
			usedRangeR = row;
		if (row > usedRangeC)
			usedRangeC = column;
		
		//convert two Integers to one Long
		long key = (long)row << 32 | column & 0xFFFFFFFFL;
		//int row = (int)(key >> 32);
		//int column = (int)key;
		
		if (!cells.containsKey(key)) {
			cells.put(key, new Cell(this, row, column));
		}
		
		return cells.get(key);
	}
}
