package de.energienetz.hupe;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractHupeDataFilter {
	public void execute(final CsvFile file) {
		final List<TemperatureEntry> l = file.getEntries().stream() //
				.filter(entry -> applyFilter(entry, file)) //
				.collect(Collectors.toList());
		file.setEntries(l);
	}

	public abstract boolean applyFilter(final TemperatureEntry entry, CsvFile file);
}
