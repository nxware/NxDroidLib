package net.bsnx.jlib.text.baselang;

import net.bsnx.jlib.collections.Lst;

import java.util.ArrayList;
import java.util.List;

public class Operation {

	private String name;
	private List<Object> params = new ArrayList<Object>();
	
	public Operation(String name) {
		this.name = name;
	}
	
	public void addParameter(Object val) {
		params.add(val);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name + "(");
		sb.append(new Lst<Object>(params).toString());
		sb.append(")");
		return sb.toString();
	}
	
	public int getInt(int pos) {
		return Integer.parseInt(params.get(pos).toString());
	}
	
	public String getString(int pos) {
		return params.get(pos).toString();
	}
	
	public String str(int pos) {
		return getString(pos);
	}
	
	public boolean is(String name) {
		return this.name.equals(name);
	}
	
}
