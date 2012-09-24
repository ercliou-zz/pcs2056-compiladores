package compilador.lexico;

import java.util.ArrayList;
import java.util.List;

public class SymbolTable {

	private List<String> table;

	public SymbolTable() {
		table = new ArrayList<String>();
	}
	public Integer get(String key) {
		return table.indexOf(key);
	}

	public int put(String identifier) {
		table.add(identifier);
		return table.indexOf(identifier);
	}

	public boolean contains(String identifier) {
		return table.contains(identifier);
	}

	@Override
	public String toString() {
		String stringForm = "Symbol Table:\n";
		for (String entry : table) {
			stringForm += (table.indexOf(entry) + " = " + entry + "\n");
		}
		return stringForm;
	}

}
