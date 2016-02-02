package de.energienetz.hupe;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvDataReader {
	private static Logger logger = LoggerFactory.getLogger(CsvDataReader.class);

	private final List<CsvFile> fileList;

	public CsvDataReader(final File file) {
		fileList = new ArrayList<>();
		if (!tryToReadAsZipFile(file)) {
			tryToReadAsCsvFile(file);
		}
	}

	private void tryToReadAsCsvFile(final File file) {
		try (InputStream inStream = new BOMInputStream(new FileInputStream(file))) {
			tryToReadCsvFile(inStream, file.getName());
		} catch (final IOException e) {
			Log.warn(e.getMessage());
		}
	}

	private boolean tryToReadAsZipFile(final File file) {
		try {
			final ZipFile zipFile = new ZipFile(file);
			zipFile.stream().forEach(entry -> tryToReadZipEntry(zipFile, entry));
			zipFile.close();
			return true;
		} catch (final Exception e) {
			logger.warn("Fehler beim Einlesen der Zip-Datei '" + file + "':\n" + ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	private void tryToReadZipEntry(final ZipFile zipFile, final ZipEntry entry) {
		try {
			tryToReadCsvFile(new BOMInputStream(zipFile.getInputStream(entry)), entry.getName());
		} catch (final IOException e) {
			throw new InputReadingException(e);
		}
	}

	private void tryToReadCsvFile(final InputStream inputStream, final String fileName) throws IOException {
		fileList.add(new CsvFile(IOUtils.readLines(inputStream), fileName));
	}

	public List<CsvFile> getFileList() {
		return fileList;
	}

	public class InputReadingException extends RuntimeException {
		private static final long serialVersionUID = 3950541090365156513L;

		public InputReadingException(final IOException e) {
			super(e);
		}
	}
}
