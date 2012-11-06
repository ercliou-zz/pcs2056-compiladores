package compiler;

/**
 * Classe que representa um token, contendo seu tipo e o valor, que contém
 * informações adicionais sobre o token (quando necessário)
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (type != other.type)
			return false;
		if (other.type == TokenType.OTHER) {
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
		}
		return true;
	}

}
