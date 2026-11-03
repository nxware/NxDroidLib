package net.bsnx.jlib.text;

import net.bsnx.jlib.collections.StringList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * Zeichenklasse	Enthaelt
 * .				jedes Zeichen
 * \d				Ziffer: [0-9]
 * \D				keine Ziffer: [^0-9] beziehungsweise [^\d]
 * \s				Weiraum: [ \t\n\x0B\f\r]
 * \S				kein Weiraum: [^\s]
 * \w				Wortzeichen: [a-zA-Z_0-9]
 * \W				kein Wortzeichen: [^\w]
 * \p{Blank}		Leerzeichen oder Tab: [ \t]
 * \p{Lower}, \p{Upper}	Klein-/Grobuchstabe: [a-z] beziehungsweise [A-Z]
 * \p{Alpha}		Buchstabe: [\p{Lower}\p{Upper}]
 * \p{Alnum}		alphanumerisches Zeichen: [\p{Alpha}\p{Digit}]
 * \p{Punct}		Punkt-Zeichen: !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
 * \p{Graph}		sichtbares Zeichen: [\p{Alnum}\p{Punct}]
 * \p{Print}		druckbares Zeichen: [\p{Graph}]
 * @author root
 *
 */
public class Text {
	
	public static void main(String[] args) {
		Text t = new Text("Testing123Tes83a3461802094821adaf70b945864435 t233ing");
		t.extractGUIDs().println();
	}

	private String s;

	public Text(String s) {
		this.s = s;
	}
	
	public StringList extractGUIDs() {
		StringList res = new StringList();
		Pattern p = Pattern.compile("([0-9A-F]{32})|([0-9a-f]{32})");
		Matcher m = p.matcher(s);
		while(m.find()) {
            res.add(m.group());
        }
		return res;
	}
	
	public StringList extractIntegers() {
		return extractRegex("[0-9]+"); // TODO parseInt
	}
	
	public StringList extractRegex(String regex) {
		StringList res = new StringList();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		while(m.find()) {
            res.add(m.group());
        }
		return res;
	}
	
	// TODO countWords()
	
}
