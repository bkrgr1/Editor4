package de.bkroeger.editor4.calculation;

import de.bkroeger.editor4.model.ICell;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Ein Knoten in einem FormulaTree.
 *
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
@ToString(callSuper=true)
public class VariableNode  extends FormulaNode {

	private String name;
	
	private ICell sourceCell;
	
	private ICell targetCell;
	
	public VariableNode() { }
	
	public VariableNode(String name) {
		this.name = name;
	}
}
