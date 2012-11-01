package compiler.commons;

import java.util.Hashtable;

public class AutomatonTransitionsTable<C> {
	protected Hashtable<Integer, Hashtable<C, Integer>> table;

	public AutomatonTransitionsTable(int statesQuantity) {
		table = new Hashtable<Integer, Hashtable<C,Integer>>();
		for (int i = 0; i < statesQuantity; i++) {
			table.put(i, new Hashtable<C,Integer>());
		}
	}

	public void put(Integer state, C consumable, Integer nextState) {
		table.get(state).put(consumable, nextState);
	}
	
	public Integer get(Integer state, C consumable){
		Hashtable<C,Integer> secondaryTable = table.get(state);
		if(secondaryTable != null){
			return secondaryTable.get(consumable);
		}
		return null;
	}

}
