package net.bsnx.jlib.net;

import net.bsnx.jlib.FileSystem;

import java.io.File;

/**
 * Funktion fuer Mime-Typen und Mime Konstanten
 * 
 * @author Bjoern R. Salgert
 * @date 25.05.2011
 */
public class Mime {

	public static final String TEXT = "text/plain";
	
	public static final String HTML = "text/html";
	
	public static final String XML = "text/xml";
	
	public static final String BIN = "application/octet-stream";
	
	public static final String JPEG = "image/jpeg";
	
	public static final String PNG = "image/png";
	
	public static final String FILE_UPLOAD = "multipart/form-data";
	
	public static String forExtension(String ext) {
		if (ext.equals("jpg")) {
			return JPEG;
		} else if (ext.equals("png")) {
			return PNG;
		} else if (ext.equals("txt") || ext.equals("java")) {
			return TEXT;
		} else if (ext.equals("exe") || ext.equals("sqlite")) {
			return BIN;
		} else {
			return "text/plain";
		}
	}
	
	/**
	 * Ermittelt den Mime-Typ zuer einer gegebenen Datei.
	 * 
	 * @param f
	 * @return
	 */
	public static String forFile(File f) {
		return forExtension(FileSystem.extractExt(f));
	}
	
}
