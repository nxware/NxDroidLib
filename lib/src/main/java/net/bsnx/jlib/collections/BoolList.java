package net.bsnx.jlib.collections;

public class BoolList extends Lst<Boolean> {

	private static final long serialVersionUID = 1L;

	public BoolList(int size) {
		for (int i = 0; i < size; i++) {
			add(false);
		}
	}
	
	public boolean is(int i) {
		return (boolean) get(i);
	}
	
}
