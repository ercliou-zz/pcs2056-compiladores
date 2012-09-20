package compilador.lexico;

import java.util.HashSet;

public class Keywords extends HashSet<String>{

	private static final long serialVersionUID = 1L;

	private static Keywords instance = new Keywords();
	
	private Keywords() {
		add("break");
		add("char");
		add("const");
		add("double");
		add("else");
		add("float");
		add("while");
		add("if");
		add("int");
		add("long");
		add("return");
		add("static");
		add("void");
	}

	public static Keywords getInstance() {
		return instance;
	}

	public boolean isKeyword(char [] word){
		for (String key : instance) {
			if((new String(word)).equals(key)){
				return true;
			}
		}
		return false;
	}
	
}
