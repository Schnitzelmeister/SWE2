package at.ac.univie.swe2.SS2017.team403;

// Workbook Listener
public interface WorkbookListener {
	void afterWorksheetAdded(String worksheetName);

	void afterWorksheetRemoved(String worksheetName);

	void afterWorksheetRenamed(String worksheetOldName, String worksheetNewName);

	void afterCellChanged(String worksheetName, int row, int column, Object newValue);

	void afterDiagramAdded(String diagramName);

	void afterDiagramRemoved(String diagramName);

	void afterDiagramRenamed(String diagramOldName, String diagramNewName);

	void afterDiagramChanged(String diagramName);

}

// Inernal Interface for Workbook-Worksheet Relation
interface WorksheetRenameCallback {
	void afterWorksheetRenamed(String worksheetOldName, Worksheet sheet);
}

// Inernal Interface for Workbook-Diagram Relation
interface DiagramChangedCallback {
	void afterDiagramRenamed(String diagramOldName, Diagram diagram);

	void afterDiagramChanged(String diagramName);

}
