package compiler.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * Tabela de símbolos, contém o mapeamento dos símbolos reconhecidos no texto
 * fonte com os ID's atribuídos à eles
 * 
 */
public class SymbolTable {

	private static SymbolTable INSTANCE;

	private List<SymbolTableElement> table;

	private SymbolTable() {
		table = new ArrayList<SymbolTableElement>();
	}

	public static SymbolTable getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SymbolTable();
		}
		return INSTANCE;
	}

	public Integer get(String key) {
		return table.indexOf(new SymbolTableElement(key, null));
	}

	public SymbolTableElement getElementById(int id) {
		return table.get(id);
	}

	public int put(String identifier) {
		table.add(new SymbolTableElement(identifier, null));
		return table.indexOf(new SymbolTableElement(identifier, null));
	}

	public boolean contains(String identifier) {
		for (SymbolTableElement element : table) {
			if (element.getName().equals(identifier)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		String stringForm = "Symbol Table:\n";
		for (SymbolTableElement entry : table) {
			stringForm += (table.indexOf(entry) + " = " + entry.getType() + "\t" + entry.getName() + "\n");
		}
		return stringForm;
	}

}
