package de.bkroeger.editor4.model;

/**
 * <p>Eine ICell ist ein beliebiges Property eines beliebigen {@link IModel}.</p>
 * @author berthold.kroeger@gmx.de
 */
public interface ICell {
	
	// String name
	public String getName();
	
	// String nameU
	public String getNameU();

	/**
	 * <p>Referenz auf das {@link IModel}, zu dem die Zelle gehört.</p>
	 * @return das zugehörige {@link IModel}
	 */
	public IModel getModel();
	
	/**
	 * Setzt das Flag 'isFormulaValid'.
	 * @param value neuer Wert des Flags
	 */
	public void setIsFormulaValid(boolean value);
	
	public boolean isFormulaValid();
	
	public String getFormula();
}
