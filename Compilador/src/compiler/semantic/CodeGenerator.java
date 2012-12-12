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
		// TODO HERE
		return env.toString();
	}

}
