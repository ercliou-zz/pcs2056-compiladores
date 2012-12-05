package compiler.syntax;

import java.util.Set;

import compiler.commons.Automaton;
import compiler.commons.AutomatonTransitionsTable;
import compiler.commons.Token;
import compiler.semantic.OperandsStack;
import compiler.semantic.OperatorsStack;
import compiler.semantic.SemanticActions;

public class SyntaxAutomaton extends Automaton<Token> {

	public SyntaxAutomaton(AutomatonTransitionsTable<Token> transitions, int statesQuantity, int initialState, Set<Integer> finalStates) {
		super(transitions, statesQuantity, initialState, finalStates);
	}

	@Override
	public void step() {
		super.step();
		SemanticActions.getIntance().processSemantic(getSemanticActionId(), actualElement);
		System.out.println("====== ACAO CHAMADA: " + getSemanticActionId() + " " + OperatorsStack.getInstance().getSize() + " " + OperandsStack.getInstance().getSize() + " " + OperandsStack.getInstance().peek());
	}

	public boolean hasLookAhead() {
		if (SyntaxState.class.isAssignableFrom(state.getClass())) {
			return ((SyntaxState) state).hasLookAhead();
		}
		return false;
	}

	public Integer getSemanticActionId() {
		if (SyntaxState.class.isAssignableFrom(state.getClass())) {
			return ((SyntaxState) state).getSemanticActionId();
		}
		return null;
	}

	public SyntaxAutomaton clone() {
		SyntaxAutomaton aut = new SyntaxAutomaton(transitions, statesQuantity, initialState.getState(), finalStates);
		aut.setName(name);
		return aut;
	}

}
