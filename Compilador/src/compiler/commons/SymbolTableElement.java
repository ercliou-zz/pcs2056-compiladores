package compiler.commons;

public class SymbolTableElement {
	private String name;
	private IdentifierType type;
	private boolean isArray;
	private boolean isFunction;
	private boolean isFunctionParameter;
	private int ownerFunction;
	private int parameterOrder;

	public SymbolTableElement(String name, IdentifierType type) {
		super();
		this.name = name;
		this.type = type;
		isArray = false;
		isFunction = false;
		isFunctionParameter = false;
	}

	public void setFunction(boolean isFunction) {
		this.isFunction = isFunction;
	}

	public void setFunctionParameter(boolean isFunctionParameter) {
		this.isFunctionParameter = isFunctionParameter;
	}

	public void setOwnerFunction(int ownerFunction) {
		this.ownerFunction = ownerFunction;
	}

	public void setParameterOrder(int parameterOrder) {
		this.parameterOrder = parameterOrder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IdentifierType getType() {
		return type;
	}

	public void setType(IdentifierType type) {
		this.type = type;
	}

	public boolean isFunction() {
		return isFunction;
	}

	public boolean isFunctionParameter() {
		return isFunctionParameter;
	}

	public int getOwnerFunction() {
		return ownerFunction;
	}

	public int getParameterOrder() {
		return parameterOrder;
	}

	public boolean isArray() {
		return isArray;
	}

	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		SymbolTableElement other = (SymbolTableElement) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
