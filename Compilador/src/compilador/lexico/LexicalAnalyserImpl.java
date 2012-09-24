package compilador.lexico;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import compilador.Automaton;
import compilador.State;

/**
 * Implementação do analisador léxico
 *
 */
public class LexicalAnalyserImpl implements LexicalAnalyser {

	private Automaton automaton;

	/**
	 * Construtor padrão, instancia o automato que faz a análise léxica de nossa
	 * linguagem
	 */
	public LexicalAnalyserImpl() {
		Set<State> states = new HashSet<State>();

		// ESTADO 1 (estado inicial)
		Map<String, Integer> transitions = new HashMap<String, Integer>();
		transitions.put("[\r\n \t]", 1);
		transitions.put("[0-9]", 2);
		transitions.put("[a-zA-Z]", 3);
		transitions.put(">", 4);
		transitions.put("<", 6);
		transitions.put("=", 8);
		transitions.put("!", 10);
		transitions.put("/", 12);
		transitions.put("[^\r\n \t0-9a-zA-Z><=!/]", 14);
		transitions.put("\"", 15);
		states.add(new State(1, transitions, false));

		// ESTADO 2 (números)
		transitions = new HashMap<String, Integer>();
		transitions.put("[0-9]", 2);
		transitions.put("[^0-9]", 0);
		states.add(new State(2, transitions, false));

		// ESTADO 3 (identificadores)
		transitions = new HashMap<String, Integer>();
		transitions.put("[a-zA-Z0-9]", 3);
		transitions.put("[^a-zA-Z0-9]", 0);
		states.add(new State(3, transitions, false));

		// ESTADO 4 (>=)
		transitions = new HashMap<String, Integer>();
		transitions.put("=", 5);
		transitions.put("[^=]", 0);
		states.add(new State(4, transitions, false));

		// ESTADO 5 (>= final)
		transitions = new HashMap<String, Integer>();
		transitions.put(".|\n|\r", 0);
		states.add(new State(5, transitions, false));

		// ESTADO 6 (<=)
		transitions = new HashMap<String, Integer>();
		transitions.put("=", 7);
		transitions.put("[^=]", 0);
		states.add(new State(6, transitions, false));

		// ESTADO 7 (<= final)
		transitions = new HashMap<String, Integer>();
		transitions.put(".|\n|\r", 0);
		states.add(new State(7, transitions, false));

		// ESTADO 8 (==)
		transitions = new HashMap<String, Integer>();
		transitions.put("=", 9);
		transitions.put("[^=]", 0);
		states.add(new State(8, transitions, false));

		// ESTADO 9 (== final)
		transitions = new HashMap<String, Integer>();
		transitions.put(".|\n|\r", 0);
		states.add(new State(9, transitions, false));

		// ESTADO 10 (!=)
		transitions = new HashMap<String, Integer>();
		transitions.put("=", 11);
		transitions.put("[^=]", 0);
		states.add(new State(10, transitions, false));

		// ESTADO 11 (!= final)
		transitions = new HashMap<String, Integer>();
		transitions.put(".|\n|\r", 0);
		states.add(new State(11, transitions, false));

		// ESTADO 12 (comentário)
		transitions = new HashMap<String, Integer>();
		transitions.put("/", 13);
		transitions.put("[^/]", 0);
		states.add(new State(12, transitions, false));

		// ESTADO 13 (comentário final)
		transitions = new HashMap<String, Integer>();
		transitions.put("[^\n]", 13);
		transitions.put("[\n]", 1);
		states.add(new State(13, transitions, false));

		// ESTADO 14 (outros caracteres)
		transitions = new HashMap<String, Integer>();
		transitions.put(".|\n|\r", 0);
		states.add(new State(14, transitions, false));

		// ESTADO 15 (String começo e conteúdo)
		transitions = new HashMap<String, Integer>();
		transitions.put("[^\"]", 15);
		transitions.put("[\"]", 16);
		states.add(new State(15, transitions, false));

		// ESTADO 16 (String final)
		transitions = new HashMap<String, Integer>();
		transitions.put(".|\n|\r", 0);
		states.add(new State(16, transitions, false));

		// ESTADO 0 (estado final)
		transitions = new HashMap<String, Integer>();
		states.add(new State(0, transitions, true));

		automaton = new Automaton(1, states);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * compilador.lexico.LexicalAnalyser#analyse(compilador.lexico.SymbolTable,
	 * java.lang.String, int)
	 */
	public LexicalResult analyse(SymbolTable symbolTable, String sourceText,
			int cursor) {

		// inicialização
		automaton.resetAutomaton();
		int stepCount = 0;
		int tokenBegin = 0;
		int stateBuffer = automaton.getActualState().getId();
		automaton.setSource(sourceText.substring(cursor));

		// loop, exeutando passo-à-passo o automato, enquanto houver caracteres
		// na cadeia ou não for um estado final
		while (automaton.getActualState().getId() != 0) {
			if (automaton.getActualState().getId() != 1 && stateBuffer == 1) {
				tokenBegin = stepCount - 1;
			}
			stateBuffer = automaton.getActualState().getId();
			stepCount++;
			if (!automaton.completed()) {
				automaton.step();
			} else {
				break;
			}
		}

		// extrai e classifica o token do texto fonte
		String tokenString = sourceText.substring(cursor + tokenBegin, cursor
				+ stepCount - 1);
		TokenType type = classify(stateBuffer, tokenString);
		Integer value = null;
		if (type != null) {
			if (type.equals(TokenType.IDENTIFIER)
					|| type.equals(TokenType.STRING)) {
				String processedString = tokenString;
				if (type.equals(TokenType.STRING)) {
					processedString = tokenString.substring(1,
							tokenString.length() - 1);
				}
				if (!symbolTable.contains(processedString)) {
					value = symbolTable.put(processedString);
				} else {
					value = symbolTable.get(processedString);
				}
			} else if (type.equals(TokenType.NUMERIC)) {
				value = Integer.parseInt(tokenString);
			} else if (type.equals(TokenType.OTHER)) {
				value = (int) tokenString.charAt(0);
			}

			// monta o resultado, contendo o token gerado e o cursor
			LexicalResult result = new LexicalResult();
			result.setCursor(cursor + stepCount - 1);
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
			case 1 :
				return null;
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
			case 4 :
			case 6 :
			case 8 :
			case 10 :
			case 12 :
				return TokenType.OTHER;
			case 16 :
				return TokenType.STRING;
		}
		throw new RuntimeException("Erro na classificação do token.");
	}

}
