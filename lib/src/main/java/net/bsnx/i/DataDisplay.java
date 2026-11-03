package net.bsnx.i;

public interface DataDisplay<T> {

	void setDataContext(T data);
	
	T getDataContext();
	
}
