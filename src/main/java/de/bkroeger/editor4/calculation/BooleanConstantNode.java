package de.bkroeger.editor4.calculation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
public class BooleanConstantNode extends FormulaNode {
	
	private Boolean value;
	
	public BooleanConstantNode(Boolean value) {
		this.value = value;
	}
}
