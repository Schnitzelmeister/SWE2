package at.ac.univie.swe2.SS2017.team403;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationTargetException;
import java.util.TreeMap;

/**
 * This class is used to handle all operations connected to a worksheet. A
 * worksheet-object contains either one or more cells and is part of a workbook.
 * 
 */
public class Worksheet implements Externalizable {

	private transient int worksheetId;
	private TreeMap<Long, Cell> worksheetCells = new TreeMap<Long, Cell>();
	private String worksheetName;
	private Workbook parentWorkbook;
	private WorksheetRenameCallback worksheetRenameCallback = null;
	private int furthestRowUsed = 1;
	private int furthestColumnUsed = 1;

	/**
	 * This constructor is used to set a worksheet.
	 * 
	 * @param name
	 *            -> worksheetName
	 * @param workbook
	 *            -> this workbook contains the worksheet
	 * @param worksheetRenameCallback
	 *            -> is part of the observer pattern
	 * 
	 */
	public Worksheet(String name, Workbook workbook, WorksheetRenameCallback worksheetRenameCallback) {
		this.worksheetId = workbook.generateNewId();
		this.worksheetName = name;
		this.parentWorkbook = workbook;
		this.worksheetRenameCallback = worksheetRenameCallback;
	}

	Worksheet(Worksheet worksheet, Workbook workbook, WorksheetRenameCallback worksheetRenameCallback) {
		this.worksheetId = workbook.generateNewId();
		this.worksheetName = worksheet.getWorksheetName();
		this.parentWorkbook = workbook;
		this.worksheetRenameCallback = worksheetRenameCallback;

		for (Cell c : worksheet.worksheetCells.values())
			this.worksheetCells.put(getUniqueCellKey(c.getCellRow(), c.getCellColumn()), new Cell(c, this));
	}

	int getId() {
		return worksheetId;
	}

	public String getWorksheetName() {
		return worksheetName;
	}

	/**
	 * If the worksheetname exists, an exception is raised. Where this is not
	 * the case, the worktsheet will be renamed and the observer pattern will be
	 * used.
	 * 
	 * @param name
	 *            worksheetname
	 */
	public void setWorksheetName(String name) {
		if (this.worksheetName.equals(name))
			return;

		// check if sheet with new name exists
		if (parentWorkbook.getWorksheets().containsKey(name))
			throw new IllegalArgumentException("Sheet with name " + name + " already exists");

		// check if diagram with new name exists
		if (parentWorkbook.getDiagrams().containsKey(name))
			throw new IllegalArgumentException("Diagram with name " + name + " already exists");

		String oldName = this.worksheetName;
		this.worksheetName = name;

		if (worksheetRenameCallback != null) {
			worksheetRenameCallback.afterWorksheetRenamed(oldName, this);
		}
	}

	public Workbook getParentWorkbook() {
		return parentWorkbook;
	}

	public Area getMaxUsedArea() {
		return new Area(getCell(1, 1), getCell(furthestRowUsed, furthestColumnUsed));
	}

	public Cell getCell(int row, int column) {
		return getCell(row, column, true);
	}

	/**
	 * This method checks the availability of a cell by a cell-key check. If
	 * there is a similar key it returns the existing cell, otherwise it returns
	 * a new cell.
	 * 
	 * @param row
	 *            cell row
	 * @param column
	 *            cell column
	 * @param createIfNotExist
	 *            wenn cell not exists in Buffer, the Method create it in put in
	 *            Buffer
	 * @return returns existing/new cell
	 */
	public Cell getCell(int row, int column, boolean createIfNotExist) {
		if (row <= 0 || column <= 0)
			throw new IllegalArgumentException(
					"Invalid row, column for getCell(" + row + ", " + column + "). They must be positiv");

		long key = getUniqueCellKey(row, column);

		if (createIfNotExist) {
			if (row > furthestRowUsed) {
				furthestRowUsed = row;
			}
			if (row > furthestColumnUsed) {
				furthestColumnUsed = column;
			}
		}

		if (!worksheetCells.containsKey(key)) {
			if (!createIfNotExist) {
				return null;
			}
			worksheetCells.put(key, new Cell(this, row, column));
		}

		return worksheetCells.get(key);
	}

	private long getUniqueCellKey(int row, int column) {
		return (long) row << 32 | column & 0xFFFFFFFFL;
	}

	/**
	 * This method is used to get a Cell which could contain references. We use
	 * the R1C1-notation, that means we have to split up the reference in two
	 * expressions. If there isn't any row reference we will take only the
	 * row-position. If there is a row reference which starts with a square
	 * bracket we will take the row-position and the complete formula. The same
	 * applies to columns. If there isn't any column reference we will take only
	 * the column-position. If there is a column reference which starts with a
	 * square bracket we will take the column-position and the complete formula.
	 * 
	 * @param cellReferences
	 *            cell-references
	 * @param cellContext
	 *            selected cell
	 * @return returns a cell with/without references
	 */
	public Cell getCell(String cellReferences, Cell cellContext) {
		int row, column;
		String[] cellRefUpperCase = cellReferences.toUpperCase().split("C");

		org.junit.Assert.assertEquals('R', cellRefUpperCase[0].charAt(0));

		if (cellRefUpperCase[0].length() == 1) {
			row = cellContext.getCellRow();
		} else if (cellRefUpperCase[0].charAt(1) == '[') {
			row = cellContext.getCellRow()
					+ Integer.valueOf(cellRefUpperCase[0].substring(2, cellRefUpperCase[0].length() - 1));
		} else {
			row = Integer.valueOf(cellRefUpperCase[0].substring(1));
		}

		if (cellRefUpperCase.length == 1 || cellRefUpperCase[1].length() == 0) {
			column = cellContext.getCellColumn();
		} else if (cellRefUpperCase[1].charAt(0) == '[') {
			column = cellContext.getCellColumn()
					+ Integer.valueOf(cellRefUpperCase[1].substring(1, cellRefUpperCase[1].length() - 1));
		} else {
			column = Integer.valueOf(cellRefUpperCase[1]);
		}

		return getCell(row, column);
	}

	public void calculate() {
		for (Cell c : this.worksheetCells.values())
			if (c.getFormula() != null)
				c.setFormula(c.getFormula());
	}

	// Externalizable
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(this.worksheetCells);
	}

	// Externalizable
	@SuppressWarnings("unchecked")
	public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
		this.worksheetCells = (TreeMap<Long, Cell>) in.readObject();
	}

}
