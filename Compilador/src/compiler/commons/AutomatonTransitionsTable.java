package compiler.commons;

import java.util.Hashtable;
import java.util.Set;

public class AutomatonTransitionsTable<C> {
	protected Hashtable<Integer, Hashtable<C, State>> table;

	public AutomatonTransitionsTable(int statesQuantity) {
		table = new Hashtable<Integer, Hashtable<C,State>>();
		for (int i = 0; i < statesQuantity; i++) {
			table.put(i, new Hashtable<C,State>());
		}
	}

	public void put(Integer state, C consumable, State nextState) {
		table.get(state).put(consumable, nextState);
	}
	
	public void put(Integer state, C consumable, int nextState) {
		table.get(state).put(consumable, new SimpleState(nextState));
	}
	
	public State get(Integer state, C consumable){
		Hashtable<C,State> secondaryTable = table.get(state);
		if(secondaryTable != null && consumable != null){
			return secondaryTable.get(consumable);
		}
		return null;
	}
	
	public Set<C> getPossibleConsumables(Integer state){
		return table.get(state).keySet();
	}

}
