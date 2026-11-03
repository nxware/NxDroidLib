package net.bsnx.jlib.io;

import net.bsnx.i.Function;
import net.bsnx.jlib.FileSystem;
import net.bsnx.jlib.collections.Collections;
import net.bsnx.json.JSONObject;

import java.util.List;

public class JsonFileDb {
	
	public static void main(String[] args) {
		JsonFileDb db = new JsonFileDb("json.txt");
		db.append(new JSONObject().put("b", true));
		System.out.println(db.items());
		System.out.println(db.last());
		FileSystem.delete("json.txt");
	}

	private String path;

	public JsonFileDb(String path) {
		this.path = path;
	}
	
	public void append(JSONObject obj) {
		FileSystem.appendLine(path, obj.toString().replace('\n', ' '));
	}
	
	public JSONObject last() {
		List<String> list = FileSystem.readLines(path);
		return new JSONObject(list.get(list.size()-1));
	}
	
	public List<JSONObject> items() {
		List<String> list = FileSystem.readLines(path);
		return Collections.convert(list, new Function<String, JSONObject>() {
			@Override public JSONObject execute(String s) {
				return new JSONObject(s);
			}
		});
	}
	
}
