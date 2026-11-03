package net.bsnx.data.reflection;

import net.bsnx.i.Titled;

import java.util.List;

public interface IProperty extends Titled {

	String getName();
	
	Class<?> getType();
	
	IDataObjectReflection getDataObjectType();
	
	boolean isWriteable();
	
	/**
	 * Class.isPrimitiv & String 
	 * 
	 * @return
	 */
	boolean isScalar();

	String asString();
	
	boolean isList();
	
	List<IProperty> getList();

	Object getValue();
	
}
