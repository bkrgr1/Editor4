package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.calculation.CalculatorService;
import de.bkroeger.editor4.exceptions.CellCalculationException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 * <p>
 * Eine ColorCell ist eine Implementierung von {@link ICell}.
 * </p>
 *
 * @author berthold.kroeger@gmx.de
 */
public class ColorCell extends SimpleObjectProperty<Color> implements ICell, ICellChangedListener {

    private static final Logger logger = LogManager.getLogger(ColorCell.class.getName());

	/**
	 * ========================================================================
	 * Fields
	 * =======================================================================
	 */

	/**
	 * Flag, ob die Zell-Formel (noch) gültig ist.
	 */
	protected boolean isFormulaValid = true;

	/**
	 * Setzt das Flag 'isFormulaValid'.
	 * 
	 * @param value
	 */
	public void setIsFormulaValid(boolean value) {
		this.isFormulaValid = value;
	}

	public boolean isFormulaValid() {
		return this.isFormulaValid;
	}

	/**
	 * <p>
	 * Liste der Listener, die diese Zelle in einer Formel verwenden.
	 * </p>
	 */
	protected List<ICellChangedListener> cellChangedListeners = new ArrayList<>();

	public void addCellChangedListener(ICellChangedListener listener) {
		cellChangedListeners.add(listener);
	}

	public void removeCellChangedListener(ICellChangedListener listener) {
		cellChangedListeners.remove(listener);
	}

	/**
	 * <p>
	 * Die Formel zur Berechnung des Zellwertes.
	 * </p>
	 */
	protected String formula;

	public String getFormula() {
		return this.formula;
	}

	/**
	 * ========================================================================
	 * Constructors
	 * =======================================================================
	 */

	public ColorCell() {
		super();
	}

	public ColorCell(IModel model, String name) {
		super(model, name);
	}

	public ColorCell(IModel model, String name, Color value) {
		super(model, name, value);
	}

	/**
	 * ========================================================================
	 * Public methods
	 * =======================================================================
	 */

	/**
	 * Liefert das {@link IModel}, zu dem diese Zelle gehört.
	 */
	@Override
	public IModel getModel() {
		return (IModel) getBean();
	}

	/**
	 * Setzt den Wert des Properties und informiert die Listeners.
	 * 
	 * @param newValue
	 */
	public void set(Color newValue) {
		super.set(newValue);
		cellChangedListeners.stream().forEach(new CellChangedConsumer(this));
	}

	/**
	 * Liefert den primitiven Wert der Zelle. Wenn notwendig wird der Zellwert neu
	 * berechnet.
	 */
	public Color get() {
		if (this.formula != null && !isFormulaValid) {
			try {
				calculateFormula();
			} catch (CellCalculationException e) {
				logger.error(e.getMessage(), e);
				return Color.TRANSPARENT;
			}
		}
		return super.get();
	}

	/**
	 * Liefert den Wert der Zelle. Wenn notwendig wird der Zellwert neu berechnet.
	 */
	public Color getValue() {
		if (this.formula != null && !isFormulaValid) {
			try {
				calculateFormula();
			} catch (CellCalculationException e) {
				logger.error(e.getMessage(), e);
				return null;
			}
		}
		return super.getValue();
	}

	/**
	 * Der Wert dieser Zelle hat sich geändert. Informiere alle anderen Zellen, die
	 * diese Zelle in ihrer Formel referenzieren.
	 */
	@Override
	protected void invalidated() {
		cellChangedListeners.stream().forEach(new CellChangedConsumer(this));
	}

	/**
	 * Eine andere Zelle, die in der Formel dieser Zelle referenziert wird, hat sich
	 * geändert. Die Formel muss neu ausgewertet werden.
	 */
	@Override
	public void formulaCellChanged(CellChangedEvent event) {
		if (isFormulaValid) {
			this.isFormulaValid = false;
			invalidated();
			fireValueChangedEvent();

			// auch die Verwender dieser Zelle informieren
			cellChangedListeners.stream().forEach(new CellChangedConsumer(this));
		}
	}

//=========================================================================

	/**
	 * Eine Consumer-Implementierung für die Notification von Zellen.
	 *
	 * @author berthold.kroeger@gmx.de
	 */
	public class CellChangedConsumer implements Consumer<ICellChangedListener> {

		private ICell cell;

		/**
		 * Constructor
		 * 
		 * @param cell
		 */
		public CellChangedConsumer(ICell cell) {
			this.cell = cell;
		}

		/**
		 * Implementierung
		 */
		@Override
		public void accept(ICellChangedListener listener) {
			CellChangedEvent cellChangedEvent = new CellChangedEvent(cell);
			listener.formulaCellChanged(cellChangedEvent);
		}
	}

	/**
	 * ========================================================================
	 * Private methods
	 * =======================================================================
	 */

	/**
	 * Berechnet die Formel und speichert das Ergebnis.
	 * @throws CellCalculationException 
	 */
	private void calculateFormula() throws CellCalculationException {
		CalculatorService<Color> calculator = new CalculatorService<Color>();
		this.setValue(calculator.calculate(this));
	}
}
