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

/**
 * This class is used to handle all operations connected to the workbook-object.
 * A workbook-object contains either one or more worksheets.
 * We use transient variables which shouldn't be serialized e.g. id or listener-list.
 * listner-list contains all observer.
 */
public class Workbook implements Serializable {
	private static final long serialVersionUID = 1L;

	private transient AtomicInteger workbookId = new AtomicInteger(0);

	public int generateNewId() {
		return workbookId.incrementAndGet();
	}

	
	private transient List<WorkbookListener> listeners = new ArrayList<WorkbookListener>();

	public void addListener(WorkbookListener listener) {
		listeners.add(listener);
	}

	/**
	 * This method removes observer from the observer-list.
	 * @param listener -> observer
	 */
	public void removeListener(WorkbookListener listener) {
		for (int i = listeners.size() - 1; i <= 0; --i) {
			if (listeners.get(i) == listener)
				listeners.remove(i);
		}
	}

	
	private TreeMap<String, Worksheet> worksheetCollection = new TreeMap<String, Worksheet>();

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
	 * @param name -> worksheetname
	 * @return -> returns a worksheet
	 */
	public Worksheet addSheet(String name) {
		if (worksheetCollection.containsKey(name)) {
			throw new IllegalArgumentException("Sheet with name " + name + " already exists");	
		}

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

	/**
	 * Here you can rename a worksheet.
	 * If the worksheet and the oldname of a worksheet exist, the oldname 
	 * could be removed and a new one could be added.
	 * Finally all observers will be informed.
	 * 
	 * @param worksheetOldName -> old name of a worksheet
	 * @param sheet -> worksheet that should be renamed
	 */
	private void renameSheet(String worksheetOldName, Worksheet sheet) {
		if (worksheetCollection.containsKey(worksheetOldName) && worksheetCollection.get(worksheetOldName) == sheet) {
			worksheetCollection.remove(worksheetOldName);
			worksheetCollection.put(sheet.getWorksheetName(), sheet);

			for (WorkbookListener l : listeners)
				l.afterWorksheetRenamed(worksheetOldName, sheet.getWorksheetName());
		}
	}

	/**
	 * This method removes worksheets.
	 * If the worksheet doesn't exist, an exception is raised.
	 * Finally observers will be informed.
	 * 
	 * @param name -> name of new worksheet
	 */
	public void removeSheet(String name) {
		if (!worksheetCollection.containsKey(name))
			throw new IllegalArgumentException("Sheet with name " + name + " does not exist");

		worksheetCollection.remove(name);

		for (WorkbookListener l : listeners)
			l.afterWorksheetRemoved(name);
	}

	
	private TreeMap<String, Diagram> diagramCollection;

	public Diagram getDiagram(String name) {
		return diagramCollection.get(name);
	}

	/**
	 * This method is used to add a diagram. 
	 * If the diagram-name exists, an exception is raised.
	 * Where this is not the case, a new diagram will be created and
	 * observers will be informed.
	 * 
	 * @param name -> diagram-name
	 * @param cls -> diagram-class
	 * @return -> returns a diagram
	 */
	public Diagram addDiagram(String name, Class<? extends Diagram> cls) {
		if (diagramCollection.containsKey(name)) {
			throw new IllegalArgumentException("Diagram with name " + name + " already exists");		
		}

		@SuppressWarnings("unchecked")
		Constructor<? extends Diagram> diagramConstructor = (Constructor<? extends Diagram>) cls.getConstructors()[0];

		Object[] constructorArguments = new Object[3];
		constructorArguments[0] = name;
		constructorArguments[1] = this;
		constructorArguments[2] = new DiagramChangedCallback() {
			@Override
			public void afterDiagramRenamed(String diagramOldName, Diagram diagram) {
				renameDiagram(diagramOldName, diagram);
			}

			@Override
			public void afterDiagramChanged(String diagramName) {
				changeDiagram(diagramName);
			}
		};

		Diagram newDiagram;
		try {
			newDiagram = (Diagram) diagramConstructor.newInstance(new Object[] { constructorArguments });
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to create diagram " + cls.getTypeName());
		}

		diagramCollection.put(name, newDiagram);

		for (WorkbookListener l : listeners)
			l.afterDiagramAdded(name);

		return diagramCollection.get(name);
	}

	/**
	 * This method allows you to rename a diagram.
	 * If the diagram and the oldname of a diagram exist, the oldname 
	 * could be removed and a new one could be added.
	 * Finally all observers will be informed.
	 * 
	 * @param diagramOldName -> old name of a diagram
	 * @param diagram -> diagram that should be renamed
	 */
	private void renameDiagram(String diagramOldName, Diagram diagram) {
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
	 * @param diagramName -> name of diagram
	 */
	private void changeDiagram(String diagramName) {
		if (diagramCollection.containsKey(diagramName)) {
			// inform observers
			for (WorkbookListener l : listeners)
				l.afterDiagramChanged(diagramName);
		}
	}

	/**
	 * This method removes diagramss.
	 * If the diagram doesn't exist, an exception is raised.
	 * Finally all observers will be informed.
	 * 
	 * @param name -> name of new worksheet
	 */
	public void removeDiagram(String name) {
		if (!diagramCollection.containsKey(name)){ 
			throw new IllegalArgumentException("Diagram with name " + name + " does not exist");			
		}

		diagramCollection.remove(name);

		for (WorkbookListener l : listeners)
			l.afterDiagramRemoved(name);
	}

	
	/** dependencies/dependents for Formula-Calculation */
	private transient TreeMap<Area, TreeSet<Cell>> dependenciesOfArea = new TreeMap<Area, TreeSet<Cell>>(
			new Area.AreaComparator());
	private transient TreeMap<Cell, TreeSet<Area>> dependenciesOfCell = new TreeMap<Cell, TreeSet<Area>>(
			new Cell.CellComparator());

	/**
	 * This method removes all dependencies of different areas where a specific cell is involved.
	 * 
	 * @param cell -> cell with reference-dependencies  
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
	 * @param cell -> selected cell
	 * @param precedent -> cell-dependency
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

	
	private TreeSet<Cell> dynamicCells = new TreeSet<Cell>(new Cell.CellComparator());

	void calculateReferenceDependencies(Cell cell) throws IllegalArgumentException {
		// empty dependency cells
		dynamicCells.clear();

		dynamicCells.add(cell);

		// get dependencies
		try {
			_calculateDependencies(cell);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(
					e.getMessage() + ", source cell R" + cell.getCellRow() + "C" + cell.getCellColumn());
		}

		// inform observers
		for (Cell c : dynamicCells)
			for (WorkbookListener l : listeners)
				l.afterCellChanged(c.getParentWorksheet().getWorksheetName(), c.getCellRow(), c.getCellColumn(),
						c.getCellValue());

		// empty dependencies
		dynamicCells.clear();
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
	}
}
