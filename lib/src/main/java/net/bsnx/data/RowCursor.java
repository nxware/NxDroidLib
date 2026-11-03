package net.bsnx.data;

import net.bsnx.i.Getter;
import net.bsnx.jlib.collections.Lst;
import net.bsnx.jlib.collections.MapList;
import net.bsnx.jlib.collections.NMap;
import net.bsnx.jlib.collections.StringList;
import net.bsnx.jlib.text.TextUtils;

import java.io.PrintStream;
import java.util.List;

public class RowCursor extends Lst<Row> implements ICursor {

	private static final long serialVersionUID = 1L;

	private int i = -1;
	
	public RowCursor() {}
	
	public RowCursor(ICursor cur) {
		int colCount = cur.getColCount();
		while (cur.moveToNext()) {
			Row row = new Row();
			for (int i = 0; i < colCount; i++) {
				row.add(cur.getColumnName(i), cur.getString(i));
			}
			add(row);
		}
	}
		
	@Override
	public boolean moveToNext() {
		i++;
		return i < size();
	}

	@Override
	public void close() {}

	@Override
	public int getInt(int columnIndex) {
		return get(i).getInt(columnIndex);
	}
	
	@Override
	public int getInt(String columnName) {
		return get(i).getInt(columnName);
	}

	@Override
	public double getDouble(int columnIndex) {
		return get(i).getDouble(columnIndex);
	}

	@Override
	public String getString(int columnIndex) {
		return get(i).getString(columnIndex);
	}

	@Override
	public String getString(String columnName) {
		return get(i).getString(columnName);
	}

	@Override
	public int getColCount() {
		return get(i).getColCount();
	}
	
	public void printTo(PrintStream out) {
		for (Row r : this) {
			out.println(r.toLine());
		}
	}
	
	public void print() {
		printTo(System.out);
	}

	@Override
	public String getColumnName(int index) {
		return this.get(0).getColumnName(index);
	}
	
	static class Int {
		int val;
		int getValue() {
			return val;
		}
		void inc() { val++; }
		void setIfMax(int v) {
			val = Math.max(val, v);
		}
	}
	
	public String toPlainTextTable(String linePrefix) {
		if (isEmpty()) {
			return "";
		} else {
			StringBuilder sb = new StringBuilder();
			NMap<String, Int> map = new NMap<String, Int>();
			for (String name : get(0).keys()) {
				map.put(name, new Int());
				map.get(name).setIfMax(name.length());
			}
			for (Row row : this) {
				for (String name : row.keys()) {
					try {
						map.get(name).setIfMax(row.getString(name).length());
					} catch (Exception e) {}
				}
			}
			sb.append(linePrefix);
			for (String name : get(0).keys()) {
				sb.append(TextUtils.fillToLength(name, ' ', map.get(name).getValue()));
				sb.append('|');
			}
			sb.append("\n");
			for (Row row : this) {
				sb.append(linePrefix);
				for (String name : row.keys()) {
					String v = row.getString(name);
					sb.append(TextUtils.fillToLength(v==null?"":v, ' ', map.get(name).getValue()));
					sb.append('|');
				}
				sb.append("\n");
			}
			return sb.toString();
		}
	}

	public void println() {
		for (Row row : this) {
			System.out.println(row);
		}
	}
	
	public RowCursor qEqualsValue(String field, String value) {
		RowCursor res = new RowCursor();
		for (Row row : this) {
			if (row.containsKey(field) && row.getString(field).equals(value)) {
				res.add(row);
			}
		}
		return res;
	}
	
	public RowCursor qNotEqualsValue(String field, String value) {
		RowCursor res = new RowCursor();
		for (Row row : this) {
			if (row.containsKey(field) && !row.getString(field).equals(value)) {
				res.add(row);
			}
		}
		return res;
	}
	
	public RowCursor qNotEmpty(String field) {
		RowCursor res = new RowCursor();
		for (Row row : this) {
			if (row.containsKey(field) && !"".equals(row.getString(field).trim())) {
				res.add(row);
			}
		}
		return res;
	}
	
	public RowCursor qLike(String field, String pattern) {
		RowCursor res = new RowCursor();
		for (Row row : this) {
			if (row.containsKey(field) && TextUtils.like(row.getString(field), pattern)) {
				res.add(row);
			}
		}
		return res;
	}
	
	public RowCursor qNotLike(String field, String pattern) {
		RowCursor res = new RowCursor();
		for (Row row : this) {
			if (row.containsKey(field) && !TextUtils.like(row.getString(field), pattern)) {
				res.add(row);
			}
		}
		return res;
	}

	public RowCursor groupByCount(String field, String countName) {
		MapList<String, Row> res = new MapList<String, Row>();
		for (Row row : this) {
			res.getList(row.getString(field)).add(row);
		}
		RowCursor rows = new RowCursor();
		for (String val : res.keyList()) {
			Row row = new Row();
			row.add(field, val);
			row.add(countName, res.get(val).size());
			rows.add(row);
		}
		return rows;
	}
	
	public RowCursor addConst(String name, String value) {
		for (Row row : this) {
			row.add(name, value);
		}
		return this;
	}
	
	public RowCursor rename(String oldName, String newName) {
		if (oldName.equals(newName)) { 
			return this;
		}
		for (Row row : this) {
			String value = row.getString(oldName);
			row.removeCol(oldName);
			row.add(newName, value);
		}
		return this;
	}
	
	/**
	 * Benennt alle Spalten des Cursors um, damit ein Einfuegen in eine Datenbank moeglich ist.
	 * 
	 * @return
	 */
	public RowCursor renameForDb() {
		for (Row row : this) {
			for (String col : row.keys().copy()) {
				rename(col, DbUtils.prepareIdentifer(col));
			}
		}
		return this;
	}

	public int aggregateSumScalar(String name) {
		int res = 0;
		for (Row row : this) {
			res += row.getInt(name);
		}
		return res;
	}
	
	public int aggregateMaxLengthScalar(String name) {
		int res = 0;
		for (Row row : this) {
			res += Math.max(res, row.getString(name).length());
		}
		return res;
	}
	
	public RowCursor insertInto(String tableName, IDatabase db) {
		for (Row row : this) {
			try {
				row.insertInto(tableName, db);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO errors z�hlen
			}
		}
		return this;
	}

	// TODO Option fuer Datentypen
	public RowCursor createTable(String name, IDatabase db) {
		try {
			for (Row row : this) {
				db.exec(row.toCreateTableSql(name));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public StringList toInsertSql(String table) {
		StringList sl = new StringList();
		for (Row row : this) {
			sl.add(row.toSqlInsert(table));
		}
		return sl;
	}

	@Override
	public Row first() {
		return size() == 0 ? new Row() : get(0);
	}
	
	public RowGroup groupBy(String attrName) {
		RowGroup cat = new RowGroup(this, new RowGetter(attrName), null);
		return cat;
	}

	public Lst<String> select(final String fieldName) {
		Lst<String> res = new Lst<String>();
		for (Row row : this) {
			res.add(row.getString(fieldName));
		}
		return res;
	}
	
	public RowCursor replace(String fieldName, String oldStr, String newStr) {
		for (Row row : this) {
			row.replaceInField(fieldName, oldStr, newStr);
		}
		return this;
	}
	
	public RowCursor joinEq(RowCursor other, String thisField, String otherField) {
		RowCursor res = new RowCursor();
		for (Row thisRow : this) {
			for (Row otherRow : other) {
				if (thisRow.getString(thisField).equals(otherRow.getString(otherField))) {
					Row row = thisRow.copy();
					for (String k : otherRow.keys()) {
						row.add(k, otherRow.getString(k));
					}
					res.add(row);
				}
			}
		}
		return res;
	}
	
	public RowCursor joinNatural(RowCursor other, String field) {
		return joinEq(other, field, field);
	}

	public Row addRow() {
		Row row = new Row();
		add(row);
		return row;
	}
	
	public <T,V> RowCursor extractProperty(String field, Getter<T, V> getter, String newField) {
		RowCursor res = new RowCursor();
		for (Row r : this) {
			@SuppressWarnings("unchecked")
			V val = getter.getValue((T)r.getObject(field));
			res.add(r.copy().add(newField, val));
		}
		return res;
	}
	
	public RowCursor removeCol(String field) {
		RowCursor res = new RowCursor();
		for (Row r : this) {
			res.add(r.copy().removeCol(field));
		}
		return res;
	}
	
	public RowCursor joinListProperty(String field, String toField) {
		RowCursor res = new RowCursor();
		for (Row r : this) {
			List<?> list = (List<?>)r.getObject(field);
			for	(Object to : list) {
				Row newRow = r.copy();
				newRow.add(toField, to);
				res.add(newRow);
			}
		}
		return res;
	}
	
}
