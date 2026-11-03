package net.bsnx.data;

import net.bsnx.jlib.collections.Lst;
import net.bsnx.jlib.xml.XML;

import java.util.Map;
import java.util.TreeMap;

/**
 * Datenbank spzifische Hilfsfunktionen
 * 
 * @author Bjoern R. Salgert
 * @date 23.05.2011
 */
public class DbUtils {

	public static Map<String, Object> singleValue(String name, Object value) {
		Map<String, Object> data = new TreeMap<String, Object>(); 
		data.put(name, value);
		return data;
	}
	
	// {{{ insertValue Ueberladungen
	
	public static Map<String, Object> insertValue(String name, Object value) {
		Map<String, Object> data = new TreeMap<String, Object>(); 
		data.put(name, value);
		return data;
	}
	
	public static Map<String, Object> insertValue(String name1, Object value1, String name2, Object value2) {
		Map<String, Object> data = new TreeMap<String, Object>(); 
		data.put(name1, value1);
		data.put(name2, value2);
		return data;
	}
	
	public static Map<String, Object> insertValue(String name1, Object value1, String name2, Object value2, String name3, Object value3) {
		Map<String, Object> data = new TreeMap<String, Object>(); 
		data.put(name1, value1);
		data.put(name2, value2);
		data.put(name3, value3);
		return data;
	}
	
	// }}}
	
	/**
	 * ResultSet f�r WebDb
	 * 
	 * @see net.bsnx.jdbc.webdb
	 */
	public static String toXmlStr(ICursor c) {
		StringBuilder sb = new StringBuilder();
		//sb.append(XML.DECLARATION);
		int colCount = c.getColCount();
		sb.append("<resultset>\n");
		while (c.moveToNext()) {
			sb.append(" <row>\n");
			for(int i = 0; i < colCount; i++) {
				sb.append("  <item>\n");
				sb.append("   <name>col_"+i+"</name>");
				sb.append("   <value>"+XML.escapeValue(c.getString(i))+"</value>");
				sb.append("  </item>\n");
			}
			sb.append(" </row>\n");
		}
		sb.append("</resultset>\n");
		return sb.toString();
	}

	public static String stringValue(String s) {
		return "'"+s.replace("'", "\\'")+"'";
	}

	public static String escapeValue(Object object) {
		if (object == null) {
			return "NULL";
		} else if (object.getClass().isPrimitive()) {
			return object.toString();
		} else {
			return stringValue(object.toString());
		}
	}
	
	public static Lst<Row> toRowList(ICursor cur) {
		if (cur instanceof RowCursor) {
			return (RowCursor)cur;
		} else {
			Lst<Row> res = new Lst<Row>();
			while (cur.moveToNext()) {
				res.add(toRow(cur));
			}
			return res;
		}
	}
	
	public static Row toRow(ICursor cur) {
		Row row = new Row();
		int colCount = cur.getColCount();
		for (int i = 0; i < colCount; i++) {
			row.add(cur.getColumnName(i), cur.getString(i));
		}
		return row;
	}
	
	public static String toHtml(ICursor cur) {
		StringBuilder sb = new StringBuilder();
		Lst<Row> lst = toRowList(cur);
		if (lst.isEmpty()) {
			
		} else {
			sb.append("<table>");
			sb.append("<tr>");
			for (String k : lst.get(0).keys()) {
				sb.append("<th>"+k+"</th>");
			}
			sb.append("<tr>");
			for (Row row : lst) {
				sb.append(row.toTr());
			}
			sb.append("</table>");
		}
		return sb.toString();
	}

	public static String prepareIdentifer(String name) {
		if (!Character.isAlphabetic(name.charAt(0))) {
			name = "c" + name;
		}
		return name.replace(" ", name).replace("\t", name);
	}
	
}
