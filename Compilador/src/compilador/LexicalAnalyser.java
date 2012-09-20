package compilador;

import compilador.lexico.LexicalResult;
import compilador.lexico.SymbolTable;

public interface LexicalAnalyser {
	public LexicalResult analyse(SymbolTable symbolTable, String sourceText, int cursor);
}
