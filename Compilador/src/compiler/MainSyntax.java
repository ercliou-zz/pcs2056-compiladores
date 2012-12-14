package compiler;

import java.io.IOException;

import compiler.commons.SymbolTable;
import compiler.syntax.SyntaxAnalyserImpl;

public class MainSyntax {

	/**
	 * Programa principal para testar o analisador léxico
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		SyntaxAnalyserImpl sa = new SyntaxAnalyserImpl();

		sa.compile(args[0],args[1]);
		System.out.println();
		System.out.println(SymbolTable.getInstance());
	}
}
