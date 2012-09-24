package compilador.lexico;



public interface LexicalAnalyser {
	public LexicalResult analyse(SymbolTable symbolTable, String sourceText, int cursor);
}
