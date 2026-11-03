package net.bsnx.jlib.net;

import net.bsnx.i.Callback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCP {

	public static TcpServer createLineReader(final int port, final Callback<String> onLine) {
		TcpServer server = new TcpServer(port, false) {
			@Override protected void processRequest(Socket client) {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
					String line = reader.readLine();
					while (line != null) {
						onLine.execute(line);
						line = reader.readLine();
					} 
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		server.run();
		return server;
	}
	
	public static boolean isPortUseable(int port) {
		try {
			ServerSocket server = new ServerSocket(port);
			// TODO testen
			return true;
		} catch (Exception __) {
			return false;
		}
	}

}
