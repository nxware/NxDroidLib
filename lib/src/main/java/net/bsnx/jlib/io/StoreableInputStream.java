package net.bsnx.jlib.io;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Stream der die Daten die ausgelesen werden zusaetzlich speichert
 * 
 * @category Binary
 * @author Bjoern R. Salgert
 * @date 02.06.2011
 */
public class StoreableInputStream extends InputStream {

	public static void main(String[] args) {
		try {
			
			byte[] bytes = {'C', '\r', '\n', 'D'};
			ByteArrayInputStream input = new ByteArrayInputStream(bytes);
			StoreableInputStream in = new StoreableInputStream(input);
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new InputStreamReader(in/*, "UTF-8"*/));
			br.readLine();
			System.out.println(Arrays.toString(in.getStorage().toByteArray()));
			in.saveToFile(new File("V:\\bytes.txt"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private InputStream in;

	public StoreableInputStream(InputStream in) {
		this.in = in;
	}
	
	private ByteArrayOutputStream storage = new ByteArrayOutputStream();
	
	@Override
	public int read() throws IOException {
		/*int i = in.read();
		if (storage != null) {
			//storage.write(new byte[] {(byte)i});
			storage.write(i);
		}
		return i;
		*/
		byte[] buf = new byte[] { 0 };
		int len = read(buf, 0, 1);
		if (len > 0) {
			return buf[0];
		} else {
			return -1;
		}
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int _len =  in.read(b, off, len);
		if (storage != null) {
			storage.write(b, off, _len);
		}
		return _len;
	}

	public void clearStorage() {
		storage = new ByteArrayOutputStream();
	}
	
	public ByteArrayOutputStream getStorage() {
		//try {
		//	storage.flush();
		//} catch (IOException e) {}
		return storage;
	}

	public void saveToFile(File file) {
		try {
			FileOutputStream out = new FileOutputStream(file);
			out.write(storage.toByteArray());
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
