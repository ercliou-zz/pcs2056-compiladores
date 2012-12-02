package compiler.semantic;

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

	private int functionParameterCounter = 0;
	private boolean isArray = false;

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
		default:
			break;
		}
	}

	private void identifierTypeStack(TokenType tokenType) {
		ods.push(getTypeEnum(tokenType));
		System.err.println("passou");
	}

	private void functionIdStack(int identifierId) {
		IdentifierType poppedType = (IdentifierType) ods.pop();
		st.getElementById(identifierId).setType(poppedType);
		ots.push(identifierId);
	}

	private void functionParameterStack(int identifierId) {
		IdentifierType poppedType = (IdentifierType) ods.pop();
		st.getElementById(identifierId).setType(poppedType);
		ods.push(identifierId);
		functionParameterCounter++;
	}

	private void generateFunctionBaseCode() {
		while (functionParameterCounter > 0) {
			String p = "P_" + ots.peek() + "_" + ods.pop();
			p += "\t$\t/0001\n";
			cg.concat(p);
			functionParameterCounter--;
		}
		cg.concat(ots.pop() + "\tK\t/0000\n");
		// System.out.println(cg.print());
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
			int functionId = (Integer) ods.pop();
			isArray = false;

			// TODO SE ARRAYSIZE <= 0 EXCECAO

			cg.concat(functionId + "\tK\t/0000\n");
			while (arraySize > 0) {
				cg.concat("\tK\t/0000\n");
			}
			cg.concat(functionId + "_P\tK\t" + functionId + "\n");
		} else {
			int functionId = (Integer) ods.pop();
			cg.concat(functionId + "\tK\t/0000\n");
		}
		
		System.out.println(cg.print());
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
