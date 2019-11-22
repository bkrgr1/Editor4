package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.List;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import javafx.beans.property.DoubleProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
public class ConnectorModel extends BaseModel {
		
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	protected List<CellModel> cells = new ArrayList<>();
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/

	public ConnectorModel() {
		super(ModelType.Connector);
	}

	public ConnectorModel(ModelType st) {
		super(st);
	}
    	
	/**========================================================================
	 * Public methods
	 *=======================================================================*/

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
}
