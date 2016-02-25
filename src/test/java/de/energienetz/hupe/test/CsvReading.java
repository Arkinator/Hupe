package de.energienetz.hupe.test;

import static org.junit.Assert.assertEquals;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jfree.chart.ChartUtilities;
import org.jfree.data.time.Second;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Period;
import org.junit.Test;

import de.energienetz.hupe.CsvDataReader;
import de.energienetz.hupe.CsvFile;
import de.energienetz.hupe.HupeChartBuilder;
import de.energienetz.hupe.HupeDateFilter;
import de.energienetz.hupe.TemperatureEntry;

public class CsvReading {
	@Test
	public void collectInputFromZipFile() {
		final CsvDataReader reader = new CsvDataReader(new File("src/test/resources/messhistorie-010E7367EFE6.csv"));
		assertEquals(1, reader.getFileList().size());
		final CsvFile csvFile = reader.getFileList().get(0);
		assertEquals("messhistorie-0111A7370D79.csv", csvFile.getFileName());
		assertEquals(2, csvFile.getAllSeries().size());
		assertEquals(222, csvFile.getAllSeries().get(0).getEntries().size());
		assertEquals("Mon Nov 30 02:08:12 CET 2015", csvFile.getAllSeries().get(0).getEntries().get(0).getDate().toString());
		assertEquals(21.7, csvFile.getAllSeries().get(0).getEntries().get(0).getTemp(), 0.01);
		assertEquals(22.9, csvFile.getAllSeries().get(0).getEntries().get(1).getTemp(), 0.01);
	}
}
