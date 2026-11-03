package net.bsnx.data;

import net.bsnx.jlib.xml.NElement;

import java.util.Iterator;

/**
 * Wandelt das Xml einer WebDb Abfrage in einen Cursor
 * 
 * @author Bjoern R. Salgert
 * @date 10.08.2011
 */
public class XmlResultSetCurosr implements ICursor {
	
	NElement root;
	Iterator<NElement> rows;
	NElement current;

	public XmlResultSetCurosr(NElement resultsetElement) {
		root = resultsetElement;
		rows = root.iter("row").iterator();
	}
	
	@Override
	public boolean moveToNext() {
		if (rows.hasNext()) {
			current = rows.next();
			return true;
		} else return false;
	}

	@Override
	public void close() {}

	@Override
	public int getInt(int columnIndex) {
		return Integer.parseInt(current.iter("item").get(columnIndex).val("value"));
	}

	@Override
	public double getDouble(int columnIndex) {
		return Double.parseDouble(current.iter("item").get(columnIndex).val("value"));
	}

	@Override
	public String getString(int columnIndex) {
		return current.iter("item").get(columnIndex).val("value");
	}

	@Override
	public int getColCount() {
		return root.iter("row").get(0).iter("item").size();
	}

	@Override
	public String getString(String columnName) {
		// TODOfind by name
		return null;
	}

	@Override
	public String getColumnName(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getInt(String columnName) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}