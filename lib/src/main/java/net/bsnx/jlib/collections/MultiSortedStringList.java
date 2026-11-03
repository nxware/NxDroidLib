package net.bsnx.jlib.collections;

import net.bsnx.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultiSortedStringList extends ArrayList<StringList> {

	private static final long serialVersionUID = 1L;

	public MultiSortedStringList(List<String>...list) {
		for (List<String> l : list) {
			add(new StringList().addList(l).sort());
		}
	}
	
	public Collection<JSONObject> toRows() {
		List<JSONObject> res = new ArrayList<JSONObject>();
		while (getNonEmptyListCount()>1) {
			JSONObject row = new JSONObject();
			int i = getMinIndex();
			String cur = first(i);
			for (int j = 0; j < size(); j++) {
				if (i==j) {
					row.put(""+j, pop(j));
				} else if (cur.compareTo(first(j))==0) {
					row.put(""+j, pop(j));
				}
			}
			res.add(row);
			//System.out.println(this);
			//System.out.println("-------------------------------------------------------");
		}
		if (!isAllEmpty()) {
			int i = getFirstNonEmptyIndex();
			while (!isEmpty(i)) {
				JSONObject row = new JSONObject();
				row.put(""+i, pop(i));
				res.add(row);
			}
		}
		return res;
	}
	private int getNonEmptyListCount() {
		int cnt = 0;
		for (int j = 0; j < size(); j++) {
			if (!isEmpty(j)) {
				cnt++;
			}
		}
		return cnt;
	}
	
	private int getMinIndex() {
		int i = 0;
		for (int j = getFirstNonEmptyIndex(); j < size(); j++) {
			if (!isEmpty(j)) {
				if (first(i).compareTo(first(j))>0) {
					i = j;
				}
			}
		}
		return i;
	}
	
	private int getFirstNonEmptyIndex() {
		for (int j = 0; j < size(); j++) {
			if (!isEmpty(j)) {
				return j;
			}
		}
		return -1;
	}
	
	private String first(int list) {
		return isEmpty(list) ? "" : get(list).get(0);
	}
	
	private String pop(int list) {
		String res = first(list);
		if (!isEmpty(list)) {
			get(list).remove(0);
		}
		return res;
	}
	
	private boolean isAllEmpty() {
		for (int i = 0; i < size(); i++) {
			if (!isEmpty(i)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isEmpty(int list) {
		return get(list).size() == 0;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (StringList sl : this) {
			sb.append(sl);
			sb.append("\n");
		}
		return sb.toString();
	}
	
}
