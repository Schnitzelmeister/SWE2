package at.ac.univie.swe2.SS2017.team403;

import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class SimpleTableModel extends AbstractTableModel implements TableModelListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	

	public SimpleTableModel() {
	
	}

	@Override
	public String getColumnName(int index) {
		return "C"+(index+1);
	}

	@Override
	public int getColumnCount() {
		return 10;
	}
	

	@Override
	public int getRowCount() {
		return 100;
	}

	@Override
	public Object getValueAt(int row, int column) {
		return new Object();
	}
	
	public boolean isCellEditable(int row, int col) { 
	    return true; 
	}

	@Override
	public void tableChanged(TableModelEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
