package net.bsnx;

import java.util.ArrayList;
import java.util.List;

public class StaticFunctions {

	public static void println(Object obj) {
		System.out.println(obj.toString());
	}
	
	public static void errorLog(Object obj) {
		System.err.println(obj.toString());
	}
	
	public static void async(final Runnable runnable) {
		new Thread(runnable).start();
	}
	
	public static <T> List<T> reverse(List<T> list) {
		ArrayList<T> res = new ArrayList<T>(list.size());
		for (int i = list.size()-1; i >= 0; i--) {
			res.add(list.get(i));
		}
		return res;
	}
	
}
