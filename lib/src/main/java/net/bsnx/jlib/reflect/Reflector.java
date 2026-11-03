package net.bsnx.jlib.reflect;

import java.util.ArrayList;
import java.util.List;

/**
 * Vereinfachung der Reflection API
 * 
 * @author Bjoern R. Salgert
 * @date 14.08.2011
 */
public class Reflector {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//Class<?> c = Queue.class;
		//System.out.println(is(c, Object.class));
	
		
	}
	
	public static boolean is(Class<?> c, Class<?> possiblity) {
		List<Class<?>> cs = getSuperClasses(c);
		return cs.contains(possiblity);
	}
	
	public static List<Class<?>> getSuperClasses(Class<?> c) {
		List<Class<?>> res = new ArrayList<Class<?>>();
		while (c != null) {
			res.add(c);
			c = c.getSuperclass();
		}
		return res;
	}

}
