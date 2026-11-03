package net.bsnx.jlib.text;

/**
 * Formatiert Text, fuer Natrlichsprachlichen Text gedacht
 * 
 * @static
 * @author Bjoern R. Salgert
 * @date 30.05.2011
 */
public class TextFormat {
	
	public static void main(String[] args) {
		System.out.println("byteToHex((byte)0x01): " + byteToHex((byte)0x01));
		System.out.println("byteToHex((byte)0xff): " + byteToHex((byte)0xff));
	}

	/**
	 * Formatiert eine Mengenangabe f�r die eine sigularform und eine Pluralform hinterlegt ist
	 * 
	 * @param count
	 * @param singular
	 * @param plural
	 * @return
	 */
	public static String plural(int count, String singular, String plural) {
		if (count == 1) {
			return "1 " + singular;
		} else {
			return "" + count + " " + plural;
		}
	}
	
	// {{{ HexaDezimal Format
	
	public static String hexString(byte[] data) {
		return hexString(data, false);
	}
	
	public static String hexString(byte[] data, boolean space) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			buffer.append(byteToHex(data[i]));
			if (space) {
				buffer.append(" ");
			}
		}
		return buffer.toString();
	}

	/**
	 * byteToHex((byte)0x01): 01
	 * byteToHex((byte)0xff): ff
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToHex(byte b) {
		final char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
		return new String(array);

	}
	
	// }}}
	
	/**
	 * Versch�nert Strings f�r Menschen
	 * 
	 * Bsp: "Harry_Potter_-_und_der_Order_des_Phoenix" -> "Harry Potter - und der Order des Phoenix"
	 * 
	 * @test jlib.test.junit.TextFormatTest#testBeautify 
	 * @param str	Eingabe
	 * @return		Versch�nerter String, besser f�r Menschen lesbar
	 */
	public static String beautify(String str) {
		if (str.indexOf(' ') == -1) {
			return str.replace('_', ' ');
		} else {
			return str;
		}
	}
	
}
