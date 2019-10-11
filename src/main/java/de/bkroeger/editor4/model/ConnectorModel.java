package de.bkroeger.editor4.model;

import java.util.HashMap;
import java.util.Map;

public class ConnectorModel {

	private Map<String, CellModel> cells = new HashMap<>();
    
    /**
     * Fügt eine Zelle hinzu
     * @param cell
     */
    public void addCell(CellModel cell) {
    	this.cells.put(cell.getNameU().toLowerCase(), cell);
    }
}
