package compiler.semantic;

import java.util.List;

public class CodeGenerator {

	private static CodeGenerator INSTANCE;

	private StringBuilder sb;
	private StringBuilder arrayVariablesSB;
	private StringBuilder simpleVariablesSB;
	private StringBuilder functionParametersSB;

	private CodeGenerator() {
		sb = new StringBuilder();
		arrayVariablesSB = new StringBuilder();
		simpleVariablesSB = new StringBuilder();
		functionParametersSB = new StringBuilder();
	}

	public static CodeGenerator getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CodeGenerator();
		return INSTANCE;
	}

	public void concat(String string) {
		sb.append(string);
	}

	public void addSimpleVariable(int identifierId) {
		simpleVariablesSB.append(identifierId + "\tK\t/0000\n");
	}

	public void addArrayVariable(int identifierId, int size) {
		arrayVariablesSB.append(identifierId + "\tK\t/0000\n");
		for (int i = 0; i < size - 1; i++) {
			arrayVariablesSB.append("\tK\t/0000\n");
		}
		arrayVariablesSB.append(identifierId + "_P\tK\t" + identifierId + "\n\n");
	}

	public void addFunction(int identifierId, List<Integer> params) {
		for (int i = 0; i < params.size(); i++) {
			functionParametersSB.append("P_" + identifierId + "_" + i);
			functionParametersSB.append("\tK\t/0000\n");
		}
		//functionParametersSB.append("\n");
		// System.out.println(cg.print());
	}

	public String print() {
		return sb.append(generateEnvironment()).toString();
	}

	public String generateEnvironment() {
		StringBuilder env = new StringBuilder();
		env.append("\tHM\t/0000\n\n");

		env.append("CONST_ZERO\tK\t/0000\n");
		env.append("CONST_ONE\tK\t/0001\n");
		env.append("CONST_TWO\tK\t/0002\n\n");

		env.append(generateReadFromArrayFunction());
		env.append(generateWriteToArrayFunction());
		env.append(generateArrayFunctionsVariables());
		env.append(generateFunctionParameters());
		env.append(generateIOFunctions());

		env.append(simpleVariablesSB.toString()).append("\n");
		env.append(arrayVariablesSB.toString());

		for (int i = 0; i < ExpressionProcessor.getInstance().getTempsNumber(); i++) {
			env.append("EXP_TEMP" + i + "\tK\t/0000\n");
		}
		return env.toString();
	}

	private String generateReadFromArrayFunction() {
		StringBuilder env = new StringBuilder();
		env.append("ACCESS_ARRAY\tJP\t/0000\n");
		env.append("\tLD\tINDEX_R\n");
		env.append("\t*\tCONST_TWO\n");
		env.append("\t+\tARRAY_R\n");
		env.append("\t+\tLOAD\n");
		env.append("\tMM\tACCESS_ARRAY_\n");
		env.append("ACCESS_ARRAY_\tMM\t/0000\n");
		env.append("\tRS\tACCESS_ARRAY\n\n");
		return env.toString();
	}

	private String generateWriteToArrayFunction() {
		StringBuilder env = new StringBuilder();
		env.append("WRITE_ARRAY\tJP\t/0000\n");
		env.append("\tLD\tINDEX_W\n");
		env.append("\t*\tCONST_TWO\n");
		env.append("\t+\tARRAY_W\n");
		env.append("\t+\tWRITE\n");
		env.append("\tMM\tWRITE_ARRAY_\n");
		env.append("\tLD\tVALUE_W\n");
		env.append("WRITE_ARRAY_\tMM\t/0000\n");
		env.append("\tRS\tWRITE_ARRAY\n\n");
		return env.toString();
	}

	private String generateArrayFunctionsVariables() {
		StringBuilder env = new StringBuilder();
		env.append("ARRAY_R\tK\t/0000\n");
		env.append("INDEX_R\tK\t/0000\n");
		env.append("ARRAY_W\tK\t/0000\n");
		env.append("INDEX_W\tK\t/0000\n");
		env.append("VALUE_W\tK\t/0000\n");
		env.append("LOAD\tLD\t/0000\n");
		env.append("WRITE\tMM\t/0000\n\n");
		return env.toString();
	}

	private String generateFunctionParameters() {
		return functionParametersSB.toString();
	}
	
	private String generateIOFunctions(){
		StringBuilder env = new StringBuilder();
		env.append("HIGH_BYTE K /0000  \r\nLOW_BYTE  K /0000  \r\nCONVERTED_VALUE K /0000  \r\nIS_NEGATIVE K /0000  \r\nBYTE_READ K /0000  \r\nMINUS_CHAR  K /2D  \r\nSHIFT_TWO K /100 \r\nMVN_READ  JP  /000 \r\n  LV  =0  \r\n  MM  CONVERTED_VALUE  \r\n  LV  =0 \r\n  MM  IS_NEGATIVE \r\n  GD  /000 \r\n  MM  BYTE_READ\r\n  SC  SPLIT_WORD\r\n  LD  HIGH_BYTE  \r\n -  MINUS_CHAR \r\n *  MINUS_ONE_INTERNAL \r\n  JN  MR_START \r\n  LV  =1 \r\n  MM  IS_NEGATIVE\r\n  LD  LOW_BYTE\r\n  SC  UPDATE_CONVERTED \r\nLOOP_READ GD  /000  \r\n  MM  BYTE_READ\r\n  SC  SPLIT_WORD  \r\nMR_START  LD  HIGH_BYTE\r\n  SC  UPDATE_CONVERTED\r\n  LD  LOW_BYTE\r\n  SC  UPDATE_CONVERTED\r\n  JP  LOOP_READ\r\nFINISH_READ LD  IS_NEGATIVE\r\n  JZ  RETURN_READ\r\n  LD  CONVERTED_VALUE\r\n *  MINUS_ONE_INTERNAL\r\n  RS  MVN_READ\r\nRETURN_READ LD  CONVERTED_VALUE\r\n  RS  MVN_READ\r\nUPDATE_CONVERTED  JP  /000 \r\n  MM  BYTE_READ  \r\n  LV  /00A \r\n  - BYTE_READ  \r\n  JZ  FINISH_READ  \r\n  LV  /00D \r\n  - BYTE_READ  \r\n  JZ  FINISH_READ\r\n  \r\n  \r\n  LV  =10\r\n  * CONVERTED_VALUE \r\n  MM  CONVERTED_VALUE\r\n  SC  ASCII_TO_INT  \r\n  + CONVERTED_VALUE\r\n  MM  CONVERTED_VALUE  \r\n  RS  UPDATE_CONVERTED\r\nSPLIT_WORD  JP  /0000 \r\n /  SHIFT_TWO\r\n  MM  HIGH_BYTE\r\n  LD  BYTE_READ\r\n *  SHIFT_TWO\r\n /  SHIFT_TWO\r\n  MM  LOW_BYTE\r\n  RS  SPLIT_WORD\r\n  \r\nINT_VALUE K =0000  \r\nASCII_ZERO  K =48  \r\nNINE  K =9 \r\nASCII_TO_INT  JP  /0000  \r\n  LV  =0\r\n  MM  INT_VALUE\r\n  LD  BYTE_READ\r\n -  ASCII_ZERO\r\n  MM  INT_VALUE\r\n  JN  ERRO_ASCII_TO_INT\r\n  LD  NINE\r\n -  INT_VALUE\r\n  JN  ERRO_ASCII_TO_INT\r\n  LD  INT_VALUE\r\n  RS  ASCII_TO_INT\r\nERRO_ASCII_TO_INT LV  =0\r\n  RS  ASCII_TO_INT\r\n  \r\nRESTO_DIVISAO K =0 \r\nQUOTIENT  K =0 \r\nTEN K =10  \r\nQUOTIENT_TIMES_POW  K =0 \r\nMINUS_ONE_INTERNAL  K /FFFF  \r\nPOW K =0 \r\nMVN_PRINT JP  /0000  \r\n  MM  INT_VALUE\r\n  JZ  PRINT_ZERO\r\n  LV  =1  \r\n  MM  POW\r\n  ; Verifica se e negativo\r\n  LD  INT_VALUE\r\n  * MINUS_ONE_INTERNAL  \r\n  JN  FIND_POW_LOOP \r\n  MM  INT_VALUE \r\n  LV  /2D \r\n  PD  /100  \r\nFIND_POW_LOOP LD  INT_VALUE\r\n  - POW\r\n  JZ  LOOP_IMPRESSAO\r\n  JN  END_FIND_POW\r\n  LD  POW\r\n  * TEN\r\n  MM  POW\r\n  JP  FIND_POW_LOOP\r\nEND_FIND_POW  LD  POW\r\n  / TEN\r\n  MM  POW\r\nLOOP_IMPRESSAO  LD  INT_VALUE\r\n  / POW\r\n  MM  QUOTIENT\r\n  * POW\r\n  MM  QUOTIENT_TIMES_POW\r\n  LD  INT_VALUE\r\n  - QUOTIENT_TIMES_POW \r\n  MM  INT_VALUE \r\n  LD  QUOTIENT\r\n  + ASCII_ZERO\r\n  PD  /100\r\n  LD  POW\r\n  / TEN\r\n  MM  POW\r\n  * MINUS_ONE_INTERNAL\r\n  JN  LOOP_IMPRESSAO \r\n  RS  MVN_PRINT\r\nPRINT_ZERO  LV  /30  \r\n  PD  /100\r\n  RS  MVN_PRINT\r\nMVN_PRINTC  JP  /000\r\n  PD  /100 \r\n  RS  MVN_PRINTC\r\nMVN_PRINTB  JP  /000\r\n  JZ  PRINTB_FALSE\r\n  LV  /46 ; F\r\n  PD  /100\r\n  LV  /41 ; A\r\n  PD  /100\r\n  LV  /4C ; L\r\n  PD  /100  \r\n  LV  /53 ; S\r\n  PD  /100  \r\n  LV  /45 ; E\r\n  PD  /100  \r\nPRINTB_FALSE  LV  /54 ; T\r\n  PD  /100\r\n  LV  /52 ; R\r\n  PD  /100\r\n  LV  /55 ; U\r\n  PD  /100  \r\n  LV  /45 ; E\r\n  RS  MVN_PRINTB");
		return env.toString();
	}

}
