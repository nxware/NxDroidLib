package net.bsnx.json;

import net.bsnx.Php;

public class Json {

	public static String name(String name) {
		return '"' + name + '"';
	}
	
	public static String str(String name, String val) {
		return name(name) + ": " + name(val);
	}

	public static String pair(String name, double val) {
		return name(name) + ": " + val;
	}
	
	public static String pair(String name, int val) {
		return name(name) + ": " + val;
	}
	
	public static String obj(String...pairs) {
		return Php.implode(",\n", pairs);
	}
	
}
