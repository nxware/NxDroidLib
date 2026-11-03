package net.bsnx.jlib;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Funktionen fuer den Umgang mit Datumswerten
 * 
 * @php 4.0/classes/NWeb/Date.php
 * 
 * @link http://www.torsten-horn.de/techdocs/java-date.htm
 * @link http://openbook.galileodesign.de/javainsel5/javainsel10_007.htm
 * @link http://commons.apache.org/lang/userguide.html#lang3.time.
 * 
 * @static
 * @author Bjoern R. Salgert
 * @date 30.05.2011
 */
public class DateUtils {
	
	public static void main(String[] args) { date("yyyy"); }
	
	public static Date timeMinus(long sec) {
		return new Date(System.currentTimeMillis() - sec*1000);
	}
	
	/**
	 * a-b
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private static Date sub(Date a, Date b) {
		return new Date(a.getTime() - b.getTime());
	}
	
	public static int minutes(Date d) {
		return (int) d.getTime()/1000/60;
	}
	
	public static int hours(Date d) {
		return (int) d.getTime()/1000/60/60;
	}
	
	public static int days(Date d) {
		return (int) d.getTime()/1000/60/60/24;
	}
	
	public static Date time() {
		return new Date(System.currentTimeMillis());
	}
	
	public static String formatDateSince(Date date) {
		//Date time = new Date(System.currentTimeMillis());
		if (date.after(timeMinus(60))) {
			return "soeben";
		} else if (date.after(timeMinus(2*60))) {
			return "vor einer Minute";
		} else if (date.after(timeMinus(60*60))) {
			Date dif = sub(time(), date);
			return "vor " + minutes(dif) + " Minuten";
		} else if (date.after(timeMinus(2*60*60))) {
			return "vor einer Stunde";
		} else if (date.after(timeMinus(60*60*24))) {
			Date dif = sub(time(), date);
			return "vor " + hours(dif) + " Stunden";
		} else if (date.after(timeMinus(60*60*24*2))) {
			return "gestern";
		} // TODO weiter machen
		return getGermanDate(date);
	}
	
	public static String getGermanDate(Date date) {
		// http://www.eshca.net/java/books/javainsel8/javainsel_13_007.htm#mjbe8ccb98b7b7d2cb4328b7782c125c80
		//DateFormat formatter = new SimpleDateFormat();
		DateFormat formatter = new SimpleDateFormat( "dd.MM.yyyy" ); 
		return formatter.format(date);
	}
	
	/**
	 * Gibt einen Datumswert formatiert aus.
	 * 
	 * @param d
	 * @param notExisting	Wert der Angezeigt wird, wenn das Datum nicht existiert bzw. null ist
	 * @return
	 */
	public static String format(Date d, String notExisting) {
		if (d == null) {
			return notExisting;
		}
		return formatDateSince(d);
	}
	
	/**
	 * �berladung von String format(Date, String)
	 * 
	 * @param d
	 * @return
	 */
	public static String format(Date d) {
		return format(d, "nie");
	}
	
	/**
	 * �berladung von String format(Date, String)
	 * 
	 * @param unix_time_stamp	Unix-Time-Stamp
	 * @return
	 */
	public static String format(int unix_time_stamp) {
		return format(new Date((long)unix_time_stamp), "nie");
	}
	
	/**
	 * Erzeugt ein Date Objekt wenn das Datum nicht gesetzt bzw. -1 ist wird null zur�ck geliefert
	 * 
	 * @param milis
	 * @return
	 */
	public static Date fromLong(long milis) {
		if (milis == -1) {
			return null;
		} else {
			return new Date(milis);
		}
	}
	
	public static Date fromUnixTimestamp(int unixtimestamp) {
		long l = unixtimestamp;
		return fromLong(l * 1000);
	}
	
	public static int currentUnixTimeStamp() {
		return (int) (System.currentTimeMillis() / 1000);
	}
	
	public static int toUnixtimestamp(Date d) {
		return (int) (d.getTime() / 1000);
	}
	
	/**
	 * Hilfsmethode zur Darstellung der �bergebenen Zeit, formatiert in eine
	 * Stunden/Minuten/Sekunden Anzeige
	 * @param _time die gew�nschte umzuformatierende Zeit in Millisekunden
	 * @return die �bergebene Zeit, richtig formatiert in einer
	 *  Stunden/Minuten/Sekunden Darstellung
	 */
	public static String formatPlaytime(int _time) {
	    String format = String.format("%%0%dd", 2);
	    int timeInSec;
	    timeInSec = _time / 1000;
	    String seconds = String.format(format, timeInSec % 60);
	    String minutes = String.format(format, (timeInSec % 3600) / 60);
	    String hours = String.format(format, timeInSec / 3600);
	    if (_time > 3600000) {
	    	return hours + ":" + minutes + ":" + seconds;
	    }
	    else return minutes + ":" + seconds;
	}
	
	/**
	 * PHP date
	 * 
	 * @link http://docs.oracle.com/javase/1.4.2/docs/api/java/text/SimpleDateFormat.html
	 * 
	 * G	Era designator	Text	AD
y	Year	Year	1996; 96
M	Month in year	Month	July; Jul; 07
w	Week in year	Number	27
W	Week in month	Number	2
D	Day in year	Number	189
d	Day in month	Number	10
F	Day of week in month	Number	2
E	Day in week	Text	Tuesday; Tue
a	Am/pm marker	Text	PM
H	Hour in day (0-23)	Number	0
k	Hour in day (1-24)	Number	24
K	Hour in am/pm (0-11)	Number	0
h	Hour in am/pm (1-12)	Number	12
m	Minute in hour	Number	30
s	Second in minute	Number	55
S	Millisecond	Number	978
z	Time zone	General time zone	Pacific Standard Time; PST; GMT-08:00
Z	Time zone	RFC 822 time zone	-0800
	 * 
	 * @param format z.B. "dd.MM.yyyy"
	 * @return
	 */
	public static String date(String format) {
		DateFormat formatter = new SimpleDateFormat(format); 
		return formatter.format(new Date());
	}
	
	public static Date mktime(int hour, int minute, int second, int month, int day, int year) {
		GregorianCalendar cal = new GregorianCalendar(year, month, day, hour, minute, second);
		return cal.getTime();
	}
	
	public static boolean isBetween(Date d, Date from, Date to) {
		return d.getTime() > from.getTime() && d.getTime() < to.getTime();
	}
	
	public static Date parse(String s) {
		// 2013-05-19T12:03:39.999Z
		if (s.indexOf('T')==10 && s.indexOf('Z')==23) {
			return mktime(
				Integer.parseInt(s.substring(11, 11+2)), 	// hour
				Integer.parseInt(s.substring(14, 14+2)),  // minute
				Integer.parseInt(s.substring(17, 17+2)), // second
				Integer.parseInt(s.substring(5, 5+2)), // month
				Integer.parseInt(s.substring(8, 8+2)), // day
				Integer.parseInt(s.substring(0, 4)) // year
			);
		}
		return null;
	}
}
