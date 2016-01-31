package de.energienetz.hupe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.MonthDateFormat;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.joda.time.format.ISODateTimeFormat;

public class HupeChartBuilder {
	private final List<HupeDataSeries> dataSeries;
	private final JFreeChart chart;
	private final List<AbstractHupeDataFilter> dataFilters;
	private DateTickUnit tickUnit = new DateTickUnit(DateTickUnitType.DAY, 1);
	private final DateAxis xAxis;
	private final NumberAxis yAxis;

	public HupeChartBuilder(final List<HupeDataSeries> files) {
		this.dataFilters = new ArrayList<>();
		this.dataSeries = files;
		this.chart = ChartFactory.createXYLineChart("", "", "", //
				getUpdatedDataSet(), // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
		);
		yAxis = new NumberAxis("Temperatur (C°)");
		yAxis.setAutoRangeIncludesZero(false);
		xAxis = new DateAxis("Messzeitpunkt");
		setTickUnit(getTickUnit());
		chart.getXYPlot().setRangeAxis(yAxis);
		chart.getXYPlot().setDomainAxis(xAxis);
	}

	public DateTickUnit getTickUnit() {
		return tickUnit;
	}

	public void setTickUnit(final DateTickUnit unit) {
		this.tickUnit = unit;
		xAxis.setTickUnit(getTickUnit());
		if (unit.getUnitType() == DateTickUnitType.DAY) {
			xAxis.setDateFormatOverride(new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN));
		} else {
			xAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
		}
	}

	private XYDataset getUpdatedDataSet() {
		final TimeSeriesCollection dataset = new TimeSeriesCollection();
		for (final HupeDataSeries series : dataSeries) {
			if (series.isVisible()) {
				dataset.addSeries(series.getSeries(dataFilters));
			}
		}
		return dataset;
	}

	public JFreeChart getChart() {
		return chart;
	}

	public void update() {
		chart.getXYPlot().setDataset(getUpdatedDataSet());
		decoratePlot((XYPlot) chart.getPlot());
	}

	private void decoratePlot(final XYPlot plot) {
		int i = 0;
		for (final HupeDataSeries series : dataSeries) {
			series.decoratePlot(i++, plot.getRenderer());
		}
	}

	public void clearData() {
		dataSeries.clear();
		update();
	}

	public void addFilter(final AbstractHupeDataFilter filter) {
		dataFilters.add(filter);
	}
}
