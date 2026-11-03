package net.bsnx.jlib.sql;

import net.bsnx.jlib.collections.Collections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;


/**
 * Statische Klasse um SQL-Code zu erzeugen
 * 
 * @author Bjoern R. Salgert
 * @date 08.04.2011
 */
public class SQL {

	String str;
	
	private SQL(String str) {
		this.str = str;
	}
	
	public static FromClause from(String name) {
		return new FromClause(name);
	}
	
	public static TableDefinition createTable(String name) {
		return new TableDefinition(name);
	}
	
	public static TableDefinition createTableFromObject(String name, Object obj) {
		TableDefinition res = new TableDefinition(name);
		Class<?> c = obj.getClass();
		
		Field[] fields = c.getDeclaredFields();
		
		for (Field field : fields) {
			res.col(field.getName(), toSqlType(field.getType()));
		}
		
		return res;
	}
	
	public static String toSqlType(Class<?> type) {
		if (type.equals(String.class)) {
			return "VARCHAR(255)";
		} else if (type.isPrimitive()) {
			if (type.equals(int.class)) {
				return "INTEGER";
			} else if (type.equals(boolean.class)) {
				return "INT(1)";
			}
		}
		return "";
	}
	
	// {{{ DML
	
	/**
	 * et.bsnx.jlib.collections.EntrySet fuer data verwenden
	 */
	public static String insert(String table, Set<Entry<String, Object>> data) {
		StringBuilder sb = new StringBuilder("INSERT INTO ");
		sb.append(table);
		List<String> fields = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		for (Entry<String, Object> entry : data) {
			fields.add(entry.getKey());
			Object val = entry.getValue();
			if (val instanceof String) {
				values.add(escape(val.toString()));
			} else {
				values.add(val.toString());
			}
		}
		sb.append(" (");
		sb.append(Collections.implode(fields, ", "));
		sb.append(") VALUES (");
		sb.append(Collections.implode(values, ", "));
		sb.append(" )");
		return sb.toString();
	}
	
	public static String update(String table, Set<Entry<String, Object>> data, String where) {
		StringBuilder sb = new StringBuilder("UPDATE ");
		sb.append(table);
		sb.append(" SET ");
		List<String> values = new ArrayList<String>();
		for (Entry<String, Object> entry : data) {
			Object val = entry.getValue();
			String s;
			if (val instanceof String) {
				s = (escape(val.toString()));
			} else {
				s = (val.toString());
			}
			values.add(entry.getKey() + " = " + s);
		}
		sb.append(Collections.implode(values, ", "));
		if (where != null) {
			sb.append(" WHERE ");
			sb.append(where);
		}
		return sb.toString();
	}
	
	// }}}
	
	public static String escape(String s) {
		return "'" + s.replaceAll("'", "\'\'") + "'";
	}
	
	public static String escape(Object obj) {
		if (obj instanceof Integer) {
			return obj.toString();
		} else {
			return escape(obj.toString());
		}
	}
	
	@Override
	public String toString() {
		return str;
	}
	
}
