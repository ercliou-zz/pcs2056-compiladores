package compilador.lexico;

public class Token {
	private Integer value;
	private TokenType type;

	public Token(Integer value, TokenType type) {
		this.value = value;
		this.type = type;
	}

	public Integer getValue() {
		return value;
	}

	public TokenType getType() {
		return type;
	}
}
