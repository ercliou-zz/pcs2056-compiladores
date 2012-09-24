package compilador.lexico;

/**
 * Classe que representa um token, contendo seu tipo e o valor, que cont�m
 * informa��es adicionais sobre o token (quando necess�rio)
 * 
 */
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

	@Override
	public String toString() {
		return "[" + type + " " + value + "]";
	}

}
