package net.bsnx.jlib.server;

public interface IServletRequest {

	/**
	 * Returns the name and version of the protocol the request uses in the form protocol/majorVersion.minorVersion, for example, HTTP/1.1.
	 * 
	 * @return
	 */
	public String getProtocol();
	
	/**
	 * Returns any extra path information after the servlet name but before the query string, and translates it to a real path. 
	 * Same as the value of the CGI variable PATH_TRANSLATED.
	 * If the URL does not have any extra path information, this method returns null or the servlet container cannot translate the virtual path to a real path for any reason (such as when the web application is executed from an archive). The web container does not decode this string.
	 *     
	 * @return a String specifying the real path, or null if the URL does not have any extra path information
	 */
	public String getPathTranslated();
	
	/**
	 * Returns the name of the HTTP method with which this request was made, for example, GET, POST, or PUT.
	 * 
	 * @return
	 */
	public String getMethod();
	
	
	public String getUrl();
	
	public String getQueryString();
	
}
