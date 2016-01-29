package de.energienetz.hupe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;

public class HupeDataSeries {
	private String seriesName;
	private boolean visible = true;

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

	private final List<TemperatureEntry> entries;

	public HupeDataSeries(final String seriesName) {
		this.seriesName = seriesName;
		this.entries = new ArrayList<>();
	}

	public TimeSeries getSeries() {
		final TimeSeries series = new TimeSeries(seriesName);
		entries.forEach(entry -> series.addOrUpdate(new Minute(entry.getDate()), entry.getTemp()));
		return series;
	}

	public void addNewEntry(final Date date, final double temp) {
		entries.add(new TemperatureEntry(date, temp));
	}
}
