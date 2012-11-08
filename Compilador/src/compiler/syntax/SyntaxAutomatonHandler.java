package compiler.syntax;

import java.util.Map;
import java.util.Set;
import java.util.Stack;

import compiler.commons.NonTerminalToken;
import compiler.commons.SymbolTable;
import compiler.commons.Token;
import compiler.commons.TokenType;
import compiler.lexical.LexicalAnalyser;
import compiler.lexical.LexicalAnalyserImpl;
import compiler.lexical.LexicalResult;

public class SyntaxAutomatonHandler {

	private SyntaxAutomaton currentAutomaton;
	private Stack<SyntaxAutomaton> stack;
	private Token currentToken;
	private LexicalAnalyser lexicalAnalyser = new LexicalAnalyserImpl();
	private int lexCursor = 0;
	private String sourceText;
	private Map<NonTerminalToken, SyntaxAutomaton> automatons;

	public SyntaxAutomatonHandler(Map<NonTerminalToken, SyntaxAutomaton> automatons, NonTerminalToken initialAutomaton) {
		this.automatons = automatons;
		currentAutomaton = automatons.get(initialAutomaton);
		currentAutomaton.resetAutomaton();
		stack = new Stack<SyntaxAutomaton>();
	}

	public void step(SymbolTable symbolTable) {
		System.out.println("Submáquina: " + currentAutomaton + "\tEstado: " + currentAutomaton.getActualState() + "\tToken:\t" + currentToken);

		Token nextPath = null;

		nextPath = decideNextState(currentAutomaton.getPossibleTransitions());

		if (nextPath == null) {
			if (currentAutomaton.isComplete() && stack.peek() != null) {
				currentAutomaton = stack.pop();
				System.out.println("POP\t" + printStack());
			} else {
				throw new RuntimeException("Token não esperado: " + currentToken);
			}
		} else {
			machineStep(nextPath);
			if (NonTerminalToken.class.isAssignableFrom(nextPath.getClass())) {
				stack.push(currentAutomaton);
				System.out.println("PUSH\t" + printStack());
				currentAutomaton = automatons.get(nextPath).clone();
				currentAutomaton.resetAutomaton();
			}
		}
	}

	public void initialize(String sourceText) {
		this.sourceText = sourceText;
		consumeToken();
	}

	public boolean isComplete() {
		return (currentAutomaton.isComplete() && currentToken == null && stack.isEmpty());
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
					throw new RuntimeException("Duas transições com não terminal.");
				}
				ntt = (NonTerminalToken) token;
			}
		}
		return ntt;
	}

	private Token decideNextState(Set<Token> possibleTransitions) {

		if (currentAutomaton.hasLookAhead()) {
			// WORKAROUND - DUMMY! MUDAR NO FUTURO DISTANTE
			Token nextToken = peekNextToken();
			if (nextToken.getType() == TokenType.OTHER && nextToken.getValue() == '(') {
				for (Token token : possibleTransitions) {
					if (token.getType() == TokenType.IDENTIFIER) {
						return token;
					}
				}
			}
			return getUniqueNonTerminal(possibleTransitions);
			// FIM DO WORKAROUND
		} else {
			if (currentAutomaton.hasTransition(currentToken)) {
				return currentToken;
			} else {
				return getUniqueNonTerminal(possibleTransitions);
			}
		}
	}

	private void machineStep(Token token) {
		currentAutomaton.setString(token);
		currentAutomaton.step();
		semantico_tbd(currentAutomaton.getSemanticActionId());
		if (!NonTerminalToken.class.isAssignableFrom(token.getClass())) {
			consumeToken();
		}
	}

	@SuppressWarnings("unchecked")
	private String printStack() {
		String result = "";
		Stack<SyntaxAutomaton> other = (Stack<SyntaxAutomaton>) stack.clone();
		while (!other.isEmpty()) {
			result += other.pop() + " ";
		}
		return result;
	}

	private void semantico_tbd(Integer semanticActionId) {
		if (semanticActionId != null) {
			System.out.println("Ação semântica chamada: " + semanticActionId);
		}
	}
}
