package at.ac.univie.swe2.SS2017.team403;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class CustomTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<String[]> list = null;

	Worksheet sheet = null;

	public CustomTableModel(Worksheet sheet) {
		this.sheet = sheet;
	}

	@Override
	public String getColumnName(int index) {
		return "C" + (index + 1);
	}

	@Override
	public int getColumnCount() {
		System.out.println("getcolumncount wurde aufgerufen");

		return sheet.getMaxUsedArea().getLastColumn();
	}

	@Override
	public int getRowCount() {
		System.out.println("getrowcount wurde aufgerufen");

		return sheet.getMaxUsedArea().getLastRow();
	}

	@Override
	public Object getValueAt(int row, int column) {

		// return list.get(row)[column]; // TODO
		Cell cell = sheet.getCell(row + 1, column + 1);
		return cell.getCellValue();
	}

	public String getWorksheetName() {
		return sheet.getWorksheetName();
	}

	public boolean isCellEditable(int row, int col) {
		return true;
	}
}
