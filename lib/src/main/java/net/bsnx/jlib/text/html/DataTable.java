package net.bsnx.jlib.text.html;

import java.util.ArrayList;
import java.util.List;

/**
 * Zeilebasierte Tabelle
 * 
 * @see php Html\DataTable
 * 
 * @author Bjoern R. Salgert
 * @date 01.05.2011
 */
public class DataTable implements HtmlAble {
	
	public DataTable() {
		
	}

	List<String> captions = new ArrayList<String>();
	
	// {{{ setColumns
	
	public DataTable setColumns(Iterable<String> captions) {
		this.captions.clear();
		for (String caption : captions) {
			this.captions.add(caption);
		}
		return this;
	}
	
	public DataTable setColumns(String[] columnNames) {
		this.captions.clear();
		for (String caption : captions) {
			this.captions.add(caption);
		}
		return this;
	}
	
	// }}}
	
	List<List<Object>> rows = new ArrayList<List<Object>>();
	
	public DataTable addRow(List<Object> row) {
		rows.add(row);
		return this;
	}
	
	public String toHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table>\n");
		if (captions.size() > 0) {
			sb.append(" <tr>\n");
			for (String caption: captions) {
				sb.append("  <th>"+caption+"</th>\n");
			}
			sb.append(" </tr>\n");
		}
		for (List<Object> row : rows) {
			sb.append(" <tr>\n");
			for (Object cell : row) {
				sb.append(" <td>"+cell+"</td>\n");
			}
			sb.append(" </tr>\n");
		}
		sb.append("</table>");
		return sb.toString();
	}
	
}