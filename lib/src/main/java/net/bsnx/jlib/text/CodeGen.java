package net.bsnx.jlib.text;

import net.bsnx.Php;
import net.bsnx.i.Function;
import net.bsnx.jlib.collections.Collections;

import java.util.List;

public class CodeGen {

	public static String toStringLiteralList(final List<String> list) {
		List<String> lst = Collections.map(list, new Function<String, String>() {
			@Override public String execute(String item) {
				return "\"" + item + "\"";
			}
		});
		return Php.implode(", ", lst);
	}
	
}
