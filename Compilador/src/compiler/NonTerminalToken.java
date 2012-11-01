package compiler;

import compiler.syntax.NonTerminalTokenType;

public class NonTerminalToken extends Token{

	private NonTerminalTokenType automatonId;
	
	public NonTerminalToken(NonTerminalTokenType automatonId) {
		super(null, null);
		this.automatonId = automatonId;
	}

	public NonTerminalTokenType getAutomatonId(){
		return automatonId;
	}
	
}
