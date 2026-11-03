package net.bsnx.data;

import net.bsnx.data.reflection.IDataObjectReflection;
import net.bsnx.data.reflection.IProperty;

import java.util.ArrayList;
import java.util.List;

public class StringProperty extends Property<String> implements IProperty {

	public StringProperty(String name, String value) {
		super(name, value);
	}

	@Override
	public String getTitle() {
		return getName();
	}

	@Override
	public Class<?> getType() {
		return null;
	}

	@Override
	public IDataObjectReflection getDataObjectType() {
		return null;
	}

	@Override
	public boolean isWriteable() {
		return false;
	}

	@Override
	public boolean isScalar() {
		return true;
	}

	@Override
	public String asString() {
		return toString();
	}

	@Override
	public boolean isList() {
		return false;
	}

	@Override
	public List<IProperty> getList() {
		return new ArrayList<IProperty>();
	}

	@Override
	public Object getValue() {
		return val();
	}

}
