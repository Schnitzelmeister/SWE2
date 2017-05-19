package at.ac.univie.swe2.SS2017.team403;

public abstract class Diagram {

	protected String name;
	protected Workbook parent;
	protected DiagramChangedCallback diagramChangedCallback = null;

	Diagram(String name, Workbook wbk, DiagramChangedCallback diagramChangedCallback) {
		this.name = name;
		this.parent = wbk;
		this.diagramChangedCallback = diagramChangedCallback;
	}
	
	public String getName() { return name; }
	public void setName(String name) throws IllegalArgumentException
	{
		boolean worksheetExists = true;
		//check if sheet with new name exists
		try {
			parent.getSheet(name);
			worksheetExists = true;
		}
		catch(IllegalArgumentException e) {
			worksheetExists = false;
		}
		
		if (worksheetExists)
			throw new IllegalArgumentException("Sheet with name " + name + " already exists");
		
		String oldName = this.name;
		this.name = name;
		
		//inform workbook
		if (diagramChangedCallback != null)
			diagramChangedCallback.AfterDiagramRenamed(oldName, this);
		
	}
}
