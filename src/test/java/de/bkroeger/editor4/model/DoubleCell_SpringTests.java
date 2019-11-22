package de.bkroeger.editor4.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import de.bkroeger.editor4.TestConfiguration;
import de.bkroeger.editor4.calculation.CalculatorService;
import de.bkroeger.editor4.calculation.DoubleConstantNode;
import de.bkroeger.editor4.calculation.FormulaTree;
import de.bkroeger.editor4.calculation.FunctionNode;
import de.bkroeger.editor4.calculation.IFormulaNode;
import de.bkroeger.editor4.calculation.StringConstantNode;
import de.bkroeger.editor4.calculation.VariableNode;
import de.bkroeger.editor4.exceptions.CellCalculationException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
public class DoubleCell_SpringTests {
	
	@Autowired
	@Qualifier("doubleCalculator")
	private CalculatorService<Double> calculator;
	
	@Test
	public void whenInjectCalculatorBean_thenNotNull() {
		assertNotNull("CalculatorService bean not injected", calculator);
	}
	
	@Test
	public void whenScanStringConstant_thenReturnTree() throws CellCalculationException {
		
		FormulaTree tree = null;
		IFormulaNode node = null;
		ICell cell = new DoubleCell();
		tree = calculator.scanFormula("string", cell);
		assertEquals("Invalid number of children", 1, tree.getChildNodes().size());
		node = tree.getChildNodes().get(0);
		assertTrue("Invalid child type", node instanceof StringConstantNode);
	}
	
	@Test
	public void whenScanDoubleConstant_thenReturnTree() throws CellCalculationException {
		
		FormulaTree tree = null;
		IFormulaNode node = null;
		ICell cell = new DoubleCell();
		tree = calculator.scanFormula("22.22", cell);
		assertEquals("Invalid number of children", 1, tree.getChildNodes().size());
		node = tree.getChildNodes().get(0);
		assertTrue("Invalid child type", node instanceof DoubleConstantNode);
	}
	
	@Test
	public void whenScanVariable_thenReturnTree() throws CellCalculationException {
		
		FormulaTree tree = null;
		IFormulaNode node = null;
		ICell cell = new DoubleCell();
		tree = calculator.scanFormula("${Page1.PageNo}", cell);
		assertEquals("Invalid number of children", 1, tree.getChildNodes().size());
		node = tree.getChildNodes().get(0);
		assertTrue("Invalid child type", node instanceof VariableNode);
		VariableNode fNode = (VariableNode)node;
		assertEquals("Name der Variablen falsch", "Page1.PageNo", fNode.getName());
	}
	
	@Test
	public void whenScanFunction1_thenReturnTree() throws CellCalculationException {
		
		FormulaTree tree = null;
		IFormulaNode node = null;
		ICell cell = new DoubleCell();
		tree = calculator.scanFormula("text(123)", cell);
		assertEquals("Invalid number of children", 1, tree.getChildNodes().size());
		node = tree.getChildNodes().get(0);
		assertTrue("Invalid child type", node instanceof FunctionNode);
		FunctionNode fNode = (FunctionNode)node;
		assertEquals("Name der Funktion falsch", "text", fNode.getFunctionDef().getName());
		assertEquals("Function node hat nicht 1 Parameter", 1, node.getChildNodes().size());
	}
	
	@Test
	public void whenScanComplexFunction_thenReturnTree() throws CellCalculationException {
		
		FormulaTree tree = null;
		IFormulaNode node1_1 = null;
		ICell cell = new DoubleCell();
		tree = calculator.scanFormula("concat('Page ',text(int(${PageNo})))", cell);
		assertEquals("Invalid number of children", 1, tree.getChildNodes().size());
		node1_1 = tree.getChildNodes().get(0);
		assertTrue("Invalid child type", node1_1 instanceof FunctionNode);
		FunctionNode fNode = (FunctionNode)node1_1;
		assertEquals("Name der Funktion falsch", "concat", fNode.getFunctionDef().getName());
		assertEquals("Function node hat nicht 2 Parameter", 2, node1_1.getChildNodes().size());
		IFormulaNode node2_1 = node1_1.getChildNodes().get(0);
		IFormulaNode node2_2 = node1_1.getChildNodes().get(1);
		assertTrue("Invalid child type", node2_1 instanceof StringConstantNode);
		assertTrue("Invalid child type", node2_2 instanceof FunctionNode);
	}
}
