package compilador.lexico;


public enum TokenType {
	NUMERIC, IDENTIFIER, GREATER_OR_EQUALS, LESS_OR_EQUALS, EQUALS, DIFFERENT, OTHER, STRING,
	KW_BREAK("break"), KW_CHAR("char"), KW_CONST("const"), KW_DOUBLE("double"), KW_ELSE("else"), KW_FLOAT("float"), KW_WHILE("while"), KW_IF("if"), KW_INT("int"), KW_LONG("long"), KW_RETURN("return"), KW_STATIC("static"), KW_VOID("void");
	
	private boolean isKeyword;
	private String keyword;
	
	private TokenType(){
		isKeyword = false;
	}
	
	private TokenType(String keyword){
		isKeyword = true;
		this.keyword = keyword;
	}
	
	public boolean isKeyword(){
		return isKeyword;
	}
	
	public boolean equals(String keyword){
		return this.keyword.equals(keyword);
	}
	
	public static TokenType getKeywordEnum(String keyword){
		for (TokenType type : values()) {
			if(type.isKeyword() && type.equals(keyword)){
				return type;
			}
		}
		return null;
	}
	
	public static boolean isKeyword(String keyword){
		for (TokenType type : values()) {
			if(type.isKeyword() && type.equals(keyword)){
				return true;
			}
		}
		return false;
	}
}
