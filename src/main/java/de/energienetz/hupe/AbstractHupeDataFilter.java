package de.energienetz.hupe;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractHupeDataFilter {
	private boolean active = true;

	public List<TemperatureEntry> execute(final List<TemperatureEntry> input, final HupeDataSeries series) {
		if (!active) {
			return input;
		}
		List<TemperatureEntry> copy = new LinkedList<>(input);
		Iterator<TemperatureEntry> it = copy.iterator();
		while(it.hasNext())
			if (!applyFilter(it.next(), series))
				it.remove();

		return copy;
//		return input.stream() //
//				.filter(entry -> applyFilter(entry, series)) //
//				.collect(Collectors.toList());
	}

	public abstract boolean applyFilter(final TemperatureEntry entry, HupeDataSeries series);

	public void setActive(final boolean active) {
		this.active = active;
	}
}
