package at.ac.univie.swe2.SS2017.team403;

import java.util.TreeMap;
import java.util.TreeSet;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * This class is used to handle all operations connected to the workbook-object.
 * A workbook-object contains either one or more worksheets.
 * We use transient variables which shouldn't be serialized e.g. id or listener-list.
 * listner-list contains all observer.
 */
public class Workbook implements Externalizable {
	public Workbook() {
		workbookId = new AtomicInteger(0);
		listeners = new ArrayList<WorkbookListener>();
		worksheetCollection = new TreeMap<String, Worksheet>();
		diagramCollection = new TreeMap<String, Diagram>();
		dependenciesOfArea = new TreeMap<Area, TreeSet<Cell>>(
					new Area.AreaComparator());
		dependenciesOfCell = new TreeMap<Cell, TreeSet<Area>>(
					new Cell.CellComparator());
		diagramDependencies = new TreeMap<Area, ArrayList<Diagram> >(new Area.AreaComparator());
	}
	
	private transient boolean autoCalculate = true;
	public boolean getAutoCalculate() {
		return autoCalculate;
	}
	
	public void setAutoCalculate(boolean autoCalculate) {
		this.autoCalculate = autoCalculate;
	}

	private transient AtomicInteger workbookId;

	int generateNewId() {
		return workbookId.incrementAndGet();
	}

	
	private transient List<WorkbookListener> listeners;

	public void addListener(WorkbookListener listener) {
		listeners.add(listener);
	}

	/**
	 * This method removes observer from the observer-list.
	 * @param listener  observer
	 */
	public void removeListener(WorkbookListener listener) {
		if (listeners.size() <= 0)
			return;
		
		for (int i = listeners.size() - 1; i < 0; --i) {
			if (listeners.get(i) == listener)
				listeners.remove(i);
		}
	}

	
	private TreeMap<String, Worksheet> worksheetCollection;

	public TreeMap<String, Worksheet> getWorksheets() {
		return worksheetCollection;
	}

	public Worksheet getWorksheet(String name) {
		return worksheetCollection.get(name);
	}

	public Integer numOfWorksheets() {
		return worksheetCollection.size();
	}
	
	/**
	 * Here you can add a new worksheet to the worksheet-Collection of a workbook.
	 * If the worksheetname exists, an exception is raised.
	 * Where this is not the case, a new worktsheet will be created and the
	 * observer will be informed.
	 * 
	 * @param name  worksheetname
	 * @return  returns a worksheet
	 */
	public Worksheet addSheet(String name) {
		if (worksheetCollection.containsKey(name)) {
			throw new IllegalArgumentException("Sheet with name " + name + " already exists");	
		}
		
		if (diagramCollection.containsKey(name))
			throw new IllegalArgumentException("Diagram with name " + name + " already exists");

		worksheetCollection.put(name, new Worksheet(name, this, new WorksheetRenameCallback() {
			@Override
			public void afterWorksheetRenamed(String worksheetOldName, Worksheet sheet) {
				renameSheet(worksheetOldName, sheet);
			}
		}));

		for (WorkbookListener l : listeners)
			l.afterWorksheetAdded(name);

		return worksheetCollection.get(name);
	}
	
	
	private Worksheet addSheet(Worksheet worksheet) throws IllegalArgumentException {
		if (worksheetCollection.containsKey(worksheet.getWorksheetName())) {
			throw new IllegalArgumentException("Sheet with name " + worksheet.getWorksheetName() + " already exists");	
		}
		
		if (diagramCollection.containsKey(worksheet.getWorksheetName()))
			throw new IllegalArgumentException("Diagram with name " + worksheet.getWorksheetName() + " already exists");

		worksheetCollection.put(worksheet.getWorksheetName(), new Worksheet(worksheet, this, new WorksheetRenameCallback() {
            @Override
            public void afterWorksheetRenamed(String worksheetOldName, Worksheet sheet) {
            	renameSheet(worksheetOldName, sheet);
            }
        }));
		
		// inform observers
		for(WorkbookListener l : listeners)
			l.afterWorksheetAdded(worksheet.getWorksheetName());

		return worksheetCollection.get(worksheet.getWorksheetName());
	}

	/**
	 * Here you can rename a worksheet.
	 * If the worksheet and the oldname of a worksheet exist, the oldname 
	 * could be removed and a new one could be added.
	 * Finally all observers will be informed.
	 * 
	 * @param worksheetOldName old name of a worksheet
	 * @param sheet  worksheet that should be renamed
	 */
	private void renameSheet(String worksheetOldName, Worksheet sheet) {
		if (worksheetCollection.containsKey(worksheetOldName) && worksheetCollection.get(worksheetOldName) == sheet) {
			worksheetCollection.remove(worksheetOldName);
			worksheetCollection.put(sheet.getWorksheetName(), sheet);

			TreeSet<Cell> changedCells = new TreeSet<Cell>(new Cell.CellComparator());
			
			//search for dependent cells and make their formula null
			for(Iterator< Map.Entry<Area, TreeSet<Cell> > > idep = dependenciesOfArea.entrySet().iterator(); idep.hasNext(); ) {
				Map.Entry<Area, TreeSet<Cell> > e = idep.next();
				
				//get only rename sheets ranges
				if ( e.getKey().getParent().getId() > sheet.getId() )
					break;
				else if ( e.getKey().getParent() != sheet )
					continue;

				//collect cells
				changedCells.addAll(e.getValue());
			}

			//set textValue = '+Formula
			for(Cell c : changedCells )
				c.setTextValue("'" + c.getFormula());
			changedCells.clear();

			TreeSet<String> changedDiagrams = new TreeSet<String>();
			for(Iterator< Map.Entry<Area, ArrayList<Diagram> > > idep = diagramDependencies.entrySet().iterator(); idep.hasNext(); ) {
				Map.Entry<Area, ArrayList<Diagram> > e = idep.next();
				
				//get only removed sheets ranges
				if ( e.getKey().getParent().getId() > sheet.getId() )
					break;
				else if ( e.getKey().getParent() != sheet )
					continue;

				for(Diagram d : e.getValue() ) {
					changedDiagrams.add(d.name);
				}
			}

			for(String c : changedDiagrams )
				this.removeDiagram(c);
			changedDiagrams.clear();

			
			for (WorkbookListener l : listeners)
				l.afterWorksheetRenamed(worksheetOldName, sheet.getWorksheetName());
		}
	}

	/**
	 * This method removes worksheets.
	 * If the worksheet doesn't exist, an exception is raised.
	 * Finally observers will be informed.
	 * 
	 * @param name  name of new worksheet
	 */
	public void removeSheet(String name) {
		if (!worksheetCollection.containsKey(name))
			throw new IllegalArgumentException("Sheet with name " + name + " does not exist");

		Worksheet sheet = worksheetCollection.get(name);		
		TreeSet<Cell> changedCells = new TreeSet<Cell>(new Cell.CellComparator());
		
		//search for dependent cells and make their formula null
		for(Iterator< Map.Entry<Area, TreeSet<Cell> > > idep = dependenciesOfArea.entrySet().iterator(); idep.hasNext(); ) {
			Map.Entry<Area, TreeSet<Cell> > e = idep.next();
			
			//get only removed sheets ranges
			if ( e.getKey().getParent().getId() > sheet.getId() )
				break;
			else if ( e.getKey().getParent() != sheet )
				continue;

			//collect cells
			changedCells.addAll(e.getValue());
		}
		
		//set textValue = '+Formula
		for(Cell c : changedCells )
			if (c.getParentWorksheet() != sheet)
				c.setTextValue("'" + c.getFormula());
		changedCells.clear();
		
		TreeSet<String> changedDiagrams = new TreeSet<String>();
		for(Iterator< Map.Entry<Area, ArrayList<Diagram> > > idep = diagramDependencies.entrySet().iterator(); idep.hasNext(); ) {
			Map.Entry<Area, ArrayList<Diagram> > e = idep.next();
			
			//get only removed sheets ranges
			if ( e.getKey().getParent().getId() > sheet.getId() )
				break;
			else if ( e.getKey().getParent() != sheet )
				continue;

			for(Diagram d : e.getValue() ) {
				changedDiagrams.add(d.name);
			}
		}

		for(String c : changedDiagrams )
			this.removeDiagram(c);
		changedDiagrams.clear();

		
		worksheetCollection.remove(name);

		for (WorkbookListener l : listeners)
			l.afterWorksheetRemoved(name);
	}

	
	private TreeMap<String, Diagram> diagramCollection;

	public TreeMap<String, Diagram> getDiagrams()
	{
		return diagramCollection;		
	}

	public Diagram getDiagram(String name) {
		return diagramCollection.get(name);
	}

	
	
	/**
	 * the method is used to add a new diagram and it has a name of diagram and an object of the DiagramCreater class as input
	 * @param name a String that contains the name of a diagram 
	 * @param creator a creator for diagramm
	 * @return an object of Diagram.
	 */
	public Diagram addDiagram(String name, DiagramCreator creator) {
		if (diagramCollection.containsKey(name)) {
			throw new IllegalArgumentException("Diagram with name " + name + " already exists");		
		}

		if (worksheetCollection.containsKey(name))
			throw new IllegalArgumentException("Sheet with name " + name + " already exists");

		Diagram newDiagram = creator.factoryMethod(name, this, new DiagramChangedCallback() {
				@Override
				public void afterDiagramRenamed(String diagramOldName, Diagram diagram) {
					renameDiagram(diagramOldName, diagram);
				}

				@Override
				public void afterDiagramChanged(String diagramName) {
					changeDiagram(diagramName);
				}
			});

		diagramCollection.put(name, newDiagram);

		for (WorkbookListener l : listeners)
			l.afterDiagramAdded(name);

		return diagramCollection.get(name);
	}

	private void addDiagram(Diagram diagram) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if (diagramCollection.containsKey(diagram.getName())) {
			throw new IllegalArgumentException("Diagram with name " + diagram.getName() + " already exists");		
		}

		if (worksheetCollection.containsKey(diagram.getName()))
			throw new IllegalArgumentException("Sheet with name " + diagram.getName() + " already exists");

		@SuppressWarnings("unchecked")
		Constructor<? extends Diagram> ctor = (Constructor<? extends Diagram>)diagram.getClass().getDeclaredConstructor(Diagram.class, Workbook.class, DiagramChangedCallback.class);
		
		Object[] ctorArgument = new Object[3];
		ctorArgument[0] = diagram;
		ctorArgument[1] = this;
		ctorArgument[2] = new DiagramChangedCallback() {
            @Override
            public void afterDiagramRenamed(String diagramOldName, Diagram diagram) {
            	renameDiagram(diagramOldName, diagram);
            }

            @Override
            public void afterDiagramChanged(String diagramName) {
            	changeDiagram(diagramName);
            }};
    
        Diagram newDiagram = (Diagram)ctor.newInstance(ctorArgument);

        diagramCollection.put(diagram.getName(), newDiagram);

	}
	
	
	/**
	 * This method allows you to rename a diagram.
	 * If the diagram and the oldname of a diagram exist, the oldname 
	 * could be removed and a new one could be added.
	 * Finally all observers will be informed.
	 * 
	 * @param diagramOldName  old name of a diagram
	 * @param diagram  diagram that should be renamed
	 */
	private void renameDiagram(String diagramOldName, Diagram diagram) {
		if (diagramCollection.containsKey(diagram.getName())) {
			throw new IllegalArgumentException("Diagram with name " + diagram.getName() + " already exists");		
		}

		if (worksheetCollection.containsKey(diagram.getName()))
			throw new IllegalArgumentException("Sheet with name " + diagram.getName() + " already exists");

		if (diagramCollection.containsKey(diagramOldName) && diagramCollection.get(diagramOldName) == diagram) {
			diagramCollection.remove(diagramOldName);
			diagramCollection.put(diagram.getName(), diagram);

			for (WorkbookListener l : listeners)
				l.afterDiagramRenamed(diagramOldName, diagram.getName());
		}
	}

	/**
	 * This method allows you to modify diagrams.
	 * If the diagram exists, the diagram will be modified.
	 * Finally all observers will be informed.
	 * 
	 * @param diagramName  name of diagram
	 */
	private void changeDiagram(String diagramName) {
		if (diagramCollection.containsKey(diagramName)) {
			// inform observers
			for (WorkbookListener l : listeners)
				l.afterDiagramChanged(diagramName);
		}
	}

	/**
	 * This method removes diagrams.
	 * If the diagram doesn't exist, an exception is raised.
	 * Finally all observers will be informed.
	 * 
	 * @param name  name of new worksheet
	 */
	public void removeDiagram(String name) {
		if (!diagramCollection.containsKey(name)){ 
			throw new IllegalArgumentException("Diagram with name " + name + " does not exist");			
		}

		removeReferenceDependencies(diagramCollection.get(name));
		diagramCollection.remove(name);

		for (WorkbookListener l : listeners)
			l.afterDiagramRemoved(name);
	}

	
	/** dependencies/dependents for Formula-Calculation */
	private transient TreeMap<Area, TreeSet<Cell>> dependenciesOfArea;
	private transient TreeMap<Cell, TreeSet<Area>> dependenciesOfCell;

	private transient TreeMap<Area, ArrayList<Diagram> > diagramDependencies;

	/**
	 * This method removes all dependencies of different areas where a specific cell is involved.
	 * 
	 * @param cell  cell with reference-dependencies  
	 */
	void removeReferenceDependencies(Cell cell) {
		if (dependenciesOfCell.containsKey(cell)) {
			
			for (Area area : dependenciesOfCell.get(cell)) {
				dependenciesOfArea.get(area).remove(cell);
				
				if (dependenciesOfArea.get(area).size() == 0)
					dependenciesOfArea.remove(area);
			}
			
			dependenciesOfCell.remove(cell);
		}
	}

	/**
	 * This method adds a referenced dependency to a specific cell.
	 * If there isn't any dependency-field, it will be created.
	 * If there aren't any dependent-field, it will be created.
	 * 
	 * @param cell  selected cell
	 * @param precedent  cell-dependency
	 */
	void addDependency(Cell cell, Area precedent) {
		if (!dependenciesOfCell.containsKey(cell)) {
			dependenciesOfCell.put(cell, new TreeSet<Area>(new Area.AreaComparator()));
		}
		dependenciesOfCell.get(cell).add(precedent);

		if (!dependenciesOfArea.containsKey(precedent)) {
			dependenciesOfArea.put(precedent, new TreeSet<Cell>(new Cell.CellComparator()));
		}
		dependenciesOfArea.get(precedent).add(cell);
	}


	void addDependency(Cell cell, Cell precedent) {
		addDependency(cell, new Area(precedent));
	}

	void removeReferenceDependencies(Diagram diagram) {
		for(Iterator< Map.Entry<Area, ArrayList<Diagram> > > idep = diagramDependencies.entrySet().iterator(); idep.hasNext(); ) {
			Map.Entry<Area, ArrayList<Diagram> > e = idep.next();
			
			for (int j = e.getValue().size()-1; j >= 0; j--)
				if (e.getValue().get(j) == diagram)
					e.getValue().remove(j);
			
			if (e.getValue().size() == 0)
				idep.remove();				
		}
	}

	void addDependency(Diagram diagram, Area precedent) {
		if (!diagramDependencies.containsKey(precedent)) {
			diagramDependencies.put(precedent, new ArrayList<Diagram>() );
		}
		diagramDependencies.get(precedent).add(diagram);
		
	}
	
	private TreeSet<Cell> dynamicCells = new TreeSet<Cell>(new Cell.CellComparator());

	//recalculated diagrams - dynamic TreeSet
	private transient TreeSet<String> recalcDiagrams = new TreeSet<String>();

	void calculateReferenceDependencies(Cell cell) throws IllegalArgumentException {
		// empty dependency cells
		dynamicCells.clear();
		recalcDiagrams.clear();

		dynamicCells.add(cell);

		// get dependencies
		try {
			_calculateDependencies(cell);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(
					e.getMessage() + ", source cell " + cell.getCellReferences());
		}

		// inform observers
		for (Cell c : dynamicCells)
			for (WorkbookListener l : listeners)
				l.afterCellChanged(c.getParentWorksheet().getWorksheetName(), c.getCellRow(), c.getCellColumn(),
						c.getCellValue());

		// empty dependencies
		dynamicCells.clear();
		
		for(String d : recalcDiagrams) {
			changeDiagram(d);
		}
		recalcDiagrams.clear();
	}

	private void _calculateDependencies(Cell cell) throws IllegalArgumentException {
		for (Iterator<Map.Entry<Area, TreeSet<Cell>>> idep = dependenciesOfArea.entrySet().iterator(); idep.hasNext();) {
			Map.Entry<Area, TreeSet<Cell>> e = idep.next();

			// get only overlapped with cells ranges
			if (e.getKey().getParent() != cell.getParentWorksheet())
				continue;

			if (e.getKey().getFirstRow() > cell.getCellRow())
				return;

			if (e.getKey().getFirstColumn() > cell.getCellColumn() || e.getKey().getLastRow() < cell.getCellRow()
					|| e.getKey().getLastColumn() < cell.getCellColumn())
				continue;

			for (Cell c : e.getValue()) {

				// calculate dependencies
				c.calculateCellExpression();
				// cellular cell check
				if (dynamicCells.contains(c))
					throw new IllegalArgumentException("Circular reference " + c.getCellReferences());
				dynamicCells.add(c);

				_calculateDependencies(c);

			}
		}
		
		for(Iterator< Map.Entry<Area, ArrayList<Diagram> > > idep = diagramDependencies.entrySet().iterator(); idep.hasNext(); ) {
			Map.Entry<Area, ArrayList<Diagram> > e = idep.next();
			
			//get only overlapped with cells ranges
			if ( e.getKey().getParent().getId() > cell.getParentWorksheet().getId() )
				return;
			else if ( e.getKey().getParent() != cell.getParentWorksheet() )
				continue;

			if ( e.getKey().getFirstRow() > cell.getCellRow() )
				return;

			if (e.getKey().getFirstColumn() > cell.getCellColumn()
					|| e.getKey().getLastRow() < cell.getCellRow()
					|| e.getKey().getLastColumn() < cell.getCellColumn()  )
				continue;

			for(Diagram d : e.getValue() ) {
				recalcDiagrams.add(d.name);
			}
		}

	}
	
	public void calculate() {
		for (Worksheet w : this.worksheetCollection.values())
			w.calculate();
		for (Diagram d : this.diagramCollection.values())
			d.calculate();
	}
	
	
	//Externalizable
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(this.worksheetCollection.values().toArray(new Worksheet[this.worksheetCollection.size()]));
		out.writeObject(this.diagramCollection.values().toArray(new Diagram[this.diagramCollection.size()]));
	}

	//Externalizable
	@Override
	@SuppressWarnings("unchecked")
	public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
		Worksheet[] tempSheets = (Worksheet[])in.readObject();
		this.setAutoCalculate(false);
		
		for(Worksheet w : tempSheets)
			addSheet(w);
			
		Diagram[] tempDiagrams = (Diagram[] )in.readObject();
		for(Diagram d : tempDiagrams)
			try {
				addDiagram(d);
			} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				//Failed to deserialise object
				e.printStackTrace();
			}
	}

}
