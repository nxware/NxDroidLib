package net.bsnx.jlib.server;

public interface IServletResponse {

	public void setContentType(String type);
	
	public java.io.PrintWriter getWriter();
	
}
