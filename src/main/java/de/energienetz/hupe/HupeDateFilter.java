package de.energienetz.hupe;

import org.joda.time.DateTime;
import org.joda.time.Period;

public class HupeDateFilter extends AbstractHupeDataFilter {
	private final Period period;
	private DateTime minimum;

	public HupeDateFilter(final Period period) {
		this.period = period;
	}

	@Override
	public boolean applyFilter(final TemperatureEntry entry, final CsvFile file) {
		initializeMinimum(file);

		if (new DateTime(entry.getDate()).isBefore(minimum)) {
			return false;
		} else {
			return true;
		}
	}

	private void initializeMinimum(final CsvFile file) {
		if (minimum == null) {
			minimum = getLastEntry(file).minus(period);
		}
	}

	private DateTime getLastEntry(final CsvFile file) {
		return new DateTime(file.getEntries().get(file.getEntries().size() - 1).getDate());
	}
}
