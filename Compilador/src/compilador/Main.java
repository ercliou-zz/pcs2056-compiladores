package compilador;

import java.io.IOException;

import compilador.lexico.LexicalAnalyser;
import compilador.lexico.LexicalAnalyserImpl;
import compilador.lexico.LexicalResult;
import compilador.lexico.SymbolTable;

public class Main {
	
	/**
	 * Programa principal para testar o analisador léxico
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		SymbolTable st = new SymbolTable();
		LexicalAnalyser la = new LexicalAnalyserImpl();

		LexicalResult r = LexicalResult.startResult();
		while (r != null) {
			// Extração da cadeia de caracteres do arquivo fonte
			String sourceText = FileExtractor.extract("source.txt");
			if (r != null && r.getToken() != null) {
				// Imprimindo o token gerado
				System.out.println(r.getToken());
			}
			// Chamada do analisador léxico, recuperando um token e a posição
			// até onde o texto fonte foi lido
			r = la.analyse(st, sourceText, r.getCursor());
		}

		// Imprimindo a tabela de símbolos
		System.out.println("\n" + st);

	}
}
