package net.bsnx.jlib;

import java.util.Arrays;

public class StaticConsole {

	// {{{ e(...)
	
	public static int E_SPACE_OFFSET = 0;
	
	public static void e(String s) {
		System.out.println(Functions.repeat(" ", E_SPACE_OFFSET) + s);
	}
	
	public static void e(int i) {
		e(""+i);
	}
	
	public static void e(boolean b) {
		e( (new Boolean(b)).toString() );
	}
	
	public static void e(Object o) {
		e(""+o);
	}
	
	public static void e(Object[] o) {
		e(""+Arrays.toString(o));
	}
	
	// }}}
	
}
