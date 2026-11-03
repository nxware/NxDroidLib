package net.bsnx.jlib.reflect;

import net.bsnx.data.Row;
import net.bsnx.data.reflection.IDataObjectReflection;
import net.bsnx.data.reflection.IProperty;
import net.bsnx.i.Named;
import net.bsnx.jlib.collections.Lst;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class JavaClass implements IDataObjectReflection, Named {

	Object obj;
	Class<?> c;
	Method[] methods;
	List<Method> getter = new ArrayList<Method>();
	
	public JavaClass(Object obj) {
		this.obj = obj;
		c = obj.getClass();
		scan();
	}
	
	public JavaClass(Class<?> c) {
		this.c = c;
		scan();
	}
	
	private void scan() {
		methods = c.getMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("get") &&
				method.getParameterCount() == 0 &&
				Modifier.isPublic(method.getModifiers()) &&
				!"get".equals(method.getName()) &&
			    !"getClass".equals(method.getName())) 
			{
				getter.add(method);
			}
		}
	}
	
	@Override
	public List<IProperty> getProperties() {
		List<IProperty> res = new ArrayList<IProperty>();
		for (Method m : getter) {
			res.add(new JavaProperty(this, m));
		}
		return res;
	}

	@Override
	public String getTypeName() {
		return c.getName();
	}

	@Override
	public String getName() {
		return c.getSimpleName();
	}
	
	public String getQualifiedName() {
		return c.getName();
	}
	
	public Row toMetaRow() {
		Row row = new Row();
		row.add("simplename", getName());
		row.add("qualifiedname", getQualifiedName());
		return row;
	}
	
	@Override
	public String toString() {
		return getQualifiedName();
	}

	public Object getPropertyValue(String name) {
		for (IProperty p : getProperties()) {
			if (p.getName().equalsIgnoreCase(name)) {
				return p.getValue();
			}
		}
		return null;
	}
	
	public List<Method> getMethods() {
		return new Lst<Method>(c.getMethods());
	}

}
