package at.ac.univie.swe2.SS2017.team403;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class CustomTableModel extends AbstractTableModel {
<<<<<<< HEAD
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<String[]> list = null;
=======
	Worksheet sheet = null;
>>>>>>> 74d1ba7ec3738bcada746293a10eb3296363d500

	public CustomTableModel(Worksheet sheet) {
		this.sheet = sheet;
	}

	@Override
	public String getColumnName(int index) {
		return "C"+(index+1);
	}

	@Override
	public int getColumnCount() {
		System.out.println("getcolumncount wurde aufgerufen");
		return sheet.getUsedArea().getC2();
	}
	

	@Override
	public int getRowCount() {
		System.out.println("getrowcount wurde aufgerufen");
		return sheet.getUsedArea().getR2();
	}

	@Override
	public Object getValueAt(int row, int column) {
<<<<<<< HEAD
		return list.get(row)[column]; // TODO
=======
		Cell cell = sheet.getCell(row + 1, column + 1);
		return cell.getValue();
>>>>>>> 74d1ba7ec3738bcada746293a10eb3296363d500
	}
	
	public String getWorksheetName(){
		return sheet.getName();
	}
	
	public boolean isCellEditable(int row, int col) { 
	    return true; 
	}
}
