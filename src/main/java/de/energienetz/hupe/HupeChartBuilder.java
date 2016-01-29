package de.energienetz.hupe;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class HupeChartBuilder {
	private final List<CsvFile> files;
	private final JFreeChart chart;

	public HupeChartBuilder(final List<CsvFile> files) {
		this.files = files;
		this.chart = ChartFactory.createXYLineChart("", "", "", //
				getUpdatedDataSet(), // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
		);
		final NumberAxis yAxis = new NumberAxis("Temperatur (C°)");
		yAxis.setAutoRangeIncludesZero(false);
		chart.getXYPlot().setDomainAxis(new DateAxis("Messzeitpunkt"));
		chart.getXYPlot().setRangeAxis(yAxis);
	}

	private XYDataset getUpdatedDataSet() {
		final TimeSeriesCollection dataset = new TimeSeriesCollection();
		for (final CsvFile file : files) {
			dataset.addSeries(file.getSensor1Series());
			dataset.addSeries(file.getSensor2Series());
		}
		return dataset;
	}

	@Deprecated
	public static JFreeChart buildChart(final CsvFile csvFile) {
		final JFreeChart chart = ChartFactory.createXYLineChart(csvFile.getFileName(), "", "", //
				csvFile.createDataSet(), // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
		);
		final NumberAxis yAxis = new NumberAxis("Temperatur (C°)");
		yAxis.setAutoRangeIncludesZero(false);
		chart.getXYPlot().setDomainAxis(new DateAxis("Uhrzeit"));
		chart.getXYPlot().setRangeAxis(yAxis);

		return chart;
	}

	public JFreeChart getChart() {
		return chart;
	}

	public void update() {
		chart.getXYPlot().setDataset(getUpdatedDataSet());
	}
}
