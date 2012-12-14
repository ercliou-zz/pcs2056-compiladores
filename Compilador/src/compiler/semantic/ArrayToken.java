package compiler.semantic;

import java.util.ArrayList;
import java.util.List;

import compiler.commons.Token;
import compiler.commons.TokenType;

public class ArrayToken extends Token {

	private List<Token> arraySizeExpression;

	public ArrayToken(Integer value) {
		super(value, TokenType.IDENTIFIER);
		this.arraySizeExpression = new ArrayList<Token>();
	}

	public List<Token> getArraySizeExpression() {
		return arraySizeExpression;
	}

	public void setArraySizeExpression(List<Token> arraySizeExpression) {
		this.arraySizeExpression = arraySizeExpression;
	}

}
