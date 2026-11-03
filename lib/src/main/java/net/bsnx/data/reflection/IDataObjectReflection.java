package net.bsnx.data.reflection;

import java.util.List;

/**
 * 
 * net.bsnx.jlib.ui.DataForm
 * 
 * @author Salgert
 *
 */
public interface IDataObjectReflection {

	List<IProperty> getProperties();
	
	String getTypeName();
	
}
