package de.energienetz.hupe;

import java.util.List;
import java.util.stream.Collectors;

import org.jfree.data.time.TimeSeries;

public abstract class AbstractHupeDataFilter {
	private boolean active = true;

	public List<TemperatureEntry> execute(final List<TemperatureEntry> input, final HupeDataSeries series) {
		if (!active) {
			return input;
		}
		return input.stream() //
				.filter(entry -> applyFilter(entry, series)) //
				.collect(Collectors.toList());
	}

	public abstract boolean applyFilter(final TemperatureEntry entry, HupeDataSeries series);

	public void setActive(final boolean active) {
		this.active = active;
	}
}
