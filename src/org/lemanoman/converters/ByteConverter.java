package org.lemanoman.converters;

import java.text.DecimalFormat;

public class ByteConverter {
	public static long toBytes(String valor) {
		String metrica = valor.substring(valor.length() - 2, valor.length())
				.toUpperCase();
		float size = Float.parseFloat(valor.substring(0, valor.length() - 2)
				.replace(" ", ""));
		if (metrica.contains("GB")) {
			return (long) size * 1024 * 1024 * 1024;
		} else if (metrica.contains("MB")) {
			return (long) size * 1024 * 1024;
		} else if (metrica.contains("KB")) {
			return (long) size * 1024;
		}
		return 0;
	}

	public static float getGB(long bytes) {
		float val = 1024;
		return ((bytes / val) / val) / val;
	}

	public static float getMB(long bytes) {
		float val = 1024;
		return (bytes / val) / val;
	}

	public static float getKB(long bytes) {
		float val = 1024;
		return (bytes / val);
	}

	public static String getSizeHumanReadable(long size) {

		DecimalFormat formatter = new DecimalFormat("0.0");
		if (size > 1024 && size <= (1024 * Math.pow(10, 3))) {
			return formatter.format(getKB(size)) + "KB";
		} else if (size > (1024 * Math.pow(10, 3))
				&& size <= (1024 * Math.pow(10, 6))) {
			return formatter.format(getMB(size)) + "MB";
		} else if (size > (1024 * Math.pow(10, 6))
				&& size <= (1024 * Math.pow(10, 9))) {
			return formatter.format(getGB(size)) + "GB";
		} else {
			return size + "Bytes";
		}
	}
}
