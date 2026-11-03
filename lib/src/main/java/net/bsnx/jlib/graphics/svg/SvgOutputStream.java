package net.bsnx.jlib.graphics.svg;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.MessageFormat;

/**
 * 
 * @author Bjoern R. Salgert
 * @date 08.05.2010
 */
public class SvgOutputStream {

	private PrintStream out;
	
	/**
	 * 
	 * @param output
	 * @param profile	"tiny", "basic", "full"
	 * @param width
	 * @param height
	 * @param viewboxX1
	 * @param viewboxY1
	 * @param viewboxX2
	 * @param viewboxY2
	 */
	public SvgOutputStream(OutputStream output, String profile, String width, String height, Integer viewboxX1, Integer viewboxY1, Integer viewboxX2, Integer viewboxY2) {
		this.out = new PrintStream(output);
		this.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		this.out.println("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");
		this.out.println("<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:ev=\"http://www.w3.org/2001/xml-events\" version=\"1.1\"");
		
		if (profile == null) {
			profile = "full";
		}
		this.out.print(" baseProfile=\""+profile+"\"");
		
		if (width != null) {
			this.out.print(" width=\""+width+"\"");
		}
		if (height != null) {
			this.out.print(" height=\""+height+"\"");
		}
		
		if (viewboxX1 != null) {
			this.out.print(MessageFormat.format(
				"  viewBox=\"{0,number,integer} {1,number,integer} {2,number,integer} {3,number,integer}\"",
				new Object[] {viewboxX1, viewboxY1, viewboxX2, viewboxY2}
			));
		}
		
		this.out.println(">");

	}
	
	public void line(int x1, int y1, int x2, int y2) {
		out.println(MessageFormat.format(
			"  <line x1=\"{0,number,integer}\" y1=\"{1,number,integer}\" x2=\"{2,number,integer}\" y2=\"{3,number,integer}\" stroke=\"black\" />",
			new Object[] {x1, y1, x2, y2}
		));		
	}
	
	public void close() {
		out.print("</svg>");
	}
	
}