package net.bsnx;

import net.bsnx.jlib.DateUtils;
import net.bsnx.jlib.FileSystem;

import java.util.List;

public class Php {

	public static String implode(String glue, String...strings) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strings.length; i++) {
			sb.append(strings[i]);
			if (i != strings.length-1) {
				sb.append(glue);
			}
		}
		return sb.toString();
	}
	
	public static String implode(String glue, List<String> strings) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strings.size(); i++) {
			sb.append(strings.get(i));
			if (i != strings.size()-1) {
				sb.append(glue);
			}
		}
		return sb.toString();
	}
	
	public static String file_get_contents(String path) {
		return FileSystem.toString(path);
	}
	
	public static void file_put_contents(String path, byte[] data) {
		FileSystem.writeToFile(path, data);
	}
	
	public static void file_put_contents(String path, String data) {
		FileSystem.writeToFile(path, data.getBytes());
	}
	
	public static int time() {
		return DateUtils.currentUnixTimeStamp();
	}
	
	public static String nl2br(String str) {
		if (str == null) return "";
		return str.replace("\n", "<br>\n");
	}
	
	/**
	 * md5("test") 098f6bcd4621d373cade4e832627b4f6
	 * @param md5
	 * @return
	 */
	public String md5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {}
		return null;
	}

	public static String[] explode(String delimiter, String s) {
		return s.split(delimiter);
	}
	
}
