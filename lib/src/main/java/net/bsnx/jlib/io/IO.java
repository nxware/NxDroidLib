package net.bsnx.jlib.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class IO {

	public static void e(String s) {
		System.out.println(s);
	}

	public static String readln() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			return br.readLine();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @link http://de.wikipedia.org/wiki/Java-Properties-Datei
	 * @param filePath
	 */
	public static Properties loadProperties(String filePath) {
		Properties properties = new Properties();
		try {
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream(filePath));
			properties.load(stream);
			stream.close();
		} catch (Exception e) {
			throw new RuntimeException("Error while load Properties from a file", e);
		}
		return properties;
	}
	
}
