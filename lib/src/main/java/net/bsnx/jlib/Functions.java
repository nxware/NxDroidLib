package net.bsnx.jlib;

/**
 * Utility Functions
 * 
 * @author Bjoern R. Salgert
 * @date 12.08.2010
 */
public class Functions {

	// {{{ empty
	
	public static boolean empty(byte[] line) {
		if (line == null) return true;
		return line.length == 0;
	}
	
	/**
	 * @link http://php.net/empty
	 */
	public static boolean empty(String s) {
		if (s == null) return true;
		return s.length() == 0;
	}
	
	// }}}
	
	/**
	 * @category String
	 */
	public static String repeat(String s, int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append(s);
		}
		return sb.toString();
	}
	
	/**
	 * @link http://php.net/rtrim
	 * @category String
	 * @param str
	 * @param charlist
	 * @return
	 */
	public static String rtrim(String str, String charlist) {
		if (empty(str)) return "";
		while (charlist.contains(""+str.charAt(str.length()-1))) {
			if (str.length()==1) return "";
			str = str.substring(0, str.length()-1);
		}
		return str;
	}
	
	/**
	 * @link http://php.net/ltrim
	 * @category String
	 * @param str
	 * @param charlist
	 * @return
	 */
	public static String ltrim(String str, String charlist) {
		if (empty(str)) return "";
		while (charlist.contains(""+str.charAt(0))) {
			if (str.length()==1) return "";
			str = str.substring(1);
		}
		return str;
	}
	
	/**
     * Fuehrt ein Runnable in einem Thread aus
     * 
     * @param r
     */
    public static Thread async(Runnable r) {
    	Thread t = new Thread(r);
    	t.setDaemon(true);
    	t.start();
    	return t;
    }
    
    public static boolean equals(byte[] array, String str) {
    	for (int i = 0; i < array.length; i++) {
			if (array[i] != (byte)str.charAt(i)) return false; 
		}
    	return true;
    }
    
    public static void saveRun(Runnable runnable) {
    	try {
    		if (runnable != null) {
    			runnable.run();
    		}
    	} catch (Exception __) {}
    }
    
    public static void sleep(int milis) {
    	try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {}
    }
    
    public static int sum(int[] array) {
    	int res = 0;
    	for (int i = 0; i < array.length; i++) {
			res += array[i];
		}
    	return res;
    }

}