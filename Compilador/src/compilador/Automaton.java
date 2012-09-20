package compilador;

import java.util.Set;

public class Automaton {
	private State actualState;
	private Set<State> states;
	private String source;

	public Automaton(int initialState, Set<State> states) {
		this.states = states;
		this.actualState = getState(initialState);
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void step() {
		if (source.length() > 0) {
			char input = extractCharacter();
			actualState = getState(actualState.nextState(input));
		} else {
			throw new RuntimeException("A cadeira de entrada não possui mais símbolos");
		}
	}

	public State getActualState() {
		return actualState;
	}

	private State getState(int stateId) {
		for (State state : states) {
			if (state.getId() == stateId) {
				return state;
			}
		}
		throw new RuntimeException("Estado com id inexistente.");
	}

	private char extractCharacter() {
		char extracted = source.charAt(0);
		source = source.substring(1);
		return extracted;
	}
}
