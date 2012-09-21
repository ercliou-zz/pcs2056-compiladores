package compilador.lexico;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import compilador.Automaton;
import compilador.LexicalAnalyser;
import compilador.State;

public class LexicalAnalyserImpl implements LexicalAnalyser {

	private Automaton automaton;

	public LexicalAnalyserImpl() {
		Set<State> states = new HashSet<State>();

		// ESTADO 1
		Map<String, Integer> transitions = new HashMap<String, Integer>();
		transitions.put("[\n \t]", 1);
		transitions.put("[0-9]", 2);
		transitions.put("[a-zA-Z]", 3);
		transitions.put(">", 4);
		transitions.put("<", 6);
		transitions.put("=", 8);
		transitions.put("!", 10);
		transitions.put("/", 12);
		transitions.put("[^\n \t0-9a-zA-Z><=!/]", 14);
		states.add(new State(1, transitions, false));

		// ESTADO 2
		transitions = new HashMap<String, Integer>();
		transitions.put("[0-9]", 2);
		transitions.put("[^0-9]", 0);
		states.add(new State(2, transitions, false));

		// ESTADO 3
		transitions = new HashMap<String, Integer>();
		transitions.put("[a-zA-Z0-9]", 3);
		transitions.put("[^a-zA-Z0-9]", 0);
		states.add(new State(3, transitions, false));

		// ESTADO 4
		transitions = new HashMap<String, Integer>();
		transitions.put("=", 5);
		states.add(new State(4, transitions, false));

		// ESTADO 5
		transitions = new HashMap<String, Integer>();
		transitions.put(".", 0);
		states.add(new State(5, transitions, false));

		// ESTADO 6
		transitions = new HashMap<String, Integer>();
		transitions.put("=", 7);
		states.add(new State(6, transitions, false));

		// ESTADO 7
		transitions = new HashMap<String, Integer>();
		transitions.put(".", 0);
		states.add(new State(7, transitions, false));

		// ESTADO 8
		transitions = new HashMap<String, Integer>();
		transitions.put("=", 9);
		states.add(new State(8, transitions, false));

		// ESTADO 9
		transitions = new HashMap<String, Integer>();
		transitions.put(".", 0);
		states.add(new State(9, transitions, false));

		// ESTADO 10
		transitions = new HashMap<String, Integer>();
		transitions.put("=", 11);
		states.add(new State(10, transitions, false));

		// ESTADO 11
		transitions = new HashMap<String, Integer>();
		transitions.put(".", 0);
		states.add(new State(11, transitions, false));

		// ESTADO 12
		transitions = new HashMap<String, Integer>();
		transitions.put("/", 13);
		states.add(new State(12, transitions, false));

		// ESTADO 13
		transitions = new HashMap<String, Integer>();
		transitions.put("[^\n]", 13);
		transitions.put("[\n]", 1);
		states.add(new State(13, transitions, false));

		// ESTADO 14
		transitions = new HashMap<String, Integer>();
		transitions.put(".", 0);
		states.add(new State(14, transitions, false));

		// ESTADO 0
		transitions = new HashMap<String, Integer>();
		states.add(new State(0, transitions, true));

		automaton = new Automaton(1, states);
	}

	public LexicalResult analyse(SymbolTable symbolTable, String sourceText,
			int cursor) {
		automaton.resetAutomaton();
		int stepCount = 0;
		int tokenBegin = 0;
		int stateBuffer = automaton.getActualState().getId();
		automaton.setSource(sourceText.substring(cursor));
		while (!automaton.completed()
				&& automaton.getActualState().getId() != 0) {
			if (automaton.getActualState().getId() != 1 && stateBuffer == 1) {
				tokenBegin = stepCount - 1;
			}
			stateBuffer = automaton.getActualState().getId();
			automaton.step();
			stepCount++;
		}
		String tokenString = sourceText.substring(cursor + tokenBegin, cursor
				+ stepCount - 1);
		TokenType type = classify(stateBuffer, tokenString);
		Integer value = null;
		if (type != null) {
			if (type.equals(TokenType.IDENTIFIER)) {
				if (!symbolTable.contains(tokenString)) {
					value = symbolTable.put(tokenString);
				} else {
					value = symbolTable.get(tokenString);
				}
			} else if (type.equals(TokenType.NUMERIC)) {
				value = Integer.parseInt(tokenString);
			} else if (type.equals(TokenType.OTHER)) {
				value = (int) tokenString.charAt(0);
			}

			LexicalResult result = new LexicalResult();
			result.setCursor(cursor + stepCount - 1);
			result.setToken(new Token(value, type));
			return result;
		}
		return null;
	}

	private TokenType classify(int lastState, String tokenString) {
//		System.out.println("Classifying: Last State=" + lastState
//				+ " Token String=" + tokenString);
		switch (lastState) {
			case 2 :
				return TokenType.NUMERIC;
			case 3 :
				if (TokenType.isKeyword(tokenString)) {
					return TokenType.getKeywordEnum(tokenString);
				}
				return TokenType.IDENTIFIER;
			case 5 :
				return TokenType.GREATER_OR_EQUALS;
			case 7 :
				return TokenType.LESS_OR_EQUALS;
			case 9 :
				return TokenType.EQUALS;
			case 11 :
				return TokenType.DIFFERENT;
			case 14 :
				return TokenType.OTHER;
		}
		return null;
	}

}
