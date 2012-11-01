package compiler;

/**
 * Enumeração dos possíveis tipos de tokens da nossa linguagem
 * 
 */
public enum TokenType {
	NUMERIC, IDENTIFIER, GREATER_OR_EQUALS, LESS_OR_EQUALS, EQUALS, DIFFERENT, OTHER, KW_ELSE(
			"else"), KW_WHILE("while"), KW_IF("if"), KW_INT("int"), KW_RETURN(
			"return"), KW_VOID("void"), KW_AND("AND"), KW_OR("OR"), KW_MAIN(
			"main"), KW_READ("read"), KW_WRITE("write");

	private boolean isKeyword;
	private String keyword;

	private TokenType() {
		isKeyword = false;
	}

	private TokenType(String keyword) {
		isKeyword = true;
		this.keyword = keyword;
	}

	/**
	 * 
	 * @return <code> true </code> caso seja uma palavra reservada e
	 *         <code> false </code> caso contrário
	 */
	public boolean isKeyword() {
		return isKeyword;
	}

	private boolean equals(String keyword) {
		return this.keyword.equals(keyword);
	}

	/**
	 * Método que devolve o tipo de um token de uma palavra reservada a partir
	 * de uma string com o valor desta palavra
	 * 
	 * @param keyword
	 *            String com o valor da palavra reservada
	 * @return O tipo referente à palavra reservada passada
	 */
	public static TokenType getKeywordEnum(String keyword) {
		for (TokenType type : values()) {
			if (type.isKeyword() && type.equals(keyword)) {
				return type;
			}
		}
		return null;
	}

	/**
	 * Método que diz se uma palavra é reservada ou não
	 * 
	 * @param word
	 *            p
	 * @return <code> true </code> se a string for uma palavra reservada e
	 *         <code> false </code> caso contrário
	 */
	public static boolean isKeyword(String word) {
		for (TokenType type : values()) {
			if (type.isKeyword() && type.equals(word)) {
				return true;
			}
		}
		return false;
	}
}
