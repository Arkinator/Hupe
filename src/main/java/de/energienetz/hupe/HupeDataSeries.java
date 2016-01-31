package de.energienetz.hupe;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;

public class HupeDataSeries {
	private String seriesName;
	private boolean visible = true;
	private List<TemperatureEntry> entries;
	private HupeColor color = null;

	public HupeDataSeries(final String seriesName) {
		this.seriesName = seriesName;
		this.entries = new ArrayList<>();
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(final String seriesName) {
		this.seriesName = seriesName;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	public TimeSeries getSeries(final List<AbstractHupeDataFilter> dataFilters) {
		final TimeSeries series = new TimeSeries(seriesName);
		getFilteredData(dataFilters).forEach(entry -> series.addOrUpdate(new Minute(entry.getDate()), entry.getTemp()));
		return series;
	}

	private List<TemperatureEntry> getFilteredData(final List<AbstractHupeDataFilter> dataFilters) {
		List<TemperatureEntry> result = new ArrayList<>();
		for (final TemperatureEntry entry : entries) {
			result.add(new TemperatureEntry(entry.getDate(), entry.getTemp()));
		}

		for (final AbstractHupeDataFilter filter : dataFilters) {
			result = filter.execute(result, this);
		}
		return result;
	}

	public void addNewEntry(final Date date, final double temp) {
		entries.add(new TemperatureEntry(date, temp));
	}

	public List<TemperatureEntry> getEntries() {
		return entries;
	}

	public void setEntries(final List<TemperatureEntry> l) {
		this.entries = l;
	}

	public HupeColor getColor() {
		return color;
	}

	public void setColor(final HupeColor v) {
		this.color = v;
	}

	public void decoratePlot(final int index, final XYItemRenderer xyItemRenderer) {
		if (color != null) {
			xyItemRenderer.setSeriesPaint(index, this.color.getAwtColor());
		}
	}
}
