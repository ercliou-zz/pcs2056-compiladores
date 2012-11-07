package compiler.lexical;

import java.util.HashSet;
import java.util.Set;

import compiler.commons.SymbolTable;
import compiler.commons.Token;
import compiler.commons.TokenType;

/**
 * Implementação do analisador léxico
 * 
 */
public class LexicalAnalyserImpl implements LexicalAnalyser {

	private LexicalAutomaton automaton;
	
	/**
	 * Construtor padrão, instancia o automato que faz a análise léxica de nossa
	 * linguagem
	 */
	public LexicalAnalyserImpl() {
		LexicalAutomatonTransitionsTable transitions = new LexicalAutomatonTransitionsTable(15);

		// ESTADO 1 (estado inicial)
		transitions.put(1, "[\r\n \t]", 1);
		transitions.put(1, "[0-9]", 2);
		transitions.put(1, "[a-zA-Z]", 3);
		transitions.put(1, ">", 4);
		transitions.put(1, "<", 6);
		transitions.put(1, "=", 8);
		transitions.put(1, "!", 10);
		transitions.put(1, "/", 12);
		transitions.put(1, "[^\r\n \t0-9a-zA-Z><=!/]", 14);
		// ESTADO 2 (números)
		transitions.put(2, "[0-9]", 2);
		transitions.put(2, "[^0-9]", 0);
		// ESTADO 3 (identificadores)
		transitions.put(3, "[a-zA-Z0-9]", 3);
		transitions.put(3, "[^a-zA-Z0-9]", 0);
		// ESTADO 4 (>=)
		transitions.put(4, "=", 5);
		transitions.put(4, "[^=]", 0);
		// ESTADO 5 (>= final)
		transitions.put(5, ".|\n|\r", 0);
		// ESTADO 6 (<=)
		transitions.put(6, "=", 7);
		transitions.put(6, "[^=]", 0);
		// ESTADO 7 (<= final)
		transitions.put(7, ".|\n|\r", 0);
		// ESTADO 8 (==)
		transitions.put(8, "=", 9);
		transitions.put(8, "[^=]", 0);
		// ESTADO 9 (== final)
		transitions.put(9, ".|\n|\r", 0);
		// ESTADO 10 (!=)
		transitions.put(10, "=", 11);
		transitions.put(10, "[^=]", 0);
		// ESTADO 11 (!= final)
		transitions.put(11, ".|\n|\r", 0);
		// ESTADO 12 (comentário)
		transitions.put(12, "/", 13);
		transitions.put(12, "[^//]", 0);
		// ESTADO 13 (comentário final)
		transitions.put(13, "[^\n]", 13);
		transitions.put(13, "[\n]", 1);
		// ESTADO 14 (outros caracteres)
		transitions.put(14, ".|\n|\r", 0);

		Set<Integer> finalStates = new HashSet<Integer>();
		finalStates.add(0);

		// Estados em que quando o automato der um passo, não guarda o caracter
		// consumido
		Set<Integer> ignoredStates = new HashSet<Integer>();
		ignoredStates.add(1);
		ignoredStates.add(13);

		automaton = new LexicalAutomaton(transitions, 15, 1, finalStates, ignoredStates);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * compilador.lexico.LexicalAnalyser#analyse(compilador.lexico.SymbolTable,
	 * java.lang.String, int)
	 */
	public LexicalResult analyse(String sourceText, int cursor) {

		// inicialização
		automaton.resetAutomaton();
		automaton.setString(sourceText.substring(cursor));

		int stepCounter = 0;

		// loop, executando passo-à-passo o automato, enquanto houver caracteres
		// na cadeia ou não for um estado final
		while (!automaton.isStringEmpty() && automaton.peekNextState() != null && automaton.peekNextState().getState() != 0) {
			automaton.step();
			stepCounter++;
		}

		// extrai e classifica o token do texto fonte
		String tokenString = automaton.getConsumedStringFormat();
		TokenType type = classify(automaton.getActualState(), tokenString);
		Integer value = null;
		if (type != null) {
			if (type.equals(TokenType.IDENTIFIER)) {
				String processedString = tokenString;
				if (!SymbolTable.getInstance().contains(processedString)) {
					value = SymbolTable.getInstance().put(processedString);
				} else {
					value = SymbolTable.getInstance().get(processedString);
				}
			} else if (type.equals(TokenType.NUMERIC)) {
				value = Integer.parseInt(tokenString);
			} else if (type.equals(TokenType.OTHER)) {
				value = (int) tokenString.charAt(0);
			}

			// monta o resultado, contendo o token gerado e o cursor
			LexicalResult result = new LexicalResult();
			result.setCursor(cursor + stepCounter);
			result.setToken(new Token(value, type));
			return result;
		}
		return null;
	}

	/*
	 * Classifica o token a partir da cadeia de caracteres lidos e o último
	 * estado do automato antes de terminar a execução
	 */
	private TokenType classify(int lastState, String tokenString) {
		// System.out.println("Classifying: Last State=" + lastState
		// + " Token String=" + tokenString);
		switch (lastState) {
		case 1:
			return null;
		case 2:
			return TokenType.NUMERIC;
		case 3:
			if (TokenType.isKeyword(tokenString)) {
				return TokenType.getKeywordEnum(tokenString);
			}
			return TokenType.IDENTIFIER;
		case 5:
			return TokenType.GREATER_OR_EQUALS;
		case 7:
			return TokenType.LESS_OR_EQUALS;
		case 9:
			return TokenType.EQUALS;
		case 11:
			return TokenType.DIFFERENT;
		case 14:
		case 4:
		case 6:
		case 8:
		case 10:
		case 12:
			return TokenType.OTHER;
		}
		throw new RuntimeException("Erro na classificação do token.");
	}

}
