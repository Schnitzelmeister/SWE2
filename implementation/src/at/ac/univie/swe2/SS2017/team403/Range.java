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
	/**
	 * to get a range by giving the cell reference (for example: 'worksheet1!A1B1:A2B2;worksheet2!A1C1:A3C2'),an object of cell class and a Workbook object
	 * @param cellReferences  cell reference (for example: 'worksheet1!A1B1:A2B2;worksheet2!A1C1:A3C2')
	 * @param cellContext a object with cell reference
	 * @param wbk is the object of the workbook  that contains the cell within the cell reference
	 * @return an object of the Range class.
	 */
	static Range getRangeByAddress(String cellReferences, Cell cellContext, Workbook wbk) {
		
		Worksheet worksheet = null;
		if (cellContext != null)
			worksheet = cellContext.getParentWorksheet();

		Range worksheetRange = new Range();
		
		for (String s : cellReferences.split(";")) {
			String singleCellReference = s;

			if (singleCellReference.indexOf('!') >= 0) {
				String wshtName = singleCellReference.substring(0, singleCellReference.indexOf('!'));
				if (wshtName.substring(0, 1).equals("'")) {
					wshtName = wshtName.substring(1, wshtName.length() - 1);					
				}
				if (!wbk.getWorksheets().containsKey(wshtName)) {
					throw new IllegalArgumentException("Worksheet " + wshtName + " doesn't exists, " + cellReferences);					
				}
				worksheet = wbk.getWorksheet(wshtName);
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
