package net.bsnx.jlib.io;

/**
 * Objekt das einen ByteArray wrappt,
 * so kann ein byte[] auch als Ausgabeparameter einer Methode benutzt werden.
 * 
 * @author Bjoern R. Salgert
 * @date 07.06.2011
 */
public class ByteArray {

	/**
	 * Benutzte laenge des byte[]
	 */
	int length = 0;
	
	byte[] array;
	
	public ByteArray(int buf_length) {
		array = new byte[buf_length];
	}
	
	public void set(int pos, byte b) {
		array[pos] = b;
	}
	
	public void setLength(int val) {
		length = val;
	}
	
	public byte[] getTrimedArray() {
		byte[] res = new byte[length];
		for (int i = 0; i < length; i++) {
			res[i] = array[i];
		}
		return res;
	}
	
}
