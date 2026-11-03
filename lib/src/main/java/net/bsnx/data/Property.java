package net.bsnx.data;

import net.bsnx.i.Named;

public class Property<T> extends ValueWrapper<T> implements Named {

	private String name;

	public Property(String name, T value) {
		super();
		this.name = name;
		this.val = value;
	}
	
	@Override
	public String getName() {
		return name;
	}

}
