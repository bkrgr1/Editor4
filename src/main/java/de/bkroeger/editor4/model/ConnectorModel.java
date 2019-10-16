package de.bkroeger.editor4.model;

import org.json.simple.JSONObject;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import javafx.beans.property.DoubleProperty;

public class ConnectorModel extends SectionModel {
	
	public ConnectorModel() {
		super(SectionModelType.Connector);
	}

	public ConnectorModel(SectionModelType st) {
		super(st);
	}
    
    /**
     * Fügt eine Zelle hinzu
     * @param cell
     */
    public void addCell(CellModel cell) {
    	this.cells.put(cell.getNameU().toLowerCase(), cell);
    }
    
    public Double getX() {
    	return cells.get("connectorx").getDoubleValue();
    }
    
    public DoubleProperty getXProperty() throws CellCalculationException {
    	return cells.get("connectorx").getDoubleProperty();
    }
    
    public Double getY() {
    	return cells.get("connectory").getDoubleValue();
    }
    
    public DoubleProperty getYProperty() throws CellCalculationException {
    	return cells.get("connectory").getDoubleProperty();
    }
    
	@Override
	public SectionModel loadModel(JSONObject jsonSection, IModel parentModel) 
			throws TechnicalException, InputFileException {
	
		super.loadModel(jsonSection, this);
		
		return this;
	}
}
