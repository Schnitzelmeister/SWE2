package at.ac.univie.swe2.SS2017.team403;

import java.util.Comparator;

public class Range {

	private Worksheet parent;
	private int r1, r2, c1, c2;
	
	public int getR1() { return r1; }
	public int getR2() { return r2; }
	public int getC1() { return c1; }
	public int getC2() { return c2; }
	
	public Range(Cell cellTopLeft, Cell cellBottomRight) {
		parent = cellTopLeft.getParent();
		r1 = cellTopLeft.getRow();
		c1 = cellTopLeft.getColumn();
		r2 = cellBottomRight.getRow();
		c2 = cellBottomRight.getColumn();
	}

	public Range(Cell cell) {
		parent = cell.getParent();
		r1 = cell.getRow();
		c1 = cell.getColumn();
		r2 = r1;
		c2 = c1;
	}

	
	//Comparator to Range class, we can order Ranges
	static class RangeComparator implements Comparator<Range>
	{
		public int compare(Range r1, Range r2)
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
