package net.bsnx.jlib.text;

/**
 * Paar aus Word und Anzahl
 * 
 * @author Bjoern R. Salgert
 * @date 09.06.2010
 */
public class WordEntry implements Comparable<WordEntry>	 {

	private String word;
	private int count;
	
	public String getWord() {
		return word;
	}

	public int getCount() {
		return count;
	}

	public WordEntry(String word, int count) {
		this.word = word;
		this.count = count;
	}

	@Override
	public int compareTo(WordEntry o) {
		return new Integer(count).compareTo(o.count);
	}

	@Override
	public String toString() {
		return word + ": " + count;
	}
	
	
	
}