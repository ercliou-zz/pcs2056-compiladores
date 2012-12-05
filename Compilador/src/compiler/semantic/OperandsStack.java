package compiler.semantic;

import java.util.Stack;

public class OperandsStack {

	private static OperandsStack operandsStack;
	private Stack<Object> stack;

	private OperandsStack() {
		stack = new Stack<Object>();
	}

	public static OperandsStack getInstance() {
		if (operandsStack == null) {
			operandsStack = new OperandsStack();
		}
		return operandsStack;
	}

	public void push(Object element) {
		stack.push(element);
	}

	public Object pop() {
		return stack.pop();
	}

	public Object peek() {
		if (!stack.isEmpty())
			return stack.peek();
		return null;
	}

	public int getSize() {
		return stack.size();
	}
}
