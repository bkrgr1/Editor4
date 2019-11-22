package de.bkroeger.editor4.calculation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
public class DoubleConstantNode extends FormulaNode {
	
	private Double value;
	
	public DoubleConstantNode(Double value) {
		this.value = value;
	}
}
