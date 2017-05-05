package at.ac.univie.swe2.SS2017.team403;

import java.util.Map;
import java.util.TreeMap;

public class Worksheet {

	private TreeMap<Long, Cell> cells;
	
	public Cell getCell(int row, int column)
	{
		//convert two Integers to one Long
		long key = (long)row << 32 | column & 0xFFFFFFFFL;
		//int row = (int)(key >> 32);
		//int column = (int)key;
		
		if (!cells.containsKey(key)) {
			cells.put(key, new Cell(this, key));
		}
		
		return cells.get(key);
	}
}
