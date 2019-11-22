package de.bkroeger.editor4.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import javafx.beans.property.DoubleProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
public class LocationModel extends BaseModel {

    @SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(LocationModel.class.getName());
	
	/**
	 * Constructor
	 */
	public LocationModel() {
		super(ModelType.Location);
	}

	/**
	 * Load the section data from JSON
	 * @throws TechnicalException 
	 * @throws InputFileException 
	 */
	public BaseModel loadModel(JSONObject jsonSection, IModel parentModel) 
			throws TechnicalException, InputFileException {
    
    	super.loadModel(jsonSection, this);
    	
		return this;
	}
	
	public DoubleProperty getLayoutXProperty() throws TechnicalException, CellCalculationException {
		
		CellModel cell = this.cells.get("LayoutX");
		if (cell != null) {
			return cell.getDoubleProperty();
		} else {
			throw new TechnicalException("Cell with type 'LayoutX' not found");
		}
	}
	
	public DoubleProperty getLayoutYProperty() throws TechnicalException, CellCalculationException {
		
		CellModel cell = this.cells.get("LayoutY");
		if (cell != null) {
			return cell.getDoubleProperty();
		} else {
			throw new TechnicalException("Cell with type 'LayoutY' not found");
		}
	}
}
