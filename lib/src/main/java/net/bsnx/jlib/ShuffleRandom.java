package net.bsnx.jlib;

import java.util.Random;

/**
 * Random Klasse fuer eine Shuffle Funktion
 * 
 * @author Bjoern R. Salgert
 * @date 09.09.2011
 */
public class ShuffleRandom {

	private Random random = new Random(); 
	
	public void setMax(int max) {
		this.max = max;
	}
	
	/**
	 * Queue der letzten Random Zahlen,
	 * je kleiner der Index destso neuer Die Zufallszahl
	 */
	private int[] queue = new int[7];
	
	private int max;
	
	/**
	 * Liefert eine neue Zufallszahl, wenn das Maximum gross genug wird daruaf gemachtet,
	 * das die generierte Zufallszahl noch gerade erst gezogen wurde.
	 * 
	 * @return	Zufallszahl im Interval [0;max) (Die Zahl max selbst wird also nicht zurueckgegeben)
	 */
	public int rand() {
		int res = random.nextInt(max);
		if (max > 21) {
			while (isRecent(res)) {
				res = random.nextInt(max);
			}
		}
		push(res);
		return res;
	}
	
	private void push(int newst_random) {
		for (int i = queue.length-2; i >= 0; i--) {
			queue[i+1] = queue[i];
		}
		queue[0] = newst_random;
	}
	
	private boolean isRecent(int random) {
		for (int i : queue) {
			if (i == random) return true;
		}
		return false;
	}
	
}
