package net.bsnx.data;

import net.bsnx.i.Function;
import net.bsnx.jlib.collections.Categories;
import net.bsnx.jlib.collections.Lst;
import net.bsnx.jlib.collections.NMap;

import java.util.Collection;
import java.util.Comparator;

public class RowGroup extends Categories<Row, String> {

	private RowGetter getter;

	private NMap<String, Function<Lst<Row>, String>> aggs = new NMap<String, Function<Lst<Row>, String>>();
	
	public RowGroup(Collection<Row> items, RowGetter getter, Function<String, String> reduce) {
		super(items, getter, reduce);
		this.getter = getter;
	}
	
	public RowGroup aggregateCount(final String fieldName) {
		aggs.put(fieldName, new Function<Lst<Row>, String>() {
			@Override public String execute(Lst<Row> item) {
				return ""+item.size();
			}
		});
		return this;
	}
	
	public RowGroup aggregateSum(final String fieldName) {
		aggs.put(fieldName, new Function<Lst<Row>, String>() {
			@Override public String execute(Lst<Row> item) {
				double mem = 0;
				for (Row r : item) {
					try {
						mem += Double.parseDouble(r.getString(fieldName));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return ""+mem;
			}
		});
		return this;
	}
	
	public RowGroup aggregateMin(final String fieldName) {
		aggs.put(fieldName, new Function<Lst<Row>, String>() {
			@Override public String execute(Lst<Row> item) {
				RowCursor cur = new RowCursor();
				cur.addAll(item);
				Lst<String> vals = cur.select(fieldName);
				vals.sort(new Comparator<String>() {
					@Override public int compare(String o1, String o2) {
						// TODO isNumberType
						return o1.compareTo(o2);
					}
				});
				return vals.isEmpty() ? "" : vals.get(0);
			}
		});
		return this;
	}
	
	public RowCursor toRows() {
		run();
		RowCursor res = new RowCursor();
		for (String cat : this.getCatList()) {
			Row row = new Row();
			row.add(getter.getName(), cat);
			for (String gField : aggs.keys()) {
				Function<Lst<Row>, String> fn = aggs.get(gField);
				row.add(gField, fn.execute(this.getMap().get(cat)));
			}
			res.add(row);
		}
		return res;
	}

}
