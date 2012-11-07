package compiler.commons;


public class SimpleState implements State{
	private int state;

	public SimpleState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + state;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleState other = (SimpleState) obj;
		if (state != other.state)
			return false;
		return true;
	}
	
}
