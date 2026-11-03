package net.bsnx.data;

/**
 * Implementierungen:
 * 		ResultSetCursor
 * 		RowCursor
 * 		XmlResultSetCursor
 * 
 * @author root
 *
 */
public interface ICursor {

	public boolean moveToNext();
	
	public void close();
	
	public int getInt(int columnIndex);
	
	public int getInt(String columnName);
	
	public double getDouble(int columnIndex);
	
	/**
	 * Index faengt bei 0 an
	 * 
	 * @param columnIndex
	 * @return
	 */
	public String getString(int columnIndex);
	
	public String getString(String columnName);
	
	public int getColCount();
	
	public String getColumnName(int index);
	
}
