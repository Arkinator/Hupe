package de.energienetz.hupe;

import java.awt.Color;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.Align;

public class HupeChartBuilder {
	public static final String initialDiagramTitle = "Heizungsdiagramm";

	private final List<HupeDataSeries> dataSeries;
	private final JFreeChart chart;
	private final List<AbstractHupeDataFilter> dataFilters;
	private DateTickUnit tickUnit = new DateTickUnit(DateTickUnitType.DAY, 1);
	private final DateAxis xAxis;
	private final NumberAxis yAxis;
	private String title = initialDiagramTitle;

	private BufferedImage logo;

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
		chart.setTitle(title);
		chart.getXYPlot().setRangeAxis(yAxis);
		chart.getXYPlot().setDomainAxis(xAxis);
		loadLogo();
		chart.getPlot().setBackgroundImage(logo);
		chart.getPlot().setBackgroundImageAlignment(Align.CENTER);
		chart.getPlot().setBackgroundImageAlpha(0.2f);
		filterDefaultColors();
	}

	private void filterDefaultColors() {
		final List<Paint> lst = new ArrayList<Paint>();
		lst.addAll(Arrays.asList(DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE));
		final Iterator<Paint> it = lst.iterator();
		while (it.hasNext()) {
			final Color c = (Color) it.next();
			if (c.equals(Color.lightGray)) {
				it.remove();
			}
		}
	}

	private void loadLogo() {
		try (InputStream imageStream = getClass().getResourceAsStream("/images/bdeLogo.png");) {
			logo = ImageIO.read(imageStream);
		} catch (final IOException e) {
		}
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
		chart.setTitle(title);
		decoratePlot((XYPlot) chart.getPlot());
	}

	private void decoratePlot(final XYPlot plot) {
		int i = 0;
		for (final HupeDataSeries series : dataSeries) {
			if (series.isVisible()) {
				series.decoratePlot(i++, plot.getRenderer());
			}
		}
	}

	public void clearData() {
		dataSeries.clear();
		update();
	}

	public void addFilter(final AbstractHupeDataFilter filter) {
		dataFilters.add(filter);
	}

	public void setTitle(final String text) {
		this.title = text;
	}
}
