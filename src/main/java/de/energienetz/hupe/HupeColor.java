package de.energienetz.hupe;

import java.awt.Color;

public enum HupeColor {
	notSet(null, "<standard>"), //
	red(Color.red, "Rot"), //
	green(Color.green, "Grün"), //
	blue(Color.blue, "Blau"), //
	white(Color.white, "Weiß"), //
	black(Color.black, "Schwarz"), //
	orange(Color.orange, "Orange"), //
	yellow(Color.yellow, "Gelb"), //
	magenta(Color.magenta, "Magenta"), //
	cyan(Color.cyan, "Cyan"), //
	gray(Color.gray, "Grau");

	private String name;
	private Color color;

	private HupeColor(final Color color, final String name) {
		this.color = color;
		this.name = name;
	}

	public Color getAwtColor() {
		return color;
	}

	public String getName() {
		return name;
	}
}
