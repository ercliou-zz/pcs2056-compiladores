package compiler.syntax;

import java.util.Set;
import java.util.Stack;

import compiler.Token;
import compiler.commons.Automaton;
import compiler.commons.AutomatonTransitionsTable;

public class SyntaxAutomaton {

	private Automaton<Token> currentAutomaton;
	private Stack<Automaton<Token>> stack;

	public SyntaxAutomaton(AutomatonTransitionsTable<Token> transitions,
			int statesQuantity, int initialState, Set<Integer> finalStates) {
		currentAutomaton = new Automaton<Token>(transitions, statesQuantity, initialState, finalStates);
		stack = new Stack<Automaton<Token>>();
	}
	
	public void push(Automaton<Token> nextAutomaton){
		stack.push(currentAutomaton);
		currentAutomaton = nextAutomaton;
	}
	
	public void pop(){
		if(currentAutomaton.isComplete()){
			currentAutomaton = stack.pop();
		}
	}
	
	public void step(){
		currentAutomaton.step();
	}

}
