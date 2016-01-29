package de.energienetz.hupe.test;

import static org.junit.Assert.assertEquals;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jfree.chart.ChartUtilities;
import org.joda.time.Period;
import org.junit.Test;

import de.energienetz.hupe.CsvDataReader;
import de.energienetz.hupe.CsvFile;
import de.energienetz.hupe.HupeChartBuilder;
import de.energienetz.hupe.HupeDateFilter;

public class CsvReading {
	// @Test
	// public void collectInputFromZipFile() {
	// final CsvDataReader reader = new CsvDataReader(new
	// File("src/test/resources/messhistorie-0111A7370D79.csv.zip"));
	// assertEquals(1, reader.getFileList().size());
	// final CsvFile csvFile = reader.getFileList().get(0);
	// assertEquals("messhistorie-0111A7370D79.csv", csvFile.getFileName());
	// assertEquals(222, csvFile.getEntries().size());
	// assertEquals("Mon Nov 30 02:08:12 CET 2015",
	// csvFile.getEntries().get(0).getDate().toString());
	// assertEquals(21.7, csvFile.getEntries().get(0).getTemp1(), 0.01);
	// assertEquals(21.7, csvFile.getEntries().get(0).getTemp2(), 0.01);
	// }
	//
	// @Test
	// public void plotDataStraight() throws IOException {
	// final CsvDataReader reader = new CsvDataReader(new
	// File("src/test/resources/messhistorie-0111A7370D79.csv.zip"));
	// final CsvFile csvFile = reader.getFileList().get(0);
	// new HupeDateFilter(Period.days(3)).execute(csvFile);
	// final byte[] data =
	// ChartUtilities.encodeAsPNG(HupeChartBuilder.buildChart(csvFile).createBufferedImage(1024,
	// 768));
	// FileUtils.writeByteArrayToFile(new File("target/render.png"), data);
	// Desktop.getDesktop().open(new File("target/render.png"));
	// }
}
