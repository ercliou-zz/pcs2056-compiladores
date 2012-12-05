package compiler.semantic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	private int functionParameterCounter = 0;
	private boolean isArray = false;
	private boolean functionToVariable = false;

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
		default:
			break;
		}
	}

	private void generateFunctionCall() {
		List<String> parameterExpressions = new ArrayList<String>();
		while (functionParameterCounter > 0) {
			parameterExpressions.add((String) ods.pop());
			functionParameterCounter--;
		}
		Collections.reverse(parameterExpressions);
		int i=0;
		for (String string : parameterExpressions) {
			cg.concat(string);
			cg.concat("\tMM\t" + lastFunctionStacked + "_" + i + "\n");
			i++;
		}

	}

	private void stackFunctionParameterExpression() {
		finalizeExpression();
		functionParameterCounter++;
	}

	private void generateExpressionAttributionCode() {
		finalizeExpression();
		cg.concat((String) ods.pop());
		if (isArray) {
			cg.concat("\tMM\tVALUE_W\n");
			cg.concat((String) ods.pop());
			cg.concat("\tMM\tINDEX_W\n");
			cg.concat("\tLD\t" + ods.pop() + "_P\n");
			cg.concat("\tMM\tARRAY_W\n");
			cg.concat("\tSC\tWRITE_ARRAY\n\n");
		} else {
			cg.concat("\tMM\t" + ods.pop() + "\n\n");
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
//		lastFunctionStacked = identifierId;
	}

	private void identifierTypeStack(TokenType tokenType) {
		ods.push(getTypeEnum(tokenType));
	}

	private void functionIdStack(int identifierId) {
		IdentifierType poppedType = (IdentifierType) ods.pop();
		st.getElementById(identifierId).setType(poppedType);
		st.getElementById(identifierId).setFunction(true);
		ots.push(identifierId);
//		lastFunctionStacked = identifierId;
	}

	private void functionParameterStack(int identifierId) {
		IdentifierType poppedType = (IdentifierType) ods.pop();
		st.getElementById(identifierId).setType(poppedType);
		st.getElementById(identifierId).setFunctionParameter(true);
//		st.getElementById(identifierId).setOwnerFunction(lastFunctionStacked);
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
		cg.concat((Integer) ots.pop() + "\tJP\t/0000\n");
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
