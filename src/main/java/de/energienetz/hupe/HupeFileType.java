package de.energienetz.hupe;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

public enum HupeFileType {
	PNG, JPEG;

	public void saveChart(final JFreeChart chart, final File file, final int width, final int height) throws IOException {
		final File target = correctFileExtension(file);
		switch (this) {
		case PNG:
			ChartUtilities.saveChartAsPNG(target, chart, width, height);
			break;
		case JPEG:
			ChartUtilities.saveChartAsJPEG(target, chart, width, height);
			break;
		default:
			break;
		}
	}

	private File correctFileExtension(final File file) {
		if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(name())) {
			return file;
		} else {
			return new File(file.toString() + "." + name().toLowerCase());
		}
	}
}
