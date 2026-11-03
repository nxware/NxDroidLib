package net.bsnx.jlib.net;

import net.bsnx.i.Closeable;

import java.net.ServerSocket;
import java.net.Socket;

import static net.bsnx.StaticFunctions.async;

public abstract class TcpServer implements Runnable, Closeable {

	private int port;
	private boolean local_only;
	
	private ServerSocket server;

	public TcpServer(int port, boolean local_only) {
		this.port = port;
		this.local_only = local_only;
	}

	/**
	 * Start den Server nicht blockierent
	 */
	@Override
	public void run() {
		async(new Runnable() { @Override public void run() {
			try {
	            server = new ServerSocket(port);
	            while (true) {
	                Socket client = server.accept(); // Auf eine Anfrage warten
	                if (local_only && client.getInetAddress().getAddress()[0] != 127 /* 0x0F */) {
	                	client.close();
	                	continue;
	                }
	                processRequest(client);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}});
	}

	protected abstract void processRequest(Socket client);

	@Override
	public void close() {
		try {
			server.close();
		} catch (Exception __) {}
	}
	
}
