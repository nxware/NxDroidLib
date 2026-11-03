package net.bsnx.jlib.sql;

import net.bsnx.jlib.collections.Collections;

import java.util.ArrayList;
import java.util.List;

public class SelectClause {

	private String sql;
	
	private Integer _limit = null;
	
	/**
	 * 
	 * @param sql SQL-String ohne SELECT (faengt also mit FROM an)
	 */
	public SelectClause(String sql) {
		this.sql = sql;
	}
	
	public SelectClause(String sql, String...fields) {
		this.sql = sql;
		for (String field : fields) {
			field(field);
		}
	}
	
	List<String> fields = new ArrayList<String>();
	
	public SelectClause field(String name) {
		fields.add(name);
		return this;
	}
	
	public SelectClause fields(String...names) {
		for (String name : names) {
			field(name);
		}
		return this;
	}
	
	public SelectClause field(String name, String alias) {
		fields.add(name + " AS " + alias);
		return this;
	}
	
	public SelectClause func(String function, String arg, String alias) {
		fields.add(function + "(" + arg + ") AS " + alias);
		return this;
	}
	
	public SelectClause count(String arg, String alias) {
		fields.add("COUNT(" + arg + ") AS " + alias);
		return this;
	}
	
	public SelectClause count(String arg) {
		fields.add("COUNT(" + arg + ")");
		return this;
	}
	
	public SelectClause count() {
		fields.add("COUNT(*)");
		return this;
	}
	
	public SelectClause limit(int limit) {
		this._limit = limit;
		return this;
	}
	
	// SQL erzeugen
	
	@Override
	public String toString() {
		if (fields.size() == 0) {
			fields.add("*");
		}
		if (_limit != null) {
			sql += " LIMIT " + _limit;
		}
		return "SELECT " + Collections.implode(fields, ", ") + " " + sql;
	}
	
	// Ergebise fetchen
	
	public void one() {}
	
	public void scalar() {}
	
	public void toList() {}
	
}
