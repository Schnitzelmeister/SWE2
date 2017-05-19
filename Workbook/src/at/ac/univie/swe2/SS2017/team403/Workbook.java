package at.ac.univie.swe2.SS2017.team403;

import java.util.TreeMap;
import java.util.TreeSet;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.Serializable;
import java.lang.reflect.Constructor;

public class Workbook implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	//id Generation
	private transient AtomicInteger counter = new AtomicInteger(0);
	int getNewId() { return counter.incrementAndGet(); }
	
	//listeners
	private transient List<WorkbookListener> listeners = new ArrayList<WorkbookListener>();
	public void addListener(WorkbookListener listener) { listeners.add(listener); }
	public void removeListener(WorkbookListener listener)
	{
		for (int i = listeners.size()-1; i <= 0; --i) {
			if (listeners.get(i) == listener)
				listeners.remove(i);
		}
	}
	
	
	//contained Worksheets
	private TreeMap<String, Worksheet> sheets = new TreeMap<String, Worksheet>();
	public TreeMap<String, Worksheet> getSheets()
	{
		return sheets;		
	}
	
	public Worksheet getSheet(String name)
	{
		return sheets.get(name);		
	}
	
	public Worksheet addSheet(String name) throws IllegalArgumentException {
		if (sheets.containsKey(name))
			throw new IllegalArgumentException("Sheet with name " + name + " already exists");
		
		sheets.put(name, new Worksheet(name, this, new WorksheetRenameCallback() {
            @Override
            public void AfterWorksheetRenamed(String worksheetOldName, Worksheet sheet) {
            	renameSheet(worksheetOldName, sheet);
            }
        }));
		
		// inform observers
		for(WorkbookListener l : listeners)
			l.AfterWorksheetAdded(name);

		return sheets.get(name);
	}
	
	private void renameSheet(String worksheetOldName, Worksheet sheet) 
	{
		if (sheets.containsKey(worksheetOldName) && sheets.get(worksheetOldName) == sheet)
		{
			sheets.remove(worksheetOldName);
			sheets.put(sheet.getName(), sheet);
			
			// inform observers
			for(WorkbookListener l : listeners)
				l.AfterWorksheetRenamed(worksheetOldName, sheet.getName());
		}
	}

	public void removeSheet(String name)
	{
		if (!sheets.containsKey(name))
			throw new IllegalArgumentException("Sheet with name " + name + " does not exist");
		
		sheets.remove(name);
		
		// inform observers
		for(WorkbookListener l : listeners)
			l.AfterWorksheetRemoved(name);
	}
	
	//contained Diagrams
	private TreeMap<String, Diagram> diagrams;
	
	public Diagram getDiagram(String name)
	{
		return diagrams.get(name);		
	}
	
	public Diagram addDiagram(String name, Class<? extends Diagram> cls) throws IllegalArgumentException {
		if (diagrams.containsKey(name))
			throw new IllegalArgumentException("Diagram with name " + name + " already exists");
		
		
		@SuppressWarnings("unchecked")
		Constructor<? extends Diagram> ctor = (Constructor<? extends Diagram>)cls.getConstructors()[0];
		
		Object[] ctorArgument = new Object[3];
		ctorArgument[0] = name;
		ctorArgument[1] = this;
		ctorArgument[2] = new DiagramChangedCallback() {
            @Override
            public void AfterDiagramRenamed(String diagramOldName, Diagram diagram) {
            	renameDiagram(diagramOldName, diagram);
            }

            @Override
            public void AfterDiagramChanged(String diagramName) {
            	changeDiagram(diagramName);
            }};
        
		Diagram newDiagram;
		try {
			newDiagram = (Diagram)ctor.newInstance(new Object[] { ctorArgument });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalArgumentException("Failed to create diagram " + cls.getTypeName());
		}
		
		diagrams.put(name, newDiagram);
		
		// inform observers
		for(WorkbookListener l : listeners)
			l.AfterDiagramAdded(name);

		return diagrams.get(name);
	}
	
	private void renameDiagram(String diagramOldName, Diagram diagram) 
	{
		if (diagrams.containsKey(diagramOldName) && diagrams.get(diagramOldName) == diagram)
		{
			diagrams.remove(diagramOldName);
			diagrams.put(diagram.getName(), diagram);
			
			// inform observers
			for(WorkbookListener l : listeners)
				l.AfterDiagramRenamed(diagramOldName, diagram.getName());
		}
	}

	private void changeDiagram(String diagramName) 
	{
		if (diagrams.containsKey(diagramName))
		{
			// inform observers
			for(WorkbookListener l : listeners)
				l.AfterDiagramChanged(diagramName);
		}
	}

	public void removeDiagram(String name)
	{
		if (!diagrams.containsKey(name))
			throw new IllegalArgumentException("Diagram with name " + name + " does not exist");
		
		diagrams.remove(name);
		
		// inform observers
		for(WorkbookListener l : listeners)
			l.AfterDiagramRemoved(name);
	}
	
	//dependencies/precedents for Formula-Calculation
	private transient TreeMap<Area, TreeSet<Cell> > dependencies = new TreeMap<Area, TreeSet<Cell> >(new Area.AreaComparator());
	private transient TreeMap<Cell, ArrayList<Area> > precedents = new TreeMap<Cell, ArrayList<Area> >(new Cell.CellComparator());
		
	void removeDependancy(Cell cell) {
		if (precedents.containsKey(cell)) {
			for ( Area dep : precedents.get(cell) ) {
				dependencies.get(dep).remove(cell);
				if (dependencies.get(dep).size() == 0)
					dependencies.remove(dep);
			}
			precedents.remove(cell);
		}
	}
	
	void addDependency(Cell cell, Expression exp, Area precedent) {
		if (!precedents.containsKey(cell)) {
			precedents.put(cell, new ArrayList<Area>() );
		}
		precedents.get(cell).add(precedent);

		if (!dependencies.containsKey(precedent)) {
			dependencies.put(precedent, new TreeSet<Cell>( new Cell.CellComparator() ) );
		}
		dependencies.get(precedent).add(cell);
	}

	void addDependency(Cell cell, Expression exp, Cell precedent) {
		addDependency(cell, exp, new Area(precedent));
	}
	
	//recalculated cells - dynamic TreeSet
	private TreeSet<Cell> calcCells = new TreeSet<Cell>(new Cell.CellComparator());
	
	void calculateDependencies(Cell cell) throws IllegalArgumentException
	{
		//empty dependencies
		calcCells.clear();
		
		//get dependencies
		try {
			_calculateDependencies(cell);
		}
		catch(IllegalArgumentException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e.getMessage() + ", source cell R"+cell.getRow()+"C"+cell.getColumn());
		}
		
		//calculate dependencies
		for (Cell c : calcCells)
			c.calculate();
		
		//empty dependencies
		calcCells.clear();
	}

	private void _calculateDependencies(Cell cell) throws IllegalArgumentException
	{
		for(Iterator< Map.Entry<Area, TreeSet<Cell> > > idep = dependencies.entrySet().iterator(); idep.hasNext(); ) {
			Map.Entry<Area, TreeSet<Cell> > e = idep.next();
			
			//get only overlapped with cells ranges
			if ( e.getKey().getParent() != cell.getParent() )
				continue;

			if ( e.getKey().getR1() > cell.getRow() )
				return;

			if (e.getKey().getC1() > cell.getColumn()
					|| e.getKey().getR2() < cell.getRow()
					|| e.getKey().getC2() < cell.getColumn()  )
				continue;

			for(Cell c : e.getValue() ) {
				//cellular cell
				if (calcCells.contains(c))
					throw new IllegalArgumentException("Circular reference R" + c.getRow() + "C" + c.getColumn());
				calcCells.add(c);
			}
		}
	}
}
