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
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXDatePicker;
import org.jfree.chart.ChartPanel;
import org.joda.time.Period;

import de.energienetz.hupe.CsvDataReader;
import de.energienetz.hupe.HupeChartBuilder;
import de.energienetz.hupe.HupeDataSeries;
import de.energienetz.hupe.HupeDateFilter;

public class HupeGui extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1964136476013026396L;
	private static final String showOpenFileDialogAction = "showOpenFileDialog";
	private static final String clearDataAction = "clearData";
	private static final String editDataAction = "editData";
	private static final String checkTimeFilterAction = "checkTimeFilter";
	private JPanel mainPanel;
	private JPanel actionPanel;
	private JPanel viewPanel;
	private JPanel loadDataPanel;
	private JButton openFileButton;
	private JButton clearDiagrammButton;
	private JButton editDataButton;
	private JCheckBox filterCheckBox;
	private final List<HupeDataSeries> series;
	private final JFileChooser fileChooser;
	private final HupeChartBuilder chartBuilder;
	private final EditDataDialog editDataDialog;
	private final HupeDateFilter hupeDateFilter;
	private JXDatePicker datePickerFrom;
	private JXDatePicker datePickerUntil;

	public HupeGui() {
		super("HUPE - HeizUngs PlottEr GUI " + HupeGui.class.getPackage().getImplementationVersion());
		series = new ArrayList<>();

		hupeDateFilter = new HupeDateFilter(Period.days(3));
		hupeDateFilter.setActive(false);
		chartBuilder = new HupeChartBuilder(series);
		chartBuilder.addFilter(hupeDateFilter);
		editDataDialog = new EditDataDialog(this);

		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./src/test/resources"));
		fileChooser.setMultiSelectionEnabled(true);

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
		final GroupLayout layout = new GroupLayout(actionPanel);
		actionPanel.setLayout(layout);

		final JLabel dataLabel = new JLabel("Daten des Diagramms verwalten");
		openFileButton = new JButton("Neue Daten hinzufügen...");
		openFileButton.addActionListener(this);
		openFileButton.setActionCommand(showOpenFileDialogAction);

		clearDiagrammButton = new JButton("Geladene Daten verwerfen");
		clearDiagrammButton.addActionListener(this);
		clearDiagrammButton.setActionCommand(clearDataAction);

		editDataButton = new JButton("Daten verwalten...");
		editDataButton.addActionListener(this);
		editDataButton.setActionCommand(editDataAction);

		filterCheckBox = new JCheckBox("Zeiten filtern");
		filterCheckBox.setSelected(false);
		filterCheckBox.addActionListener(this);
		filterCheckBox.setActionCommand(checkTimeFilterAction);
		final JLabel fromLabel = new JLabel("Von vor ");
		final JLabel day1Label = new JLabel("Tagen");
		final JLabel untilLabel = new JLabel("bis vor ");
		final JLabel day2Label = new JLabel("Tagen");
		datePickerFrom = new JXDatePicker();
		datePickerUntil = new JXDatePicker();
		datePickerFrom.addActionListener(this);
		datePickerUntil.addActionListener(this);

		layout.setAutoCreateGaps(true);
		layout.setVerticalGroup(layout.createSequentialGroup()//
				.addComponent(dataLabel) //
				.addComponent(openFileButton) //
				.addComponent(clearDiagrammButton) //
				.addComponent(editDataButton) //
				.addContainerGap() //
				.addComponent(filterCheckBox) //
				.addGroup(layout.createParallelGroup() //
						.addComponent(fromLabel) //
						.addComponent(datePickerFrom) //
						.addComponent(day1Label)) //
				.addGroup(layout.createParallelGroup() //
						.addComponent(untilLabel) //
						.addComponent(datePickerUntil) //
						.addComponent(day2Label)));
		layout.setHorizontalGroup(layout.createParallelGroup() //
				.addComponent(dataLabel) //
				.addComponent(openFileButton) //
				.addComponent(clearDiagrammButton) //
				.addComponent(editDataButton)//
				.addComponent(filterCheckBox) //
				.addGroup(layout.createSequentialGroup()//
						.addComponent(fromLabel) //
						.addComponent(datePickerFrom) //
						.addComponent(day1Label))
				.addGroup(layout.createSequentialGroup()//
						.addComponent(untilLabel) //
						.addComponent(datePickerUntil) //
						.addComponent(day2Label)));
		layout.linkSize(SwingConstants.HORIZONTAL, openFileButton, clearDiagrammButton, editDataButton);
		layout.linkSize(SwingConstants.HORIZONTAL, datePickerFrom, datePickerUntil);
		layout.linkSize(SwingConstants.HORIZONTAL, fromLabel, untilLabel);
		layout.linkSize(SwingConstants.VERTICAL, fromLabel, datePickerFrom, day1Label);
		layout.linkSize(SwingConstants.VERTICAL, untilLabel, datePickerUntil, day2Label);
		return loadDataPanel;
	}

	public static void main(final String[] args) throws Exception {
		javax.swing.SwingUtilities.invokeLater(() -> new HupeGui());
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (showOpenFileDialogAction.equals(e.getActionCommand())) {
			javax.swing.SwingUtilities.invokeLater(() -> showOpenFileDialog());
		} else if (clearDataAction.equals(e.getActionCommand())) {
			javax.swing.SwingUtilities.invokeLater(() -> chartBuilder.clearData());
		} else if (editDataAction.equals(e.getActionCommand())) {
			javax.swing.SwingUtilities.invokeLater(() -> editDataDialog.showEditDataGui());
		} else if (checkTimeFilterAction.equals(e.getActionCommand())) {
			javax.swing.SwingUtilities.invokeLater(() -> hupeDateFilter.setActive(filterCheckBox.isSelected()));
		} else if (e.getSource() instanceof JXDatePicker) {
			hupeDateFilter.setMinimum(datePickerFrom.getDate());
			hupeDateFilter.setMaximum(datePickerUntil.getDate());
			chartBuilder.update();
		}
	}

	private void showOpenFileDialog() {
		final int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			for (final File selectedFile : fileChooser.getSelectedFiles()) {
				new CsvDataReader(selectedFile).getFileList().forEach(file -> series.addAll(file.getAllSeries()));
			}
			chartBuilder.update();
		}
	}

	public List<HupeDataSeries> getDataSeries() {
		return series;
	}

	public HupeChartBuilder getChartBuilder() {
		return chartBuilder;
	}
}
