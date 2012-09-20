package compilador.lexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import compilador.Automaton;
import compilador.LexicalAnalyser;
import compilador.State;

public class LexicalAnalyserImpl implements LexicalAnalyser{

	private Automaton automaton;

	public LexicalAnalyserImpl() {
		List<State> states = new ArrayList<State>();

		Map<String,Integer> transitions = new HashMap<String,Integer>();
		transitions.put("[0-9]",2);
		transitions.put("[a-zA-Z]", 3);
		transitions.put(">", 4);
		transitions.put("<", 6);
		transitions.put("=",8);
		transitions.put("!", 10);
		transitions.put("/", 12);
		transitions.put(".", 14);
		states.add(new State(1, transitions, false));
		
		
		
		this.automaton = automaton;
	}

	public LexicalResult analyse(SymbolTable symbolTable, String sourceText, int cursor) {
		
		return null;
	}

//	private boolean isBlank(char character) {
//		if (character == ' ' || character == '\t') {
//			return true;
//		}
//		return false;
//	}
//
//	private boolean isNewLine(char character) {
//		if (character == '\n') {
//			return true;
//		}
//		return false;
//	}
	

}
