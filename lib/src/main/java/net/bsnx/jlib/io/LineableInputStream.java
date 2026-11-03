package net.bsnx.jlib.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream der auch ein ReadLine unterstuetzt
 * 
 * @category Binary
 * @author Bjoern R. Salgert
 * @date 02.06.2011
 */
public class LineableInputStream extends InputStream {

	private InputStream in;

	public LineableInputStream(InputStream in) {
		this.in = in;
	}
	
	int pos = 0;
	
	@Override
	public int read() throws IOException {
		int i = in.read();
		if (i != -1) pos++;
		return i;
	}
	
	public String readLine() {
		try {
			StringBuilder sb = new StringBuilder();
			int i = in.read();
			while (i != -1) {
				pos++;
				if (i == 10) {
					return sb.toString();
				}
				sb.append((char)i);
				i = in.read();
			}
			return sb.toString();			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String readLine(ByteArray data) { // <- m�sste gesetzt werden
		try {
			StringBuilder sb = new StringBuilder();
			int i = in.read();
			int pos = 0;
			while (i != -1) {
				// TODO i geht von 0 bis 255 wie zum Byte
				//byte b = (byte)  ((i>127) ? (i-383) : i); // 383
				byte b = (byte) i;
				this.pos++;
				if (i == 10) {
					data.setLength(pos);
					return sb.toString();
				}
				data.set(pos, b);
				sb.append((char)i);
				
				i = in.read();
				pos++;
			}
			data.setLength(pos);
			return sb.toString();		
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * List den Strem bis zu einer Leerzeile
	 */
	public void readTilEmptyLine() {
		String s = readLine();
		while (!s.trim().equals("")) {
			s = readLine();
		}
	}

}
