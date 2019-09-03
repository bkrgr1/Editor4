package de.bkroeger.editor4.functions;

public class VariableDef<T> implements IVariable<T> {
	
	private T value;
	public void setValue(T value) { this.value = value; }
	public T getValue() { return this.value; }

	private String name;
	public void setName(String value) { this.name = value; }
	public String getName() { return this.name; }
	
	public VariableDef(String name, T value) {
		this.name = name;
		this.value = value;
	}
}
