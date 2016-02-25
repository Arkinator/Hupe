package de.energienetz.hupe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvFile {
	private static final String introLineStart = "Messzeitpunkt;Temperatur 1;Temperatur 2";
	private static final String csvSplitCharacter = ";";
	// 30.11.2015 02:08:12
	// Mon Nov 30 02:08:12 CET 2015
	private static final String dateParsePattern = "dd.MM.yyyy HH:mm:ss";

	private static Logger logger = LoggerFactory.getLogger(CsvFile.class);

	private String fileName;
	private final List<HupeDataSeries> series;

	private boolean visible;

	public CsvFile(final List<String> content, final String fileName) {
		Assert.assertTrue("Unzulässiges Dateiformat", content.size() >= 1);
		Assert.assertTrue("Unzulässiges Dateiformat: " + content.get(0), content.get(0).startsWith(introLineStart));
		this.fileName = fileName;
		this.series = new ArrayList<HupeDataSeries>();
		series.add(new HupeDataSeries(fileName + " Sensor 1"));
		series.add(new HupeDataSeries(fileName + " Sensor 2"));
		content.forEach(line -> parseAndAddCsvLine(line));
	}

	private void parseAndAddCsvLine(final String line) {
		try {
			final String[] parts = line.split(csvSplitCharacter);
			final Date date = DateUtils.parseDate(parts[0], dateParsePattern);
			final double temp1 = Double.parseDouble(parts[1].replace(",", "."));
			final double temp2 = Double.parseDouble(parts[2].replace(",", "."));
			series.get(0).addNewEntry(date, temp1);
			series.get(1).addNewEntry(date, temp2);
		} catch (final Exception e) {
			logger.trace("Skipping line '" + line + "'." + ExceptionUtils.getStackTrace(e));
		}
	}

	public String getFileName() {
		return fileName;
	}

	public List<HupeDataSeries> getAllSeries() {
		return series;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(final boolean value) {
		this.visible = value;
	}

	public void setFileName(final String value) {
		this.fileName = value;
	}

	public class DateParseException extends RuntimeException {
		private static final long serialVersionUID = 2698583506459715717L;

		public DateParseException(final Exception e) {
			super(e);
		}
	}
}
