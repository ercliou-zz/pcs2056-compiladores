package compiler.syntax;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import compilador.FileExtractor;
import compiler.NonTerminalToken;
import compiler.SymbolTable;
import compiler.Token;
import compiler.TokenType;
import compiler.commons.Automaton;
import compiler.commons.AutomatonTransitionsTable;

public class SyntaxAnalyserImpl {

	SyntaxAutomaton syntaxAutomaton;
	SymbolTable st = new SymbolTable();

	public SyntaxAnalyserImpl() {

		Automaton<Token> programAutomaton;
		Automaton<Token> functionAutomaton;

		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(2);
			transitionTable.put(0, new NonTerminalToken(NonTerminalTokenType.FUNCAO), 1);
			transitionTable.put(1, new NonTerminalToken(NonTerminalTokenType.FUNCAO), 1);
			Set<Integer> finals = new HashSet<Integer>();
			finals.add(1);
			programAutomaton = new Automaton<Token>(transitionTable,2,0,finals);
		}

		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(13);
			transitionTable.put(0, new Token(null, TokenType.KW_VOID), 1);
			transitionTable.put(0, new Token(null, TokenType.KW_INT), 1);
			transitionTable.put(1, new Token(null, TokenType.KW_VOID), 2);
			transitionTable.put(1, new NonTerminalToken(NonTerminalTokenType.FUNCAO), 1);
			Set<Integer> finals = new HashSet<Integer>();
			finals.add(12);
			programAutomaton = new Automaton<Token>(transitionTable, 2, 0, finals);
		}

	}

	public void compile(String filePath) throws IOException {
		syntaxAutomaton.setSourceText(FileExtractor.extract(filePath));
		while (true) {
			syntaxAutomaton.step(st);
		}
	}
}
