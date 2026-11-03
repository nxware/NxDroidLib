package net.bsnx.data;

import net.bsnx.Java;
import net.bsnx.data.reflection.IDataObjectReflection;
import net.bsnx.data.reflection.IProperty;
import net.bsnx.i.CopyAble;
import net.bsnx.i.IConfig;
import net.bsnx.jlib.collections.Lst;
import net.bsnx.jlib.collections.StringList;
import net.bsnx.jlib.text.TextUtils;
import net.bsnx.jlib.xml.NElement;
import net.bsnx.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Row implements CopyAble<Row>, Cloneable, IBasicRow {
	
	// TODO KV  
	// TODO alias

	private StringList keys = new StringList();
	private Map<String, Object> map = new HashMap<String, Object>();
	
	public Row() {}
	
	public Row(String[] args) {
		for (String arg : args) {
			String a = TextUtils.trimLeft(arg, '-');
			if (a.indexOf('=') != -1) {
				String[] array = TextUtils.splitInTwo(a, '=');
				keys.add(array[0]);
				map.put(array[0], array[1]);
			} else {
				keys.add(a);
				map.put(a, true);
			}
		}
	}
	
	public Row add(String key, Object value) {
		keys.add(key);
		map.put(key, value);
		return this;
	}
	
	public int getInt(int columnIndex) {
		return (Integer) map.get(keys.get(columnIndex));
	}
	
	public int getInt(String columnName) {
		Object obj =map.get(columnName);
		return obj==null? null : (Integer)obj; // TODO cast from String
	}
	
	public double getDouble(int columnIndex) {
		return (Double) map.get(keys.get(columnIndex));
	}
	
	/**
	 * Index faengt bei 0 an
	 * 
	 * @param columnIndex
	 * @return
	 */
	public String getString(int columnIndex) {
			return map.get(keys.get(columnIndex)).toString();
	}
	
	public String getString(String columnName) {
		Object obj =map.get(columnName);
		return obj==null? null : obj.toString();
	}

	public int getColCount() {
		return keys.size();
	}

	/**
	 * Gibt die Row als Tab getrennte Werte aus
	 * 
	 * @return
	 */
	public String toLine() {
		StringBuilder sb = new StringBuilder();
		for (String k : keys) {
			sb.append(getString(k));
			sb.append('\t');
		}
		return sb.toString();
	}
	
	public String toAttrStr() {
		StringBuilder sb = new StringBuilder();
		for (String k : keys) {
			sb.append(" " + k + "=\"");
			sb.append(getString(k));
			sb.append("\"");
		}
		return sb.toString();
	}
	
	public String toAttrStr(String escape) {
		StringBuilder sb = new StringBuilder();
		for (String k : keys) {
			sb.append(" " + k + "=" + escape);
			sb.append(getString(k));
			sb.append(escape);
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String k : keys) {
			sb.append(k);
			sb.append(": ");
			sb.append(getString(k));
			sb.append(' ');
		}
		return sb.toString();
	}
	
	public String getColumnName(int index) {
		int i = 0;
		for (String k : keys) {
			if (i == index) {
				return k;
			}
			i++;
		}
		return "unknown";
	}
	
	public String toUmlText() {
		StringBuilder sb = new StringBuilder();
		for (String k : keys) {
			sb.append(k);
			sb.append(" = \"");
			sb.append(getString(k));
			sb.append("\"\n");
		}
		return sb.toString();
	}

	public String toTr() {
		StringBuilder sb = new StringBuilder();
		sb.append("<tr>");
		for (String k : keys) {
			sb.append("<td>");
			sb.append(getString(k));
			sb.append("</td>");
		}
		sb.append("</tr>");
		return sb.toString();
	}

	public StringList keys() {
		return keys;
	}
	
	public NElement toXml() {
		NElement res = new NElement("<row />");
		for (String k : keys) {
			res.add(k, getString(k));
		}
		return res;
	}
	
	public JSONObject toJson() {
		JSONObject res = new JSONObject();
		for (String k : keys) {
			res.put(k, getString(k));
		}
		return res;
	}
	
	public Properties toProperties() {
		Properties res = new Properties();
		for (String k : keys) {
			res.put(k, getString(k));
		}
		return res;
	}
	
	public IConfig toConfig() {
		return new IConfig() {
			@Override public String getString(String name, String defaultValue) {
				if (containsKey(name)) {
					return Row.this.getString(name);
				} else {
					return defaultValue;
				}
			}
			@Override public boolean getBool(String name, boolean defaultValue) {
				if (containsKey(name)) {
					return Java.toBool(Row.this.getString(name));
				} else {
					return defaultValue;
				}
			}
		};
	}
	
	public IDataObjectReflection toDataObjectReflection() {
		return new IDataObjectReflection() {
			@Override public String getTypeName() {
				return "Row";
			}
			@Override public List<IProperty> getProperties() {
				List<IProperty> res = new Lst<IProperty>();
				for (String k : keys()) {
					res.add(new StringProperty(k, getString(k)));
				}
				return res;
			}
		};
	}
	
	public boolean containsKey(String key) {
		return keys.contains(key);
	}

	public Row removeCol(String name) {
		map.remove(name);
		keys.remove(name);
		return this;
	}
	
	public void insertInto(String tableName, IDatabase db) {
		db.insert(tableName, map);	
	}

	public String toCreateTableSql(String name) {
		StringList lst = new StringList();
		for (String s : keys) {
			String v = getString(s);
			lst.add(s + " " + (v.length()>100?"TEXT":"VARCHAR("+(v.length()+40)+")"));
		}
		return "CREATE TABLE " + name + "("+lst.implode(",\n")+")";
	}
	
	public String toSqlInsert(String table) {
		StringBuilder sb = new StringBuilder("INSERT INTO ");
		sb.append(table);
		StringList sl = new StringList(keys);
		sl = sl.sort();
		sb.append("(");
		sb.append(sl.implode(", "));
		sb.append(") VALUES (");
		StringList s2 = new StringList();
		for (String s : sl) {
			s2.add(TextUtils.toSqlLiteral(getString(s)));
		}
		sb.append(s2.implode(", "));
		sb.append(")");
		return sb.toString();
	}

	public void replaceInField(String fieldName, String oldStr, String newStr) {
		String s = map.get(fieldName).toString().replace(oldStr, newStr);
		map.remove(fieldName);
		map.put(fieldName, s);
	}

	@Override
	public Row copy() {
		Row row = new Row();
		for (String k : keys) {
			row.add(k, getObject(k));
		} 
		return row;
	}

	public Object getObject(String field) {
		return map.get(field);
	}
	
}
