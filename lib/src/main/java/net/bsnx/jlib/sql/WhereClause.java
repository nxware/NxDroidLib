package net.bsnx.jlib.sql;

public class WhereClause {

	public  static String EqV(String col, String val) {
		WhereClause where = new WhereClause("");
		where.eqV(col, val);
		return "("+where.sb.toString()+")";
	}
	
	public  static String EqV(String col, int val) {
		WhereClause where = new WhereClause("");
		where.eqV(col, val);
		return "("+where.sb.toString()+")";
	}
	
	private String sql;
	private StringBuilder sb = new StringBuilder();
	
	public WhereClause(String sql) {
		this.sql = sql;
	}
	
	// {{{ =
	
	public WhereClause eq(String a, String b) {
		sb.append(" ");
		sb.append(a);
		sb.append(" = ");
		sb.append(b);
		sb.append(" ");
		return this;
	}
	
	public WhereClause eqV(String a, String b) {
		sb.append(" ");
		sb.append(a);
		sb.append(" = ");
		sb.append(SQL.escape(b));
		sb.append(" ");
		return this;
	}
	
	public WhereClause eqV(String a, int b) {
		sb.append(" ");
		sb.append(a);
		sb.append(" = ");
		sb.append(b);
		sb.append(" ");
		return this;
	}
	
	// }}}
	
	public WhereClause like(String s, String pattern) {
		sb.append(" ");
		sb.append(s);
		sb.append(" LIKE '");
		sb.append(pattern);
		sb.append("' ");
		return this;
	}
	
	public WhereClause conndition(String s) {
		sb.append(" ");
		sb.append(s);
		sb.append(" ");
		return this;
	}
	
	public WhereClause and() {
		sb.append(" AND ");
		return this;
	}
	
	public WhereClause or() {
		sb.append(" OR ");
		return this;
	}
	
	/**
	 * Selektiert mit dem Wildcard (*)
	 * 
	 * @return
	 */
	public SelectClause select() {
		return new SelectClause(sql + " WHERE " +  sb.toString());
	}
	
	public SelectClause select(String...fields) {
		return new SelectClause(sql + " WHERE " +  sb.toString(), fields);
	}
	
	@Override
	public String toString() {
		return sql + " WHERE " + sb.toString();
	}
	
}
