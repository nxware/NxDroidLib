package net.bsnx.jlib;

import net.bsnx.Php;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Funktionen zum Umgang mit dem Dateisystem
 * 
 * @author Bjoern R. Salgert
 * @date 18.04.2011
 */
public class FileSystem {

	// {{{ mkdir...
	
	public static boolean mkdirs(File dir, String[] tocreate) {
		return mkdirs(dir.getAbsolutePath(), tocreate);
	}
	
	public static boolean mkdirs(String dir, String[] tocreate) {
		boolean res = true;
		for (String s : tocreate) {
			if (!(new File(dir + s).mkdirs())) {
				res = false;
			}
		}
		return res;
	}
	
	public static boolean mkdir(String path) {
		File f = new File(path);
		f.mkdirs();
		return f.isDirectory();
	}
	
	// }}}
	
	// {{{ extractExt(...)
	
	public static String extractExt(File f) {
		return extractExt(f.getName());
	}
	
	/**
	 * Liefert die Dateierweiterung in kleinen Buchstaben ohne Punkt zurueck.
	 * 
	 * Bsp: Aus C:\myfile.mp3 wird mp3
	 * 
	 * @param path
	 * @return
	 */
	public static String extractExt(String path) {
		String[] array = path.split("\\.");
		return array[array.length-1].toLowerCase();
	}
	
	// }}}
	
	public static String extractFileName(String file) {
		String[] array = file.replace("\\", "/").split("/");
		return array[array.length-1];
	}
	
	public static String extractFileTitle(String file) {
		return removeExt(extractFileName(file));
	}
	
	public static String removeExt(String path) {
		if (path.charAt(path.length()-4) == '.') {
			return path.substring(0, path.length()-4);
		} else {
			return path;
		}
	}
	
	public static String extractFilePath(String file) {
		String[] array = file.replace("\\", "/").split("/");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length-1; i++) {
			sb.append(array[i]);
			if (i < array.length-1) {
				sb.append(File.separatorChar);
			}
			
		}
		return (file.startsWith("/")?"/":"") + sb.toString();
	}
	
	/**
	 * Entfernt die VerzeichnisTrennZeichen (/\) von Anfang und Ende des Strings $path
	 * 
	 * @param path
	 * @return
	 */
	public static String removeAllTrailingPathDelimiters(String path) {
		final String DELIMITERS = "/\\";
		return Functions.ltrim(Functions.rtrim(path, DELIMITERS), DELIMITERS);
	}
	
	public static void writeToFile(String path, byte[] data) {
		try {
			FileOutputStream out = new FileOutputStream(new File(path));
			out.write(data);
			out.flush();
			out.close();
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim schreiben eines ByteArray in eine Datei", e);
		}
	}
	
	public static void copyFile(String from, String to) {
		copyFile(new File(from), new File(to));
	}
	
	/**
	 * @link http://www.journaldev.com/861/4-ways-to-copy-file-in-java
	 * 
	 * @param from
	 * @param to
	 */
	public static void copyFile(File from, File to) {
		FileInputStream in;
		FileOutputStream out;
		try {
			in = new FileInputStream(from);
			out = new FileOutputStream(to);
			byte[] buffer = new byte[ 4096 ];
			int len = in.read(buffer);
			while (len != -1) {
				out.write(buffer, 0, len);
				len = in.read(buffer);
		    }
			in.close();
			out.close();
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim Dateikopieren", e);
		}
	}
	
	/**
	 * @link http://php.net/file_get_contents
	 * 
	 * Siehe auch die Methoden der Klasse Php
	 * 
	 * @param f
	 * @return
	 */
	public static String fileGetContents(File f) {
		try {
			FileReader in = new FileReader(f);
			char[] buffer = new char[ 4096 ];
			StringBuilder sb = new StringBuilder();
			int len = in.read(buffer);
			while (len != -1) {
				sb.append(buffer, 0, len);
				len = in.read(buffer);
		    }
			in.close();
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim lesen einer Datei (Vielleicht existiert die Datei nicht)", e);
		}
	}
	
	public static String fileGetContents(String path) {
		return fileGetContents(new File(path));
	}
	
	public static List<String> readLines(File f) {
		try {
			FileReader in = new FileReader(f);
			BufferedReader br = new BufferedReader(in);
			List<String> res = new ArrayList<String>();
			String s = br.readLine();
			while (s != null) {
				res.add(s);
				s = br.readLine();
			}
			br.close();
			return res;
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim lesen einer Datei (Vielleicht existiert die Datei nicht)", e);
		}
	}
	
	public static List<String> readLines(String path) {
		return readLines(new File(path));
	}
	
	/**
	 * Escapt einen String $s so das er als Verzeichnisnamen verwendet werden kann
	 * 
	 * @param s
	 * @return
	 */
	public static String escape(String s) {
		return s.replace('/', '_').replace('\\', '_').replace('*', '_');
	}
	
	public static String formatBytes(long bytes) {
		return formatBytes(bytes, NumberFormat.getInstance());
	}
	
	public static String formatBytes(long bytes, NumberFormat format) {  
		String[] strs = {"Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};        
		for(int i = strs.length-1; i>0; i--) {
			if(bytes > Math.pow(1024, i)) {  
				bytes /= Math.pow(1024, i);  
				return format.format(bytes) + " " + strs[i];  
			} 
		}
		return format.format(bytes) + " Bytes";  
	}
	
	/**
	 * L�scht ein Verzeichnis auch wenn es Unterverzeichnisse und Dateiene enth�lt.
	 * 
	 * Diese Methode arbeitet rekursiv
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
	
	/**
	 * 
	 * @param path
	 * @param files
	 * @param basePath	Pfad der vor jeden Eintrag in $files verrangestellt wird (endet mit /)
	 */
	public static void listFilesRek(File path, List<String> files, String basePath) {
		File[] contents = path.listFiles();
		if (contents != null) {
			for (File file : contents) {
				if (file.isDirectory()) {
					listFilesRek(file, files, basePath + file.getName() + "/");
				} else {
					String add = basePath + file.getName();
					if (add.startsWith("/")) add = add.substring(1);
					files.add(add);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param path
	 * @return	Liste mit absoluten Pfade
	 */
	public static List<String> listFilesRek(File path) {
		List<String> res = new ArrayList<String>();
		listFilesRek(path, res, "/");
		return res;
	}
	
	public static List<String> listFilesRek(String path) {
		return listFilesRek(new File(path));
	}
	
	/**
	 * 
	 * @param path
	 * @return Liste mit absoluten Pfade
	 *  auch Verzeichnisse
	 */
	public static List<String> listFiles(String path) {
		List<String> res = new ArrayList<String>();
		File[] contents = (new File(path)).listFiles();
		if (contents != null) {
			for (File f : contents) {
				res.add(f.getAbsolutePath());
			}
		}
		return res;
	}
	
	public static String cleanFilename(String fileName) {
		return fileName
				.replace('*', '_')
				.replace(':', '_')
				.replace('/', '_')
				.replace('\\', '_')
				.replace('"', '_')
				.replace('\'', '_')
				.replace('?', '_');
	}

	public static long sizeOf(String fullpath) {
		return new File(fullpath).length();		
	}
	
	public static String toString(String filePath) {
		try {
			FileInputStream fis = new FileInputStream(filePath);
			String res = Streams.toString(fis);
			fis.close();
			return res;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void appendLine(String path, String line) {
		FileOutputStream os = Streams.open(path);
		try {
			os.write((line+"\n").getBytes());
			os.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void appendLineTSV(String path, String...values) {
		appendLine(path, Php.implode("\t", values));
	}
	
	/**
	 * Filter alle Dateien aus der Liste files die nicht mit einer der Dateiendungen aus ext
	 * �bereinstimmen.
	 * 
	 * Bsp. 
	 * 		files = (a.jpg, b.jpg, c.txt)
	 * 		ext = jpg
	 *  Ergibt ----------> (a.jpg, b.jpg)
	 * 
	 * @param files
	 * @param ext
	 * @return
	 */
	public static List<String> filterExt(List<String> files, String...ext) {
		List<String> res = new ArrayList<String>();
		for (String file : files) {
			String current_ext = FileSystem.extractExt(file);
			wanted: for (String wanted_ext : ext) {
				if (current_ext.toLowerCase().equals(wanted_ext.toLowerCase())) {
					res.add(file);
					continue wanted;
				}
			}
		}
		return res;
	}
	
	/**
	 * 
	 * @param path
	 * @return URL wie "file:///D:/data/file.txt"
	 */
	public static String toUrl(String path) {
		String s = path.replace('\\', '/').replace(" ", "%20");
		return "file:///" + s;
	}

	public static boolean existsFile(String path) {
		return new File(path).exists();
	}

	public static boolean delete(String path) {
		return (new File(path)).delete();
	}
	
}
