package compiler.syntax;

import java.util.Map;
import java.util.Set;
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
		this.automatons = automatons;
		currentAutomaton = automatons.get(initialAutomaton);
		currentAutomaton.resetAutomaton();
		stack = new Stack<Automaton<Token>>();
	}

	public void step(SymbolTable symbolTable) {
		System.out.println("Submáquina: " + currentAutomaton + " Estado: " + currentAutomaton.getActualState() + " Token: " + currentToken);
		if (currentAutomaton.hasTransition(currentToken)) {
			currentAutomaton.setString(currentToken);
			currentAutomaton.step();
			consumeToken();
		} else {
			NonTerminalToken nextMachine = getUniqueNonTerminal(currentAutomaton.getPossibleTransitions());
			if(nextMachine != null){
				currentAutomaton.setString(nextMachine);
				currentAutomaton.step();
				stack.push(currentAutomaton);
				currentAutomaton = automatons.get(nextMachine);
			} else {
				if(currentAutomaton.isComplete() && stack.peek() != null){
					currentAutomaton = stack.pop();
				}
			}
			
		}
	}

	public void initialize(String sourceText) {
		this.sourceText = sourceText;
		consumeToken();
	}

	private void consumeToken() {
		LexicalResult result = lexicalAnalyser.analyse(sourceText, lexCursor);
		if (result != null) {
			lexCursor = result.getCursor();
			currentToken = result.getToken();
		} else {
			currentToken = null;
		}
	}

	@SuppressWarnings("unused")
	private Token peekNextToken() {
		LexicalResult result = lexicalAnalyser.analyse(sourceText, lexCursor);
		if (result != null) {
			return result.getToken();
		}
		return null;
	}

	private NonTerminalToken getUniqueNonTerminal(Set<Token> tokens) {
		NonTerminalToken ntt = null;
		for (Token token : tokens) {
			if (NonTerminalToken.class.isAssignableFrom(token.getClass())) {
				if (ntt != null) {
					// Nao deveria ser excecao, e sim decidir qual expandir
					// fazendo o look ahead
					throw new RuntimeException("Duas transilções com não terminal.");
				}
				ntt = (NonTerminalToken) token;
			}
		}
		return ntt;
	}

}
