package net.bsnx.jlib.media;

public interface IPlaylist<T> {

	/**
	 * Liefert das Lied das gerade gespielt wird
	 * @return
	 */
	public abstract T current();

	public abstract T next();

	public abstract T previous();

}