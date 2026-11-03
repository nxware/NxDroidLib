package net.bsnx.jlib.text.baselang;

import net.bsnx.jlib.collections.StringList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OperationLang implements Iterable<Operation> {
	
	private List<Operation> list = new ArrayList<Operation>();
	
	public static void main(String[] args) {
		OperationLang lang = new OperationLang();
		lang.parseLine("a(1,2,3);");
		lang.parseLine("a(1 ,2, 3);");
		lang.parseLine("a(1 , \"dfff\", 3);");
		lang.parseLine("myOp(sffg , \"d,.,fff\", 3);");
		System.out.println(lang);
	}

	public void parseScript(String script) {
		StringList list = StringList.fromText(script);
		for (String s : list) {
			if (!s.trim().equals("") && !s.trim().startsWith("//") && !s.trim().startsWith("#")) {
				parseLine(s);
			}
		}
	}
	
	public void parseLine(String s) {
		BaseLang lang = new BaseLang(s);
		lang = lang.withoutWhiteSpaces();
		Operation op = new Operation(lang.current().getValue());
		lang.next();
		while (!lang.isEof()) {
			Token t = lang.next();
			if (!t.isOperator() && !t.isEof()) {
				op.addParameter(t.getValue());
			}
		}
		list.add(op);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Operation op : list) {
			sb.append(op.toString() + ";\n");
		}
		return sb.toString();
	}

	@Override
	public Iterator<Operation> iterator() {
		return list.iterator();
	}
	
}
