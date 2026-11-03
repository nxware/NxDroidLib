package net.bsnx.jlib.server;

import net.bsnx.jlib.FileSystem;
import net.bsnx.jlib.io.LineableInputStream;
import net.bsnx.jlib.io.StoreableInputStream;
import net.bsnx.jlib.net.HeaderCollection;
import net.bsnx.jlib.net.Mime;
import net.bsnx.jlib.net.MultiPartFormDataReader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * HTTP-Server auf Sockets basierend,
 * laeuft in der JVM und auf Android
 * 
 * @author Bjoern R. Salgert
 * @date 27.04.2011
 */
public class Httpd implements Runnable {

	private int port;
	
	private boolean local_only = false;
	
	private IServlet servlet;
	
	/**
	 * Erzeugt einen Server der auf dem Port 8888 lauschen wird.
	 */
	public Httpd() {
		this(8888, new StaticServlet());
	}
	
	/**
	 * Hauptkonstruktor
	 * 
	 * @param port
	 */
	public Httpd(int port, IServlet servlet) {
		this.port = port;
		this.servlet = servlet;
	}
	
	/**
	 * 
	 * @param port
	 * @param servlet
	 * @param local_only	Wenn auf True gesetzt nimmt der Server nur Verbindungen vom Localhost an.
	 */
	public Httpd(int port, IServlet servlet, boolean local_only) {
		this.port = port;
		this.servlet = servlet;
		this.local_only = local_only;
	}
	
	// {{{ Runnable (Server Loop)
	
	@Override
	public void run() {
		try {
			// Hier k�nnte man wenn local_only das ServerSocket st�rker an den localhost binden
            ServerSocket server = new ServerSocket(port);
            try {
	            while (true) {
	                Socket client = server.accept(); // Auf eine Anfrage warten
	                if (local_only && client.getInetAddress().getAddress()[0] != 127 /* 0x0F */) {
	                	client.close();
	                	continue;
	                }
	                System.out.println("Http Anfrage von " + client.getInetAddress());
	                processRequest(client.getInputStream(), client.getOutputStream(), client);
	            }
            } finally {
            	server.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}
	
	// }}}
	
	// {{{ threading
	
	private Thread thread = null;
	
	public void runAsync() {
		thread = new Thread(this);
		thread.start();
	}
	
	// }}}

	/**
	 * @param input
	 * @param output
	 * @param client	Client der die Http-Anfrage stellt.
	 */
	protected void processRequest(InputStream input, OutputStream output, Socket client) {
		try {
			
			StoreableInputStream in = new StoreableInputStream(input);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(in/*, "UTF-8"*/));
			
			HttpdServerletRequest req = new HttpdServerletRequest();
			
			// Zeile: GET / lesen
			final String rawHeadLine = br.readLine();
			final HttpHeadLine headLine = new HttpHeadLine(rawHeadLine);
			System.out.println("Http Anfrage ist " + rawHeadLine);
			req.setHeadLine(headLine);
			if (headLine.isPost()) {
				HeaderCollection headers = new HeaderCollection(br);
				if (headers.getContentType().equals(Mime.FILE_UPLOAD)) {
					MultiPartFormDataReader reader = new MultiPartFormDataReader(headers.getBoundary(), br, in);
					reader.run();
					req.setMultipart(reader);
				}
				//ByteArrayOutputStream bout = in.getStorage();
				//FileSystem.writeToFile("V:\\put.png", bout.toByteArray());
			} else if (headLine.isPut()) {
				HeaderCollection headers = new HeaderCollection(br);
				int length = headers.getContentLength();
				int counter = 0;
				while (counter < (length - 4097)) {
					counter += 4096;
					char[] cbuf = new char[4096]; 
					br.read(cbuf, 0, 4096);
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				int dif = length - counter;
				char[] cbuf = new char[dif];
				br.read(cbuf, 0, dif);
				ByteArrayOutputStream bout = in.getStorage();
				ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
				LineableInputStream lin = new LineableInputStream(bin);
				lin.readTilEmptyLine();
				byte[] bytes = new byte[length]; 
				lin.read(bytes);
				lin.close();
				FileSystem.writeToFile("V:\\put.png", bytes); // TODO
			}
			
			HttpdServletResponse res = new HttpdServletResponse(output);
			
			try {
				servlet.service(req, res);
			} catch (Exception e) {
				System.err.println("Fehler beim ausf�hren des Servlets (service()):");
				e.printStackTrace();
			}
			res.close();
			
		} catch (Exception e) {
			System.err.println("Fehler beim verarbeiten der Http-Anfrage:");
			e.printStackTrace();
		} 
	}
	


	
}
