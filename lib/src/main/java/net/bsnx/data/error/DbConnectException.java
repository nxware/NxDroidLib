package net.bsnx.data.error;

public class DbConnectException  extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DbConnectException(String message, Exception e) {
		super(message, e);
	}

}
