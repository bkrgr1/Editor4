package de.bkroeger.editor4.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DoubleCell_UnitTests {

	@Test
	public void whenSimpleCreate_thenSuccess() {
		
		DoubleCell cell = new DoubleCell();
		assertEquals("Name must be empty", "", cell.getName());
		assertEquals("Bean must be empty", null, cell.getModel());
		assertEquals("Double value should be 0.0", 0.0, cell.get(), 0.001);
		assertEquals("DoubleValue should be 0.0", new Double(0.0), cell.getValue());
	}
	
	@Test
	public void whenComplexCreate_thenSuccess() {
		
		IModel bean = new PageModel();
		DoubleCell cell = new DoubleCell(bean, "test", 3.3);
		assertEquals("Name must be 'test'", "test", cell.getName());
		assertEquals("Bean must be a PageModel", bean, cell.getModel());
		assertEquals("Double value should be 3.3", 3.3, cell.get(), 0.001);
		assertEquals("DoubleValue should be 3.3", new Double(3.3), cell.getValue());
	}
	
	@Test
	public void whenThereIsAListener_thenIMustBeNofified() {
		
		DoubleCell cell2 = new DoubleCell(0.0);
		TestListener listener = new TestListener();
		cell2.addCellChangedListener(listener);
		cell2.set(2.2);
		cell2.removeCellChangedListener(listener);
		cell2.set(4.4);
	}
	
	public class TestListener implements ICellChangedListener {

		@Override
		public void formulaCellChanged(CellChangedEvent event) {
			ICell eventCell = event.getSource();
			assertTrue("Instance of DoubleCell", eventCell instanceof DoubleCell);
			System.out.println("Value of referencedCell = "+((DoubleCell)eventCell).getValue());
		}
		
	}
}
