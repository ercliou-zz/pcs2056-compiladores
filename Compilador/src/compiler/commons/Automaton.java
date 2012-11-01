package compiler.commons;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Automaton<C> {
	protected int state;
	protected AutomatonTransitionsTable<C> transitions;
	protected int statesQuantity;
	protected Queue<C> string = new LinkedList<C>();
	protected Set<Integer> finalStates;
	protected int initialState;

	public Automaton(AutomatonTransitionsTable<C> transitions,
			int statesQuantity, int initialState, Set<Integer> finalStates) {
		this.state = initialState;
		this.initialState = initialState;
		this.transitions = transitions;
		this.statesQuantity = statesQuantity;
		this.finalStates = finalStates;
	}

	public void setString(List<C> string) {
		for (C c : string) {
			string.add(c);
		}
	}

	public int getActualState() {
		return state;
	}

	public void step() {
		C stringAtom = string.remove();
		if (!isStringEmpty()) {
			state = transitions.get(state, stringAtom);
		} else {
			throw new RuntimeException(
					"Uma tentativa de passo do automato falhou pois a cadeia está vazia.");
		}
	}
	
	public boolean isStringEmpty() {
		return string.isEmpty();
	}

	public boolean isComplete() {
		return finalStates.contains(state);
	}
	
	public void resetAutomaton(){
		state = initialState;
	}
	
}
