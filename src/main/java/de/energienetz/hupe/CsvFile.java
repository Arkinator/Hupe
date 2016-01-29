package de.energienetz.hupe;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvFile {
	private static final String csvSplitCharacter = ";";

	private static Logger logger = LoggerFactory.getLogger(CsvFile.class);

	private final String fileName;
	private List<TemperatureEntry> entries;

	public CsvFile(final List<String> content, final String fileName) {
		this.fileName = fileName;
		this.entries = new ArrayList<TemperatureEntry>();
		content.forEach(line -> parseAndAddCsvLine(line));
	}

	private void parseAndAddCsvLine(final String line) {
		try {
			entries.add(new TemperatureEntry(line.split(csvSplitCharacter)));
		} catch (final Exception e) {
			logger.trace("Skipping line '" + line + "'." + ExceptionUtils.getStackTrace(e));
		}
	}

	public String getFileName() {
		return fileName;
	}

	public List<TemperatureEntry> getEntries() {
		return entries;
	}

	public XYDataset createDataSet() {
		final TimeSeries sensor1 = new TimeSeries(getFileName() + " Sensor 1");
		entries.forEach(entry -> sensor1.add(new Minute(entry.getDate()), entry.getTemp1()));

		final TimeSeries sensor2 = new TimeSeries(getFileName() + " Sensor 2");
		entries.forEach(entry -> sensor2.add(new Minute(entry.getDate()), entry.getTemp2()));

		final TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(sensor1);
		dataset.addSeries(sensor2);
		return dataset;
	}

	public void setEntries(final List<TemperatureEntry> list) {
		this.entries = list;
	}

	public TimeSeries getSensor1Series() {
		final TimeSeries series = new TimeSeries(getFileName() + " Sensor 1");
		entries.forEach(entry -> series.addOrUpdate(new Minute(entry.getDate()), entry.getTemp1()));
		return series;
	}

	public TimeSeries getSensor2Series() {
		final TimeSeries series = new TimeSeries(getFileName() + " Sensor 2");
		entries.forEach(entry -> series.addOrUpdate(new Minute(entry.getDate()), entry.getTemp2()));
		return series;
	}
}
