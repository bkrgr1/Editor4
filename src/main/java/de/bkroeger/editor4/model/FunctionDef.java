package de.bkroeger.editor4.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FunctionDef {
	
	private String name;
	
	private String className;
	
	public FunctionDef(String name, String className) {
		this.name = name;
		this.className = className;
	}
}
