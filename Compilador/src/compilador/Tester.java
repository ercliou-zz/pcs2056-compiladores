package compilador;

import compilador.lexico.LexicalAnalyserImpl;
import compilador.lexico.LexicalResult;
import compilador.lexico.SymbolTable;

public class Tester {
	public static void main(String[] args) {
		SymbolTable st = new SymbolTable();
		LexicalAnalyser la = new LexicalAnalyserImpl();
		LexicalResult r = la.analyse(st, "teste ", 0);
		System.out.println(r);
	}
}
