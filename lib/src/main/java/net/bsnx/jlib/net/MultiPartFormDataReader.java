package net.bsnx.jlib.net;

import net.bsnx.jlib.Functions;
import net.bsnx.jlib.io.ByteArray;
import net.bsnx.jlib.io.LineableInputStream;
import net.bsnx.jlib.io.StoreableInputStream;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Verarbeitet einen MultipartFormData Upload (Daten des Mime-Typs "multipart/form-data")
 * 
 * @author Bjoern R. Salgert
 * @date 13.06.2011
 */ 
public class MultiPartFormDataReader implements Runnable {

	private static boolean _DEBUG = false;
	
	/**
	 * fucking test case!
	 * 
	 * Testfall
	 */
	public static void main(String[] args) {
		
		byte[] data = new byte[] {
			// POST /explorer/ HTTP/1.1
			80, 79, 83, 84, 32, 47, 101, 120, 112, 108, 111, 114, 101, 114, 47, 32, 72, 84, 84, 80, 47, 49, 46, 49, 13, 10,
			// Host: localhost:8011
			72, 111, 115, 116, 58, 32, 108, 111, 99, 97, 108, 104, 111, 115, 116, 58, 56, 48, 49, 49, 13, 10,
			// User-Agent: Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1
			85, 115, 101, 114, 45, 65, 103, 101, 110, 116, 58, 32, 77, 111, 122, 105, 108, 108, 97, 47, 53, 46, 48, 32, 40, 87, 105, 110, 100, 111, 119, 115, 32, 78, 84, 32, 54, 46, 49, 59, 32, 114, 118, 58, 50, 46, 48, 46, 49, 41, 32, 71, 101, 99, 107, 111, 47, 50, 48, 49, 48, 48, 49, 48, 49, 32, 70, 105, 114, 101, 102, 111, 120, 47, 52, 46, 48, 46, 49, 13, 10,
			// Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
			65, 99, 99, 101, 112, 116, 58, 32, 116, 101, 120, 116, 47, 104, 116, 109, 108, 44, 97, 112, 112, 108, 105, 99, 97, 116, 105, 111, 110, 47, 120, 104, 116, 109, 108, 43, 120, 109, 108, 44, 97, 112, 112, 108, 105, 99, 97, 116, 105, 111, 110, 47, 120, 109, 108, 59, 113, 61, 48, 46, 57, 44, 42, 47, 42, 59, 113, 61, 48, 46, 56, 13, 10,
			// Accept-Language: de-de,de;q=0.8,en-us;q=0.5,en;q=0.3
			65, 99, 99, 101, 112, 116, 45, 76, 97, 110, 103, 117, 97, 103, 101, 58, 32, 100, 101, 45, 100, 101, 44, 100, 101, 59, 113, 61, 48, 46, 56, 44, 101, 110, 45, 117, 115, 59, 113, 61, 48, 46, 53, 44, 101, 110, 59, 113, 61, 48, 46, 51, 13, 10, 65, 99, 99, 101, 112, 116, 45, 69, 110, 99, 111, 100, 105, 110, 103, 58, 32, 103, 122, 105, 112, 44, 32, 100, 101, 102, 108, 97, 116, 101, 13, 10,
			// Accept-Encoding: gzip, deflate
			65, 99, 99, 101, 112, 116, 45, 67, 104, 97, 114, 115, 101, 116, 58, 32, 73, 83, 79, 45, 56, 56, 53, 57, 45, 49, 44, 117, 116, 102, 45, 56, 59, 113, 61, 48, 46, 55, 44, 42, 59, 113, 61, 48, 46, 55, 13, 10, 75, 101, 101, 112, 45, 65, 108, 105, 118, 101, 58, 32, 49, 49, 53, 13, 10,
			// Content-Type: multipart/form-data; boundary=---------------------------198552485513142
			67, 111, 110, 110, 101, 99, 116, 105, 111, 110, 58, 32, 107, 101, 101, 112, 45, 97, 108, 105, 118, 101, 13, 10, 82, 101, 102, 101, 114, 101, 114, 58, 32, 104, 116, 116, 112, 58, 47, 47, 108, 111, 99, 97, 108, 104, 111, 115, 116, 58, 56, 48, 49, 49, 47, 101, 120, 112, 108, 111, 114, 101, 114, 47, 13, 10,
			
			67, 111, 110, 116, 101, 110, 116, 45, 84, 121, 112, 101, 58, 32, 109, 117, 108, 116, 105, 112, 97, 114, 116, 47, 102, 111, 114, 109, 45, 100, 97, 116, 97, 59, 32, 98, 111, 117, 110, 100, 97, 114, 121, 61, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 49, 57, 56, 53, 53, 50, 52, 56, 53, 53, 49, 51, 49, 52, 50, 13, 10,
			67, 111, 110, 116, 101, 110, 116, 45, 76, 101, 110, 103, 116, 104, 58, 32, 56, 57, 54, 13, 10,
			13, 10,
			45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 49, 57, 56, 53, 53, 50, 52, 56, 53, 53, 49, 51, 49, 52, 50, 13, 10,
			67, 111, 110, 116, 101, 110, 116, 45, 68, 105, 115, 112, 111, 115, 105, 116, 105, 111, 110, 58, 32, 102, 111, 114, 109, 45, 100, 97, 116, 97, 59, 32, 110, 97, 109, 101, 61, 34, 102, 34, 59, 32, 102, 105, 108, 101, 110, 97, 109, 101, 61, 34, 105, 99, 111, 110, 95, 111, 115, 95, 119, 105, 110, 100, 111, 119, 115, 46, 103, 105, 102, 34, 13, 10,
			67, 111, 110, 116, 101, 110, 116, 45, 84, 121, 112, 101, 58, 32, 105, 109, 97, 103, 101, 47, 103, 105, 102, 13, 10, 13, 10,
			// gif begin
			71, 73, 70, 56, 57, 97, 16, 0, 18, 0, -26, 127, 0, -20, -20, -19, -19, -20, -23, -1, -40, 35, -1, -115, 61, -18, -21, -22, -17, -25, -28, -21, -19, -23, -30, -22, -38, -25, -22, -19, -124, -89, -48, -125, -54, 55, -1, -107, 66, 97, -98, -35, 95, -104, 59, -81, -54, 123, -20, -19, -20, -38, -27, -17, -21, -24, -28, 80, -114, 24, -62, -127, 24, 125, -54, 44, 66, 121, -72, 112, -65, -1, 123, -51, 36, -19, -20, -20, 107, -70, -2, 45, 97, -93, -3, -41, 71, 104, -86, -24, -15, 116, 83, -14, 84, 30, 126, -60, 53, -17, -84, 34, -18, -26, -53, -12, -116, 104, -55, -83, -128, -89, -49, -18, 111, -76, 39, -1, -121, 61, 111, -75, 49, -1, 113, 39, 107, -85, 44, -18, -33, -98, -1, -47, 37, -78, -52, -94, -1, -42, 45, -1, -36, 77, -44, -26, -59, -67, 123, 17, -9, -49, 31, -18, -22, -25, -20, 83, 38, -55, -122, 22, -5, -117, 70, -65, -50, 122, 107, -109, -62, -23, -84, -124, -73, -43, -15, -15, 72, 15, -25, -72, -85, 71, -127, -61, -20, 73, 22, -25, -25, -25, -25, -46, -83, -118, -62, -18, 89, -106, 46, 88, -103, -37, -30, -24, -34, 124, -58, -1, 101, -116, -66, 122, -61, -1, -16, -31, -38, -1, -108, 71, 125, -50, 51, -47, -76, -92, 125, -61, -1, 115, -71, -7, -43, -79, 122, -22, 121, 92, -15, -34, -107, -21, -28, -56, -34, -24, -42, -83, -65, -107, -57, -63, -75, -1, 114, 48, -19, -20, -21, -103, -47, 95, -43, -82, -96, -66, -50, -78, -4, -101, 98, -88, -62, -105, -83, -49, 111, -15, -50, 95, 76, -120, 26, -52, -93, 99, -51, -60, -47, -90, -71, -51, -128, -52, 55, -1, -42, 29, 41, 97, -91, -1, -114, 67, 116, -86, 93, 111, -75, -12, 59, 108, -86, -45, -32, -18, 97, -98, 38, -12, -14, -13, -1, 102, 40, -32, -100, -125, -55, -80, -110, -53, -101, 79, -127, -64, -4, -1, -59, 32, -20, -19, -19, 52, 107, -85, -1, -118, 54, -9, -71, 39, -121, -47, 54, -97, -56, -19, -14, -42, -58, -45, -116, 14, -57, -62, -110, -1, -46, 83, -16, -25, -30, -31, -99, 37, -3, 85, 22, -124, -47, 47, -1, -1, -1, 33, -7, 4, 1, 0, 0, 127, 0, 44, 0, 0, 0, 0, 16, 0, 18, 0, 0, 7, -79, -128, 127, -126, -125, -124, -123, -122, -121, -121, 24, 4, 123, 71, 5, -120, -125, 5, 53, 3, 40, 30, 59, -113, 127, 119, 115, 72, 84, 58, 78, 88, 90, 67, 6, 7, 7, -124, 89, 11, 100, 107, 61, 108, 101, 18, 37, 20, 23, 81, -125, 4, 3, 11, 38, 125, 51, 109, 13, 105, 31, 117, 10, 6, -125, 50, 56, 74, 87, 34, 29, 82, 93, 41, 10, 126, 86, 15, -125, 104, 111, 75, 12, 55, 95, 44, 65, 39, 97, 73, 47, -125, 0, 57, 22, 102, 60, 99, 96, 77, 121, 14, 91, 54, 1, -125, 8, 76, 70, 28, 21, 103, 83, 48, 124, 122, 46, 27, 85, -125, 16, 25, 68, 66, 114, 69, 35, 19, 32, 43, -60, 60, 33, 100, 7, 8, -119, 4, 26, 18, -72, -95, 65, -89, -123, -128, 16, -124, -30, 0, -8, 3, 0, -127, 15, 47, 120, -32, 8, -120, -127, -18, 81, -124, 31, 92, 84, 64, -71, 52, 72, -51, 68, -110, 40, 83, 6, 2, 0, 59, 13, 10,
			45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 49, 57, 56, 53, 53, 50, 52, 56, 53, 53, 49, 51, 49, 52, 50, 13, 10,
			67, 111, 110, 116, 101, 110, 116, 45, 68, 105, 115, 112, 111, 115, 105, 116, 105, 111, 110, 58, 32, 102, 111, 114, 109, 45, 100, 97, 116, 97, 59, 32, 110, 97, 109, 101, 61, 34, 115, 117, 98, 109, 105, 116, 34, 13, 10, 13, 10, 83, 101, 110, 100, 101, 110, 13, 10, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 49, 57, 56, 53, 53, 50, 52, 56, 53, 53, 49, 51, 49, 52, 50, 45, 45, 13, 10
		};
		byte[] gif = new byte[] {71, 73, 70, 56, 57, 97, 16, 0, 18, 0, -26, 127, 0, -20, -20, -19, -19, -20, -23, -1, -40, 35, -1, -115, 61, -18, -21, -22, -17, -25, -28, -21, -19, -23, -30, -22, -38, -25, -22, -19, -124, -89, -48, -125, -54, 55, -1, -107, 66, 97, -98, -35, 95, -104, 59, -81, -54, 123, -20, -19, -20, -38, -27, -17, -21, -24, -28, 80, -114, 24, -62, -127, 24, 125, -54, 44, 66, 121, -72, 112, -65, -1, 123, -51, 36, -19, -20, -20, 107, -70, -2, 45, 97, -93, -3, -41, 71, 104, -86, -24, -15, 116, 83, -14, 84, 30, 126, -60, 53, -17, -84, 34, -18, -26, -53, -12, -116, 104, -55, -83, -128, -89, -49, -18, 111, -76, 39, -1, -121, 61, 111, -75, 49, -1, 113, 39, 107, -85, 44, -18, -33, -98, -1, -47, 37, -78, -52, -94, -1, -42, 45, -1, -36, 77, -44, -26, -59, -67, 123, 17, -9, -49, 31, -18, -22, -25, -20, 83, 38, -55, -122, 22, -5, -117, 70, -65, -50, 122, 107, -109, -62, -23, -84, -124, -73, -43, -15, -15, 72, 15, -25, -72, -85, 71, -127, -61, -20, 73, 22, -25, -25, -25, -25, -46, -83, -118, -62, -18, 89, -106, 46, 88, -103, -37, -30, -24, -34, 124, -58, -1, 101, -116, -66, 122, -61, -1, -16, -31, -38, -1, -108, 71, 125, -50, 51, -47, -76, -92, 125, -61, -1, 115, -71, -7, -43, -79, 122, -22, 121, 92, -15, -34, -107, -21, -28, -56, -34, -24, -42, -83, -65, -107, -57, -63, -75, -1, 114, 48, -19, -20, -21, -103, -47, 95, -43, -82, -96, -66, -50, -78, -4, -101, 98, -88, -62, -105, -83, -49, 111, -15, -50, 95, 76, -120, 26, -52, -93, 99, -51, -60, -47, -90, -71, -51, -128, -52, 55, -1, -42, 29, 41, 97, -91, -1, -114, 67, 116, -86, 93, 111, -75, -12, 59, 108, -86, -45, -32, -18, 97, -98, 38, -12, -14, -13, -1, 102, 40, -32, -100, -125, -55, -80, -110, -53, -101, 79, -127, -64, -4, -1, -59, 32, -20, -19, -19, 52, 107, -85, -1, -118, 54, -9, -71, 39, -121, -47, 54, -97, -56, -19, -14, -42, -58, -45, -116, 14, -57, -62, -110, -1, -46, 83, -16, -25, -30, -31, -99, 37, -3, 85, 22, -124, -47, 47, -1, -1, -1, 33, -7, 4, 1, 0, 0, 127, 0, 44, 0, 0, 0, 0, 16, 0, 18, 0, 0, 7, -79, -128, 127, -126, -125, -124, -123, -122, -121, -121, 24, 4, 123, 71, 5, -120, -125, 5, 53, 3, 40, 30, 59, -113, 127, 119, 115, 72, 84, 58, 78, 88, 90, 67, 6, 7, 7, -124, 89, 11, 100, 107, 61, 108, 101, 18, 37, 20, 23, 81, -125, 4, 3, 11, 38, 125, 51, 109, 13, 105, 31, 117, 10, 6, -125, 50, 56, 74, 87, 34, 29, 82, 93, 41, 10, 126, 86, 15, -125, 104, 111, 75, 12, 55, 95, 44, 65, 39, 97, 73, 47, -125, 0, 57, 22, 102, 60, 99, 96, 77, 121, 14, 91, 54, 1, -125, 8, 76, 70, 28, 21, 103, 83, 48, 124, 122, 46, 27, 85, -125, 16, 25, 68, 66, 114, 69, 35, 19, 32, 43, -60, 60, 33, 100, 7, 8, -119, 4, 26, 18, -72, -95, 65, -89, -123, -128, 16, -124, -30, 0, -8, 3, 0, -127, 15, 47, 120, -32, 8, -120, -127, -18, 81, -124, 31, 92, 84, 64, -71, 52, 72, -51, 68, -110, 40, 83, 6, 2, 0, 59};
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		StoreableInputStream input = new StoreableInputStream(in);
		BufferedReader br = new BufferedReader(new InputStreamReader(input));
		HeaderCollection headers = new HeaderCollection(br);
		_DEBUG = true;
		MultiPartFormDataReader reader = new MultiPartFormDataReader(headers.getBoundary(), br, input);
		reader.run();
		System.out.println(reader);
		Item file = reader.get(0);
		System.out.println("GIF Length: "  + gif.length);
		System.out.println(Arrays.toString(gif));
		System.out.println(Arrays.toString(file.data));
		if (file.data.equals(gif)) {
			System.out.println("Super");
		}
	}
	
	private static boolean isFileUpload(String line) {
		return line.contains("filename=");
	}
	
	private String boundary;
	private BufferedReader br;
	
	private StoreableInputStream in;

	/**
	 * 
	 * @param boundary	Trennzeichenkette, wird im Header uebertragen
	 * @param br		Reader der den Header gelesen hat und vor dem nun die MultipartformData liegt
	 * @param in		Binaerdaten (header ist auch noch enthalten)
	 */
	public MultiPartFormDataReader(String boundary, BufferedReader br, StoreableInputStream in) {
		this.boundary = boundary;
		this.in = in;
		//this.in.clearStorage();
		this.br = br;
	}
	
	public static class Item {
		public Item(String key, byte[] data, String filename) {
			this.key = key;
			this.data = data;
			this.filename = filename;
		}
		String key;
		byte[] data;
		String filename;
		public void saveToFile(File f) {
			try {
				FileOutputStream out = new FileOutputStream(f);
				out.write(data);
				out.close();
			} catch (IOException e) {
				throw new RuntimeException("Fehler beim Speichern der Datei", e);
			}
		}
		@Override
		public String toString() {
			return "MultipartItem("+key+", Datalength: "+data.length+" Bytes, Filename: "+filename+")";
		}
	}
	
	private List<Item> items = new ArrayList<Item>();
	
	private static interface STATE {
		final int BEGIN = 0;
		final int BLOCK_ENTER = 1;
		final int IN_BLOCK = 2;
		final int READ_CONTENTTYPE = 3;
		final int READ_LINE = 4;
	}
	
	private String readLine() throws IOException {
		String line = br.readLine();
		// Stockt nach einer endblock zeile
		//debug("[HTTP] " + line);
		return line;
	}
	
	private void debug(String s) {
		if (_DEBUG) {
			System.out.println(s);
		}
	}

	@Override
	public void run() {
		try {
			int state = STATE.BEGIN;
			String line = readLine();
			String key = null;
			//ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			StringWriter buffer = new StringWriter();
			while (state==STATE.READ_LINE || state==STATE.IN_BLOCK || !Functions.empty(line)) {
				//debug("[PRE] " + line);
				switch (state) {
				case STATE.BEGIN:
					state = STATE.BLOCK_ENTER; 
					break;
				case STATE.BLOCK_ENTER:
					key = line.toString();
					state = STATE.READ_LINE;
					if (isFileUpload(key)) {
						state = STATE.READ_CONTENTTYPE;
						// TODO den namen bitte speichern
					}
					break;
				case STATE.IN_BLOCK:
					// TODO testen und alles
					if (line.equals("--"+boundary+"--") || line.equals("--"+boundary)) {
					//if (Functions.equals(line, "--"+boundary+"--") || Functions.equals(line, "--"+boundary)) {
						// TODO eintrag abschlie�en
						//items.add(new Item(key, buffer, in.getStorage()));
						key = null;
						buffer = new StringWriter();
						state = STATE.BEGIN;
					} else {
						// TODO in buffer schreiben
						buffer.write(line+ "\r\n");
					}
					break;
				case STATE.READ_CONTENTTYPE:
					state = STATE.READ_LINE;
					break;
				case STATE.READ_LINE:
					state = STATE.IN_BLOCK;
					break;
				}
				line = readLine();
				if ( ("--"+boundary+"--").equals(line.trim()) ) {		//
					break;												// f�r den Upload aus C#
				}														// 
			}
			//debug("[HTTP] Items: " + items.size());
			//items.get(0).saveToFile(new File("V:\\test.png"));
			//in.saveToFile(new File("V:\\data.dat"));
			//get();
		} catch (IOException e) {
			throw new RuntimeException("IO Fehler im MultiPartFormDataReader", e);
		}	
		// OK Stream wurde gelesen
		ByteArrayOutputStream bout = in.getStorage();
		//debug("[BINDATA] " +  Arrays.toString(bout.toByteArray()));
		ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
		LineableInputStream lin = new LineableInputStream(bin);
		lin.readTilEmptyLine();
		// Okay jetzt liegt die multipartdata auf dem Stream lin
		readFiledsBinary(lin);
		//items.get(0).saveToFile(new File("V:\\a.png"));
		//items.toString();
	}
	
	/**
	 * Liest die Multipart-Felder als Bin�rdaten aus
	 * 
	 * @param in	Stream auf dem die Multipartdata liegt
	 */
	private void readFiledsBinary(LineableInputStream in) {
		try {
			StateMachine automat = new StateMachine(in);
			automat.execute();
		} catch (Exception e) {
			//throw new RuntimeException("IO Fehler im MultiPartFormDataReader", e);
		}
	}
	
	private class StateMachine {
		LineableInputStream in;
		int state = STATE.BEGIN;
		ByteArray bytes = new ByteArray(4096);
		String key = null;
		HeaderValue val;
		String filename = "";
		ByteArrayOutputStream bufferB = new ByteArrayOutputStream();
		StringWriter bufferS = new StringWriter();
		int exit_counter = 0;
		
		StateMachine(LineableInputStream in) {
			debug("StateMachine created.");
			this.in = in;
		}
		public void execute() throws IOException {
			String line = in.readLine(bytes);
			//debug("[BIN] " + line);
			while (state==STATE.READ_LINE || state==STATE.IN_BLOCK || !Functions.empty(line)) {
				//debug("[BIN] " + line);
				switch (state) {
				case STATE.BEGIN:
					state = STATE.BLOCK_ENTER; 
					key = line.toString();
					val = new HeaderValue(key);
					key = val.getExtraValue("name");
					if (key != null) {
						state = STATE.READ_LINE;
						debug("BEGINNNNNNNNNNNNNNN");
					}
					break;
				case STATE.BLOCK_ENTER:
					key = line.toString();
					val = new HeaderValue(key);
					key = val.getExtraValue("name");
					state = STATE.READ_LINE;
					debug("[HTTP POST] " + key);
					if (isFileUpload(line)) {
						state = STATE.READ_CONTENTTYPE;
						filename = val.getExtraValue("filename");
					}
					break;
				case STATE.IN_BLOCK:
					debug("IN_BLOCK");
					if (line.trim().equals("--"+boundary+"--") || line.trim().equals("--"+boundary)) {
						byte[] buf = bufferB.toByteArray();
						byte[] dat = Arrays.copyOf(buf, Math.max(buf.length-2, 0));
						items.add(new Item(key, dat, filename));
						toBegin();
					} else {
						bufferB.write(bytes.getTrimedArray());
						bufferB.write(new byte[] {10});
						bufferS.write(line+ "\n");
						//if (line.trim().equals("")) 
							exit_counter++;
						if (exit_counter > 1000) {
							items.add(new Item(key, bufferB.toByteArray(), filename));
							debug("Too Much");
							return;
						}
					}
					break;
				case STATE.READ_CONTENTTYPE:
					state = STATE.READ_LINE;
					break;
				case STATE.READ_LINE:
					state = STATE.IN_BLOCK;
					break;
				}
				line = in.readLine(bytes);
			}
		}
		private void toBegin() {
			debug("[HTTP POST] end.");
			key = null;
			bufferB = new ByteArrayOutputStream();
			filename = "";
			exit_counter = 0;
			state = STATE.BEGIN;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Item item : this.items) {
			sb.append(item);
		}
		return sb.toString();
	}

	public void saveToFile(File f) {
		byte[] bytes = in.getStorage().toByteArray();
		int c = 0;
		int index = 0;
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i]==13 && bytes[i+1]==10 && bytes[i+2]==13 && bytes[i+3]==10) c++;
			if (c == 2) {
				index = i+4;
				break;
			}
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			out.write(bytes, index, bytes.length-index);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getFilename() {
		for (Item item : items) {
			if (item.filename != null && item.filename.length()>0) {
				return item.filename;
			}
		}
		return "";
	}
	
	public Item get(int i) {
		return items.get(i);
	}
	
	public String get(String key) {
		for (Item item : items) {
			if (item.key != null && item.key.equals(key)) {
				return new String(item.data);
			}
		}
		return "";
	}

}
