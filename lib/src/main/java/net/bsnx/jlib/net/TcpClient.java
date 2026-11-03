package net.bsnx.jlib.net;

import net.bsnx.i.Callback;
import net.bsnx.jlib.io.LineReadWrite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TcpClient implements LineReadWrite {

	public TcpClient() {
		
	}
	
	public TcpClient(String host, int port) {
		this();
		connect(host, port);
	}
	
	private Socket sock;
	
	private List<Callback<String>> onReadLine = new ArrayList<Callback<String>>();
	
	public void addReadLineListner(Callback<String> listener) {
		onReadLine.add(listener);
	}
	
	public void connect(String host, int port) {
		try {
			sock = new Socket(host, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void writeln(String text) {
		try {
			OutputStream os = sock.getOutputStream();
			os.write( (text+"\n").getBytes());
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void write(byte[] data) {
		try {
			OutputStream os = sock.getOutputStream();
			os.write( data);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void raiseReadLine(String line) {
		for (Callback<String> cb : onReadLine) {
			cb.execute(line);
		}
	}
	
	public void beginReceive() {
		try {
			final InputStream is = sock.getInputStream();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			Thread thread = new Thread(new Runnable() {
				@Override public void run() {
					while (true) {
						try {
							String line = reader.readLine();
							if (line==null) break;
							raiseReadLine(line);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			thread.setDaemon(true);
			thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private BufferedReader _reader = null;
	
	@Override
	public String readln() {
		try {
			if (_reader == null) {
				_reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			}
			return _reader.readLine();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
