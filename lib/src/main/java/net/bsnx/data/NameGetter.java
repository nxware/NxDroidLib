package net.bsnx.data;

import net.bsnx.i.Getter;
import net.bsnx.i.Named;

public class NameGetter<T extends Named> implements Getter<T, String> {

	@Override
	public String getValue(T instance) {
		return instance.getName();
	}

}
