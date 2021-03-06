package de.energienetz.hupe.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import de.energienetz.hupe.HupeColor;

public class EditDataDialog extends JFrame implements ActionListener {
	private static final long serialVersionUID = 9217860900420979997L;
	private static final String okAction = "okAction";
	private static final String[] defaultNames = { "VLHzg", "VLKessel", "Ruecklauf", "Warmw", "Aussen", "Wohnen", "Speicher", "<anderes>" };
	private final HupeGui mainGui;

	public EditDataDialog(final HupeGui mainGui) {
		super("Daten editieren");
		this.mainGui = mainGui;
		createEditDataGui();
	}

	public void showEditDataGui() {
		repaint();
		revalidate();
		setVisible(true);
	}

	private void createEditDataGui() {
		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		final JPanel buttonPanel = new JPanel();
		getContentPane().add(mainPanel);
		// getContentPane().add(buttonPanel);

		final JButton okButton = new JButton("Ok");
		okButton.addActionListener(this);
		okButton.setActionCommand(okAction);

		buttonPanel.add(okButton);

		fillTablePanel(mainPanel);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		pack();
		setSize(600, 400);
	}

	private void fillTablePanel(final JPanel tablePanel) {
		final TableModel dm = new HupeDataEditTableModel(mainGui.getDataSeries(), mainGui.getChartBuilder());
		final JTable table = new JTable(dm);
		table.setRowHeight(30);
		setUpTableColumnWidth(table);
		setUpTableColumnEditors(table);
		final JScrollPane scrollPane = new JScrollPane(table);
		// scrollPane.add(table);
		table.setFillsViewportHeight(true);
		tablePanel.add(scrollPane, BorderLayout.CENTER);
	}

	private void setUpTableColumnEditors(final JTable table) {
		setColorEditor(table);
		setCellRenderer(table);
		setNameEditor(table);
		setCurveWidthEditor(table);
	}

	private void setNameEditor(final JTable table) {
		final JComboBox<String> editComboBox = new JComboBox<>();
		editComboBox.setEditable(true);
		for (final String name : defaultNames) {
			editComboBox.addItem(name);
		}
		final DefaultCellEditor colorEditor = new DefaultCellEditor(editComboBox);
		table.getColumnModel().getColumn(0).setCellEditor(colorEditor);
	}

	private void setCurveWidthEditor(final JTable table) {
		final JComboBox<Double> editComboBox = new JComboBox<>();
		editComboBox.setEditable(true);
		editComboBox.addItem(0.5);
		editComboBox.addItem(1.);
		editComboBox.addItem(2.);
		editComboBox.addItem(3.);
		editComboBox.addItem(5.);
		editComboBox.addItem(8.);
		final DefaultCellEditor editor = new DefaultCellEditor(editComboBox);
		table.getColumnModel().getColumn(2).setCellEditor(editor);
	}

	private void setCellRenderer(final JTable table) {
		final TableCellRenderer colorRenderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 13284903859340285L;

			@Override
			public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row,
					final int column) {
				final JLabel result = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (value != null) {
					result.setBackground(((HupeColor) value).getAwtColor());
					// result.setText(value.toString());
					result.setText(((HupeColor) value).getName());
				} else {
					result.setText("<standard>");
					result.setBackground(Color.lightGray);
				}
				return result;
			}
		};
		table.getColumnModel().getColumn(1).setCellRenderer(colorRenderer);
	}

	@SuppressWarnings("unchecked")
	private void setColorEditor(final JTable table) {
		final JComboBox<HupeColor> colorComboBox = new JComboBox<>();
		for (final HupeColor color : HupeColor.values()) {
			colorComboBox.addItem(color);
		}
		colorComboBox.setRenderer(new BasicComboBoxRenderer() {
			private static final long serialVersionUID = 5504753277993405519L;

			@SuppressWarnings("rawtypes")
			@Override
			public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
				final JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value != null) {
					final HupeColor color = (HupeColor) value;
					label.setBackground(color.getAwtColor());
					label.setText(color.getName());
				}
				return label;
			}
		});
		final DefaultCellEditor colorEditor = new DefaultCellEditor(colorComboBox);
		table.getColumnModel().getColumn(1).setCellEditor(colorEditor);
	}

	private void setUpTableColumnWidth(final JTable table) {
		table.getColumnModel().getColumn(1).setMaxWidth(130);
		table.getColumnModel().getColumn(2).setMaxWidth(80);
		table.getColumnModel().getColumn(3).setMaxWidth(80);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (okAction.equals(e.getActionCommand())) {
			javax.swing.SwingUtilities.invokeLater(() -> closeEditDataGui());
		}
	}

	public void closeEditDataGui() {
		setVisible(false);
	}

}
