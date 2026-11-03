package net.bsnx.jlib.server;

import net.bsnx.jlib.Streams;
import net.bsnx.jlib.net.Mime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class HttpdServletResponse implements IServletResponse {

	private OutputStream out;
	
	private PrintWriter writer;

	public HttpdServletResponse(OutputStream out) {
		this.out = out;
	}
	
	@Override
	public void setContentType(String type) {
		writer = new PrintWriter(out);
		writer.println("HTTP/1.1 200 OK");
		writer.println("Content-Type: "+type);
		writer.println("\n");
	}

	@Override
	public PrintWriter getWriter() {
		return writer;
	}
	
	// {{{ readFile(...)
	
	/**
	 * Liest eine Datei und sendet die an den Client.
	 * 
	 * File f wird als Binaerdatei behandelt.
	 * 
	 * @link http://php.net/readfile
	 */
	public void readFile(File f, String mime) {
		try {
			FileInputStream in = new FileInputStream(f);
			String s = "HTTP/1.1 200 OK\nContent-Type:"+mime+"\n\n";
			Streams.write(out, s);
			Streams.writeTo(in, out);
		} catch (Exception e) {
			throw new RuntimeException("Fehler bei der Ausgabe via readFile(...)", e);
		}
	}
	
	public void readFile(File f) {
		readFile(f, Mime.BIN);
	}
	
	// }}}
	
	public void close() {
		if (writer != null) {
			writer.close();
		} else {
			try {
				out.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
