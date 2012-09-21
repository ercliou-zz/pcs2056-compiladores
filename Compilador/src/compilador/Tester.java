package compilador;

import compilador.lexico.LexicalAnalyserImpl;
import compilador.lexico.LexicalResult;
import compilador.lexico.SymbolTable;

public class Tester {
	public static void main(String[] args) {
		SymbolTable st = new SymbolTable();
		LexicalAnalyser la = new LexicalAnalyserImpl();
		LexicalResult r = new LexicalResult();
		r.setToken(null);
		r.setCursor(0);
		while (r != null) {
			r = la.analyse(st, "teste batata teste != <= == - if teste1 1002\n",
					r.getCursor());
			System.out.println(r);
		}

	}
}
