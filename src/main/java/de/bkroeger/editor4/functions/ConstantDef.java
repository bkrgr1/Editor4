package de.bkroeger.editor4.functions;

public class ConstantDef<T> implements IConstant<T> {
	
	private T value;
	public void setValue(T value) { this.value = value; }

	@Override
	public T getValue() { return this.value; }

	public ConstantDef(T value) {
		this.value = value;
	}
}
