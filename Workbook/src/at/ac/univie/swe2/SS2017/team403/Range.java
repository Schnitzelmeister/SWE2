package at.ac.univie.swe2.SS2017.team403;
import java.util.TreeSet;

public class Range {

	private TreeSet<Area> areas = new TreeSet<Area>(new Area.AreaComparator());
	private TreeSet<Cell> cells = new TreeSet<Cell>(new Cell.CellComparator());

	public TreeSet<Area> getAreas() { return areas; }
	public TreeSet<Cell> getCells() { return cells; }
	
	static Range getRangeByAddress(String address, Cell contextCell) {
		Worksheet worksheet = contextCell.getParent();
        Range ret = new Range();
		for (String s : address.split(";"))
        {
            String el = s;

            if (el.indexOf('!') >= 0)
            {
                String wshtName = el.substring(0, el.indexOf('!'));
                if (wshtName.substring(0, 1) == "'")
                    wshtName = wshtName.substring(1, wshtName.length() - 2);
                
                if (!Application.getActiveWorkbook().getSheets().containsKey(wshtName))
        			throw new IllegalArgumentException("Worksheet " + wshtName + " doesn't exists");

                worksheet = Application.getActiveWorkbook().getSheet(wshtName);
                el = el.substring(el.indexOf('!') + 1);
            }

            if (worksheet == null)
    			throw new IllegalArgumentException("Undefined worksheet param " + address + " to get Range object in method Range(string address, Worksheet worksheet = null)");

            if (el.indexOf(':') > 0)
                ret.areas.add(new Area(el, worksheet, contextCell));
            else
                ret.cells.add(worksheet.getCell(el, contextCell));
        }
		return ret;
	}

	
}
