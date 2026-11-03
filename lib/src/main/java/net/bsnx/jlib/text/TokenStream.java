package net.bsnx.jlib.text;

public class TokenStream {

	public TokenStream(String s) {
		if (s == null) {
			this.s = "";
		} else {
			this.s = s;
		}
	}

	private String s;

	public char getPreviewChar() {
		return getPreview(1).charAt(0);
	}
	
	public String getPreview(int length) {
		try {
			return s.substring(0, length);
		} catch (Exception e) {
			return "";
		}
	}

	public boolean isOnTop(String str) {
		return s.startsWith(str);
	}

	public boolean isAnyOnTop(Iterable<String> s) {
		return isAnyOnTop(s, true);
	}

	public boolean isAnyOnTop(Iterable<String> s, boolean casesensetiv) {
		for (String e : s) {
			String preview = getPreview(e.length());
			if (casesensetiv) {
				if (e.toLowerCase().equals(preview.toLowerCase()))
					return true;
			} else {
				if (e.equals(preview))
					return true;
			}
		}
		return false;
	}

	public boolean isEof() {
		return s.equals("");
	}

	public String get() {
		return get(1);
	}

	public String get(int length) {
		try {
			String res = getPreview(length);
			s = s.substring(length);
			return res;
		} catch (Exception e) {
			return "";
		}
	}

	public String readTil(String stop) {
		StringBuilder sb = new StringBuilder();
		while (!isOnTop(stop)) {
			sb.append(get());
			if (isEof())
				break;
		}
		return sb.toString();
	}

	/**
	 * Liest ein String Literal
	 * 
	 * @param escape
	 * @return StringLiteral ohne Escape Zeichen
	 */
	public String readStringLiteral(char escape) {
		StringBuilder sb = new StringBuilder();
		get();
		while (!isOnTop("" + escape)) {
			sb.append(get());
		}
		get();
		return sb.toString();
	}

	public String readWhile(char c) {
		StringBuilder sb = new StringBuilder();
		while (isOnTop("" + c)) {
			sb.append(get());
		}
		return sb.toString();
	}

	/**
	 * Liest den String str solange er OnTop ist 
	 * 
	 * @param str
	 * @return
	 */
	public String readWhile(String str) {
		StringBuilder sb = new StringBuilder();
		while (isOnTop(str)) {
			sb.append(get(str.length()));
		}
		return sb.toString();
	}
	
	public String readTilAny(char...chars) {
		StringBuilder sb = new StringBuilder();
		LOOP: while (!isEof()) {
			for (char c : chars) {
				if (s.charAt(0)==c) {
					break LOOP;
				}
			}
			sb.append(get());
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return "TokenStream [s=" + s + "]";
	}

	public void readWhiteSpaces() {
		readWhile(" ");
	}
	
	public int size() {
		return s.length();
	}

}
