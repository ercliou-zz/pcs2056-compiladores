package compilador.lexico;

/**
 * Analisador léxico
 * 
 */
public interface LexicalAnalyser {

	/**
	 * Recupera um token a partir do texto fonte e um cursor que aponta a partir
	 * de onde o texto deve ser processado, preenchendo a tabela de símbolos
	 * passada com os identificadores e strings encontradas
	 * 
	 * @param symbolTable
	 *            Tabela de símbolos onde serão mapeados os identificadores e
	 *            strings encontrados
	 * @param sourceText
	 *            Texto fonte que será analisado
	 * @param cursor
	 *            Posição a partir de onde o texto fonte deve ser analisado
	 * @return Um token, contendo um tipo e uma informação sobre o token, e uma
	 *         posição indicando até onde o texto fonte foi analisado
	 */
	public LexicalResult analyse(SymbolTable symbolTable, String sourceText,
			int cursor);
}
