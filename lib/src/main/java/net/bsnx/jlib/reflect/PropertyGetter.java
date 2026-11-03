package net.bsnx.jlib.reflect;

import net.bsnx.i.Getter;
import net.bsnx.i.Named;

public class PropertyGetter<T> implements Getter<T, Object>, Named {

	private String name;

	public PropertyGetter(String name) {
		this.name = name;
	}
	
	@Override
	public Object getValue(T instance) {
		JavaClass j = new JavaClass(instance);
		return j.getPropertyValue(name);
	}

	@Override
	public String getName() {
		return name;
	}

	
	
}
