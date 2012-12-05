package compiler.semantic;

import java.util.Stack;

public class OperatorsStack {

	private static OperatorsStack INSTANCE;
	private Stack<Object> stack;

	private OperatorsStack() {
		stack = new Stack<Object>();
	}

	public static OperatorsStack getInstance() {
		if (INSTANCE == null)
			INSTANCE = new OperatorsStack();
		return INSTANCE;
	}

	public void push(Object element) {
		stack.push(element);
	}

	public Object pop() {
		return stack.pop();
	}

	public Object peek() {
		if (stack.isEmpty())
			return null;
		return stack.peek();
	}
	
	public int getSize(){
		return stack.size();
	}
}
