package compiler.syntax;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import compiler.commons.AutomatonTransitionsTable;
import compiler.commons.FileExtractor;
import compiler.commons.NonTerminalToken;
import compiler.commons.SymbolTable;
import compiler.commons.Token;
import compiler.commons.TokenType;
import compiler.semantic.CodeGenerator;

public class SyntaxAnalyserImpl {

	SyntaxAutomatonHandler syntaxAutomatonHandler;
	SymbolTable st = SymbolTable.getInstance();

	public SyntaxAnalyserImpl() {

		SyntaxAutomaton programAutomaton;
		SyntaxAutomaton commandAutomaton;
		SyntaxAutomaton expressionAutomaton;

		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(16);
			transitionTable.put(0, new Token(null, TokenType.KW_VOID), new SyntaxState(1, false, 1));
			transitionTable.put(0, new Token(null, TokenType.KW_INT), new SyntaxState(1, false, 1));
			transitionTable.put(0, new Token(null, TokenType.KW_BOOL), new SyntaxState(1, false, 1));
			transitionTable.put(1, new Token(null, TokenType.IDENTIFIER), new SyntaxState(2, false, 2));
			transitionTable.put(2, new Token((int) '(', TokenType.OTHER), 3);
			transitionTable.put(3, new Token(null, TokenType.KW_VOID), new SyntaxState(4, false, 1));
			transitionTable.put(3, new Token(null, TokenType.KW_INT), new SyntaxState(4, false, 1));
			transitionTable.put(3, new Token(null, TokenType.KW_BOOL), new SyntaxState(4, false, 1));
			transitionTable.put(3, new Token((int) ')', TokenType.OTHER), 5);
			transitionTable.put(4, new Token(null, TokenType.IDENTIFIER), new SyntaxState(6, false, 3));
			transitionTable.put(5, new Token((int) '{', TokenType.OTHER), new SyntaxState(7, false, 5));
			transitionTable.put(6, new Token((int) ',', TokenType.OTHER), 8);
			transitionTable.put(6, new Token((int) ')', TokenType.OTHER), 5);
			transitionTable.put(7, new Token(null, TokenType.KW_VOID), new SyntaxState(9, false, 1));
			transitionTable.put(7, new Token(null, TokenType.KW_INT), new SyntaxState(9, false, 1));
			transitionTable.put(7, new Token(null, TokenType.KW_BOOL), new SyntaxState(9, false, 1));
			transitionTable.put(7, new NonTerminalToken(NonTerminalTokenType.COMMAND), 10);
			transitionTable.put(7, new Token((int) '}', TokenType.OTHER), 11);
			transitionTable.put(8, new Token(null, TokenType.KW_VOID), new SyntaxState(4, false, 1));
			transitionTable.put(8, new Token(null, TokenType.KW_INT), new SyntaxState(4, false, 1));
			transitionTable.put(8, new Token(null, TokenType.KW_BOOL), new SyntaxState(4, false, 1));
			transitionTable.put(9, new Token(null, TokenType.IDENTIFIER), new SyntaxState(12, false, 4));
			transitionTable.put(10, new NonTerminalToken(NonTerminalTokenType.COMMAND), 10);
			transitionTable.put(10, new Token((int) '}', TokenType.OTHER), 11);
			transitionTable.put(11, new Token(null, TokenType.KW_VOID), new SyntaxState(1, false, 1));
			transitionTable.put(11, new Token(null, TokenType.KW_INT), new SyntaxState(1, false, 1));
			transitionTable.put(11, new Token(null, TokenType.KW_BOOL), new SyntaxState(1, false, 1));
			transitionTable.put(12, new Token((int) '[', TokenType.OTHER), 13);
			transitionTable.put(12, new Token((int) ';', TokenType.OTHER), new SyntaxState(7, false, 7));
			transitionTable.put(13, new Token(null, TokenType.NUMERIC), new SyntaxState(14, false, 6));
			transitionTable.put(14, new Token((int) ']', TokenType.OTHER), 15);
			transitionTable.put(15, new Token((int) ';', TokenType.OTHER), new SyntaxState(7, false, 7));

			Set<Integer> finals = new HashSet<Integer>();
			finals.add(11);
			programAutomaton = new SyntaxAutomaton(transitionTable, 16, 0, finals);
			programAutomaton.setName("PROGRAMA");
		}

		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(37);
			transitionTable.put(0, new Token(null, TokenType.IDENTIFIER), new SyntaxState(1, false, 8));
			transitionTable.put(0, new Token(null, TokenType.KW_IF), 2);
			transitionTable.put(0, new Token(null, TokenType.KW_WHILE), 3);
			transitionTable.put(0, new Token(null, TokenType.KW_READ), 4);
			transitionTable.put(0, new Token(null, TokenType.KW_WRITE), 5);
			transitionTable.put(0, new Token((int) '{', TokenType.OTHER), 6);
			transitionTable.put(0, new Token(null, TokenType.KW_RETURN), 7);
			transitionTable.put(1, new Token((int) '[', TokenType.OTHER), 8);
			transitionTable.put(1, new Token((int) '=', TokenType.OTHER), new SyntaxState(9, true, 12));
			transitionTable.put(1, new Token((int) '(', TokenType.OTHER), 10);
			transitionTable.put(2, new Token((int) '(', TokenType.OTHER), 26);
			transitionTable.put(3, new Token((int) '(', TokenType.OTHER), 33);
			transitionTable.put(4, new Token((int) '(', TokenType.OTHER), 27);
			transitionTable.put(5, new Token((int) '(', TokenType.OTHER), 22);
			transitionTable.put(6, new NonTerminalToken(NonTerminalTokenType.COMMAND), 14);
			transitionTable.put(6, new Token(null, TokenType.KW_VOID), 15);
			transitionTable.put(6, new Token(null, TokenType.KW_INT), 15);
			transitionTable.put(6, new Token(null, TokenType.KW_BOOL), 15);
			transitionTable.put(6, new Token((int) '}', TokenType.OTHER), 13);
			transitionTable.put(7, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), new SyntaxState(11, false, 11));
			transitionTable.put(8, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), new SyntaxState(24, false, 13));
			transitionTable.put(9, new Token(null, TokenType.IDENTIFIER), new SyntaxState(19, false, 8));
			transitionTable.put(9, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), new SyntaxState(11, false, 14));
			transitionTable.put(10, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), new SyntaxState(12, false, 15));
			transitionTable.put(10, new Token((int) ')', TokenType.OTHER), 11);
			transitionTable.put(11, new Token((int) ';', TokenType.OTHER), 13);
			transitionTable.put(12, new Token((int) ')', TokenType.OTHER), new SyntaxState(11, false, 16));
			transitionTable.put(12, new Token((int) ',', TokenType.OTHER), 16);
			transitionTable.put(14, new NonTerminalToken(NonTerminalTokenType.COMMAND), 14);
			transitionTable.put(14, new Token((int) '}', TokenType.OTHER), 13);
			transitionTable.put(15, new Token(null, TokenType.IDENTIFIER), 17);
			transitionTable.put(16, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), new SyntaxState(12, false, 15));
			transitionTable.put(17, new Token((int) '[', TokenType.OTHER), 18);
			transitionTable.put(17, new Token((int) ';', TokenType.OTHER), 6);
			transitionTable.put(18, new Token(null, TokenType.NUMERIC), 20);
			transitionTable.put(19, new Token((int) '(', TokenType.OTHER), 10);
			transitionTable.put(20, new Token((int) ']', TokenType.OTHER), 21);
			transitionTable.put(21, new Token((int) ';', TokenType.OTHER), 6);
			transitionTable.put(22, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), new SyntaxState(23, false, 11));
			transitionTable.put(23, new Token((int) ')', TokenType.OTHER), 11);
			transitionTable.put(24, new Token((int) ']', TokenType.OTHER), 25);
			transitionTable.put(25, new Token((int) '=', TokenType.OTHER), 9);
			transitionTable.put(26, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), new SyntaxState(28, false, 11));
			transitionTable.put(27, new Token(null, TokenType.IDENTIFIER), 29);
			transitionTable.put(28, new Token((int) ')', TokenType.OTHER), 30);
			transitionTable.put(28, new Token((int) '>', TokenType.OTHER), 26);
			transitionTable.put(28, new Token((int) '<', TokenType.OTHER), 26);
			transitionTable.put(28, new Token(null, TokenType.GREATER_OR_EQUALS), 26);
			transitionTable.put(28, new Token(null, TokenType.LESS_OR_EQUALS), 26);
			transitionTable.put(28, new Token(null, TokenType.EQUALS), 26);
			transitionTable.put(28, new Token(null, TokenType.DIFFERENT), 26);
			transitionTable.put(29, new Token((int) '[', TokenType.OTHER), 31);
			transitionTable.put(29, new Token((int) ')', TokenType.OTHER), 11);
			transitionTable.put(30, new NonTerminalToken(NonTerminalTokenType.COMMAND), 35);
			transitionTable.put(31, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), new SyntaxState(32, false, 11));
			transitionTable.put(32, new Token((int) ']', TokenType.OTHER), 23);
			transitionTable.put(33, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), new SyntaxState(34, false, 11));
			transitionTable.put(34, new Token((int) ')', TokenType.OTHER), 36);
			transitionTable.put(34, new Token((int) '>', TokenType.OTHER), 33);
			transitionTable.put(34, new Token((int) '<', TokenType.OTHER), 33);
			transitionTable.put(34, new Token(null, TokenType.GREATER_OR_EQUALS), 33);
			transitionTable.put(34, new Token(null, TokenType.LESS_OR_EQUALS), 33);
			transitionTable.put(34, new Token(null, TokenType.EQUALS), 33);
			transitionTable.put(34, new Token(null, TokenType.DIFFERENT), 33);
			transitionTable.put(35, new Token(null, TokenType.KW_ELSE), 36);
			transitionTable.put(36, new NonTerminalToken(NonTerminalTokenType.COMMAND), 13);

			Set<Integer> finals = new HashSet<Integer>();
			finals.add(13);
			finals.add(35);
			commandAutomaton = new SyntaxAutomaton(transitionTable, 37, 0, finals);
			commandAutomaton.setName("COMANDO");
		}

		{
			AutomatonTransitionsTable<Token> transitionTable = new AutomatonTransitionsTable<Token>(8);
			transitionTable.put(0, new Token(null, TokenType.NUMERIC), new SyntaxState(1, false, 10));
			transitionTable.put(0, new Token((int) '!', TokenType.OTHER), new SyntaxState(2, false, 10));
			transitionTable.put(0, new Token(null, TokenType.IDENTIFIER), new SyntaxState(3, false, 10));
			transitionTable.put(0, new Token((int) '(', TokenType.OTHER), new SyntaxState(4, false, 10));
			transitionTable.put(0, new Token(null, TokenType.KW_TRUE), new SyntaxState(1, false, 10));
			transitionTable.put(0, new Token(null, TokenType.KW_FALSE), new SyntaxState(1, false, 10));
			transitionTable.put(1, new Token((int) '*', TokenType.OTHER), new SyntaxState(0, false, 10));
			transitionTable.put(1, new Token((int) '/', TokenType.OTHER), new SyntaxState(0, false, 10));
			transitionTable.put(1, new Token(null, TokenType.KW_AND), new SyntaxState(0, false, 10));
			transitionTable.put(1, new Token((int) '+', TokenType.OTHER), new SyntaxState(0, false, 10));
			transitionTable.put(1, new Token(null, TokenType.KW_OR), new SyntaxState(0, false, 10));
			transitionTable.put(2, new Token((int) '!', TokenType.OTHER), new SyntaxState(2, false, 10));
			transitionTable.put(2, new Token(null, TokenType.IDENTIFIER), new SyntaxState(3, false, 10));
			transitionTable.put(2, new Token((int) '(', TokenType.OTHER), new SyntaxState(4, false, 10));
			transitionTable.put(3, new Token((int) '[', TokenType.OTHER), new SyntaxState(6, false, 10));
			transitionTable.put(3, new Token((int) '*', TokenType.OTHER), new SyntaxState(0, false, 10));
			transitionTable.put(3, new Token((int) '/', TokenType.OTHER), new SyntaxState(0, false, 10));
			transitionTable.put(3, new Token(null, TokenType.KW_AND), new SyntaxState(0, false, 10));
			transitionTable.put(3, new Token((int) '+', TokenType.OTHER), new SyntaxState(0, false, 10));
			transitionTable.put(3, new Token((int) '-', TokenType.OTHER), new SyntaxState(0, false, 10));
			transitionTable.put(3, new Token(null, TokenType.KW_OR), new SyntaxState(0, false, 10));
			transitionTable.put(4, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), 5);
			transitionTable.put(5, new Token((int) ')', TokenType.OTHER), new SyntaxState(1, false, 10));
			transitionTable.put(6, new NonTerminalToken(NonTerminalTokenType.EXPRESSION), 7);
			transitionTable.put(7, new Token((int) ']', TokenType.OTHER), new SyntaxState(1, false, 10));

			Set<Integer> finals = new HashSet<Integer>();
			finals.add(1);
			finals.add(3);
			expressionAutomaton = new SyntaxAutomaton(transitionTable, 8, 0, finals);
			expressionAutomaton.setName("EXPRESSAO");
		}

		Map<NonTerminalToken, SyntaxAutomaton> automatons = new HashMap<NonTerminalToken, SyntaxAutomaton>();
		automatons.put(new NonTerminalToken(NonTerminalTokenType.PROGRAM), programAutomaton);
		automatons.put(new NonTerminalToken(NonTerminalTokenType.COMMAND), commandAutomaton);
		automatons.put(new NonTerminalToken(NonTerminalTokenType.EXPRESSION), expressionAutomaton);

		syntaxAutomatonHandler = new SyntaxAutomatonHandler(automatons, new NonTerminalToken(NonTerminalTokenType.PROGRAM));
	}

	public void compile(String filePath) throws IOException {
		long time = System.currentTimeMillis();
		System.out.println("Compila��o iniciada.\n");
		syntaxAutomatonHandler.initialize(FileExtractor.extract(filePath));
		while (!syntaxAutomatonHandler.isComplete()) {
			syntaxAutomatonHandler.step();
		}
		CodeGenerator.getInstance().generateEnvironment();
		System.out.println(CodeGenerator.getInstance().print());
		System.out.println("\nCompila��o finalizada com sucesso em " + (System.currentTimeMillis() - time) + "ms.");
	}
}
