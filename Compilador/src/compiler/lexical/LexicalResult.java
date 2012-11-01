package compiler.lexical;

import compiler.Token;

/**
 * Classe que representa o resultado de uma an�lise l�xica segundo nossa
 * arquitetura, contendo um token e um cursor que indica at� onde o texto fonte
 * foi analisado
 * 
 */
public class LexicalResult {
	private Token token;
	private int cursor;

	public Token getToken() {
		return token;
	}
	public void setToken(Token token) {
		this.token = token;
	}
	public int getCursor() {
		return cursor;
	}
	public void setCursor(int cursor) {
		this.cursor = cursor;
	}

	@Override
	public String toString() {
		return "token: " + token + " cursor: " + cursor;
	}

	public static LexicalResult startResult() {
		LexicalResult lr = new LexicalResult();
		lr.setCursor(0);
		lr.setToken(null);
		return lr;
	}

}
