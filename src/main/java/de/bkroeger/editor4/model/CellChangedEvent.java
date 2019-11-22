package de.bkroeger.editor4.model;

/**
 * <p>Die Daten eines CellChange-Events.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
public class CellChangedEvent {

	private ICell source;
	public ICell getSource() { return this.source; }
	
	public CellChangedEvent(ICell source) {
		this.source = source;
	}
}
