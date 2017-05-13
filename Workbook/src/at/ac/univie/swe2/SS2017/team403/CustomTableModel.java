package at.ac.univie.swe2.SS2017.team403;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class CustomTableModel extends AbstractTableModel {

	List<String[]> list = null;

	public CustomTableModel(List<String[]> values) {
		this.list = values;
	}

	@Override
	public int getColumnCount() {
		if (list.size() == 0)
			return 0;
		else
			return list.get(0).length;
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		return list.get(row)[column];
	}

}
