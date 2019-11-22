package de.bkroeger.editor4.calculation;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.model.ICell;

public interface ICalculator<T> {

	public T calculate(ICell cell) throws CellCalculationException;

	public FormulaTree scanFormula(String formula, ICell targetCell) throws CellCalculationException;
}
