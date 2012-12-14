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

	public ExpressionProcessor(int tempNumberOffset) {
		this();
		tempTokenCounter = tempNumberOffset;
	}
	
	public void setExpression(List<Token> expression){
		this.expression = expression;
	}

	public void incrementTempTokenCounter() {
		tempTokenCounter++;
	}

	public static ExpressionProcessor getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ExpressionProcessor();
		return INSTANCE;
	}

	public void concatToken(Token token) {
		expression.add(token);
	}

	public void reversePoloneseTransformation() {
		expression = reversePoloneseTransformationImpl(expression);
		isReversePolonese = true;
	}

	private List<Token> reversePoloneseTransformationImpl(List<Token> oldExpression) {
		List<Token> newExpression = new ArrayList<Token>();

		oldExpression = groupArrayTokens(oldExpression);

		for (Token token : oldExpression) {
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

		return newExpression;
	}

	public void restart() {
		expression = new ArrayList<Token>();
		tokenStack = new Stack<Token>();
		isReversePolonese = false;
	}

	public int getTempsNumber() {
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
			arg2 = "\tLD\t" + "EXP_TEMP" + ((ExpressionTempToken) firstArg).getTempTokenId() + "\r\n";
		else if (firstArg.getType() == TokenType.IDENTIFIER) {
			if (ArrayToken.class.isAssignableFrom(firstArg.getClass())) {
				ExpressionProcessor ep = new ExpressionProcessor(tempTokenCounter*1000);
				ep.reversePoloneseTransformation();
				ep.setExpression(((ArrayToken)firstArg).getArraySizeExpression());
				arg2 = ep.generateExpressionCode();
				arg2 += "\tMM\tINDEX_R\r\n";
				arg2 += "\tLD\t" + firstArg.getValue() + "_P\r\n";
				arg2 += "\tMM\tARRAY_R\r\n";
				arg2 += "\tSC\tACCESS_ARRAY\r\n\r\n";
			} else {
				arg2 = "\tLD\t" + firstArg.getValue().toString() + "\r\n";
			}
		} else if (firstArg.getType() == TokenType.NUMERIC)
			arg2 = "\tLV\t" + NumberConverter.convert(firstArg.getValue()) + "\r\n";

		if (secondArg.getClass().equals(ExpressionTempToken.class))
			arg1 = "\tLD\t" + "EXP_TEMP" + ((ExpressionTempToken) secondArg).getTempTokenId() + "\r\n";
		else if (secondArg.getType() == TokenType.IDENTIFIER) {
			if (ArrayToken.class.isAssignableFrom(secondArg.getClass())) {
				ExpressionProcessor ep = new ExpressionProcessor(tempTokenCounter*1000);
				ep.reversePoloneseTransformation();
				ep.setExpression(((ArrayToken)secondArg).getArraySizeExpression());
				arg1 = ep.generateExpressionCode();
				arg1 += "\tMM\tINDEX_R\r\n";
				arg1 += "\tLD\t" + secondArg.getValue() + "_P\r\n";
				arg1 += "\tMM\tARRAY_R\r\n";
				arg1 += "\tSC\tACCESS_ARRAY\r\n\r\n";
			} else {
				arg1 = "\tLD\t" + secondArg.getValue().toString() + "\r\n";
			}
		} else if (secondArg.getType() == TokenType.NUMERIC)
			arg1 = "\tLV\t" + NumberConverter.convert(secondArg.getValue()) + "\r\n";

		if (operator.getType() == TokenType.OTHER && operator.getValue() == (int) '+') {
			result = arg1 + "\tMM\tEXP_TEMP" + tempTokenCounter + "\r\n" + arg2 + "\t+\tEXP_TEMP" + tempTokenCounter + "\r\n\tMM\tEXP_TEMP" + tempTokenCounter
					+ "\r\n\r\n";
		} else if (operator.getType() == TokenType.OTHER && operator.getValue() == (int) '-') {
			result = arg1 + "\tMM\tEXP_TEMP" + tempTokenCounter + "\r\n" + arg2 + "\t-\tEXP_TEMP" + tempTokenCounter + "\r\n\tMM\tEXP_TEMP" + tempTokenCounter
					+ "\r\n\r\n";
		} else if (operator.getType() == TokenType.OTHER && operator.getValue() == (int) '/') {
			result = arg1 + "\tMM\tEXP_TEMP" + tempTokenCounter + "\r\n" + arg2 + "\t/\tEXP_TEMP" + tempTokenCounter + "\r\n\tMM\tEXP_TEMP" + tempTokenCounter
					+ "\r\n\r\n";
		} else if (operator.getType() == TokenType.OTHER && operator.getValue() == (int) '*') {
			result = arg1 + "\tMM\tEXP_TEMP" + tempTokenCounter + "\r\n" + arg2 + "\t*\tEXP_TEMP" + tempTokenCounter + "\r\n\tMM\tEXP_TEMP" + tempTokenCounter
					+ "\r\n\r\n";
		} else if (operator.getType() == TokenType.KW_AND) {
			result = arg1 + "\tMM\tEXP_TEMP" + tempTokenCounter + "\r\n" + arg2 + "\t*\tEXP_TEMP" + tempTokenCounter + "\r\n\tMM\tEXP_TEMP" + tempTokenCounter
					+ "\r\n\r\n";
		} else if (operator.getType() == TokenType.KW_OR) {
			result = arg1 + "\tMM\tEXP_TEMP" + tempTokenCounter + "\r\n" + arg2 + "\t+\tEXP_TEMP" + tempTokenCounter;
			result += "\r\n\t-\tCONST_TWO\r\n" + "\tJZ\tEXP_OR_EQ" + orCounter + "\r\n" + "\tJP\tEXP_OR_DF" + orCounter + "\r\n";
			result += "EXP_OR_EQ" + orCounter + "\tOS\r\n";
			result += "\tLV\t/0001\r\n" + "\tJP\tEXP_OR_END" + orCounter + "\r\n";
			result += "EXP_OR_DF" + orCounter + "\tOS\r\n" + "\t+\tCONST_TWO\r\n" + "\tMM\tEXP_TEMP" + tempTokenCounter + "\r\n\r\n";
			orCounter++;
		} else
			throw new RuntimeException("Operação de expressão não existente.");

		return result;
	}

	private String generateExpressionElementCode(Token singleArg) {
		String result = "";

		if (singleArg.getClass().equals(ExpressionTempToken.class))
			result = "\tLD\t" + "EXP_TEMP" + ((ExpressionTempToken) singleArg).getTempTokenId() + "\r\n";
		else if (singleArg.getType() == TokenType.IDENTIFIER)
			result = "\tLD\t" + singleArg.getValue().toString() + "\r\n";
		else if (singleArg.getType() == TokenType.NUMERIC)
			result = "\tLV\t" + NumberConverter.convert(singleArg.getValue()) + "\r\n";

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

	private List<Token> groupArrayTokens(List<Token> expression) {
		List<Token> groupedTokens = new ArrayList<Token>();
		if (expression.size() < 4)
			return expression;
		for (int i = 0; i < expression.size() - 3; i++) {
			if (isIdentifier(expression.get(i)) && isOpenBracket(expression.get(i + 1))) {
				Token arrayToken = new ArrayToken(expression.get(i).getValue());
				i += 2;
				while (!isCloseBracket(expression.get(i))) {
					((ArrayToken) arrayToken).getArraySizeExpression().add(expression.get(i));
					i++;
				}
				i++;
				((ArrayToken) arrayToken).setArraySizeExpression(reversePoloneseTransformationImpl(((ArrayToken) arrayToken).getArraySizeExpression()));
				groupedTokens.add(arrayToken);
			} else {
				groupedTokens.add(expression.get(i));
			}
		}
		return groupedTokens;
	}

	private boolean isOpenBracket(Token token) {
		if (token.getType() == TokenType.OTHER && token.getValue() == (int) '[')
			return true;
		return false;
	}

	private boolean isCloseBracket(Token token) {
		if (token.getType() == TokenType.OTHER && token.getValue() == (int) ']')
			return true;
		return false;
	}

	private boolean isIdentifier(Token token) {
		if (token.getType() == TokenType.IDENTIFIER)
			return true;
		return false;
	}
}
