package at.ac.univie.swe2.SS2017.team403;

import java.util.TreeMap;

public class Range {

	private TreeMap<Area, Integer> worksheetAreas = new TreeMap<Area, Integer>(new Area.AreaComparator());
	private TreeMap<Cell, Integer> worksheetCells = new TreeMap<Cell, Integer>(new Cell.CellComparator());

	public TreeMap<Area, Integer> getWorksheetAreas() {
		return worksheetAreas;
	}

	public TreeMap<Cell, Integer> getWorksheetCells() {
		return worksheetCells;
	}

	/**
	 * This is a method for creating cell references to rows and columns.
	 * 
	 * @param cellReferences
	 * @param cellContext
	 * @return
	 */
	static Range getRangeByAddress(String cellReferences, Cell cellContext) {
		
		Worksheet worksheet = cellContext.getParentWorksheet();
		Range worksheetRange = new Range();
		
		for (String s : cellReferences.split(";")) {
			String singleCellReference = s;

			if (singleCellReference.indexOf('!') >= 0) {
				String wshtName = singleCellReference.substring(0, singleCellReference.indexOf('!'));
				if (wshtName.substring(0, 1) == "'") {
					wshtName = wshtName.substring(1, wshtName.length() - 2);					
				}
				if (!Application.getActiveWorkbook().getWorksheets().containsKey(wshtName)) {
					throw new IllegalArgumentException("Worksheet " + wshtName + " doesn't exists");					
				}
				worksheet = Application.getActiveWorkbook().getWorksheet(wshtName);
				singleCellReference = singleCellReference.substring(singleCellReference.indexOf('!') + 1);
			}

			if (worksheet == null) {
				throw new IllegalArgumentException("Undefined worksheet param " + cellReferences
						+ " to get Range object in method getRangeByAddress(String address, Cell contextCell)");		
			}

			if (singleCellReference.indexOf(':') > 0) {
				Area worksheetArea = new Area(singleCellReference, worksheet, cellContext);
				if (!worksheetRange.worksheetAreas.containsKey(worksheetArea)) {
					worksheetRange.worksheetAreas.put(worksheetArea, 1);					
				}
				else {
					worksheetRange.worksheetAreas.put(worksheetArea, worksheetRange.worksheetAreas.get(worksheetArea) + 1);					
				}
			} else {
				Cell worksheetCell = worksheet.getCell(singleCellReference, cellContext);
				if (!worksheetRange.worksheetCells.containsKey(worksheetCell)) {
					worksheetRange.worksheetCells.put(worksheetCell, 1);					
				}
				else {
					worksheetRange.worksheetCells.put(worksheetCell, worksheetRange.worksheetCells.get(worksheetCell) + 1);					
				}
			}
		}
		
		return worksheetRange;
	}

}
