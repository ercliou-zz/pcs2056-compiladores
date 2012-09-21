package compilador;

import java.util.Map;
import java.util.regex.Pattern;

public class State {
	
	private int id;
	private boolean isFinalState;
	private Map<String,Integer> transitions;
	
	public State(int id, Map<String, Integer> transitions, boolean isFinalState) {
		this.id = id;
		this.transitions = transitions;
	}
	
	public int nextState(char input){
//		System.out.print(id + " -> ");
		for (String key : transitions.keySet()) {
			Pattern pattern = Pattern.compile(key);
			char [] inputString = new char[1];
			inputString[0] = input;
			if(pattern.matcher(new String(inputString)).matches()){
//				System.out.println(transitions.get(key));
				return transitions.get(key);
			}
		}
		throw new RuntimeException("Erro fatal - O caracter não possui estado reconhecido.");
	}
	
	public int getId(){
		return id;
	}
	
	public boolean isFinalState(){
		return isFinalState; 
	}
	
}
