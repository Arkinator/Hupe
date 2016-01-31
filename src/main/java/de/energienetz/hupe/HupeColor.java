package de.energienetz.hupe;

import java.awt.Color;

public enum HupeColor {
	notSet(null), red(Color.red), green(Color.green), blue(Color.blue);

	private Color color;

	private HupeColor(final Color color) {
		this.color = color;
	}

	public Color getAwtColor() {
		return color;
	}
}
