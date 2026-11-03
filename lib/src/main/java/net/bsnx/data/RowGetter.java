package net.bsnx.data;

import net.bsnx.i.Getter;
import net.bsnx.i.Named;

public class RowGetter implements Getter<Row, String>, Named {

	private String fieldName;

	public RowGetter(String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Override
	public String getValue(Row instance) {
		return instance.getString(fieldName);
	}
	
	@Override
	public String toString() {
		return "RowGetter for " + fieldName;
	}

	@Override
	public String getName() {
		return fieldName;
	}

}
