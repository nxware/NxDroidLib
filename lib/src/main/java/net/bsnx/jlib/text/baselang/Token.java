package net.bsnx.jlib.text.baselang;

public class Token {

	private String value;
	private int line;
	private int col;
	private int length;
	private TokenKind kind;
	
	public Token(String value, int line, int col, int length, TokenKind kind) {
		super();
		this.value = value;
		this.line = line;
		this.col = col;
		this.length = length;
		this.kind = kind;
	}
	
	public String getValue() {
		return value;
	}

	public int getLine() {
		return line;
	}

	public int getCol() {
		return col;
	}

	public int getLength() {
		return length;
	}

	public TokenKind getKind() {
		return kind;
	}

	@Override
	public String toString() {
		return value;
	}
	
	public boolean isWhiteSpace() {
		return kind.equals(TokenKind.WHITE_SPACE);
	}
	
	public boolean isIdentifer() {
		return kind.equals(TokenKind.IDENTIFER);
	}
	
	public boolean isString() {
		return kind.equals(TokenKind.STRING_LITERAL);
	}
	
	public boolean isNumber() {
		return kind.equals(TokenKind.NUMBER_LITERAL);
	}
	
	public boolean isOperator() {
		return kind.equals(TokenKind.OPERATOR);
	}
	
	public boolean isEof() {
		return kind.equals(TokenKind.EOF);
	}
	
	public String val() {
		return value;
	}
	
}
