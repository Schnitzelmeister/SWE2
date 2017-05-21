package at.ac.univie.swe2.SS2017.team403;

import java.util.TreeMap;

public class Range {

	private TreeMap<Area, Integer> areas = new TreeMap<Area, Integer>(new Area.AreaComparator());
	private TreeMap<Cell, Integer> cells = new TreeMap<Cell, Integer>(new Cell.CellComparator());

	public TreeMap<Area, Integer> getAreas() {
		return areas;
	}

	public TreeMap<Cell, Integer> getCells() {
		return cells;
	}

	/**
	 * This is a method for creating cell references to rows and columns.
	 * 
	 * @param address
	 * @param contextCell
	 * @return
	 */
	static Range getRangeByAddress(String address, Cell contextCell) {
		Worksheet worksheet = contextCell.getParentWorksheet();
		Range ret = new Range();
		for (String s : address.split(";")) {
			String el = s;

			if (el.indexOf('!') >= 0) {
				String wshtName = el.substring(0, el.indexOf('!'));
				if (wshtName.substring(0, 1) == "'")
					wshtName = wshtName.substring(1, wshtName.length() - 2);

				if (!Application.getActiveWorkbook().getSheets().containsKey(wshtName))
					throw new IllegalArgumentException("Worksheet " + wshtName + " doesn't exists");

				worksheet = Application.getActiveWorkbook().getSheet(wshtName);
				el = el.substring(el.indexOf('!') + 1);
			}

			if (worksheet == null)
				throw new IllegalArgumentException("Undefined worksheet param " + address
						+ " to get Range object in method getRangeByAddress(String address, Cell contextCell)");

			if (el.indexOf(':') > 0) {
				Area a = new Area(el, worksheet, contextCell);
				if (!ret.areas.containsKey(a))
					ret.areas.put(a, 1);
				else
					ret.areas.put(a, ret.areas.get(a) + 1);
			} else {
				Cell c = worksheet.getCell(el, contextCell);
				if (!ret.cells.containsKey(c))
					ret.cells.put(c, 1);
				else
					ret.cells.put(c, ret.cells.get(c) + 1);
			}
		}
		return ret;
	}

}
