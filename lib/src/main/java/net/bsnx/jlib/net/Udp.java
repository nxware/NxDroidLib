package net.bsnx.jlib.net;

import net.bsnx.i.Callback;
import net.bsnx.i.Closeable;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Collections;
import java.nio.charset.StandardCharsets;
import java.net.NetworkInterface;
import java.net.InterfaceAddress;
import java.net.SocketException;

/**
 * Einfacher Umgang mit dem UDP-Protokoll
 * 
 * @author Bjoern R. Salgert
 * @date 06.08.2011
 */
public class Udp {

	/**
	 * Versendet ein Paket per UDP
	 * 
	 * @param message
	 * @param toHost
	 * @param toPort
	 */
	public static void send(String message, String toHost, int toPort) {
		try {
			DatagramSocket s = new DatagramSocket();
			InetAddress host = InetAddress.getByName(toHost);
			byte[] bytes = message.getBytes();
			DatagramPacket p = new DatagramPacket(bytes, bytes.length, host, toPort);
			s.send(p);
			s.close();
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim versenden eines UDP-Paketes", e);
		}
	}
	
	/**
	 * Sendet die Nachricht $message als Broadcast
	 * 
	 * @param message
	 * @param toPort
	 */
	public static void send(String message, int toPort) {
		send(message, "255.255.255.255", toPort);
	}
	
	/**
	 * Sendet message (UTF-8) als Broadcast an die Broadcast-Adresse jeder aktiven IPv4-Schnittstelle
	 * (z.B. 192.168.1.255 im WLAN); ohne solche Schnittstelle an 255.255.255.255.
	 * Blockierend, nicht im UI-Thread aufrufen.
	 *
	 * @return Anzahl der Adressen, an die gesendet wurde
	 */
	public static int broadcast(String message, int toPort) throws IOException {
		byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
		Set<InetAddress> targets = new LinkedHashSet<>();
		for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
			if (!ni.isUp() || ni.isLoopback()) {
				continue;
			}
			for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
				if (ia.getBroadcast() != null) {
					targets.add(ia.getBroadcast());
				}
			}
		}
		if (targets.isEmpty()) {
			targets.add(InetAddress.getByName("255.255.255.255"));
		}
		try (DatagramSocket s = new DatagramSocket()) {
			s.setBroadcast(true);
			for (InetAddress target : targets) {
				s.send(new DatagramPacket(bytes, bytes.length, target, toPort));
			}
		}
		return targets.size();
	}

	/**
	 * Empfaengt ein UDP_Paket
	 * 
	 * (Blockierent)
	 * 
	 * @param serverPort
	 * @return
	 */
	public static String receive(int serverPort) {
		String res = null;
	    try {
			byte[] message = new byte[1500];	
			DatagramPacket p = new DatagramPacket(message, message.length);
			DatagramSocket s = new DatagramSocket(serverPort);
			s.receive(p);
			res = new String(message, 0, p.getLength());
			s.close();
	    } catch (Exception e) {
	    	throw new RuntimeException("Fehler beim empfangen eines UDP-Paketes", e);
	    }
	    return res;
	}
	
	/**
	 * (non-blocking)
	 * 
	 * @param serverPort
	 * @param receiver		(Wird aus einen anderen Thread aufgerufen)
	 * @return
	 */
	public static Closeable startReceiver(int serverPort, final Callback<String> receiver) {
		final DatagramSocket socket;
		try {
			 socket = new DatagramSocket(serverPort);
		} catch (SocketException e) {
			throw new RuntimeException("Fehler beim erstellen eines UDP-Sockets auf Port " + serverPort, e);
		}
		final BooleanHolder holder = new BooleanHolder();
		final Thread t = new Thread(new Runnable() {
			@Override public void run() {
				byte[] message = new byte[1500];
				DatagramPacket p = new DatagramPacket(message, message.length);
				while (holder.active) {
					try {
						socket.receive(p);
						String s = new String(message, 0, p.getLength());
						receiver.execute(s);
					} catch (IOException e) {}
				}
				socket.close();
			}
		});
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
		return new Closeable() {
			@Override
			public void close() {
				holder.active = false;
			}
		};
	}
	
	/**
	 * Hilfsklasse f�r die verwendung in startReceiver(...)
	 */
	private static class BooleanHolder {
		public boolean active = true;
	}
	
}
