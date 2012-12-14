package compiler.semantic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import compiler.commons.IdentifierType;
import compiler.commons.SymbolTable;
import compiler.commons.Token;
import compiler.commons.TokenType;

public class SemanticActions {

	private static SemanticActions INSTANCE;

	private OperandsStack ods = OperandsStack.getInstance();
	private OperatorsStack ots = OperatorsStack.getInstance();
	private SymbolTable st = SymbolTable.getInstance();
	private CodeGenerator cg = CodeGenerator.getInstance();

	private int lastFunctionStacked;
	private Stack<Integer> whileStack = new Stack<Integer>();
	private int whileCounter = 0;
	private int functionParameterCounter = 0;
	private boolean isArray = false;

	private ExpressionProcessor expressionProcessor = ExpressionProcessor.getInstance();

	private SemanticActions() {

	}

	public static SemanticActions getIntance() {
		if (INSTANCE == null)
			INSTANCE = new SemanticActions();
		return INSTANCE;
	}

	public void processSemantic(Integer semanticId, Token token) {
		if (semanticId == null)
			return;

		switch (semanticId) {
		case 1:
			identifierTypeStack(token.getType());
			break;
		case 2:
			functionIdStack(token.getValue());
			break;
		case 3:
			functionParameterStack(token.getValue());
			break;
		case 4:
			associateVariableType(token.getValue());
			break;
		case 5:
			generateFunctionBaseCode();
			break;
		case 6:
			// TODO SE TOKEN NAO NUMERICO EXCEPTION
			arraySizeStack(token.getValue());
			break;
		case 7:
			generateVariableDeclaration();
			break;
		case 8:
			// caso usado em atribuicao e chamada de funcao
			identifierIdStack(token.getValue());
			break;
		case 9:
			// gerar codigo da expressao e atribuicao
		case 10:
			processExpression(token);
			break;
		case 11:
			finalizeExpression();
			break;
		case 12:
			isSimpleVarAttribution();
			break;
		case 13:
			isArrayVarAttribution();
			break;
		case 14:
			// gerar codigo atribuicao simples ( finalizar expressao )
			generateExpressionAttributionCode();
			break;
		case 15:
			// expressao parametro de uma funcao
			stackFunctionParameterExpression();
			break;
		case 16:
			generateFunctionCall();
			break;
		case 17:
			stackComparator(token);
			break;
		case 18:
			generateWhileCode();
			break;
		case 19:
			finalizeWhileCode();
			break;
		default:
			break;
		}
	}

	private void finalizeWhileCode() {
		int whileCount = whileStack.pop();
		cg.concat("\tJP\tWHILE_" + whileCount + "\r\n");
		cg.concat("END_WHILE_" + whileCount + "\tSC\tNOP\r\n\r\n");
	}

	private void generateWhileCode() {
		cg.concat("WHILE_" + whileCounter + "\tSC\tNOP\r\n");
		String exp1 = (String) ods.pop();
		String exp2 = (String) ods.pop();
		cg.concat(generateComparision(exp1, exp2, (Token) ots.pop(), whileCounter));
		cg.concat("\tJZ\tEND_WHILE_" + whileCounter + "\r\n\r\n");
		whileStack.push(whileCounter);
		whileCounter++;
	}

	private String generateComparision(String exp1, String exp2, Token operator, int counter) {
		StringBuilder sb = new StringBuilder();
		sb.append(exp1);
		sb.append("\tMM\tD_BUFFER\r\n");
		sb.append(exp2);
		sb.append("\t-\tD_BUFFER\r\n");

		sb.append(generateComparatorCode(operator, counter));

		sb.append("W_F_" + counter + "\tLD\tCONST_ZERO\r\n");
		sb.append("\tJP\tW_E_" + counter + "\r\n");
		sb.append("W_T_" + counter + "\tLD\tCONST_ONE\r\n");
		sb.append("\tJP\tW_E_" + counter + "\r\n");
		sb.append("W_E_" + counter + "\tMM\tD_BUFFER\r\n");
		return sb.toString();
	}

	private String generateComparatorCode(Token operator, int counter) {
		StringBuilder sb = new StringBuilder();
		if (operator.getType() == TokenType.OTHER) {
			switch (operator.getValue()) {
			case (int) '>':
				sb.append("\tJZ\tW_F_" + counter + "\r\n");
				sb.append("\tJN\tW_F_" + counter + "\r\n");
				sb.append("\tJP\tW_T_" + counter + "\r\n");
				break;
			case (int) '<':
				sb.append("\tJN\tW_T_" + counter + "\r\n");
				sb.append("\tJP\tW_F_" + counter + "\r\n");
				break;
			}
		} else if (operator.getType() == TokenType.GREATER_OR_EQUALS) {
			sb.append("\tJN\tW_F_" + counter + "\r\n");
			sb.append("\tJP\tW_T_" + counter + "\r\n");
		} else if (operator.getType() == TokenType.LESS_OR_EQUALS) {
			sb.append("\tJZ\tW_T_" + counter + "\r\n");
			sb.append("\tJN\tW_T_" + counter + "\r\n");
			sb.append("\tJP\tW_F_" + counter + "\r\n");
		} else if (operator.getType() == TokenType.EQUALS) {
			sb.append("\tJZ\tW_T_" + counter + "\r\n");
			sb.append("\tJP\tW_F_" + counter + "\r\n");
		} else if (operator.getType() == TokenType.DIFFERENT) {
			sb.append("\tJZ\tW_F_" + counter + "\r\n");
			sb.append("\tJP\tW_T_" + counter + "\r\n");
		}

		return sb.toString();
	}

	private void stackComparator(Token token) {
		ots.push(token);
	}

	private void generateFunctionCall() {
		List<String> parameterExpressions = new ArrayList<String>();
		while (functionParameterCounter > 0) {
			parameterExpressions.add((String) ods.pop());
			functionParameterCounter--;
		}
		Collections.reverse(parameterExpressions);
		int i = 0;
		for (String string : parameterExpressions) {
			cg.concat(string);
			cg.concat("\tMM\t" + lastFunctionStacked + "_" + i + "\r\n");
			i++;
		}
		cg.concat("\tSC\t" + lastFunctionStacked + "\r\n\r\n");

	}

	private void stackFunctionParameterExpression() {
		finalizeExpression();
		functionParameterCounter++;
	}

	private void generateExpressionAttributionCode() {
		finalizeExpression();
		cg.concat((String) ods.pop());
		if (isArray) {
			cg.concat("\tMM\tVALUE_W\r\n");
			cg.concat((String) ods.pop());
			cg.concat("\tMM\tINDEX_W\r\n");
			cg.concat("\tLD\t" + ods.pop() + "_P\r\n");
			cg.concat("\tMM\tARRAY_W\r\n");
			cg.concat("\tSC\tWRITE_ARRAY\r\n\r\n");
		} else {
			cg.concat("\tMM\t" + ods.pop() + "\r\n\r\n");
		}
	}

	private void isArrayVarAttribution() {
		isArray = true;
		finalizeExpression();
	}

	private void isSimpleVarAttribution() {
		isArray = false;
	}

	private void processExpression(Token token) {
		expressionProcessor.concatToken(token);
	}

	private void finalizeExpression() {
		expressionProcessor.reversePoloneseTransformation();
		ods.push(expressionProcessor.generateExpressionCode());
		expressionProcessor.restart();
	}

	private void identifierIdStack(int identifierId) {
		ods.push(identifierId);
		lastFunctionStacked = identifierId;
	}

	private void identifierTypeStack(TokenType tokenType) {
		ods.push(getTypeEnum(tokenType));
	}

	private void functionIdStack(int identifierId) {
		IdentifierType poppedType = (IdentifierType) ods.pop();
		st.getElementById(identifierId).setType(poppedType);
		st.getElementById(identifierId).setFunction(true);
		ots.push(identifierId);
		// lastFunctionStacked = identifierId;
	}

	private void functionParameterStack(int identifierId) {
		IdentifierType poppedType = (IdentifierType) ods.pop();
		st.getElementById(identifierId).setType(poppedType);
		st.getElementById(identifierId).setFunctionParameter(true);
		// st.getElementById(identifierId).setOwnerFunction(lastFunctionStacked);
		st.getElementById(identifierId).setParameterOrder(functionParameterCounter);
		ods.push(identifierId);
		functionParameterCounter++;
	}

	private void generateFunctionBaseCode() {
		List<Integer> parameters = new ArrayList<Integer>();
		while (functionParameterCounter > 0) {
			parameters.add((Integer) ods.pop());
			functionParameterCounter--;
		}
		cg.addFunction((Integer) ots.peek(), parameters);
		cg.concat((Integer) ots.pop() + "\tJP\t/0000\r\n");
	}

	private void associateVariableType(int identifierId) {
		IdentifierType poppedType = (IdentifierType) ods.pop();
		st.getElementById(identifierId).setType(poppedType);
		ods.push(identifierId);
	}

	private void arraySizeStack(int arraySize) {
		ods.push(arraySize);
		isArray = true;
	}

	private void generateVariableDeclaration() {
		if (isArray) {
			int arraySize = (Integer) ods.pop();
			int identifierId = (Integer) ods.pop();
			isArray = false;
			// TODO SE ARRAYSIZE <= 0 EXCECAO
			cg.addArrayVariable(identifierId, arraySize);
			st.getElementById(identifierId).setArray(true);
		} else {
			int identifierId = (Integer) ods.pop();
			cg.addSimpleVariable(identifierId);
		}

	}

	private IdentifierType getTypeEnum(TokenType type) {
		switch (type) {
		case KW_BOOL:
			return IdentifierType.BOOL;
		case KW_INT:
			return IdentifierType.INTEGER;
		case KW_VOID:
			return IdentifierType.VOID;
		}
		return null;
	}

}
