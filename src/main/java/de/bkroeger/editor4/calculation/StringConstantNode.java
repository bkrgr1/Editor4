package de.bkroeger.editor4.calculation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
public class StringConstantNode extends FormulaNode {
	
	private String value;
	
	public StringConstantNode(String value) {
		this.value = value;
	}
}
