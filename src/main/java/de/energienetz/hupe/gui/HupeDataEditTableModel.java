package de.energienetz.hupe.gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.energienetz.hupe.HupeChartBuilder;
import de.energienetz.hupe.HupeColor;
import de.energienetz.hupe.HupeDataSeries;

public class HupeDataEditTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 9176445957680423004L;

	private final String[] columnNames = { "Name", "Farbe", "Strichstärke", "Sichtbar?" };
	private final Class<?>[] columnClasses = { String.class, HupeColor.class, Double.class, Boolean.class };
	private final List<HupeDataSeries> dataSeries;
	private final HupeChartBuilder builder;

	public HupeDataEditTableModel(final List<HupeDataSeries> list, final HupeChartBuilder builder) {
		this.dataSeries = list;
		this.builder = builder;
	}

	@Override
	public int getRowCount() {
		return dataSeries.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(final int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(final int columnIndex) {
		return columnClasses[columnIndex];
	}

	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		return true;
	}

	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		switch (columnIndex) {
		case 0:
			return dataSeries.get(rowIndex).getSeriesName();
		case 1:
			return dataSeries.get(rowIndex).getColor();
		case 2:
			return dataSeries.get(rowIndex).getWidth();
		case 3:
			return dataSeries.get(rowIndex).isVisible();
		default:
			return null;
		}
	}

	@Override
	public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
		switch (columnIndex) {
		case 0:
			dataSeries.get(rowIndex).setSeriesName((String) aValue);
			break;
		case 1:
			if (aValue != null) {
				dataSeries.get(rowIndex).setColor((HupeColor) aValue);
			} else {
				dataSeries.get(rowIndex).setColor(null);
			}
			break;
		case 2:
			dataSeries.get(rowIndex).setWidth((Double) aValue);
			break;
		case 3:
			dataSeries.get(rowIndex).setVisible((Boolean) aValue);
			break;
		default:
			break;
		}
		builder.update();
	}
}
