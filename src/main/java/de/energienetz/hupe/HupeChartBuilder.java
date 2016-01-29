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
	private final List<HupeDataSeries> dataSeries;
	private final JFreeChart chart;

	public HupeChartBuilder(final List<HupeDataSeries> files) {
		this.dataSeries = files;
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
		for (final HupeDataSeries series : dataSeries) {
			if (series.isVisible()) {
				dataset.addSeries(series.getSeries());
			}
		}
		return dataset;
	}

	public JFreeChart getChart() {
		return chart;
	}

	public void update() {
		chart.getXYPlot().setDataset(getUpdatedDataSet());
	}

	public void clearData() {
		dataSeries.clear();
		update();
	}
}
