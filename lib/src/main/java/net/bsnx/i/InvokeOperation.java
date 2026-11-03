package net.bsnx.i;

public interface InvokeOperation<T> {

	void invoke(T instance, Object...args);
	
}
