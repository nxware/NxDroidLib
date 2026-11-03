package net.bsnx.jlib.xml;

/**
 * Objekte die in ihrer XML-Darstellung durch das Feld id="..." identifiziert werden koennen
 * 
 * @link http://de.selfhtml.org/html/referenz/attribute.htm#id_idref_name
 * 
 * @author Bjoern R. Salgert
 * @date 28.05.2011
 */
public interface IDomIDed {

	/**
	 * R�ckgabewert muss mit einem Buchstaben A-Za-z beginnen,
	 * danach A-Z, a-z, 0-9, -, _, :, . beliebig oft
	 * 
	 * @return
	 */
	public String getDomId();
	
}
