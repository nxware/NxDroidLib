package net.bsnx.jlib.sql;

import net.bsnx.i.Function;
import net.bsnx.jlib.collections.Collections;

import java.util.ArrayList;
import java.util.List;

/**
 * Erstellt den Code fuer ein CREATE-TABLE-Statement
 * 
 * @author Bjoern R. Salgert
 * @date 26.05.2011
 */
public class TableDefinition {

	private String name;
	
	private List<ColumnDefinition> cols = new ArrayList<ColumnDefinition>();
	
	public TableDefinition(String name) {
		this.name = name;
	}
	
	public TableDefinition(String name, ColumnDefinition...cols) {
		this(name);
		for (ColumnDefinition col : cols) {
			this.cols.add(col);
		}
	}
	
	public TableDefinition add(ColumnDefinition col) {
		cols.add(col);
		return this;
	}
	
	public TableDefinition col(String name, String datatype) {
		cols.add(new ColumnDefinition(name, datatype));
		return this;
	}
	
	@Override
	public String toString() {
		String colstr = Collections.implode(Collections.mapS(cols, new Function<String, String>() {
			public String execute(String item) {
				return "  " + item;
			}
		}), ",\n");
		return "CREATE TABLE "+name+" (\n" + colstr + "\n)";
	}
	
}
