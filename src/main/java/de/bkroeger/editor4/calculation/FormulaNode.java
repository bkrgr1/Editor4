package de.bkroeger.editor4.calculation;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
public class FormulaNode implements IFormulaNode {

	protected IFormulaNode parentNode;
	@Override
	public IFormulaNode getParentNode() {
		return this.parentNode;
	}

	protected List<IFormulaNode> children = new ArrayList<>();;
	@Override
	public List<IFormulaNode> getChildNodes() {
		return this.children;
	}
	@Override 
	public void addChildNode(IFormulaNode node) {
		this.children.add(node);
	}
}
