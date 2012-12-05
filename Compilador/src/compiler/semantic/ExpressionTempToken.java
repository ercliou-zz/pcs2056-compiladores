package compiler.semantic;

import compiler.commons.IdentifierType;
import compiler.commons.SymbolTable;
import compiler.commons.Token;
import compiler.commons.TokenType;

public class ExpressionTempToken extends Token {

	private int tempTokenId;

	public ExpressionTempToken(int tempTokenId, Token firstArg, Token secondArg, Token operator) {
		super(null, calculateType(firstArg, secondArg, operator));
		this.tempTokenId = tempTokenId;
	}

	public int getTempTokenId() {
		return tempTokenId;
	}

	private static TokenType calculateType(Token firstArg, Token secondArg, Token operator) {
		if (isComparation(operator)) {
			if ((firstArg.getType() == TokenType.NUMERIC || (firstArg.getType() == TokenType.IDENTIFIER && SymbolTable.getInstance()
					.getElementById(firstArg.getValue()).getType() == IdentifierType.INTEGER))
					&& (secondArg.getType() == TokenType.NUMERIC || (secondArg.getType() == TokenType.IDENTIFIER && SymbolTable.getInstance()
							.getElementById(secondArg.getValue()).getType() == IdentifierType.INTEGER))) {
				return TokenType.KW_TRUE;
			} else if (operator.getType() == TokenType.EQUALS || operator.getType() == TokenType.DIFFERENT || operator.getType() == TokenType.KW_AND
					|| operator.getType() == TokenType.KW_OR) {
				if ((firstArg.getType() == TokenType.KW_FALSE || firstArg.getType() == TokenType.KW_TRUE || (firstArg.getType() == TokenType.IDENTIFIER && SymbolTable
						.getInstance().getElementById(firstArg.getValue()).getType() == IdentifierType.BOOL))
						&& (secondArg.getType() == TokenType.KW_FALSE || secondArg.getType() == TokenType.KW_TRUE || (secondArg.getType() == TokenType.IDENTIFIER && SymbolTable
								.getInstance().getElementById(secondArg.getValue()).getType() == IdentifierType.BOOL)))
					return TokenType.KW_TRUE;
			}
		} else {
			if ((firstArg.getType() == TokenType.NUMERIC || (firstArg.getType() == TokenType.IDENTIFIER && SymbolTable.getInstance()
					.getElementById(firstArg.getValue()).getType() == IdentifierType.INTEGER))
					&& (secondArg.getType() == TokenType.NUMERIC || (secondArg.getType() == TokenType.IDENTIFIER && SymbolTable.getInstance()
							.getElementById(secondArg.getValue()).getType() == IdentifierType.INTEGER))) {
				return TokenType.NUMERIC;
			}
		}
		throw new RuntimeException("Tipos conflitantes.");
	}

	private static boolean isComparation(Token token) {
		if (token.getType() == TokenType.EQUALS || token.getType() == TokenType.DIFFERENT || token.getType() == TokenType.GREATER_OR_EQUALS
				|| token.getType() == TokenType.LESS_OR_EQUALS
				|| (token.getType() == TokenType.OTHER && (token.getValue() == (int) '>' || token.getValue() == (int) '<'))) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "[ExpressionTempToken " + tempTokenId + "]";
	}

}
