package compiler.commons;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Automaton<C> {
	protected String name;
	protected State state;
	protected C actualElement;
	protected AutomatonTransitionsTable<C> transitions;
	protected int statesQuantity;
	protected Queue<C> string = new LinkedList<C>();
	protected Set<Integer> finalStates;
	protected State initialState;

	public Automaton(AutomatonTransitionsTable<C> transitions, int statesQuantity, State initialState, Set<Integer> finalStates) {
		this.state = initialState;
		this.initialState = initialState;
		this.transitions = transitions;
		this.statesQuantity = statesQuantity;
		this.finalStates = finalStates;
	}

	public Automaton(AutomatonTransitionsTable<C> transitions, int statesQuantity, int initialState, Set<Integer> finalStates) {
		this(transitions, statesQuantity, new SimpleState(initialState), finalStates);
	}

	public void setString(List<C> string) {
		this.string = new LinkedList<C>();
		for (C c : string) {
			string.add(c);
		}
	}

	public void setString(C string) {
		this.string = new LinkedList<C>();
		this.string.add(string);
	}

	public int getActualState() {
		return state.getState();
	}

	public void step() {
		if (!isStringEmpty()) {
			actualElement = string.remove();
			state = transitions.get(state.getState(), actualElement);
		} else {
			throw new RuntimeException("Uma tentativa de passo do automato falhou pois a cadeia está vazia.");
		}
	}

	public boolean isStringEmpty() {
		return string.isEmpty();
	}

	public boolean isComplete() {
		return finalStates.contains(state.getState());
	}

	public void resetAutomaton() {
		state = initialState;
	}

	public boolean hasTransition(C consumable) {
		return transitions.get(state.getState(), consumable) != null;
	}

	public Set<C> getPossibleTransitions() {
		return transitions.getPossibleConsumables(state.getState());
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
