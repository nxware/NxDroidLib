package net.bsnx.jlib.server;

import net.bsnx.jlib.net.MultiPartFormDataReader;

public interface IMultipartProvider {

	public MultiPartFormDataReader getMultipart();
	
}
