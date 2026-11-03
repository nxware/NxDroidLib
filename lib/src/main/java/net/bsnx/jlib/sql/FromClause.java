package net.bsnx.jlib.sql;

/**
 * @link http://sourceforge.net/apps/trac/jooq/wiki/Manual/JOOQ/Query Ein aehnliches Projekt
 * 
 * @author Bjoern R. Salgert
 * @date 19.04.2011
 */
public class FromClause {

	StringBuilder sql = new StringBuilder();
	
	public FromClause(String sql) {
		this.sql.append("FROM ");
		this.sql.append(sql);
	}
	
	public FromClause from(String table) {
		sql.append(", ");
		sql.append(table);
		return this;
	}
	
	public FromClause join(String table) {
		sql.append(" JOIN ");
		sql.append(table);
		return this;
	}
	
	public FromClause joinNatural(String table) {
		sql.append(" NATURAL JOIN ");
		sql.append(table);
		return this;
	}
	
	public FromClause joinLeft(String table) {
		sql.append(" LEFT JOIN ");
		sql.append(table);
		return this;
	}
	
	public FromClause on(String a, String b) {
		sql.append(" ON ");
		sql.append(a);
		sql.append(" = ");
		sql.append(b);
		return this;
	}
	
	public WhereClause where() {
		return new WhereClause(sql.toString());
	}
	
	public SelectClause select() {
		return new SelectClause(sql.toString());
	}
	
}
