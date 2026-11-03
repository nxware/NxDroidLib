package net.bsnx.jlib.server;

/**
 * Bietet Zugriff auf die Daten der ersten Zeile einer HTTP-Anfrage
 * 
 * @author Bjoern Salgert
 * @date 27.04.2011
 */
class HttpHeadLine {
	
	private static final String METHOD_GET = "GET";
	
	private static final String METHOD_POST = "POST";
	
	private static final String METHOD_PUT = "PUT";

	private String method;
	
	private String path;
	
	private String fullpath;
	
	private String query;
	
	private String protocol;
	
	public HttpHeadLine(String line) {
		String[] frist_args = line.split(" ");
		method	 =	frist_args[0];
        path	 =	frist_args[1];	
        protocol =	frist_args[2];
        fullpath = path.toString();
        if (path.contains("?")) {
        	query = path.substring(path.indexOf('?'));
        	path = path.substring(0, path.indexOf('?'));
        }
	}
	
	/**
	 * Liefert das Protocol mit Version zur�ck "HTTP/1.0" oder "HTTP/1.1"
	 * 
	 * @return
	 */
	public String getProtocol() {
		return protocol;
	}
	
	public String getMethod() {
		return method;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getQuery() {
		return query;
	}
	
	@Override
	public String toString() {
		return method + " " + path + (query.length()>0 ? "?" + query : "") + " " + protocol;
		
	}

	public boolean isGet() {
		return method.equals(METHOD_GET);
	}
	
	public boolean isPost() {
		return method.equals(METHOD_POST);
	}
	
	public boolean isPut() {
		return method.equals(METHOD_PUT);
	}

	public String getFullPath() {
		return fullpath;
	}
	
}