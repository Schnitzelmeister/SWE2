package at.ac.univie.swe2.SS2017.team403;

import java.util.Comparator;

public class Area {
	private Worksheet parent;
	public Worksheet getParent() { return parent; }

	private int r1, r2, c1, c2;
	
	public int getR1() { return r1; }
	public int getR2() { return r2; }
	public int getC1() { return c1; }
	public int getC2() { return c2; }
	
	public Area(Cell cellTopLeft, Cell cellBottomRight) {
		parent = cellTopLeft.getParent();
		r1 = cellTopLeft.getRow();
		c1 = cellTopLeft.getColumn();
		r2 = cellBottomRight.getRow();
		c2 = cellBottomRight.getColumn();
	}

	public Area(Cell cell) {
		parent = cell.getParent();
		r1 = cell.getRow();
		c1 = cell.getColumn();
		r2 = r1;
		c2 = c1;
	}
	
    public Area(String address, Worksheet worksheet, Cell contextCell)
    {
    	parent = worksheet;
        String[] tmp = address.split(":");
        Cell cellTopLeft = worksheet.getCell(tmp[0], contextCell);
        Cell cellBottomRight = worksheet.getCell(tmp[1], contextCell);
		r1 = cellTopLeft.getRow();
		c1 = cellTopLeft.getColumn();
		r2 = cellBottomRight.getRow();
		c2 = cellBottomRight.getColumn();
    }

	
	//Comparator to Area class, we can order Areas
	static class AreaComparator implements Comparator<Area>
	{
		public int compare(Area r1, Area r2)
	    {
			int res = r1.parent.getId() - r2.parent.getId();
			if (res == 0) {
				res = r1.r1 - r2.r1;
				if (res == 0) {
					res = r1.c1 - r2.c1;
					if (res == 0) {
						res = r2.r2 - r1.r2;
						if (res == 0) {
							return r2.c2 - r1.c2;
						}
						else {
							return res;
						}

					}
					else {
						return res;
					}					
				}
				else {
					return res;
				}
			}
			else {
				return res;
			}
		}
	}

}
