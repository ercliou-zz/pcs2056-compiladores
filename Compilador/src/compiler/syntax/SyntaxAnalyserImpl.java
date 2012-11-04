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
	SymbolTable st = new SymbolTable();

	public SyntaxAnalyserImpl() {

		Automaton<Token> programAutomaton;
		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(2);
			transitionTable.put(0, new NonTerminalToken(NonTerminalTokenType.FUNCTION), 1);
			transitionTable.put(1, new NonTerminalToken(NonTerminalTokenType.FUNCTION), 1);
			Set<Integer> finals = new HashSet<Integer>();
			finals.add(1);
			programAutomaton = new Automaton<Token>(transitionTable,2,0,finals);
		}
		
		Automaton<Token> functionAutomaton;
		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(19);
			transitionTable.put(0, new Token(null, TokenType.KW_VOID), 1);
			transitionTable.put(0, new Token(null, TokenType.KW_INT), 1);
			transitionTable.put(1, new Token(null, TokenType.HUMBLE_IDENTIFIER), 2);
			transitionTable.put(2, new Token(40, TokenType.OTHER), 3);  // (
			transitionTable.put(3, new Token(null, TokenType.KW_VOID), 4);
			transitionTable.put(3, new Token(null, TokenType.KW_INT), 4);
			transitionTable.put(3, new Token(41, TokenType.OTHER), 5);  // )
			transitionTable.put(4, new Token(null, TokenType.HUMBLE_IDENTIFIER), 6);
			transitionTable.put(5, new Token(123, TokenType.OTHER), 7);  // {
			transitionTable.put(6, new Token(44, TokenType.OTHER), 8);  // ,
			transitionTable.put(6, new Token(41, TokenType.OTHER), 5);  // )
			transitionTable.put(7, new Token(null, TokenType.KW_VOID), 9);
			transitionTable.put(7, new Token(null, TokenType.KW_INT), 9);
			transitionTable.put(7, new NonTerminalToken(NonTerminalTokenType.COMMAND), 10);
			transitionTable.put(7, new Token(null, TokenType.KW_RETURN), 11);
			transitionTable.put(7, new Token(125, TokenType.OTHER), 12);  // }
			transitionTable.put(8, new Token(null, TokenType.KW_VOID), 4);
			transitionTable.put(8, new Token(null, TokenType.KW_INT), 4);
			transitionTable.put(9, new Token(null, TokenType.HUMBLE_IDENTIFIER), 15);
			transitionTable.put(10, new NonTerminalToken(NonTerminalTokenType.COMMAND), 10);
			transitionTable.put(10, new Token(null, TokenType.KW_RETURN), 11);
			transitionTable.put(10, new Token(125, TokenType.OTHER), 12);  // }
			transitionTable.put(11, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), 13);
			transitionTable.put(13, new Token(59, TokenType.OTHER), 14);  // ;
			transitionTable.put(14, new Token(125, TokenType.OTHER), 12);  // }
			transitionTable.put(15, new Token(91, TokenType.OTHER), 16);  // [
			transitionTable.put(15, new Token(59, TokenType.OTHER), 7);  // ;
			transitionTable.put(16, new NonTerminalToken(NonTerminalTokenType.ARITHM_EXP), 17);
			transitionTable.put(17, new Token(93, TokenType.OTHER), 18);  // ]
			transitionTable.put(18, new Token(59, TokenType.OTHER), 7);  // ;
			
			Set<Integer> finals = new HashSet<Integer>();
			finals.add(12);
			functionAutomaton = new Automaton<Token>(transitionTable, 19, 0, finals);
		}
		
		Automaton<Token> arithmeticExpressionAutomaton;
		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(4);
			transitionTable.put(0, new NonTerminalToken(NonTerminalTokenType.IDENTIFIER), 1);
			transitionTable.put(0, new Token(null, TokenType.NUMERIC), 1);
			transitionTable.put(0, new Token(40, TokenType.OTHER), 2);  // (
			transitionTable.put(1, new Token(42, TokenType.OTHER), 0);  // *
			transitionTable.put(1, new Token(47, TokenType.OTHER), 0);  // /
			transitionTable.put(1, new Token(43, TokenType.OTHER), 0);  // +
			transitionTable.put(1, new Token(45, TokenType.OTHER), 0);  // -
			transitionTable.put(2, new NonTerminalToken(NonTerminalTokenType.ARITHM_EXP), 3);
			transitionTable.put(3, new Token(41, TokenType.OTHER), 1);  // ) 
			
			Set<Integer> finals = new HashSet<Integer>();
			finals.add(1);
			arithmeticExpressionAutomaton = new Automaton<Token>(transitionTable,4,0,finals);
		}
		
		Automaton<Token> logicalExpressionAutomaton;
		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(6);
			transitionTable.put(0, new Token(32, TokenType.OTHER), 0);  // !
			transitionTable.put(0, new Token(null, TokenType.KW_TRUE), 1);
			transitionTable.put(0, new Token(null, TokenType.KW_FALSE), 1);
			transitionTable.put(0, new NonTerminalToken(NonTerminalTokenType.IDENTIFIER), 1);
			transitionTable.put(0, new NonTerminalToken(NonTerminalTokenType.ARITHM_EXP), 2);
			transitionTable.put(0, new Token(40, TokenType.OTHER), 3);  // (
			transitionTable.put(1, new Token(null, TokenType.KW_AND), 0);
			transitionTable.put(1, new Token(null, TokenType.KW_OR), 0);
			transitionTable.put(3, new Token(62, TokenType.OTHER), 4);  // >
			transitionTable.put(3, new Token(60, TokenType.OTHER), 4);  // <
			transitionTable.put(3, new Token(null, TokenType.EQUALS), 4);
			transitionTable.put(3, new Token(null, TokenType.DIFFERENT), 4);
			transitionTable.put(3, new Token(null, TokenType.GREATER_OR_EQUALS), 4);
			transitionTable.put(3, new Token(null, TokenType.LESS_OR_EQUALS), 4);
			transitionTable.put(4, new Token(41, TokenType.OTHER), 1);  // )
			transitionTable.put(5, new NonTerminalToken(NonTerminalTokenType.ARITHM_EXP), 1);
			
			Set<Integer> finals = new HashSet<Integer>();
			finals.add(1);
			logicalExpressionAutomaton = new Automaton<Token>(transitionTable, 6, 0, finals);
		}
		
		Automaton<Token> identifierAutomaton;
		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(5);
			transitionTable.put(0, new Token(null, TokenType.HUMBLE_IDENTIFIER), 1);
			transitionTable.put(1, new Token(91, TokenType.OTHER), 2);  // [
			transitionTable.put(2, new NonTerminalToken(NonTerminalTokenType.ARITHM_EXP), 3);
			transitionTable.put(3, new Token(93, TokenType.OTHER), 4);  // ]
			
			Set<Integer> finals = new HashSet<Integer>();
			finals.add(1);
			finals.add(4);
			identifierAutomaton = new Automaton<Token>(transitionTable, 5, 0, finals);
		}
		
		Automaton<Token> expressionAutomaton;
		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(2);
			transitionTable.put(0, new NonTerminalToken(NonTerminalTokenType.ARITHM_EXP), 1);
			transitionTable.put(0, new NonTerminalToken(NonTerminalTokenType.LOGICAL_EXP), 1);
			
			Set<Integer> finals = new HashSet<Integer>();
			finals.add(1);
			expressionAutomaton = new Automaton<Token>(transitionTable, 2, 0, finals);
		}
		
		
		Automaton<Token> blockAutomaton;
		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(9);
			transitionTable.put(0, new Token(123, TokenType.OTHER), 1);  // {
			transitionTable.put(1, new Token(null, TokenType.KW_VOID), 2);
			transitionTable.put(1, new Token(null, TokenType.KW_INT), 2);
			transitionTable.put(1, new NonTerminalToken(NonTerminalTokenType.COMMAND), 3);
			transitionTable.put(1, new Token(125, TokenType.OTHER), 4);  // }
			transitionTable.put(2, new Token(null, TokenType.HUMBLE_IDENTIFIER), 5);
			transitionTable.put(3, new NonTerminalToken(NonTerminalTokenType.COMMAND), 3);
			transitionTable.put(3, new Token(125, TokenType.OTHER), 4);  // }
			transitionTable.put(5, new Token(91, TokenType.OTHER), 6);  // [
			transitionTable.put(5, new Token(59, TokenType.OTHER), 1);  // ;
			transitionTable.put(6, new NonTerminalToken(NonTerminalTokenType.ARITHM_EXP), 7);
			transitionTable.put(7, new Token(93, TokenType.OTHER), 8);  // ]
			transitionTable.put(8, new Token(59, TokenType.OTHER), 1);  // ;
			
			Set<Integer> finals = new HashSet<Integer>();
			finals.add(4);
			blockAutomaton = new Automaton<Token>(transitionTable, 9, 0, finals);
		}
		
		
		Automaton<Token> commandAutomaton;
		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(25);
			transitionTable.put(0, new Token(null, TokenType.HUMBLE_IDENTIFIER), 1);
			transitionTable.put(0, new Token(null, TokenType.KW_IF), 2);
			transitionTable.put(0, new Token(null, TokenType.KW_WHILE), 3);
			transitionTable.put(0, new Token(null, TokenType.KW_READ), 4);
			transitionTable.put(0, new Token(null, TokenType.KW_WRITE), 5);
			transitionTable.put(0, new NonTerminalToken(NonTerminalTokenType.BLOCK), 6);
			transitionTable.put(1, new Token(91, TokenType.OTHER), 7);  // [
			transitionTable.put(1, new Token(93, TokenType.OTHER), 8);  // =
			transitionTable.put(1, new Token(40, TokenType.OTHER), 9);  // (
			transitionTable.put(2, new Token(40, TokenType.OTHER), 20);  // (
			transitionTable.put(3, new Token(40, TokenType.OTHER), 18);  // (
			transitionTable.put(4, new Token(40, TokenType.OTHER), 15);  // (
			transitionTable.put(5, new Token(40, TokenType.OTHER), 12);  // (
			transitionTable.put(7, new NonTerminalToken(NonTerminalTokenType.ARITHM_EXP), 16);
			transitionTable.put(8, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), 11);
			transitionTable.put(9, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), 10);
			transitionTable.put(9, new Token(41, TokenType.OTHER), 11);  // )
			transitionTable.put(10, new Token(41, TokenType.OTHER), 11);  // )
			transitionTable.put(10, new Token(44, TokenType.OTHER), 14);  // ,
			transitionTable.put(11, new Token(59, TokenType.OTHER), 6);  // ;
			transitionTable.put(12, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), 13);
			transitionTable.put(13, new Token(41, TokenType.OTHER), 11);  // )
			transitionTable.put(14, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), 10);
			transitionTable.put(15, new NonTerminalToken(NonTerminalTokenType.IDENTIFIER), 13);
			transitionTable.put(16, new Token(93, TokenType.OTHER), 17);  // ]
			transitionTable.put(17, new Token(93, TokenType.OTHER), 8);  // =
			transitionTable.put(18, new NonTerminalToken(NonTerminalTokenType.LOGICAL_EXP), 19);
			transitionTable.put(19, new Token(41, TokenType.OTHER), 21);  // )
			transitionTable.put(20, new NonTerminalToken(NonTerminalTokenType.LOGICAL_EXP), 22);
			transitionTable.put(21, new NonTerminalToken(NonTerminalTokenType.COMMAND), 6);
			transitionTable.put(22, new Token(41, TokenType.OTHER), 23);  // )
			transitionTable.put(23, new NonTerminalToken(NonTerminalTokenType.COMMAND), 24);
			transitionTable.put(24, new Token(null, TokenType.KW_ELSE), 21);
			
			Set<Integer> finals = new HashSet<Integer>();
			finals.add(6);
			finals.add(24);
			commandAutomaton = new Automaton<Token>(transitionTable, 25, 0, finals);
		}
		
		Map<NonTerminalToken, Automaton<Token>> automatons = new HashMap<NonTerminalToken, Automaton<Token>>();
		automatons.put(new NonTerminalToken(NonTerminalTokenType.PROGRAM), programAutomaton);
		automatons.put(new NonTerminalToken(NonTerminalTokenType.FUNCTION), functionAutomaton);
		automatons.put(new NonTerminalToken(NonTerminalTokenType.BLOCK), blockAutomaton);
		automatons.put(new NonTerminalToken(NonTerminalTokenType.COMMAND), commandAutomaton);
		automatons.put(new NonTerminalToken(NonTerminalTokenType.EXPRESSION), expressionAutomaton);
		automatons.put(new NonTerminalToken(NonTerminalTokenType.ARITHM_EXP), arithmeticExpressionAutomaton);
		automatons.put(new NonTerminalToken(NonTerminalTokenType.LOGICAL_EXP), logicalExpressionAutomaton);
		automatons.put(new NonTerminalToken(NonTerminalTokenType.IDENTIFIER), identifierAutomaton);
		
		syntaxAutomaton = new SyntaxAutomaton(automatons, new NonTerminalToken(NonTerminalTokenType.PROGRAM));
	}

	public void compile(String filePath) throws IOException {
		syntaxAutomaton.setSourceText(FileExtractor.extract(filePath));
		while (true) {
			syntaxAutomaton.step(st);
		}
	}
}
