package de.energienetz.hupe;

import java.util.Date;

public class TemperatureEntry {
	private Date date;
	private double temp;

	public TemperatureEntry(final Date date, final double temp) {
		this.date = date;
		this.temp = temp;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(final double temp) {
		this.temp = temp;
	}
}
