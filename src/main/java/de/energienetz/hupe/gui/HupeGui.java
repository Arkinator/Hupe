package de.energienetz.hupe.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.energienetz.hupe.*;
import org.jdesktop.swingx.JXDatePicker;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Period;

public class HupeGui extends JFrame implements ActionListener, ChangeListener, KeyListener {
    private static final String saveImageLabel = "Speichern...";
    private static final String saveAsLabel = "Speichern als:";
    private static final String untilFilterLabel = "bis";
    private static final String fromFilterLabel = "von";
    private static final String filterTimesLabel = "Zeiten filtern";
    private static final String manageLabel = "Daten verwalten...";
    private static final String clearDataLabel = "Geladene Daten verwerfen";
    private static final String addDataLabel = "Neue Daten hinzufügen...";
    private static final String editDataLabel = "Daten des Diagramms verwalten";
    private static final String guiTitle = "HUPE - HeizUngs PlottEr GUI ";
    private static final long serialVersionUID = 1964136476013026396L;
    private static final String showOpenFileDialogAction = "showOpenFileDialog";
    private static final String clearDataAction = "clearData";
    private static final String editDataAction = "editData";
    private static final String checkTimeFilterAction = "checkTimeFilter";
    private static final String saveFileAction = "saveFile";
    private static final String changeDiagramTitleAction = "changeDiagramAction";
    private static final String rangeActivationLabel = "Y-Achse manuell wählen";
    private static final String rangeActivationAction = "ActionToActivationRange";

    private static final String minuteTickLabel = "Minuten";
    private static final String hourTickLabel = "Stunden";
    private static final String dayTickLabel = "Tage";
    private static final String titleLabelText = "Überschrift wählen";
    public static final int DEFAULT_FILTERDURATION_IN_DAYS = 7;
    private JPanel mainPanel;
    private JPanel actionPanel;
    private JPanel viewPanel;
    private JPanel loadDataPanel;
    private JButton openFileButton;
    private JButton clearDiagrammButton;
    private JButton editDataButton;
    private JCheckBox filterCheckBox;
    private JCheckBox rangeCheckBox;
    private final List<HupeDataSeries> series;
    private final JFileChooser openFileChooser;
    private final JFileChooser saveFileChooser;
    private final HupeChartBuilder chartBuilder;
    private final EditDataDialog editDataDialog;
    private final HupeDateFilter hupeDateFilter;
    private JXDatePicker datePickerFrom;
    private JXDatePicker datePickerUntil;
    private JComboBox<HupeFileType> fileTypeComboBox;
    private JSpinner widthImageSpinner;
    private JSpinner heightImageSpinner;
    private JSpinner axisFromSpinner;
    private JSpinner axisUntilSpinner;
    private JSpinner tickUnitCounter;
    private JComboBox<String> tickUnitComboBox;
    private JSpinner timePickerFrom;
    private JSpinner timePickerUntil;
    private JTextField diagramTitelField;

    public HupeGui() {
        super(guiTitle + HupeGui.class.getPackage().getImplementationVersion());
        series = new ArrayList<>();

        hupeDateFilter = new HupeDateFilter(Period.days(3));
        hupeDateFilter.setActive(false);
        chartBuilder = new HupeChartBuilder(series);
        chartBuilder.addFilter(hupeDateFilter);
        editDataDialog = new EditDataDialog(this);

        openFileChooser = new JFileChooser();
        openFileChooser.setCurrentDirectory(new File("./src/test/resources"));
        openFileChooser.setMultiSelectionEnabled(true);
        openFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);

        saveFileChooser = new JFileChooser();
        saveFileChooser.setCurrentDirectory(new File("./src/test/resources"));
        saveFileChooser.setMultiSelectionEnabled(false);
        saveFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);

        initMainWindow();

        updateTickUnits();
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
        setSize(1000, 600);
        setVisible(true);
    }

    private void buildActionPanel() {
        actionPanel = new JPanel();
        final JPanel innerActionPanel = new JPanel();
        createLoadDataPanel(innerActionPanel);
        actionPanel.add(innerActionPanel);
    }

    private Component createLoadDataPanel(final JPanel actionPanel) {
        final GroupLayout layout = new GroupLayout(actionPanel);
        actionPanel.setLayout(layout);

        final JLabel dataLabel = new JLabel(editDataLabel);
        openFileButton = new JButton(addDataLabel);
        openFileButton.addActionListener(this);
        openFileButton.setActionCommand(showOpenFileDialogAction);

        clearDiagrammButton = new JButton(clearDataLabel);
        clearDiagrammButton.addActionListener(this);
        clearDiagrammButton.setActionCommand(clearDataAction);

        editDataButton = new JButton(manageLabel);
        editDataButton.addActionListener(this);
        editDataButton.setActionCommand(editDataAction);

        filterCheckBox = new JCheckBox(filterTimesLabel);
        filterCheckBox.setSelected(false);
        filterCheckBox.addActionListener(this);
        filterCheckBox.setActionCommand(checkTimeFilterAction);
        final JLabel fromLabel = new JLabel(fromFilterLabel);
        final JLabel untilLabel = new JLabel(untilFilterLabel);
        datePickerFrom = new JXDatePicker();
        datePickerUntil = new JXDatePicker();
        datePickerUntil.setDate(new Date());
        datePickerFrom.setDate(Instant.now().minus(Duration.standardDays(DEFAULT_FILTERDURATION_IN_DAYS)).toDate());
        datePickerFrom.addActionListener(this);
        datePickerUntil.addActionListener(this);
        timePickerFrom = new JSpinner(new SpinnerDateModel());
        final JSpinner.DateEditor timeEditorFrom = new JSpinner.DateEditor(timePickerFrom, "HH:mm:ss");
        timePickerFrom.setEditor(timeEditorFrom);
        timePickerFrom.setValue(new DateTime().withMillisOfDay(0).toDate());
        timePickerUntil = new JSpinner(new SpinnerDateModel());
        final JSpinner.DateEditor timeEditorUntil = new JSpinner.DateEditor(timePickerUntil, "HH:mm:ss");
        timePickerUntil.setEditor(timeEditorUntil);
        timePickerUntil.setValue(new DateTime().withMillisOfDay(0).toDate());
        timePickerFrom.addChangeListener(this);
        timePickerUntil.addChangeListener(this);

        rangeCheckBox = new JCheckBox(rangeActivationLabel);
        rangeCheckBox.setSelected(false);
        rangeCheckBox.addActionListener(this);
        rangeCheckBox.setActionCommand(rangeActivationAction);
        axisFromSpinner = new JSpinner(new SpinnerNumberModel(0, -100, 100, 1));
        axisFromSpinner.setValue(0);
        axisFromSpinner.addChangeListener(this);
        axisUntilSpinner = new JSpinner(new SpinnerNumberModel(0, -100, 100, 1));
        axisUntilSpinner.setValue(30);
        axisUntilSpinner.addChangeListener(this);

        final JLabel titleLabel = new JLabel(titleLabelText);
        diagramTitelField = new JTextField();
        diagramTitelField.setText(HupeChartBuilder.initialDiagramTitle);
        diagramTitelField.setActionCommand(changeDiagramTitleAction);
        diagramTitelField.addKeyListener(this);

        final JLabel saveLabel = new JLabel(saveAsLabel);
        fileTypeComboBox = new JComboBox<>(HupeFileType.values());
        fileTypeComboBox.setSelectedItem(HupeFileType.JPEG);
        fileTypeComboBox.setEditable(false);
        final JButton saveButton = new JButton(saveImageLabel);
        saveButton.addActionListener(this);
        saveButton.setActionCommand(saveFileAction);

        tickUnitCounter = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        tickUnitComboBox = new JComboBox<>();
        tickUnitComboBox.addItem(minuteTickLabel);
        tickUnitComboBox.addItem(hourTickLabel);
        tickUnitComboBox.addItem(dayTickLabel);
        tickUnitComboBox.setSelectedItem(hourTickLabel);
        tickUnitCounter.setValue(4);
        tickUnitComboBox.addActionListener(this);
        tickUnitCounter.addChangeListener(this);

        heightImageSpinner = new JSpinner(new SpinnerNumberModel(768, 0, 20000, 10));
        widthImageSpinner = new JSpinner(new SpinnerNumberModel(1024, 0, 20000, 10));
        final JLabel sizeLabel = new JLabel("x");
        final JSeparator line1 = new JSeparator(JSeparator.HORIZONTAL);
        line1.setMaximumSize(new Dimension(1000, 5));
        final JSeparator line2 = new JSeparator(JSeparator.HORIZONTAL);
        line2.setMaximumSize(new Dimension(1000, 5));
        final JSeparator line3 = new JSeparator(JSeparator.HORIZONTAL);
        line3.setMaximumSize(new Dimension(1000, 5));
        final JSeparator line4 = new JSeparator(JSeparator.HORIZONTAL);
        line1.setMaximumSize(new Dimension(1000, 5));

        layout.setAutoCreateGaps(true);
        layout.setVerticalGroup(layout.createSequentialGroup()//
                .addGap(10) //
                .addComponent(dataLabel) //
                .addComponent(openFileButton) //
                .addComponent(clearDiagrammButton) //
                .addComponent(editDataButton) //
                .addGap(10) //
                .addComponent(line1) //
                .addComponent(filterCheckBox) //
                .addGroup(layout.createParallelGroup() //
                        .addComponent(fromLabel) //
                        .addComponent(datePickerFrom). //
                        addComponent(timePickerFrom)) //
                .addGroup(layout.createParallelGroup() //
                        .addComponent(untilLabel) //
                        .addComponent(datePickerUntil). //
                        addComponent(timePickerUntil))//
                .addGap(10) //
                .addComponent(line2) //
                .addGroup(layout.createParallelGroup() //
                        .addComponent(tickUnitCounter) //
                        .addComponent(tickUnitComboBox)) //
                .addGap(10) //
                .addComponent(rangeCheckBox)
                .addGroup(layout.createParallelGroup() //
                        .addComponent(axisFromSpinner) //
                        .addComponent(axisUntilSpinner)) //
                .addGap(10) //
                .addComponent(line3) //
                .addGap(10) //
                .addComponent(titleLabel) //
                .addComponent(diagramTitelField) //
                .addComponent(line4) //
                .addGroup(layout.createParallelGroup() //
                        .addComponent(saveLabel) //
                        .addComponent(fileTypeComboBox))
                .addGroup(layout.createParallelGroup()//
                        .addComponent(widthImageSpinner) //
                        .addComponent(sizeLabel) //
                        .addComponent(heightImageSpinner)) //
                .addGap(20) //
                .addComponent(saveButton));
        layout.setHorizontalGroup(layout.createParallelGroup() //
                .addComponent(dataLabel) //
                .addComponent(openFileButton) //
                .addComponent(clearDiagrammButton) //
                .addComponent(editDataButton)//
                .addComponent(line1) //
                .addComponent(filterCheckBox) //
                .addGroup(layout.createSequentialGroup()//
                        .addComponent(fromLabel) //
                        .addComponent(datePickerFrom) //
                        .addComponent(timePickerFrom))
                .addGroup(layout.createSequentialGroup()//
                        .addComponent(untilLabel) //
                        .addComponent(datePickerUntil) //
                        .addComponent(timePickerUntil)) //
                .addComponent(line2) //
                .addGroup(layout.createSequentialGroup() //
                        .addComponent(tickUnitCounter) //
                        .addComponent(tickUnitComboBox)) //
                .addComponent(rangeCheckBox) //
                .addGroup(layout.createSequentialGroup()
                        .addComponent(axisFromSpinner) //
                        .addComponent(axisUntilSpinner)) //
                .addComponent(line3) //
                .addComponent(titleLabel) //
                .addComponent(diagramTitelField) //
                .addComponent(line4) //
                .addGroup(layout.createSequentialGroup()//
                        .addComponent(saveLabel) //
                        .addComponent(fileTypeComboBox)) //
                .addGroup(layout.createSequentialGroup()//
                        .addComponent(widthImageSpinner) //
                        .addComponent(sizeLabel) //
                        .addComponent(heightImageSpinner)) //
                .addComponent(saveButton));
        layout.linkSize(SwingConstants.HORIZONTAL, openFileButton, clearDiagrammButton, editDataButton, saveButton);
        layout.linkSize(SwingConstants.HORIZONTAL, datePickerFrom, datePickerUntil, timePickerFrom, timePickerUntil);
        layout.linkSize(SwingConstants.HORIZONTAL, fromLabel, untilLabel);
        layout.linkSize(SwingConstants.VERTICAL, fromLabel, datePickerFrom);
        layout.linkSize(SwingConstants.VERTICAL, untilLabel, datePickerUntil);
        layout.linkSize(SwingConstants.VERTICAL, saveLabel, fileTypeComboBox);
        layout.linkSize(SwingConstants.VERTICAL, widthImageSpinner, heightImageSpinner, sizeLabel);
        layout.linkSize(SwingConstants.HORIZONTAL, widthImageSpinner, heightImageSpinner);
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
            javax.swing.SwingUtilities.invokeLater(() -> toggleFilterActive());
        } else if (saveFileAction.equals(e.getActionCommand())) {
            javax.swing.SwingUtilities.invokeLater(() -> initiateSaveFileSequence());
        } else if (e.getSource() instanceof JXDatePicker) {
            filterCheckBox.setSelected(true);
            changeFilterValues();
        } else if ((e.getSource() == tickUnitComboBox) || (e.getSource() == tickUnitCounter)) {
            updateTickUnits();
            chartBuilder.update();
        } else if (rangeActivationAction.equals(e.getActionCommand())) {
            updateDiagramAxis();
        }
    }

    private void updateDiagramAxis() {
        chartBuilder.setAutoAdjustedYAxis(rangeCheckBox.isSelected());
        chartBuilder.setRangeYAxis((Integer)axisFromSpinner.getValue(), (Integer)axisUntilSpinner.getValue());
    }

    private void updateDiagramTitle() {
        chartBuilder.setTitle(diagramTitelField.getText());
        chartBuilder.update();
    }

    private void changeFilterValues() {
        final DateTime timeFrom = new DateTime(timePickerFrom.getValue());
        final DateTime timeUntil = new DateTime(timePickerUntil.getValue());
        final Duration timeIncrementForFrom = new Duration(timeFrom.withMillisOfDay(0), timeFrom);
        final Duration timeIncrementForUntil = new Duration(timeUntil.withMillisOfDay(0), timeUntil);
        hupeDateFilter.setMinimum(new DateTime(datePickerFrom.getDate()).plus(timeIncrementForFrom));
        hupeDateFilter.setMaximum(new DateTime(datePickerUntil.getDate()).plus(timeIncrementForUntil));
        toggleFilterActive();
    }

    private void updateTickUnits() {
        final int count = (Integer) tickUnitCounter.getValue();
        DateTickUnitType type = null;
        switch (tickUnitComboBox.getSelectedItem().toString()) {
            case minuteTickLabel:
                type = DateTickUnitType.MINUTE;
                break;
            case hourTickLabel:
                type = DateTickUnitType.HOUR;
                break;
            case dayTickLabel:
                type = DateTickUnitType.DAY;
                break;
            default:
                type = DateTickUnitType.DAY;
                break;
        }
        chartBuilder.setTickUnit(new DateTickUnit(type, count));
    }

    private void initiateSaveFileSequence() {
        try {

            final int result = saveFileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                ((HupeFileType) fileTypeComboBox.getSelectedItem()).saveChart(chartBuilder.getChart(), saveFileChooser.getSelectedFile(), getTargetWidth(), getTargetHeight());
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getTargetHeight() {
        return Integer.parseInt(heightImageSpinner.getValue().toString());
    }

    private int getTargetWidth() {
        return Integer.parseInt(widthImageSpinner.getValue().toString());
    }

    private void toggleFilterActive() {
        hupeDateFilter.setActive(filterCheckBox.isSelected());
        chartBuilder.update();
    }

    private void showOpenFileDialog() {
        final int result = openFileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            for (final File selectedFile : openFileChooser.getSelectedFiles()) {
                new CsvDataReader(selectedFile).getFileList().forEach(file -> series.addAll(file.getAllSeries()));
            }
            selectNewDefaultFilterTimes();
            chartBuilder.update();
        }
    }

    private void selectNewDefaultFilterTimes() {
        // select unbeatable startime (within reason)
        DateTime latest = DateTime.now().minusYears(100);
        boolean changed = false;

        for (HupeDataSeries actSeries : series){
            List<TemperatureEntry> entryList = actSeries.getEntries();
            DateTime act = new DateTime(entryList.get(entryList.size()-1).getDate());
            if (act.isAfter(latest)){
                latest = act;
                changed = true;
            }
        }

        if (!changed)
            latest = new DateTime();

        datePickerUntil.setDate(latest.toDate());
        datePickerFrom.setDate(latest.minus(Duration.standardDays(DEFAULT_FILTERDURATION_IN_DAYS)).toDate());
    }

    public List<HupeDataSeries> getDataSeries() {
        return series;
    }

    public HupeChartBuilder getChartBuilder() {
        return chartBuilder;
    }

    @Override
    public void stateChanged(final ChangeEvent e) {
        if ((e.getSource() == this.timePickerFrom) || (e.getSource() == this.timePickerUntil)) {
            filterCheckBox.setSelected(true);
        }
        updateTickUnits();
        changeFilterValues();
        updateDiagramAxis();
        chartBuilder.update();
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        javax.swing.SwingUtilities.invokeLater(() -> updateDiagramTitle());
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(final KeyEvent e) {
        // TODO Auto-generated method stub

    }
}
