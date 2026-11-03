package net.bsnx.jlib.text.html;

import static net.bsnx.StaticFunctions.println;

public class TableBuilder {
	
	public static void main(String[] args) {
		println(new TableBuilder()
			.cell("A1")
			.cell("B1")
			.newLine()
			.cell("A2")
			.cell("B2")
			.toHtml());
	}

	private enum TableBuildState { EMPTY, TR,TD }
	private StringBuilder sb = new StringBuilder();
	private TableBuildState state = TableBuildState.EMPTY;
	
	@SuppressWarnings("incomplete-switch")
	public TableBuilder newLine() {
		switch (state) {
		case EMPTY:
			sb.append("<table>");
			break;
		case TD:
			sb.append("</tr>");
			break;
		}
		state = TableBuildState.TR;
		sb.append("<tr>");
		return this;
	}
	
	@SuppressWarnings("incomplete-switch")
	public TableBuilder cell(String val) {
		switch (state) {
		case EMPTY:
			sb.append("<table>");
			sb.append("<tr>");
			break;
		}
		state = TableBuildState.TD;
		sb.append("<td>");
		sb.append(val);
		sb.append("</td>");
		return this;
	}
	
	public String toHtml() {
		sb.append("</tr>");
		sb.append("</table>");
		return sb.toString();
	}

}
