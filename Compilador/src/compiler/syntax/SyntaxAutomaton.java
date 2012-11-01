package compiler.syntax;

import java.util.Map;
import java.util.Stack;

import compiler.NonTerminalToken;
import compiler.SymbolTable;
import compiler.Token;
import compiler.commons.Automaton;
import compiler.lexical.LexicalAnalyser;
import compiler.lexical.LexicalAnalyserImpl;
import compiler.lexical.LexicalResult;

public class SyntaxAutomaton {

	private Automaton<Token> currentAutomaton;
	private Stack<Automaton<Token>> stack;
	private Token currentToken;
	private LexicalAnalyser lexicalAnalyser = new LexicalAnalyserImpl();
	private int lexCursor = 0;
	private String sourceText;
	private Map<NonTerminalToken, Automaton<Token>> automatons;

	public SyntaxAutomaton(Map<NonTerminalToken, Automaton<Token>> automatons, NonTerminalToken initialAutomaton) {
		currentAutomaton = automatons.get(initialAutomaton);
		currentAutomaton.resetAutomaton();
		stack = new Stack<Automaton<Token>>();
	}

	public void push(Automaton<Token> nextAutomaton) {
		stack.push(currentAutomaton);
		currentAutomaton = nextAutomaton;
	}

	public void pop() {
		if (currentAutomaton.isComplete()) {
			currentAutomaton = stack.pop();
		}
	}

	public void step(SymbolTable symbolTable) {
		if (currentAutomaton.hasTransition(currentAutomaton.getActualState(), currentToken)) {
			currentAutomaton.setString(currentToken);
			currentAutomaton.step();
			consumeToken(symbolTable);
		} else {
			boolean foundValidState = false;
			for (Token token : currentAutomaton.getPossibleTransitions(currentAutomaton.getActualState())) {
				if (token.getClass().isAssignableFrom(NonTerminalToken.class)) {
					if (automatons.get(token).hasTransition(currentAutomaton.getActualState(), currentToken)) {
						currentAutomaton.setString(token);
						currentAutomaton.step();
						push(currentAutomaton);
						currentAutomaton = automatons.get(token);
						foundValidState = true;
					}
				}
			}
			if (!foundValidState) {
				throw new RuntimeException("Token não reconhecido no automato.");
			}
		}
	}

	public void setSourceText(String sourceText) {
		this.sourceText = sourceText;
	}

	private void consumeToken(SymbolTable symbolTable) {
		LexicalResult result = lexicalAnalyser.analyse(symbolTable, sourceText, lexCursor);
		if (result != null) {
			lexCursor = result.getCursor();
			currentToken = result.getToken();
		} else {
			currentToken = null;
		}
	}

	private Token peekNextToken(SymbolTable symbolTable) {
		LexicalResult result = lexicalAnalyser.analyse(symbolTable, sourceText, lexCursor);
		if (result != null) {
			return result.getToken();
		}
		return null;
	}
}
