package net.bsnx.jlib.collections;

import java.util.ArrayList;

public class Queue<T> extends ArrayList<T> {
	
	private static final long serialVersionUID = 8004617875962666384L;

	public void enqueue(T item) {
		add(item);
	}
	
	public T dequeue() {
		T res = get(0);
		remove(0);
		return res;
	}
	
}
