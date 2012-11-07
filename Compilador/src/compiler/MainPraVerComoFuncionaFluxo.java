package compiler;

import java.io.IOException;

import compiler.syntax.SyntaxAnalyserImpl;

public class MainPraVerComoFuncionaFluxo {

	/**
	 * Programa principal para testar o analisador l�xico
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		SyntaxAnalyserImpl sa = new SyntaxAnalyserImpl();

		sa.compile(args[0]);
		System.out.println();
		System.out.println(SymbolTable.getInstance());
	}
}
