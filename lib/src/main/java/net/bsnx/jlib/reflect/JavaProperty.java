package net.bsnx.jlib.reflect;

import net.bsnx.Java;
import net.bsnx.data.reflection.IDataObjectReflection;
import net.bsnx.data.reflection.IProperty;

import java.lang.reflect.Method;
import java.util.List;

public class JavaProperty implements IProperty {

	private JavaClass owner;
	private Method m;
	private Class<?> type;

	public JavaProperty(JavaClass owner, Method getter) {
		this.owner = owner;
		this.m = getter;
		this.type = m.getReturnType();
	}

	@Override
	public String getTitle() {
		return getName();
	}

	@Override
	public String getName() {
		return m.getName().substring(3);
	}

	@Override
	public Class<?> getType() {
		return type;
	}

	@Override
	public IDataObjectReflection getDataObjectType() {
		return new JavaClass(call());
	}

	@Override
	public boolean isWriteable() {
		// TODO
		return false;
	}

	@Override
	public boolean isScalar() {
		return type.isPrimitive() || String.class.equals(type);
	}

	@Override
	public String asString() {
		return Java.toString(call());
	}

	@Override
	public boolean isList() {
		return false;
	}

	@Override
	public List<IProperty> getList() {
		return null;
	}
	
	public Object call() {
		try {
			if (m.getParameterCount()==0) {
				return m.invoke(owner.obj);
			} else {
				return "";
			}
		} catch (Exception e) {
			//throw new RuntimeException(e);
			return "";
		}
	}

	@Override
	public Object getValue() {
		return call();
	}
	
}
