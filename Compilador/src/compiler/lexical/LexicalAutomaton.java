package compiler.lexical;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import compiler.commons.Automaton;

public class LexicalAutomaton extends Automaton<String> {

	protected Queue<String> consumedString;
	protected Set<Integer> ignoredStates;

	public LexicalAutomaton(LexicalAutomatonTransitionsTable transitions,
			int statesQuantity, int initialState, Set<Integer> finalStates,
			Set<Integer> ignoredStates) {
		super(transitions, statesQuantity, initialState, finalStates);
		this.consumedString = new LinkedList<String>();
		this.ignoredStates = ignoredStates;
	}

	public void setString(String string) {
		Queue<String> result = new LinkedList<String>();
		for (int i = 0; i < string.length(); i++) {
			result.add(string.substring(i, i + 1));
		}
		this.string = result;
	}

	public Integer peekNextState() {
		return transitions.get(state, string.peek());
	}

	public String getConsumedStringFormat() {
		String result = "";
		for (String character : consumedString) {
			result += character.charAt(0);
		}
		return result;
	}

	public Queue<String> getConsumedString() {
		return consumedString;
	}

	@Override
	public void step() {
		if (!isStringEmpty()) {
			String stringAtom = string.remove();
			state = transitions.get(state, stringAtom);
			// ignora os caracteres no estado inicial e nos estados referentes à
			// comentários
			if (!ignoredStates.contains(state)) {
				consumedString.add(stringAtom);
			}
		} else {
			throw new RuntimeException(
					"Uma tentativa de passo do automato falhou pois a cadeia está vazia.");
		}
	}

	@Override
	public void resetAutomaton() {
		super.resetAutomaton();
		this.consumedString = new LinkedList<String>();
	}

}
