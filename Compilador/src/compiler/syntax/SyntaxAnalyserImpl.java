package compiler.syntax;

import java.util.HashSet;
import java.util.Set;

import compiler.NonTerminalToken;
import compiler.Token;
import compiler.TokenType;
import compiler.commons.AutomatonTransitionsTable;

public class SyntaxAnalyserImpl {

	public SyntaxAnalyserImpl() {

		SyntaxAutomaton programAutomaton;
		SyntaxAutomaton functionAutomaton;
		
		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(2);
			transitionTable.put(0, new NonTerminalToken(NonTerminalTokenType.FUNCAO), 1);
			transitionTable.put(1, new NonTerminalToken(NonTerminalTokenType.FUNCAO), 1);
			Set<Integer> finals = new HashSet<Integer>();
			finals.add(1);
			programAutomaton =  new SyntaxAutomaton(transitionTable, 2, 0, finals);
		}

		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(13);
			transitionTable.put(0, new Token(null, TokenType.KW_VOID), 1);
			transitionTable.put(0, new Token(null, TokenType.KW_INT), 1);
			transitionTable.put(1, new Token(null, TokenType.KW_VOID), 2);
			transitionTable.put(1, new NonTerminalToken(NonTerminalTokenType.FUNCAO), 1);
			Set<Integer> finals = new HashSet<Integer>();
			finals.add(12);
			programAutomaton =  new SyntaxAutomaton(transitionTable, 2, 0, finals);
		}
		

	}
}
