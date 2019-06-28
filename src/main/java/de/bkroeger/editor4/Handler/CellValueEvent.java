package de.bkroeger.editor4.Handler;

public class CellValueEvent {
	
	@SuppressWarnings("unused")
	private String cellKey;

	public CellValueEvent(String key) {
		this.cellKey = key;
	}
}
