package compiler.semantic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import compiler.commons.IdentifierType;
import compiler.commons.NumberConverter;
import compiler.commons.SymbolTable;
import compiler.commons.Token;
import compiler.commons.TokenType;

public class ExpressionProcessor {
	private List<Token> expression;
	private Stack<Token> tokenStack;

	private boolean isReversePolonese;
	private int tempTokenCounter = 0;
	private int orCounter = 0;

	private static ExpressionProcessor INSTANCE;
	
	private ExpressionProcessor() {
		expression = new ArrayList<Token>();
		tokenStack = new Stack<Token>();
		isReversePolonese = false;
	}
	
	public static ExpressionProcessor getInstance(){
		if(INSTANCE == null)
			INSTANCE = new ExpressionProcessor();
		return INSTANCE;
	}

	public void concatToken(Token token) {
		expression.add(token);
	}

	public void reversePoloneseTransformation() {
		List<Token> newExpression = new ArrayList<Token>();

		for (Token token : expression) {
			if (isOperator(token)) {
				while (hasGreaterOrEqualsOperatorOnTop(token)) {
					newExpression.add(tokenStack.pop());
				}
				tokenStack.push(token);
			}
			if (isOpenParentesis(token))
				tokenStack.push(token);
			if (isCloseParentesis(token)) {
				while (!hasOpenParentesisOnTop()) {
					newExpression.add(tokenStack.pop());
				}
				tokenStack.pop();
			}
			if (isOperand(token))
				newExpression.add(token);
		}

		while (!tokenStack.isEmpty()) {
			newExpression.add(tokenStack.pop());
		}

		expression = newExpression;
		isReversePolonese = true;

	}

	public void restart(){
		expression = new ArrayList<Token>();
		tokenStack = new Stack<Token>();
		isReversePolonese = false;
	}
	
	public int getTempsNumber(){
		return tempTokenCounter;
	}
	
	public String generateExpressionCode() {
		if (isReversePolonese) {

			String expressionCode = "";

			tokenStack.clear();

			Token firstArg;
			Token secondArg;

			for (Token token : expression) {
				if (!isOperator(token)) {
					tokenStack.push(token);
				} else {
					secondArg = tokenStack.pop();
					firstArg = tokenStack.pop();
					expressionCode += generateExpressionElementCode(firstArg, secondArg, token);
					tokenStack.push(new ExpressionTempToken(tempTokenCounter, firstArg, secondArg, token));
					tempTokenCounter++;
				}
			}

			if (!tokenStack.isEmpty())
				expressionCode += generateExpressionElementCode(tokenStack.pop());

			return expressionCode;
		}
		return null;
	}

	private String generateExpressionElementCode(Token firstArg, Token secondArg, Token operator) {

		validateOperands(firstArg, secondArg);

		String result = "";

		String arg1 = "";
		String arg2 = "";

		if (firstArg.getClass().equals(ExpressionTempToken.class))
			arg2 = "\tLD\t" + "EXP_TEMP" + ((ExpressionTempToken) firstArg).getTempTokenId() + "\n";
		else if (firstArg.getType() == TokenType.IDENTIFIER)
			arg2 = "\tLD\t" + firstArg.getValue().toString() + "\n";
		else if (firstArg.getType() == TokenType.NUMERIC)
			arg2 = "\tLV\t" + NumberConverter.convert(firstArg.getValue()) + "\n";

		if (secondArg.getClass().equals(ExpressionTempToken.class))
			arg1 = "\tLD\t" + "EXP_TEMP" + ((ExpressionTempToken) secondArg).getTempTokenId() + "\n";
		else if (secondArg.getType() == TokenType.IDENTIFIER)
			arg1 = "\tLD\t" + secondArg.getValue().toString() + "\n";
		else if (secondArg.getType() == TokenType.NUMERIC)
			arg1 = "\tLV\t" + NumberConverter.convert(secondArg.getValue()) + "\n";

		if (operator.getType() == TokenType.OTHER && operator.getValue() == (int) '+') {
			result = arg1 + "\tMM\tEXP_TEMP" + tempTokenCounter + "\n" + arg2 + "\t+\tEXP_TEMP" + tempTokenCounter + "\n\tMM\tEXP_TEMP" + tempTokenCounter + "\n\n";
		} else if (operator.getType() == TokenType.OTHER && operator.getValue() == (int) '-') {
			result = arg1 + "\tMM\tEXP_TEMP" + tempTokenCounter + "\n" + arg2 + "\t-\tEXP_TEMP" + tempTokenCounter + "\n\tMM\tEXP_TEMP" + tempTokenCounter + "\n\n";
		} else if (operator.getType() == TokenType.OTHER && operator.getValue() == (int) '/') {
			result = arg1 + "\tMM\tEXP_TEMP" + tempTokenCounter + "\n" + arg2 + "\t/\tEXP_TEMP" + tempTokenCounter + "\n\tMM\tEXP_TEMP" + tempTokenCounter + "\n\n";
		} else if (operator.getType() == TokenType.OTHER && operator.getValue() == (int) '*') {
			result = arg1 + "\tMM\tEXP_TEMP" + tempTokenCounter + "\n" + arg2 + "\t*\tEXP_TEMP" + tempTokenCounter + "\n\tMM\tEXP_TEMP" + tempTokenCounter + "\n\n";
		} else if (operator.getType() == TokenType.KW_AND) {
			result = arg1 + "\tMM\tEXP_TEMP" + tempTokenCounter + "\n" + arg2 + "\t*\tEXP_TEMP" + tempTokenCounter + "\n\tMM\tEXP_TEMP" + tempTokenCounter + "\n\n";
		} else if(operator.getType() == TokenType.KW_OR){
			result = arg1 + "\tMM\tEXP_TEMP" + tempTokenCounter + "\n" + arg2 + "\t+\tEXP_TEMP" + tempTokenCounter;
			result += "\n\t-\tCONST_TWO\n" + "\tJZ\tEXP_OR_EQ" + orCounter + "\n" + "\tJP\tEXP_OR_DF" + orCounter +"\n";
			result += "EXP_OR_EQ" + orCounter + "\tOS\n";
			result += "\tLV\t/0001\n" + "\tJP\tEXP_OR_END" + orCounter + "\n";
			result += "EXP_OR_DF" + orCounter + "\tOS\n" + "\t+\tCONST_TWO\n" + "\tMM\tEXP_TEMP" + tempTokenCounter + "\n\n";
			orCounter++;
		} else
			throw new RuntimeException("Operação de expressão não existente.");

		return result;
	}
	
	private String generateExpressionElementCode(Token singleArg) {
		String result = "";
		
		if (singleArg.getClass().equals(ExpressionTempToken.class))
			result = "\tLD\t" + "EXP_TEMP" + ((ExpressionTempToken) singleArg).getTempTokenId() + "\n";
		else if (singleArg.getType() == TokenType.IDENTIFIER)
			result = "\tLD\t" + singleArg.getValue().toString() + "\n";
		else if (singleArg.getType() == TokenType.NUMERIC)
			result = "\tLV\t" + NumberConverter.convert(singleArg.getValue()) + "\n";
		
		return result;
	}

	private void validateOperands(Token arg1, Token arg2) {
		IdentifierType arg1Type = null;
		IdentifierType arg2Type = null;

		if (arg1.getType() == TokenType.NUMERIC)
			arg1Type = IdentifierType.INTEGER;
		if (arg1.getType() == TokenType.KW_TRUE || arg1.getType() == TokenType.KW_FALSE)
			arg1Type = IdentifierType.BOOL;
		if (arg1.getType() == TokenType.IDENTIFIER)
			if (SymbolTable.getInstance().getElementById(arg1.getValue()).getType() == IdentifierType.BOOL)
				arg1Type = IdentifierType.BOOL;
			else
				arg1Type = IdentifierType.INTEGER;

		if (arg2.getType() == TokenType.NUMERIC)
			arg2Type = IdentifierType.INTEGER;
		if (arg2.getType() == TokenType.KW_TRUE || arg2.getType() == TokenType.KW_FALSE)
			arg2Type = IdentifierType.BOOL;
		if (arg2.getType() == TokenType.IDENTIFIER)
			if (SymbolTable.getInstance().getElementById(arg2.getValue()).getType() == IdentifierType.BOOL)
				arg2Type = IdentifierType.BOOL;
			else
				arg2Type = IdentifierType.INTEGER;

		if (arg1Type != arg2Type || arg1Type == null || arg2Type == null)
			throw new RuntimeException("conflito de tipos " + arg1 + " " + arg2);
	}

	private boolean isOperator(Token token) {
		return isLowOperator(token) || isHighOperator(token);
	}

	private boolean isLowOperator(Token token) {
		if (token.getType() == TokenType.KW_OR || (token.getType() == TokenType.OTHER && (token.getValue() == (int) '+' || token.getValue() == (int) '-'))) {
			return true;
		}
		return false;
	}

	private boolean isHighOperator(Token token) {
		if (token.getType() == TokenType.KW_AND || (token.getType() == TokenType.OTHER && (token.getValue() == (int) '/' || token.getValue() == (int) '*'))) {
			return true;
		}
		return false;
	}

	private boolean isOpenParentesis(Token token) {
		return token.getType() == TokenType.OTHER && token.getValue() == (int) '(';
	}

	private boolean isCloseParentesis(Token token) {
		return token.getType() == TokenType.OTHER && token.getValue() == (int) ')';
	}

	private boolean hasGreaterOrEqualsOperatorOnTop(Token token) {
		if (tokenStack.isEmpty())
			return false;
		if (isLowOperator(token) && isOperator(tokenStack.peek()))
			return true;
		if (isHighOperator(token) && isHighOperator(tokenStack.peek()))
			return true;
		return false;
	}

	private boolean hasOpenParentesisOnTop() {
		if (tokenStack.isEmpty())
			return false;
		return isOpenParentesis(tokenStack.peek());
	}

	private boolean isOperand(Token token) {
		if (token.getType() == TokenType.IDENTIFIER || token.getType() == TokenType.NUMERIC)
			return true;
		return false;
	}
}
