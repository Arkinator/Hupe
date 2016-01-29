package de.energienetz.hupe.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

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
		// table.setBounds(15, 295, 1000, 150);
		// table.setRowHeight(30);
		final JScrollPane scrollPane = new JScrollPane(table);
		// scrollPane.add(table);
		table.setFillsViewportHeight(true);
		tablePanel.add(scrollPane, BorderLayout.CENTER);
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
