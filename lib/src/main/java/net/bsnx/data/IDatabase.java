package net.bsnx.data;

import net.bsnx.jlib.collections.StringList;

import java.util.Map;

/**
 * Abstrakte Datenbankschnittstelle
 * 
 * @project JLib
 * @author Bjoern R. Salgert
 * @date 16.06.2011
 */
public interface IDatabase {

	/**
	 * SQL INSERT INTO
	 * 
	 * @param table	Tabelle in die ein Datensatz eingefuegt werden soll
	 * @param data	Ein Datensatz
	 * @return
	 */
	public int insert(String table, Map<String, Object> data);
	
	/**
	 * SQL UPDATE ... SET 
	 * 
	 * @param table
	 * @param data	Datensatz der die Paare enthaelt die geupdatet werden sollen
	 * @param where	Where Klausel (z.B. "id=42")
	 * @return
	 */
	public int update(String table, Map<String, Object> data, String where);
	
	/**
	 * Waehlt den ersten Wert der ersten Zeile der Ergebnismenge aus
	 * 
	 * Siehe auch die php/nweb Funktion select_scalar
	 * 
	 * @param sql
	 * @return
	 */
	public int selectInt(String sql);
	
	/**
	 * Gibt die erste Spalte der Ergebnismenge aus String-Iterable zurueck.
	 * 
	 * @param sql
	 * @return
	 */
	public StringList selectStringList(String sql);
	
	/**
	 * SQL SELECT
	 * 
	 * @param sql
	 * @return
	 */
	public ICursor selectCursor(String sql);
	
	/**
	 * Escaped einen String und umrahmt ihn mit Hochkommata ('...')
	 * 
	 * @param val
	 * @return
	 */
	public String escape(String val);

	public void exec(String sql);
	
}
