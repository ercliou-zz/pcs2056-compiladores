package compiler.lexical;


/**
 * Analisador l�xico
 * 
 */
public interface LexicalAnalyser {

	/**
	 * Recupera um token a partir do texto fonte e um cursor que aponta a partir
	 * de onde o texto deve ser processado, preenchendo a tabela de s�mbolos
	 * passada com os identificadores e strings encontradas
	 * 
	 * @param sourceText
	 *            Texto fonte que ser� analisado
	 * @param cursor
	 *            Posi��o a partir de onde o texto fonte deve ser analisado
	 * @return Um token, contendo um tipo e uma informa��o sobre o token, e uma
	 *         posi��o indicando at� onde o texto fonte foi analisado
	 */
	public LexicalResult analyse(String sourceText, int cursor);
}
