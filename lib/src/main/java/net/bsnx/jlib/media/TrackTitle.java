package net.bsnx.jlib.media;

import net.bsnx.i.Titled;
import net.bsnx.jlib.text.TextUtils;

/**
 * Repraesentiert den Titel eines Tracks,
 * ein TrackTitel kann ich einer Disk stecken muss es aber nicht
 * 
 * @author Bjoern R. Salgert
 * @date 14.09.2011
 */
public class TrackTitle implements Titled {

	private String path;
	
	/**
	 * 
	 * @param relativ_path	Relativer Pfad z.B. "/Disk 5/Track 17.mp3"
	 */
	public TrackTitle(String relativ_path) {
		if (relativ_path == null) {
			throw new NullPointerException("relativ_path darf nicht null sein");
		} else if (relativ_path.charAt(0)=='/') {
			path = relativ_path.substring(1);
		} else {
			path = relativ_path;
		}
		int ext_index = path.lastIndexOf('.');
		if (ext_index != -1) {
			path = path.substring(0, ext_index);
		}
	}
	
	/**
	 * Gibt an ob der Track auf einer Disk ist
	 * 
	 * @return
	 */
	public boolean hasDisk() {
		return path.indexOf('/') != -1;
	}
	
	/**
	 * Liefert den Namen der Disk zur�ck auf der sich der Track befindet
	 * z.B. "Disk 1", "CD 05"
	 * 
	 * ( hasDisk() muss vorher true zur�ckgeliefert haben um diese Methode verwenden zu k�nnen )
	 * @return
	 */
	public String getDiskTitle() {
		return path.substring(0, path.indexOf('/'));
		
	}
	
	/**
	 * Liefert die Nummer der CD zur�ck, oder 0 wenn keine Disk vorhanden ist
	 * 
	 * @return
	 */
	public int getDiskNr() {
		if (hasDisk()) {
			return Integer.parseInt(TextUtils.removeNonNumberChars(getDiskTitle()));
		} else {
			return 0;
		}
		
	}

	/**
	 * Liefert den Titel des Tracks zur�ck.
	 * 
	 * z.B. "Track 7"
	 */
	public String getTitle() {
		int i = path.indexOf('/');
		if (i== -1) {
			return path;
		} else {
			return path.substring(i+1);
		}
	}
	
	@Override
	public String toString() {
		return (hasDisk()?getDiskTitle()+" - ":"") + getTitle();
	}
	
}