package at.ac.univie.swe2.SS2017.team403;

import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.Serializable;
import java.util.ArrayList;

public class Workbook implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	//id Generation
	private transient AtomicInteger counter = new AtomicInteger(0);
	int getNewId() { return counter.incrementAndGet(); }
	
	//contained Worksheets
	private TreeMap<String, Worksheet> sheets;
	//contained Diagrams
	private ArrayList<Diagram> diagrams;
	
	//dependencies/precedents for Formula-Calculation
	private transient TreeMap<Range, TreeSet<Cell> > dependencies = new TreeMap<Range, TreeSet<Cell> >(new Range.RangeComparator());
	private transient TreeMap<Cell, ArrayList<Range> > precedents = new TreeMap<Cell, ArrayList<Range> >(new Cell.CellComparator());
		
	void RemoveDependancy(Cell cell) {
		if (precedents.containsKey(cell)) {
			for ( Range dep : precedents.get(cell) ) {
				dependencies.get(dep).remove(cell);
			}
			precedents.remove(cell);
		}
	}
	
	void AddDependency(Cell cell, Expression exp, Range precedent) {
		if (!precedents.containsKey(cell)) {
			precedents.put(cell, new ArrayList<Range>() );
		}
		precedents.get(cell).add(precedent);

		if (!dependencies.containsKey(precedent)) {
			dependencies.put(precedent, new TreeSet<Cell>( new Cell.CellComparator() ) );
		}
		dependencies.get(precedent).add(cell);

	}

	void AddDependency(Cell cell, Expression exp, Cell precedent) {
		AddDependency(cell, exp, new Range(precedent));
	}

	public Worksheet getSheet(String name)
	{
		return sheets.get(name);		
	}
	
	public Worksheet addSheet(String name) throws IllegalArgumentException {
		if (sheets.containsKey(name))
			throw new IllegalArgumentException("Sheet with name " + name + " already exists");
		
		sheets.put(name, new Worksheet(name, this));
		return sheets.get(name);
	}
}
