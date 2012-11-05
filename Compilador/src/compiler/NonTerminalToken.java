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
	
	public static NonTerminalToken ProgramToken(){
		return new NonTerminalToken(NonTerminalTokenType.PROGRAM);
	}
	public static NonTerminalToken FunctionToken(){
		return new NonTerminalToken(NonTerminalTokenType.FUNCTION);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((automatonId == null) ? 0 : automatonId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NonTerminalToken other = (NonTerminalToken) obj;
		if (automatonId != other.automatonId)
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return "[ " + automatonId + " ]";
	}

//	public static NonTerminalToken ProgramToken() {
//		return new NonTerminalToken(NonTerminalTokenType.PROGRAMA);
//	}
//
//	public static NonTerminalToken ProgramToken() {
//		return new NonTerminalToken(NonTerminalTokenType.PROGRAMA);
//	}
//
//	public static NonTerminalToken ProgramToken() {
//		return new NonTerminalToken(NonTerminalTokenType.PROGRAMA);
//	}
//
//	public static NonTerminalToken ProgramToken() {
//		return new NonTerminalToken(NonTerminalTokenType.PROGRAMA);
//	}
	
	
}
