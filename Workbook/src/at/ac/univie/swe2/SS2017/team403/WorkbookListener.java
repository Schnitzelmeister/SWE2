package at.ac.univie.swe2.SS2017.team403;

// Workbook Listener
public interface WorkbookListener {
	void AfterWorksheetAdded(String worksheetName);
	void AfterWorksheetRemoved(String worksheetName);
	void AfterWorksheetRenamed(String worksheetOldName, String worksheetNewName);
	void AfterCellChanged(String worksheetName, int row, int column);

	void AfterDiagramAdded(String diagramName);
	void AfterDiagramRemoved(String diagramName);
	void AfterDiagramRenamed(String diagramOldName, String diagramNewName);
	void AfterDiagramChanged(String diagramName);

}

// Inernal Interface for Workbook-Worksheet Relation
interface WorksheetRenameCallback {
	void AfterWorksheetRenamed(String worksheetOldName, Worksheet sheet);
}

//Inernal Interface for Workbook-Diagram Relation
interface DiagramChangedCallback {
	void AfterDiagramRenamed(String diagramOldName, Diagram diagram);
	void AfterDiagramChanged(String diagramName);
}
