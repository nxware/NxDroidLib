package net.bsnx.jlib.net;

import net.bsnx.jlib.collections.Collections;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class UrlPath {

	List<String> list = new ArrayList<String>();

	UrlPath() {}
	
	public UrlPath(String s) {
		String[] array = s.split("/");
		for (String str : array) {
			if (str!=null && str.length() > 0) {
				list.add(str);
			}
		}
	}
	
	public UrlPath(URI uri) {
		this(uri.getPath());
	}
	
	public String get(int i) {
		return list.get(i);
	}
	
	public boolean has(String s) {
		return get(0).equals(s);
	}
	
	public int count() {
		return list.size();
	}
	
	public boolean isEmpty() {
		return count() == 0;
	}

	public String first() {
		return get(0);
	}
	
	public UrlPath extract() {
		UrlPath res = new UrlPath();
		for (int i = 1; i < list.size(); i++) {
			res.list.add(list.get(i));
		}
		return res;
	}
	
	@Override
	public String toString() {
		return "/" + Collections.implode(list, "/");
	}
	
}
