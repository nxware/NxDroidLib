package net.bsnx.jlib;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.zip.GZIPInputStream;

/**
 * Hilfsfunktionen fuer den Umgang mit Streams
 * 
 * @author Bjoern R. Salgert
 * @date 26.05.2011
 */
public class Streams {

	static int BUFFER_SIZE = 1024;
	
	/**
	 * Schreibt einen Stream in den anderen.
	 * 
	 * F�r byte-basierte Str�me
	 * 
	 * @category Binary
	 * 
	 * @param source	Quelle die gelesen wird
	 * @param target	Ziel in das geschrieben wird
	 */
	public static void writeTo(InputStream source, OutputStream target) {
		try {
			byte[] buf = new byte[BUFFER_SIZE];
			int i;
			i = source.read(buf, 0, BUFFER_SIZE);
			while (i>0) {
				target.write(buf, 0, i);
				i = source.read(buf, 0, BUFFER_SIZE);
			} 
		} catch (IOException e) {
			throw new RuntimeException("Fehler beim schreiben eines Streams in den anderen", e);
		}
	}
	
	/**
	 * Schreibt einen String in einem Stream
	 * 
	 * @category Binary
	 * 
	 * @param target
	 * @param s
	 */
	public static void write(OutputStream target, String s) {
		char[] chars = s.toCharArray();
		try {
			for (char c : chars) {
				target.write(c);
			}
		} catch (IOException e) {
			throw new RuntimeException("Fehler beim schreiben in einen Bin�r Stream", e);
		}
	}
	
	public static String toString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		try {
			int n;
			InputStreamReader reader = new InputStreamReader(is);
	        char tmp_buffer[] = new char[4096];
	        while ((n = reader.read(tmp_buffer)) > 0) {
	            sb.append(tmp_buffer, 0, n);
	
	        }
		} catch (Exception e) {}
        return sb.toString();
	}
	
	public static BufferedReader createReaderForString(String s) {
		StringReader sr = new StringReader(s);
		return new BufferedReader(sr);
	}
	
	public static BufferedReader createReaderForFile(String path) {
		try {
			Reader reader = new FileReader(new File(path));
			return new BufferedReader(reader);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Fehler beim lesen der Datei " + path, e);
		}
	}
	
	public static boolean isGzipStream(byte[] bytes) {
	      int head = ((int) bytes[0] & 0xff) | ((bytes[1] << 8) & 0xff00);
	      return (GZIPInputStream.GZIP_MAGIC == head);
	}

	public static FileOutputStream open(String path) {
		try {
			return new FileOutputStream(path);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Fehler beim �ffnen der Datei " + path, e);
		}
		
	}

	public static InputStream read(String path) {
		try {
			return new FileInputStream(path);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static byte[] read(InputStream is) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int size = 4096;
		try {
			byte[] buffer = new byte[size];
			int count = is.read(buffer, 0, size);
			while (count > 0) {
				bos.write(buffer, 0, count);
				count = is.read(buffer, 0, size);
			}
		} catch (IOException e) {
			throw new RuntimeException("Fehler beim auslesen eines InputStreams", e);
		}
		return bos.toByteArray();
	}
	
}
