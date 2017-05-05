package at.ac.univie.swe2.SS2017.team403;

import java.util.TreeMap;
import java.util.ArrayList;

public class Workbook {
	private TreeMap<String, Worksheet> sheets;
	private ArrayList<ArrayList> diagrams;
	
	public Worksheet getSheet(String name)
	{
		return sheets.get(name);		
	}
}
