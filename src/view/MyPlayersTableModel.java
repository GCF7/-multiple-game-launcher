package es.ucm.fdi.tp.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

class MyPlayersTableModel extends AbstractTableModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private String[] colNames;
	List<String> color;

	MyPlayersTableModel() {
		this.colNames = new String[] { "#Player", "Color" };
		color = new ArrayList<>();
	}

	@Override
	public String getColumnName(int col) {
		return colNames[col];
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public int getRowCount() {
		return color != null ? color.size() : 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return rowIndex;
		} else {
			return color.get(rowIndex);
		}
	}

	public void addPlayer(String name) {
		color.add(name);
		refresh();
	}

	public void refresh() {
		fireTableDataChanged();
	}

};