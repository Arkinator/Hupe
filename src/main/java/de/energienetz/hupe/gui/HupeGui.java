package de.energienetz.hupe.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.jfree.chart.ChartPanel;

import de.energienetz.hupe.CsvDataReader;
import de.energienetz.hupe.CsvFile;
import de.energienetz.hupe.HupeChartBuilder;

public class HupeGui extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1964136476013026396L;
	private static final String showOpenFileDialogAction = "showOpenFileDialog";
	private static final String clearDataAction = "clearData";
	private JPanel mainPanel;
	private JPanel actionPanel;
	private JPanel viewPanel;
	private JPanel loadDataPanel;
	private JButton openFileButton;
	private JButton clearDiagrammButton;
	private final List<CsvFile> files;
	private final JFileChooser fileChooser;
	private final HupeChartBuilder chartBuilder;

	public HupeGui() {
		super("HUPE - HeizUngs PlottEr GUI " + HupeGui.class.getPackage().getImplementationVersion());
		files = new ArrayList<>();
		chartBuilder = new HupeChartBuilder(files);

		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));

		initMainWindow();
	}

	private void initMainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel(new BorderLayout());
		buildActionPanel();
		viewPanel = new ChartPanel(chartBuilder.getChart());
		mainPanel.add(actionPanel, BorderLayout.LINE_START);
		mainPanel.add(viewPanel, BorderLayout.CENTER);
		getContentPane().add(mainPanel);
		// pack();
		setSize(600, 400);
		setVisible(true);
	}

	private void buildActionPanel() {
		actionPanel = new JPanel();
		actionPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
		createLoadDataPanel();
	}

	private Component createLoadDataPanel() {
		final JLabel dataLabel = new JLabel("Daten des Diagramms verwalten");
		final GroupLayout layout = new GroupLayout(actionPanel);
		actionPanel.setLayout(layout);
		openFileButton = new JButton("Neue Daten hinzufügen...");
		openFileButton.addActionListener(this);
		openFileButton.setActionCommand(showOpenFileDialogAction);
		clearDiagrammButton = new JButton("Geladene Daten verwerfen");
		clearDiagrammButton.addActionListener(this);
		clearDiagrammButton.setActionCommand(clearDataAction);
		layout.setAutoCreateGaps(true);
		layout.setVerticalGroup(layout.createSequentialGroup()//
				.addComponent(dataLabel) //
				.addComponent(openFileButton) //
				.addComponent(clearDiagrammButton));
		layout.setHorizontalGroup(layout.createParallelGroup() //
				.addComponent(dataLabel) //
				.addComponent(openFileButton) //
				.addComponent(clearDiagrammButton));
		layout.linkSize(SwingConstants.HORIZONTAL, openFileButton, clearDiagrammButton);
		return loadDataPanel;
	}

	public static void main(final String[] args) throws Exception {
		javax.swing.SwingUtilities.invokeLater(() -> new HupeGui());
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (showOpenFileDialogAction.equals(e.getActionCommand())) {
			javax.swing.SwingUtilities.invokeLater(() -> showOpenFileDialog());
		}
	}

	private void showOpenFileDialog() {
		final int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			final File selectedFile = fileChooser.getSelectedFile();
			files.addAll(new CsvDataReader(selectedFile).getFileList());
			chartBuilder.update();
		}
	}
}
