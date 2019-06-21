package de.bkroeger.editor4.Handler;

import de.bkroeger.editor4.model.CellModelType;

public class CellValueEvent {
	
	private String cellKey;

	public CellValueEvent(String key) {
		this.cellKey = key;
	}
}
