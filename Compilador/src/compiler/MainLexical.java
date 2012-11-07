package compiler;

import java.io.IOException;

import compiler.commons.FileExtractor;
import compiler.lexical.LexicalAnalyser;
import compiler.lexical.LexicalAnalyserImpl;
import compiler.lexical.LexicalResult;


public class MainLexical {
	
	/**
	 * Programa principal para testar o analisador l�xico
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		LexicalAnalyser la = new LexicalAnalyserImpl();

		LexicalResult r = LexicalResult.startResult();
		while (r != null) {
			// Extra��o da cadeia de caracteres do arquivo fonte
			String sourceText = FileExtractor.extract(args[0]);
			if (r != null && r.getToken() != null) {
				// Imprimindo o token gerado
				System.out.println(r.getToken());
			}
			// Chamada do analisador l�xico, recuperando um token e a posi��o
			// at� onde o texto fonte foi lido
			r = la.analyse(sourceText, r.getCursor());
		}

		// Imprimindo a tabela de s�mbolos
		System.out.println("\n" + SymbolTable.getInstance());

	}
}
