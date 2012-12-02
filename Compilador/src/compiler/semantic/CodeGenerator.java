package compiler.semantic;

public class CodeGenerator {

	private static CodeGenerator INSTANCE;

	private CodeGenerator() {
		sb = new StringBuilder();
	}

	public static CodeGenerator getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CodeGenerator();
		return INSTANCE;
	}

	private StringBuilder sb;

	public void concat(String string) {
		sb.append(string);
	}
	
	public String print(){
		return sb.toString();
	}
}
