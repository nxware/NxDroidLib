package net.bsnx.data;

public class ValueWrapper<T> {

	protected T val;
	
	public T val() {
		return val;
	}
	
	public void val(T val) {
		this.val = val;
	}

	@Override
	public String toString() {
		return val.toString();
	}
	
	public static void main(String[] z){}
}
