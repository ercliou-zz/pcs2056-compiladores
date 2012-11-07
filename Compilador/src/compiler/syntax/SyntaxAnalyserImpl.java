package compiler.syntax;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import compiler.NonTerminalToken;
import compiler.SymbolTable;
import compiler.Token;
import compiler.TokenType;
import compiler.commons.Automaton;
import compiler.commons.AutomatonTransitionsTable;
import compiler.commons.FileExtractor;

public class SyntaxAnalyserImpl {

	SyntaxAutomaton syntaxAutomaton;
	SymbolTable st = SymbolTable.getInstance();

	public SyntaxAnalyserImpl() {

		Automaton<Token> programAutomaton;
		Automaton<Token> commandAutomaton;
		Automaton<Token> expressionAutomaton;

		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(16);
			transitionTable.put(0, new Token(null, TokenType.KW_VOID), 1);
			transitionTable.put(0, new Token(null, TokenType.KW_INT), 1);
			transitionTable.put(0, new Token(null, TokenType.KW_BOOL), 1);
			transitionTable.put(1, new Token(null, TokenType.HUMBLE_IDENTIFIER), 2);
			transitionTable.put(2, new Token((int) '(', TokenType.OTHER), 3);
			transitionTable.put(3, new Token(null, TokenType.KW_VOID), 4);
			transitionTable.put(3, new Token(null, TokenType.KW_INT), 4);
			transitionTable.put(3, new Token(null, TokenType.KW_BOOL), 4);
			transitionTable.put(3, new Token((int) ')', TokenType.OTHER), 5);
			transitionTable.put(4, new Token(null, TokenType.HUMBLE_IDENTIFIER), 6);
			transitionTable.put(5, new Token((int) '{', TokenType.OTHER), 7);
			transitionTable.put(6, new Token((int) ',', TokenType.OTHER), 8);
			transitionTable.put(6, new Token((int) ')', TokenType.OTHER), 5);
			transitionTable.put(7, new Token(null, TokenType.KW_VOID), 9);
			transitionTable.put(7, new Token(null, TokenType.KW_INT), 9);
			transitionTable.put(7, new Token(null, TokenType.KW_BOOL), 9);
			transitionTable.put(7, new NonTerminalToken(NonTerminalTokenType.COMMAND), 10);
			transitionTable.put(7, new Token((int) '}', TokenType.OTHER), 11);
			transitionTable.put(8, new Token(null, TokenType.KW_VOID), 4);
			transitionTable.put(8, new Token(null, TokenType.KW_INT), 4);
			transitionTable.put(8, new Token(null, TokenType.KW_BOOL), 4);
			transitionTable.put(9, new Token(null, TokenType.HUMBLE_IDENTIFIER), 12);
			transitionTable.put(10, new NonTerminalToken(NonTerminalTokenType.COMMAND), 10);
			transitionTable.put(10, new Token((int) '}', TokenType.OTHER), 11);
			transitionTable.put(11, new Token(null, TokenType.KW_VOID), 1);
			transitionTable.put(11, new Token(null, TokenType.KW_INT), 1);
			transitionTable.put(11, new Token(null, TokenType.KW_BOOL), 1);
			transitionTable.put(12, new Token((int) '[', TokenType.OTHER), 13);
			transitionTable.put(12, new Token((int) ';', TokenType.OTHER), 7);
			transitionTable.put(13, new Token(null, TokenType.NUMERIC), 14);
			transitionTable.put(14, new Token((int) ']', TokenType.OTHER), 15);
			transitionTable.put(15, new Token((int) ';', TokenType.OTHER), 7);

			Set<Integer> finals = new HashSet<Integer>();
			finals.add(11);
			programAutomaton = new Automaton<Token>(transitionTable, 16, 0, finals);
			programAutomaton.setName("PROGRAMA");
		}

		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(35);
			transitionTable.put(0, new Token(null, TokenType.HUMBLE_IDENTIFIER), 1);
			transitionTable.put(0, new Token(null, TokenType.KW_IF), 2);
			transitionTable.put(0, new Token(null, TokenType.KW_WHILE), 3);
			transitionTable.put(0, new Token(null, TokenType.KW_READ), 4);
			transitionTable.put(0, new Token(null, TokenType.KW_WRITE), 5);
			transitionTable.put(0, new Token((int) '{', TokenType.OTHER), 6);
			transitionTable.put(1, new Token((int) '{', TokenType.OTHER), 7);
			transitionTable.put(1, new Token((int) '=', TokenType.OTHER), 8);
			transitionTable.put(1, new Token((int) '(', TokenType.OTHER), 9);
			transitionTable.put(2, new Token((int) '(', TokenType.OTHER), 24);
			transitionTable.put(3, new Token((int) '(', TokenType.OTHER), 33);
			transitionTable.put(4, new Token((int) '(', TokenType.OTHER), 28);
			transitionTable.put(5, new Token((int) '(', TokenType.OTHER), 22);
			transitionTable.put(6, new NonTerminalToken(NonTerminalTokenType.COMMAND), 10);
			transitionTable.put(6, new Token(null, TokenType.KW_VOID), 11);
			transitionTable.put(6, new Token(null, TokenType.KW_INT), 11);
			transitionTable.put(6, new Token(null, TokenType.KW_BOOL), 11);
			transitionTable.put(6, new Token((int) '}', TokenType.OTHER), 12);
			transitionTable.put(7, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), 20);
			transitionTable.put(8, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), 14);
			transitionTable.put(9, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), 13);
			transitionTable.put(9, new Token((int) ')', TokenType.OTHER), 14);
			transitionTable.put(10, new NonTerminalToken(NonTerminalTokenType.COMMAND), 10);
			transitionTable.put(10, new Token((int) '}', TokenType.OTHER), 12);
			transitionTable.put(11, new Token(null, TokenType.HUMBLE_IDENTIFIER), 15);
			transitionTable.put(13, new Token((int) ')', TokenType.OTHER), 14);
			transitionTable.put(13, new Token((int) ',', TokenType.OTHER), 17);
			transitionTable.put(14, new Token((int) ';', TokenType.OTHER), 12);
			transitionTable.put(15, new Token((int) '[', TokenType.OTHER), 16);
			transitionTable.put(15, new Token((int) ';', TokenType.OTHER), 6);
			transitionTable.put(16, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), 18);
			transitionTable.put(17, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), 13);
			transitionTable.put(18, new Token((int) ']', TokenType.OTHER), 19);
			transitionTable.put(19, new Token((int) ';', TokenType.OTHER), 6);
			transitionTable.put(20, new Token((int) ']', TokenType.OTHER), 21);
			transitionTable.put(21, new Token((int) '=', TokenType.OTHER), 8);
			transitionTable.put(22, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), 23);
			transitionTable.put(23, new Token((int) ')', TokenType.OTHER), 14);
			transitionTable.put(24, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), 25);
			transitionTable.put(25, new Token((int) '>', TokenType.OTHER), 24);
			transitionTable.put(25, new Token((int) '<', TokenType.OTHER), 24);
			transitionTable.put(25, new Token(null, TokenType.GREATER_OR_EQUALS), 24);
			transitionTable.put(25, new Token(null, TokenType.LESS_OR_EQUALS), 24);
			transitionTable.put(25, new Token(null, TokenType.EQUALS), 24);
			transitionTable.put(25, new Token(null, TokenType.DIFFERENT), 24);
			transitionTable.put(25, new Token((int) ')', TokenType.OTHER), 26);
			transitionTable.put(26, new NonTerminalToken(NonTerminalTokenType.COMMAND), 27);
			transitionTable.put(27, new Token((int) ')', TokenType.KW_ELSE), 29);
			transitionTable.put(28, new Token(null, TokenType.HUMBLE_IDENTIFIER), 30);
			transitionTable.put(29, new NonTerminalToken(NonTerminalTokenType.COMMAND), 12);
			transitionTable.put(30, new Token((int) '[', TokenType.OTHER), 31);
			transitionTable.put(30, new Token((int) ')', TokenType.OTHER), 14);
			transitionTable.put(31, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), 32);
			transitionTable.put(32, new Token((int) ']', TokenType.OTHER), 23);
			transitionTable.put(33, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), 34);
			transitionTable.put(34, new Token((int) ')', TokenType.OTHER), 29);

			Set<Integer> finals = new HashSet<Integer>();
			finals.add(12);
			finals.add(27);
			commandAutomaton = new Automaton<Token>(transitionTable, 35, 0, finals);
			commandAutomaton.setName("COMANDO");
		}

		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(8);
			transitionTable.put(0, new Token(null, TokenType.NUMERIC), 1);
			transitionTable.put(0, new Token((int) '!', TokenType.OTHER), 2);
			transitionTable.put(0, new Token(null, TokenType.HUMBLE_IDENTIFIER), 3);
			transitionTable.put(0, new Token((int) '(', TokenType.OTHER), 4);
			transitionTable.put(0, new Token(null, TokenType.KW_TRUE), 1);
			transitionTable.put(0, new Token(null, TokenType.KW_FALSE), 1);
			transitionTable.put(1, new Token((int) '*', TokenType.OTHER), 0);
			transitionTable.put(1, new Token((int) '/', TokenType.OTHER), 0);
			transitionTable.put(1, new Token(null, TokenType.KW_AND), 0);
			transitionTable.put(1, new Token((int) '+', TokenType.OTHER), 0);
			transitionTable.put(1, new Token((int) '-', TokenType.OTHER), 0);
			transitionTable.put(1, new Token(null, TokenType.KW_OR), 0);
			transitionTable.put(1, new Token((int) '!', TokenType.OTHER), 2);
			transitionTable.put(2, new Token(null, TokenType.HUMBLE_IDENTIFIER), 3);
			transitionTable.put(2, new Token((int) '(', TokenType.OTHER), 4);
			transitionTable.put(3, new Token((int) '[', TokenType.OTHER), 6);
			transitionTable.put(3, new Token((int) '*', TokenType.OTHER), 0);
			transitionTable.put(3, new Token((int) '/', TokenType.OTHER), 0);
			transitionTable.put(3, new Token(null, TokenType.KW_AND), 0);
			transitionTable.put(3, new Token((int) '+', TokenType.OTHER), 0);
			transitionTable.put(3, new Token((int) '-', TokenType.OTHER), 0);
			transitionTable.put(3, new Token(null, TokenType.KW_OR), 0);
			transitionTable.put(4, new Token(null, TokenType.HUMBLE_IDENTIFIER), 5);
			transitionTable.put(5, new Token((int) ')', TokenType.OTHER), 1);
			transitionTable.put(6, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), 7);
			transitionTable.put(7, new Token((int) ']', TokenType.OTHER), 1);
			
			Set<Integer> finals = new HashSet<Integer>();
			finals.add(1);
			finals.add(3);
			expressionAutomaton = new Automaton<Token>(transitionTable, 8, 0, finals);
			expressionAutomaton.setName("EXPRESSAO");
		}

		Map<NonTerminalToken, Automaton<Token>> automatons = new HashMap<NonTerminalToken, Automaton<Token>>();
		automatons.put(new NonTerminalToken(NonTerminalTokenType.PROGRAM), programAutomaton);
		automatons.put(new NonTerminalToken(NonTerminalTokenType.COMMAND), commandAutomaton);
		automatons.put(new NonTerminalToken(NonTerminalTokenType.EXPRESSION), expressionAutomaton);

		syntaxAutomaton = new SyntaxAutomaton(automatons, new NonTerminalToken(NonTerminalTokenType.PROGRAM));
	}

	public void compile(String filePath) throws IOException {
		syntaxAutomaton.initialize(FileExtractor.extract(filePath));
		while (true) {
			syntaxAutomaton.step(st);
		}
	}
}
