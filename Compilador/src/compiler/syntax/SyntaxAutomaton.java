package compiler.syntax;

import java.util.Set;

import compiler.Token;
import compiler.commons.Automaton;
import compiler.commons.AutomatonTransitionsTable;

public class SyntaxAutomaton extends Automaton<Token>{

	public SyntaxAutomaton(AutomatonTransitionsTable<Token> transitions, int statesQuantity, int initialState, Set<Integer> finalStates) {
		super(transitions, statesQuantity, initialState, finalStates);
	}
	
	public boolean hasLookAhead(){
		if(SyntaxState.class.isAssignableFrom(state.getClass())){
			return ((SyntaxState) state).hasLookAhead();
		}
		return false;
	}
	
	public SyntaxAutomaton clone() {
		SyntaxAutomaton aut = new SyntaxAutomaton(transitions, statesQuantity, initialState.getState(), finalStates);
		aut.setName(name);
		return aut;
	}

}
