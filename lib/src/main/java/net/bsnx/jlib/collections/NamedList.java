package net.bsnx.jlib.collections;

import net.bsnx.i.InvokeAble;
import net.bsnx.i.InvokeOperation;
import net.bsnx.i.Named;

public class NamedList<T extends Named> extends Lst<T> implements InvokeAble<T> {

	private static final long serialVersionUID = 1L;

	public boolean containsName(String name) {
		for (Named item : this) {
			if (name.equals(item.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsNameIgnoreCase(String name) {
		for (Named item : this) {
			if (name.equalsIgnoreCase(item.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public T byName(String name) {
		for (T item : this) {
			if (name.equals(item.getName())) {
				return item;
			}
		}
		return null;
	}
	
	public T byNameIgnoreCase(String name) {
		for (T item : this) {
			if (name.equalsIgnoreCase(item.getName())) {
				return item;
			}
		}
		return null;
	}
	
	/**
	 * Liefert das erste Element zurueck das mit einem Namen in der Liste Uebereinstimmt.
	 * 
	 * @param name
	 * @return
	 */
	public T findByName(String...name) {
		for (String n : name) {
			if (containsName(n)) {
				return byName(n);
			}
		}
		return null;
	}
	
	public StringList getNames() {
		StringList res = new StringList();
		for (Named named : this) {
			res.add(named.getName());
		}
		return res;
	}

	@Override
	public void invoke(InvokeOperation<T> operation, Object...args) {
		for (T item : this) {
			operation.invoke(item, args);
		}
		
	}
	
}
