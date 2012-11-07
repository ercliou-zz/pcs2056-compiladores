package compiler.syntax;

import compiler.commons.State;

public class SyntaxState implements State{

	private Integer state;
	private boolean lookAhead;
	private Integer semanticAction;
	
	public SyntaxState(Integer state, boolean lookAhead, Integer semanticAction) {
		this.state = state;
		this.lookAhead = lookAhead;
		this.semanticAction = semanticAction;
	}

	@Override
	public int getState() {
		return state;
	}

	public boolean hasLookAhead() {
		return lookAhead;
	}

	public int getSemanticAction() {
		return semanticAction;
	}
	
	

}
