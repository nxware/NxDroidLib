package net.bsnx.data;

import net.bsnx.jlib.NetUtils;
import net.bsnx.jlib.collections.StringList;
import net.bsnx.jlib.xml.NElement;

import java.util.Map;

/**
 * SQL-Datenbank via HTTP
 * 
 * @see net.bsnx.jdbc.webdb
 * @author Bjoern R. Salgert
 * @date 10.08.2011
 */
public class WebDbDatabase implements IDatabase {

	public WebDbDatabase(String url) {
		this.url = url;
	}
	
	private String url;

	private XmlResultSetCurosr webSelect(String sql) {
		String res = NetUtils.wget(url+"?mode=select&sql="+NetUtils.urlencode(sql));
		System.out.println(res);
		return new XmlResultSetCurosr(new NElement(res));
	}
	
	@Override
	public int insert(String table, Map<String, Object> data) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(String table, Map<String, Object> data, String where) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int selectInt(String sql) {
		ICursor c = selectCursor(sql);
		if (c.moveToNext()) {
			return c.getInt(0);
		} else {
			return 0;
		}
	}

	@Override
	public StringList selectStringList(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICursor selectCursor(String sql) {
		return webSelect(sql);
	}

	@Override
	public String escape(String val) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exec(String sql) {
		webSelect(sql);
	}
	
}
