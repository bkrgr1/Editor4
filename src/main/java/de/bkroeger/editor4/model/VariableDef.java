package de.bkroeger.editor4.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VariableDef {
	
	private String name;
	
	private String variable;
	
	public VariableDef(String name, String variable) {
		this.name = name;
		this.variable = variable;
	}
}
