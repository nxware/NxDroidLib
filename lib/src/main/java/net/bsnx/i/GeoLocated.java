package net.bsnx.i;

public interface GeoLocated {
	/**
	 * Latitude: Bezeichnung fuer Geographische Breite
	 * 
	 * Y-Achse
	 * 
	 * @return
	 */
	double getLat();
	/**
	 * Longitude: Bezeichnung fuer Geographische Laenge
	 * 
	 * X-Achse
	 * 
	 * @return
	 */
	double getLng();
}
