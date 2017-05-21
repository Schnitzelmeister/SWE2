package at.ac.univie.swe2.SS2017.team403;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class CustomTableModel extends AbstractTableModel {
	Worksheet sheet = null;

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
		return sheet.getUsedArea().getLastColumn();
	}
	

	@Override
	public int getRowCount() {
		System.out.println("getrowcount wurde aufgerufen");
		return sheet.getUsedArea().getLastRow();
	}

	@Override
	public Object getValueAt(int row, int column) {
		Cell cell = sheet.getCell(row + 1, column + 1);
		return cell.getValue();
	}
	
	public String getWorksheetName(){
		return sheet.getName();
	}
	
	public boolean isCellEditable(int row, int col) { 
	    return true; 
	}
}
