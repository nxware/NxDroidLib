package net.bsnx.jlib.text.baselang;

import net.bsnx.jlib.collections.Lst;
import net.bsnx.jlib.text.TokenStream;

import java.util.Iterator;

public class BaseLang implements Iterable<Token>{
	
	public static void main(String[] args) {
		//new BaseLang("");
		//new BaseLang("id");
		BaseLang lang = new BaseLang("op(): int");
		System.out.println(lang);
		lang = new BaseLang("operation(1, \"ok\", true);");
		System.out.println(lang);
		//System.out.println(new BaseLang("'val'").getTokens().get(0).getValue());
	}
	
	public static boolean SAFE_MODE = true;
	private static int SAFE_MODE_LIMIT = 10000000;
	
	private String[] operators = new String[] {
		":", ";", "=", "(", ")", "{", "}", "[", "]", "*", "/", "*", "+", "-", ",", "<<", ">>", "#", "%", "&", "|", "<", ">"
	};

	private TokenStream stream;
	private int line = 0;
	private int col = 0;
	private int pos = 0;
	
	private int index = 0;
	
	private Lst<Token> tokens = new Lst<Token>();
	
	private int _saveModeCount = 0;
	
	private BaseLang() {}
	
	public BaseLang(String s) {
		this();
		this.stream = new TokenStream(s);
		parse();
	}
	
	private void parse() {
		while (!stream.isEof()) {
			//System.out.println("BaseLang: " + stream.getPreview(3));
			readWhiteSpace();
			readComment();
			readOp();
			readLiteral();
			readIdentifer();
			if (SAFE_MODE) {
				_saveModeCount++;
				if (_saveModeCount > SAFE_MODE_LIMIT) {
					System.err.println("BaseLang Error: (Limit Exceeded) " + this.stream);
					break;
				}
			}
		}
	}
	

	private void readIdentifer() {
		//System.out.println("readIdentifer");
		if (stream.isEof()) return;
		char c = stream.getPreviewChar();
		if (Character.isJavaIdentifierStart(c)) {
			int startCol = col;
			int startPos = pos;
			stream.get(1);
			pos++;
			col++;
			String id = "" + c;
			if (!stream.isEof()) {
				c = stream.getPreviewChar();
				while (Character.isJavaIdentifierPart(c)) {
					stream.get();
					id+=c;
					pos++;
					col++;
					if (stream.isEof()) {
						break;
					}
					c = stream.getPreviewChar();
				}
			}
			tokens.add(new Token(id, line, startCol, pos-startPos, TokenKind.IDENTIFER));
		}
	}

	private void readLiteral() {
		//System.out.println("readLiteral");
		if (stream.isEof()) return;
		char c = stream.getPreviewChar();
		if (Character.isDigit(c)) {
			readNumber();
		} else if (c == '\'' || c == '"') {
			readStringLiteral();
		}
	}

	private void readStringLiteral() {
		char escape = stream.get(1).charAt(0);
		int startLine = line;
		int startCol = col;
		int startPos = pos;
		String buf = "";
		int state = 0;
		LOOP: while (!stream.isEof()) {
			char c = stream.get(1).charAt(0);
			pos++;
			if (c == '\n') {
				line++;
				col = 0;
			} else {
				col++;
			}
			switch (state) {
			case 0:
				if (c == '\\') {
					state = 1;
				} else if (c == escape) {
					break LOOP;
				} else {
					buf += c;
				}
				break;

			case 1:
				if (c == 'n') {
					buf += "\n";
				} else {
					buf += c;
				}
				state = 0;
				break;
			}
		}
		tokens.add(new Token(buf, startLine, startCol, pos-startPos, TokenKind.STRING_LITERAL));
	}

	private void readNumber() {
		String buf = "";
		int startCol = col;
		int startPos = pos;
		do {
			if (stream.isEof()) break;
			char c = stream.getPreviewChar();
			if (Character.isDigit(c)) {
				stream.get();
				col++;
				pos++;
				buf += c;
			} else {
				break;
			}
		} while (!stream.isEof());
		tokens.add(new Token(buf, startPos, startCol, pos-startPos, TokenKind.NUMBER_LITERAL));
	}

	private void readOp() {
		//System.out.println("readOp");
		for (String op : operators) {
			if (stream.isOnTop(op)) {
				tokens.add(new Token(op, line, col, op.length(), TokenKind.OPERATOR));
				stream.get(op.length());
				col += op.length();
				pos += op.length();
			}
		}
		
	}

	private void readComment() {
		// TODO Auto-generated method stub
		
	}

	private void readWhiteSpace() {
		//System.out.println("readWhiteSpace");
		int startLine = line;
		int startCol = col;
		int startPos = pos;
		String buf = "";
		do {
			char c = stream.getPreviewChar();
			if (c == ' ' || c=='\t' || c =='\n' || c == '\r') {
				buf += c;
				stream.get();
				pos++;
				if (c == '\n') {
					col = 0;
					line++;
				}
			} else {
				break;
			}
		} while (!stream.isEof());
		if (buf.length() > 0) {
			tokens.add(new Token(buf, startLine, startCol, pos-startPos, TokenKind.WHITE_SPACE));
		}
	}
	
	public Lst<Token> getTokens() {
		return tokens;
	}

	@Override
	public Iterator<Token> iterator() {
		return tokens.iterator();
	}
	
	public BaseLang withoutWhiteSpaces() {
		BaseLang res = new BaseLang();
		for (Token token : tokens) {
			if (!token.isWhiteSpace()) {
				res.tokens.add(token);
			}
		}
		return res;
	}
	
	public Token next() {
		index++;
		return current();
	}
	
	public Token current() {
		return isEof() ? new Token("", line, col, 0, TokenKind.EOF) : tokens.get(index);
	}
	
	public boolean isEof() {
		return index >= tokens.size()-1;
	}
	
	public boolean has(TokenKind...tokenKinds) {
		if (index+tokenKinds.length > tokens.size()) {
			return false;
		}
		int i = index;
		for (int j = index; j < tokenKinds.length; j++) {
			TokenKind tokenKind = tokenKinds[j];
			if (!tokens.get(i+j).getKind().equals(tokenKind)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Token t : this) {
			sb.append(t.getValue() + "|");
		}
		return sb.toString();
	}
	
}
