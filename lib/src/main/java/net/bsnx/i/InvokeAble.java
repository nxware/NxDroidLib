package net.bsnx.i;

public interface InvokeAble<T> {

	void invoke(InvokeOperation<T> operation, Object...args);
	
}
