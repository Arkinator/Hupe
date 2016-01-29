package de.energienetz.hupe;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Period;

public class HupeDateFilter extends AbstractHupeDataFilter {
	private final Period minPeriod;
	private final Period maxPeriod;
	private DateTime minimum;
	private DateTime maximum;

	public HupeDateFilter(final Period maxPeriod, final Period minPeriod) {
		this.minPeriod = minPeriod;
		this.maxPeriod = maxPeriod;
	}

	public HupeDateFilter(final Period maxPeriod) {
		this.maxPeriod = maxPeriod;
		this.minPeriod = Period.seconds(0);
	}

	@Override
	public boolean applyFilter(final TemperatureEntry entry, final HupeDataSeries series) {
		initializeMinimum(series);

		if (new DateTime(entry.getDate()).isBefore(minimum) || //
				new DateTime(entry.getDate()).isAfter(maximum)) {
			return false;
		} else {
			return true;
		}
	}

	private void initializeMinimum(final HupeDataSeries file) {
		if (minimum == null) {
			minimum = getLastEntry(file).minus(maxPeriod);
			maximum = getLastEntry(file).minus(minPeriod);
		}
	}

	private DateTime getLastEntry(final HupeDataSeries file) {
		return new DateTime(file.getEntries().get(file.getEntries().size() - 1).getDate());
	}

	public void setMinimum(final Date date) {
		minimum = new DateTime(date);
	}

	public void setMaximum(final Date date) {
		maximum = new DateTime(date);
	}
}
