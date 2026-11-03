package net.bsnx.i;

public interface IConfig {

	String getString(String name, String defaultValue);
	boolean getBool(String name, boolean defaultValue);
	
}
