package compilador.lexico;

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

}
