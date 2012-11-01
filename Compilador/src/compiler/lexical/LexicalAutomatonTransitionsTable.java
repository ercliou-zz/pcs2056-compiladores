package compiler.lexical;

import java.util.regex.Pattern;

import compiler.commons.AutomatonTransitionsTable;

public class LexicalAutomatonTransitionsTable
		extends
			AutomatonTransitionsTable<String> {

	public LexicalAutomatonTransitionsTable(int statesQuantity) {
		super(statesQuantity);
	}

	@Override
	public Integer get(Integer state, String consumable) {
		for (String regex : table.get(state).keySet()) {
			Pattern pattern = Pattern.compile(regex);
			char[] inputString = new char[1];
			inputString[0] = consumable.charAt(0);
			if (pattern.matcher(new String(inputString)).matches()) {
				return table.get(state).get(regex);
			}
		}
		throw new RuntimeException(
				"Erro fatal - O caracter não possui estado reconhecido.");
	}

}
