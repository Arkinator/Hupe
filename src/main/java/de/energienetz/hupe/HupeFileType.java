package de.energienetz.hupe;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

public enum HupeFileType {
	PNG, JPEG;

	public void saveChart(final JFreeChart chart, final File target, final int width, final int height) throws IOException {
		switch (this) {
		case PNG:
			ChartUtilities.saveChartAsPNG(target, chart, width, height);
		case JPEG:
			ChartUtilities.saveChartAsJPEG(target, chart, width, height);
		default:
			break;
		}
	}
}
