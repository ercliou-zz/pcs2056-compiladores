package compiler.syntax;

import compiler.commons.State;

public class SyntaxState implements State{

	private Integer state;
	private boolean lookAhead;
	private Integer semanticActionId;
	
	public SyntaxState(Integer state, boolean lookAhead, Integer semanticActionId) {
		this.state = state;
		this.lookAhead = lookAhead;
		this.semanticActionId = semanticActionId;
	}

	@Override
	public int getState() {
		return state;
	}

	public boolean hasLookAhead() {
		return lookAhead;
	}

	public int getSemanticActionId() {
		return semanticActionId;
	}
	
	

}
