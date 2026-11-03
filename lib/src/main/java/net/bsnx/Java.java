package net.bsnx;

import net.bsnx.jlib.Streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Iterator;

public class Java {

	/**
	 * Schreibt eine Exception als Text
	 *  
	 * @param ex
	 * @return
	 */
	public static String toString(Throwable ex) {
		StringBuilder sb = new StringBuilder();
		if (ex instanceof Exception) {
			sb.append("Fehler: " + ex.getClass().getSimpleName() + ": " + ((Exception) ex).getMessage());
			sb.append("\n");
			for (StackTraceElement elem : ((Exception) ex).getStackTrace()) {
				sb.append("  " + elem.getClassName() + "." + elem.getMethodName() + ":" + elem.getLineNumber());
				sb.append("\n");
			}
			if (((Exception) ex).getCause() != null) {
				sb.append(toString(((Exception) ex).getCause()));
			}
		}
		return sb.toString();
	}
	
	public static String toString(StackTraceElement[] trace) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement elem : trace) {
			sb.append("  " + elem.getClassName() + "." + elem.getMethodName() + ":" + elem.getLineNumber());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public static String toString(Object obj) { 
		return obj == null ? "" : obj.toString();
	}
	
	public static boolean toBool(String s) {
		if (s == null) {
			return false;
		} else {
			String b = s.toLowerCase();
			return  "1".equals(b) || 
					"true".equals(b) ||
					"yes".equals(b) ||
					"y".equals(b) ||
					"j".equals(b) ||
					"ja".equals(b) ||
					"on".equals(b) ||
					"an".equals(b) ||
					"wahr".equals(b);
		}
	}
	
	/**
	 * Fuehrt einen Process aus und schreibt die ausgabe des Prozesses nach stdout
	 * 
	 * @param cmd Wie Runtime.exec(cmd)
	 */
	public static void execOnStdOut(String...cmd) {
		try {
			Process p= Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
			    System.out.println(line);
			}
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static String execForStdOut(String...cmd) {
		try {
			Process p= Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
			    sb.append(line+"\n");
			}
			p.waitFor();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void exec(String cmd) {
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException __) {}
		
	}

	public static <T> Iterable<T> toIterable(final Enumeration<T> enumeration) {
		return new Iterable<T>() {
			@Override public Iterator<T> iterator() {
				return new Iterator<T>() {
					@Override public boolean hasNext() {
						return enumeration.hasMoreElements();
					}
					@Override public T next() {
						return enumeration.nextElement();
					}
					@Override public void remove() {}
				};
			}
		};
	}
	
	/**
	 * 
	 * @param cls	
	 * @param name
	 * @return
	 */
	public static String loadRessourceAsString(Class<?> cls, String name) {
		String pkg = cls.getPackage().getName().replace('.', '/');
		InputStream stream = cls.getResourceAsStream("/" + pkg + "/" + name);
		return Streams.toString(stream);
	}

	public static int parseIntOp(String param, int errorValue) {
		try {
			return Integer.parseInt(param);
		} catch (Exception e) {
			return errorValue;
		}
	}
	
}
