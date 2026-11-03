package net.bsnx.jlib.sql;

public class ColumnDefinition {

	private String name;
	
	private String datatype;

	public ColumnDefinition(String name) {
		this.name = name;
	}
	
	public ColumnDefinition(String name, String datatype) {
		this(name);
		if (datatype.equals("String")) {
			datatype = "VARCHAR(255)";
		}
		this.datatype = datatype;
	}
	
	@Override
	public String toString() {
		return name + " " + datatype;
	}
	
}
