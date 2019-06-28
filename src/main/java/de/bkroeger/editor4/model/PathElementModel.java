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
public class PathElementModel extends SectionModel {

	private static final Logger logger = LogManager.getLogger(PathElementModel.class.getName());

    /**
     * Art des PathElement
     */
	private PathElementType elemType;
	
	/**
	 * Constructor
	 * @param pt
	 */
	public PathElementModel(PathElementType pt) {
		super(SectionModelType.PathElement);
		this.elemType = pt;
	}
	
	/**
	 * Load the section data from JSON
	 * @throws TechnicalException 
	 * @throws InputFileException 
	 */
	public SectionModel loadModel(JSONObject jsonSection, IModel parentModel) 
			throws TechnicalException, InputFileException {
    
    	super.loadModel(jsonSection, this);
    	
    	logger.debug(String.format("PathElement model has %d cells and %d sections",
    			this.cells.size(), this.sections.size()));
    	
		return this;
	}
	
	public DoubleProperty getXProperty() throws TechnicalException, CellCalculationException {
		
		CellModel cell = this.cells.get("X");
		if (cell != null) {
			return cell.getDoubleProperty();
		} else {
			throw new TechnicalException("Cell with type 'X' not found");
		}
	}
	
	public DoubleProperty getYProperty() throws TechnicalException, CellCalculationException {
		
		CellModel cell = this.cells.get("Y");
		if (cell != null) {
			return cell.getDoubleProperty();
		} else {
			throw new TechnicalException("Cell with type 'Y' not found");
		}
	}
}
