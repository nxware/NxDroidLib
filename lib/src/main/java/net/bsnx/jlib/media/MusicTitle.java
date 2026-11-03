package net.bsnx.jlib.media;

import net.bsnx.i.Titled;

public class MusicTitle implements Titled {

	public MusicTitle(String str) {
		String[] array = str.split(" - ");
		title = array[0];
		artist = array[0];
	}
	
	private String title;
	private String artist;

	@Override
	public String getTitle() {
		return title;
	}
	
	public String getArtist() {
		return artist;
	}
	
}
