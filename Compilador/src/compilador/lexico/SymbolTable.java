package compilador.lexico;

import java.util.HashMap;

public class SymbolTable {

	private HashMap<String, Integer> table;
	private int higherIndex;

	public SymbolTable() {
		table = new HashMap<String, Integer>();
	}

	public Integer get(String key){
		return table.get(key);
	}
	
	public int put(String identifier) {
		table.put(identifier, higherIndex+1);
		higherIndex++;
		return higherIndex;
	}
	
	public boolean contains(String identifier){
		return table.containsKey(identifier);
	}
	
}
