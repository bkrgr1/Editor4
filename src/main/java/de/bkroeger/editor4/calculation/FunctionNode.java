package de.bkroeger.editor4.calculation;

import de.bkroeger.editor4.functions.FunctionDef;
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
public class FunctionNode  extends FormulaNode {

	private FunctionDef functionDef;
	
	public FunctionNode(FunctionDef functionDef) {
		this.functionDef = functionDef;
	}
}
