package net.bsnx.data;

import net.bsnx.Java;
import net.bsnx.i.IConfig;

import java.util.Properties;

public class Config implements IConfig {

	private Properties props;
	private String prefix;

	public Config(Properties props, String prefix) {
		this.props = props;
		this.prefix = prefix;
	}
	
	@Override
	public String getString(String name, String defaultValue) {
		if (props.containsKey(prefix + "." + name)) {
			return props.getProperty(prefix + "." + name);
		} else if (props.containsKey(prefix + name)) {
			return props.getProperty(prefix + name);
		} else if (props.containsKey(name)) {
			return props.getProperty(name);
		} else {
			return defaultValue;
		}
	}

	@Override
	public boolean getBool(String name, boolean defaultValue) {
		return Java.toBool(getString(name, defaultValue?"true":"false"));
	}

}
