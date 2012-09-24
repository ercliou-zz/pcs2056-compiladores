package compilador;

import java.io.IOException;

import compilador.lexico.LexicalAnalyser;
import compilador.lexico.LexicalAnalyserImpl;
import compilador.lexico.LexicalResult;
import compilador.lexico.SymbolTable;

public class Main {
	public static void main(String[] args) throws IOException {
		
		SymbolTable st = new SymbolTable();
		LexicalAnalyser la = new LexicalAnalyserImpl();
		
		
		LexicalResult r = LexicalResult.startResult();
		while (r != null) {
			r = la.analyse(st, FileExtractor.extract("source.txt"),
					r.getCursor());
			System.out.println(r);
		}
		System.out.println(st);

	}
}
