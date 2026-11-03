package net.bsnx.i;

/**
 * 
 * @author Bjoern R. Salgert
 *
 * @param <A>	Eingabe
 * @param <B>	Ausgabe
 */
public interface Function<A, B> {
	
	public B execute(A item);

}
