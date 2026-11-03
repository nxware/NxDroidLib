package net.bsnx.jlib.text.html;

import net.bsnx.Php;

public class Jq {

	public static String str(String val) {
		return "\"" + val + "\"";
	}
	
	public static String obj(String ...pairs) {
		return "{\n" + Php.implode(",\n", pairs)+ "}";
	}
	
	public static String onLoad(String js) {
		return "$(function() {\n"+js+"\n});";
	}
	
	public static String get(String url, String dataJson) {
		return "$.get("+str(url)+", "+dataJson+");";
	}
	
	public static String focusout(String selector, String onExecCode) {
		return "$(\""+selector+"\").focusout(function() {\n"+onExecCode+"});";
	}

}
