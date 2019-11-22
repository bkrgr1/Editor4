package de.bkroeger.editor4.calculation;

import java.util.List;

public interface IFormulaNode {

	public IFormulaNode getParentNode();
	
	public List<IFormulaNode> getChildNodes();
	
	public void addChildNode(IFormulaNode node);
}
