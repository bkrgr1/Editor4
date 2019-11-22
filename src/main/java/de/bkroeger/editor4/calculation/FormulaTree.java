package de.bkroeger.editor4.calculation;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
public class FormulaTree extends FormulaNode {

	public FormulaTree() {
		this.parentNode = null;
	}
	
	/**
	 * Constructor
	 * @param nodes
	 */
	public FormulaTree(List<IFormulaNode> nodes) {
		this.parentNode = null;
		this.children.addAll(nodes);
	}
}