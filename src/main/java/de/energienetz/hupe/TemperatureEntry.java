package de.energienetz.hupe;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class TemperatureEntry {
	// 30.11.2015 02:08:12
	// Mon Nov 30 02:08:12 CET 2015
	private static final String dateParsePattern = "dd.MM.yyyy hh:mm:ss";
	private Date date;
	private double temp1;
	private double temp2;

	public TemperatureEntry(final String[] parts) {
		try {
			date = DateUtils.parseDate(parts[0], dateParsePattern);
			temp1 = Double.parseDouble(parts[1].replace(",", "."));
			temp2 = Double.parseDouble(parts[2].replace(",", "."));
		} catch (final Exception e) {
			throw new DateParseException(e);
		}
	}

	public Date getDate() {
		return date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public double getTemp1() {
		return temp1;
	}

	public void setTemp1(final double temp1) {
		this.temp1 = temp1;
	}

	public double getTemp2() {
		return temp2;
	}

	public void setTemp2(final double temp2) {
		this.temp2 = temp2;
	}

	public class DateParseException extends RuntimeException {
		private static final long serialVersionUID = 2698583506459715717L;

		public DateParseException(final Exception e) {
			super(e);
		}
	}
}
