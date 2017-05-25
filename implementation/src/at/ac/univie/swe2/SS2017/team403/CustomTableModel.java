package at.ac.univie.swe2.SS2017.team403;

import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;

public class CustomTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private Worksheet sheet = null;
	private JTable table;
	private int lastVisibleRow = 0, lastVisibleColumn = 0;

	public CustomTableModel(Worksheet sheet, JTable jtable, JScrollPane jPane) {
		this.sheet = sheet;
		this.table = jtable;
		jPane.getViewport().addChangeListener(new ChangeListener() { 
			public void stateChanged(ChangeEvent e)
			{
				Rectangle viewRect = table.getVisibleRect();
			    lastVisibleRow = table.rowAtPoint(new Point( 0 , (int)viewRect.getMaxY() - 1)) + 1;
			    
			    int tmp = table.columnAtPoint(new Point( (int)viewRect.getMaxX() - 1 , 0)) + 1;
			    if (tmp != lastVisibleColumn) {
			    	int r = table.getSelectedRow();
			    	int c = table.getSelectedColumn();
			    	lastVisibleColumn = tmp;
					getColumnCount();
					fireTableStructureChanged();
					
					if (r != -1 && c != -1) {
						table.setRowSelectionInterval(r, r);
					    table.setColumnSelectionInterval(c, c);
					}
			    }
			}
		});
	}
	
	@Override
	public String getColumnName(int index) {
		return "C" + (index + 1);
	}
	
	@Override
	public int getColumnCount() {
		int extraCols = 10;
		
		if (lastVisibleColumn + extraCols > sheet.getMaxUsedArea().getLastColumn())
			return lastVisibleColumn + extraCols;
		else
			return sheet.getMaxUsedArea().getLastColumn();
	}
	
	@Override
	public int getRowCount() {
		int extraRows = 50;

		if (lastVisibleRow + extraRows > sheet.getMaxUsedArea().getLastRow())
			return lastVisibleRow + extraRows;
		else
			return sheet.getMaxUsedArea().getLastRow();
	}
	
	@Override
	public Object getValueAt(int row, int column) {
		Cell cell = sheet.getCell(row + 1, column + 1, false);
		if (cell == null)
			return null;
		return cell.getCellValue();
	}


	public boolean isCellEditable(int row, int col) {
		return false;
	}
}
