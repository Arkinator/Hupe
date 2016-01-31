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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import de.energienetz.hupe.HupeColor;

public class EditDataDialog extends JFrame implements ActionListener {
	private static final long serialVersionUID = 9217860900420979997L;
	private static final String okAction = "okAction";
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

		final JButton okButton = new JButton("Ok");
		okButton.addActionListener(this);
		okButton.setActionCommand(okAction);

		buttonPanel.add(okButton);

		fillTablePanel(mainPanel);
		// mainPanel.add(buttonPanel, BorderLayout.SOUTH);

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
		final JComboBox<HupeColor> colorComboBox = new JComboBox<>();
		for (final HupeColor color : HupeColor.values()) {
			colorComboBox.addItem(color);
		}
		final DefaultCellEditor colorEditor = new DefaultCellEditor(colorComboBox);
		table.getColumnModel().getColumn(1).setCellEditor(colorEditor);
		final TableCellRenderer colorRenderer = new DefaultTableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row,
					final int column) {
				final JLabel result = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (value != null) {
					result.setBackground(((HupeColor) value).getAwtColor());
					// result.setText(value.toString());
					result.setText(((HupeColor) value).name());
				} else {
					result.setText("<standard>");
					result.setBackground(Color.lightGray);
				}
				return result;
			}
		};
		table.getColumnModel().getColumn(1).setCellRenderer(colorRenderer);
	}

	private void setUpTableColumnWidth(final JTable table) {
		table.getColumnModel().getColumn(1).setMaxWidth(200);
		table.getColumnModel().getColumn(2).setMaxWidth(50);
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
