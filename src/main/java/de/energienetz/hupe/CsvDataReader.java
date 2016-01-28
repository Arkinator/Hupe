package de.energienetz.hupe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvDataReader {
	private static Logger logger = LoggerFactory.getLogger(CsvDataReader.class);

	private final String inputFilename;
	private final List<CsvFile> fileList;

	public CsvDataReader(final File file) {
		fileList = new ArrayList<>();
		inputFilename = file.getAbsolutePath();
		tryToReadAsZipFile(file);
	}

	private void tryToReadAsZipFile(final File file) {
		try {
			final ZipFile zipFile = new ZipFile(file);
			zipFile.stream().forEach(entry -> tryToReadZipEntry(zipFile, entry));
			zipFile.close();
		} catch (final IOException e) {
			logger.error("Fehler beim Einlesen der Zip-Datei '" + file + "':\n" + ExceptionUtils.getStackTrace(e));
			throw new InputReadingException(e);
		}
	}

	private void tryToReadZipEntry(final ZipFile zipFile, final ZipEntry entry) {
		try {
			tryToReadCsvFile(zipFile.getInputStream(entry), entry.getName());
		} catch (final IOException e) {
			logger.error("Fehler beim Einlesen des Eintrags '" + entry + "' einer Zip-Datei:\n" + ExceptionUtils.getStackTrace(e));
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
