package de.bkroeger.editor4.functions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FunctionDef implements IFuncVarConst {
	
	private String name;
	
	private IFunction function;
	
	public FunctionDef(String name, IFunction function) {
		this.name = name;
		this.function = function;
	}
}
