package compiler.syntax;

import java.util.Map;
import java.util.Set;
import java.util.Stack;

import compiler.NonTerminalToken;
import compiler.SymbolTable;
import compiler.Token;
import compiler.TokenType;
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
		System.out.println("Subm�quina: " + currentAutomaton + " Estado: " + currentAutomaton.getActualState() + " Token: " + currentToken);

		Token nextPath = null;

		nextPath = decideAhead(currentAutomaton.getPossibleTransitions());

		if (nextPath == null) {
			if (currentAutomaton.isComplete() && stack.peek() != null) {
				currentAutomaton = stack.pop();
				System.out.println("POP\t" + printStack());
			}
		} else {
			step(nextPath);
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
					throw new RuntimeException("Duas transi��es com n�o terminal.");
				}
				ntt = (NonTerminalToken) token;
			}
		}
		return ntt;
	}

	private Token decideAhead(Set<Token> possibleTransitions) {

		if (currentAutomaton.hasLookAhead()) {
			// WORKAROUND - DUMMY! MUDAR NO FUTURO DISTANTE
			Token nextToken = peekNextToken();
			if (nextToken.getType() == TokenType.OTHER && nextToken.getValue() == '(') {
				for (Token token : possibleTransitions) {
					if (token.getType() == TokenType.HUMBLE_IDENTIFIER) {
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

	private void step(Token token) {
		currentAutomaton.setString(token);
		currentAutomaton.step();
		if (!NonTerminalToken.class.isAssignableFrom(token.getClass())) {
			consumeToken();
		}
	}

	@SuppressWarnings("unchecked")
	private String printStack() {
		String result = "";
		Stack<SyntaxAutomaton> other = (Stack<SyntaxAutomaton>) stack.clone();
		while(!other.isEmpty()){
			result += other.pop() + " ";
		}
		return result;
	}

}
