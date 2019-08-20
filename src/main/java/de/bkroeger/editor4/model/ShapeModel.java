package de.bkroeger.editor4.model;

import java.util.UUID;

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

/**
 * <p>Dies ist die Basis-Datenstruktur für alle 2D-Shapes.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
@ToString(callSuper=true)
public class ShapeModel extends SectionModel {

	private static final Logger logger = LogManager.getLogger(ShapeModel.class.getName());
	
	private static final String DIMENSION_KEY = "shapeDimension";
   
    private String shapeDimension;

	/**
	 * Backreferenz auf die Seite
	 */
	protected PageModel page;
	
	/**
	 * Art dieses Shapes
	 */
	protected ShapeType shapeType;
	
	// Constructors
	
	public ShapeModel() {
		super(SectionModelType.Shape);
		this.id = UUID.randomUUID();
	}
	
	public ShapeModel(ShapeType shapeType) {
		super(SectionModelType.Shape);
		this.id = UUID.randomUUID();
		this.shapeType = shapeType;
	}

	/**
	 * Load the section data from JSON
	 * @throws TechnicalException 
	 * @throws InputFileException 
	 */
	public SectionModel loadModel(JSONObject jsonSection, IModel parentModel) 
			throws TechnicalException, InputFileException {
    	
    	for (Object key : jsonSection.keySet()) {
    		String k = key.toString();
    		switch (k) {   			
    		case DIMENSION_KEY:
	    		this.shapeDimension = (String) jsonSection.get(DIMENSION_KEY);
	    		break;
    		default:
    			// skip
    		}
    	}
	
		super.loadModel(jsonSection, this);
    	
    	logger.debug(String.format("Shape model has %d cells and %d sections",
    			this.cells.size(), this.sections.size()));
		
		return this;
	}
	
	public DoubleProperty getLayoutXProperty() throws TechnicalException, CellCalculationException {
		
		CellModel cell = this.cells.get("LayoutX".toLowerCase());
		if (cell != null) {
			return cell.getDoubleProperty();
		} else {
			throw new TechnicalException("Cell with type 'LayoutX' not found");
		}
	}
	
	public DoubleProperty getLayoutYProperty() throws TechnicalException, CellCalculationException {
		
		CellModel cell = this.cells.get("LayoutY".toLowerCase());
		if (cell != null) {
			return cell.getDoubleProperty();
		} else {
			throw new TechnicalException("Cell with type 'LayoutY' not found");
		}
	}
}
