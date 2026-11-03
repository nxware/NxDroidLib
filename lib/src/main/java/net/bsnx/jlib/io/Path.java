package net.bsnx.jlib.io;

import net.bsnx.jlib.collections.Collections;

import java.io.File;

/**
 * Erleichtert das Arbeiten mit (Verzeichnis-)Pfaden
 * 
 * @author Bjoern R. Salgert
 * @date 28.07.2011
 */
public class Path {
	
	/**
	 * Entfernt einen Slash (/) am Ende  des Pfads
	 * 
	 * @param path
	 * @return
	 */
	public static String RTrim(String path) {
		if (path.endsWith("/")) {
			return path.substring(0, path.length()-1);
		} else {
			return path.toString();
		}
	}
	
	public static String RTrim(File path) {
		return RTrim(path.getAbsolutePath().replace('\\', '/'));
	}
	
	public static String LTrim(String path) {
		if (path.startsWith("/")) {
			return path.substring(1);
		} else {
			return path.toString();
		}
	}
	
	public static String LTrim(File path) {
		return LTrim(path.getAbsolutePath().replace('\\', '/'));
	}
	
	public static String Trim(String path) {
		return RTrim(LTrim(path));
	}
	
	public static String Trim(File path) {
		return RTrim(LTrim(path.getAbsolutePath().replace('\\', '/')));
	}
	
	/**
	 * Ermitellt den Relativen Pfad
	 * 
	 * @param base	Kompletter Pfad z.B. /mnt/sdcard/Audiobooks/Holmes/Die verrueckte Miterin
	 * @param full	Relative Pfad komponente z.B. Holmes/Die verrueckte Miterin
	 * @return
	 */
	public static String CalcRelativ(String base, String full) {
		return full.substring(base.length());
	}
	
	public static String CalcRelativ(File base, File full) {
		return Trim(CalcRelativ(base.getAbsolutePath().replace('\\', '/'), full.getAbsolutePath().replace('\\', '/')));
	}
	
	/**
	 * Liefert die einzelnen Verzeichnisnamen eines Pfads zur�ck
	 * 
	 * @param path		Pfad z.B. mnt/sdcard/Audiobooks/SciFi
	 * @return			Verzeichnisnamen z.B. [mnt, sdcard, Audiobook, SciFi]
	 */
	public static String[] GetDirNames(String path) {
		return Trim(path).split("\\/");
	}
	
	/**
	 * �berladung von GetDirNames(String)
	 * 
	 * @param path
	 * @return
	 */
	public static String[] GetDirNames(File path) {
		return GetDirNames(path.getAbsolutePath().replace('\\', '/'));
	}
	
	/**
	 * Ein Beispiel f�r einen PathListString ist die Umgebungsvariable PATH
	 * 
	 * @param paths	z.B. [C:\Programme, D:\bin]
	 * @return		z.B. C:\Programme;D:\bin
	 */
	public static String ToPathListString(String[] paths) {
		return Collections.implode(paths, ";");
	}
	
	/**
	 * Bsp: [Krimi, Englang, Holmes] => [Krimi, Krimi/England, Krimi/England/Holmes]
	 * 
	 * @param dirnames	Ein Pfad repr�sentiert durch einzelne Verzeichnisnamen z.B. [Krimi, Englang, Holmes]
	 * @return	Array mit kompletten Pfadangaben z.B. [Krimi, Krimi/England, Krimi/England/Holmes]
	 */
	public static String[] ExpandFromDirNamePath(String[] dirnames) {
		String[] res = new String[dirnames.length];
		for (int i = 0; i < dirnames.length; i++) {
			res[i] = Collections.implode("/", dirnames, 0, i+1);
		}
		return res;
	}
	
	/**
	 * Benennt den letzten Eintrag in Pfad in newName um
	 * 
	 * @param path
	 * @param newName
	 * @return	Umbenannter Pfad
	 */
	public static String Rename(String path, String newName) {
		String[] names = GetDirNames(Trim(path));
		names[names.length-1] = newName;
		return (path.startsWith("/") ? "/" : "") + Collections.implode(names, "/");
	}
	
	public static String GetDirName(String path) {
		String[] names = GetDirNames(path);
		return names[names.length-1];
	}
	
	/**
	 * Setzt $addpath an $path heran 
	 * 
	 * @param path		z.B. "a/b/c/"
	 * @param addpath	z.B. "/d/e"
	 * @return			z.B. "a/b/c/d/e"
	 */
	public static String Combine(String path, String addpath) {
		return RTrim(path) + "/" + LTrim(addpath);
	}
	
	//public static String Build(String[] names, ) 
	
	/**
	 * Geht im Pfad $path_to_go_up eine Verzeichnisebene Nach Oben aber nicht �ber $root hinaus
	 * 
	 * @test jlib.test.junit.IoPathTest#testGoUp
	 * 
	 * @param path_to_go_up
	 * @param root
	 * @return
	 */
	public static String GoUp(String path_to_go_up, String root) {
		String path = RTrim(path_to_go_up);
		root = RTrim(root);
		if (path.length() <= root.length()) {
			return path;
		} else {
			String[] names = GetDirNames(path);
			String res = Collections.implode("/", names, 0, names.length-1);
			if (path_to_go_up.charAt(0)=='/') {
				return "/" + res;
			} else {
				return res;
			}
		}
	}
	
	private String path;
	
	public Path(String path) {
		this.path = path;
	}
	
	public String[] getDirNames() {
		return Trim(path).split("\\/");
	}
}