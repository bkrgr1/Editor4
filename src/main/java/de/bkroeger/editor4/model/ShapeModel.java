package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
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
	private static final String CONNECTORS_KEY = "connectors";
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
   
    private String shapeDimension;

	/**
	 * Backreferenz auf die Seite
	 */
	protected PageModel page;
	
	/**
	 * Art dieses Shapes
	 */
	protected ShapeType shapeType;
	
	/**
	 * Art der Linie
	 */
	protected LineType lineType;
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/
	
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
	 * Clone-Controller
	 * @param model ein {@link ShapeModel}
	 * @throws CellCalculationException 
	 */
	public ShapeModel(ShapeModel model)  {
		super(model.getSectionType());
		
		this.page = model.page;
		this.shapeDimension = model.shapeDimension;
		this.shapeType = model.shapeType;
		
		// Sections kopieren
		
		// Cells kopieren
	}
	
	/**========================================================================
	 * Public methods
	 *=======================================================================*/

	/**
	 * Load the section data from JSON
	 * @throws TechnicalException 
	 * @throws InputFileException 
	 */
	@Override
	public SectionModel loadModel(JSONObject jsonSection, IModel parentModel) 
			throws TechnicalException, InputFileException {
    	
    	for (Object key : jsonSection.keySet()) {
    		String k = key.toString();
    		switch (k) {   			
    		case DIMENSION_KEY:
	    		this.shapeDimension = (String) jsonSection.get(DIMENSION_KEY);
	    		break;
    		case CONNECTORS_KEY:
    	    	JSONArray jsonConnectors = (JSONArray) jsonSection.get(CONNECTORS_KEY);
    	    	for (int j=0; j<jsonConnectors.size(); j++) {
    	    		
    	    		JSONObject jsonConnector = (JSONObject) jsonConnectors.get(j);
    	    		ConnectorModel connectorModel = new ConnectorModel(); 	    		
    	    		connectorModel.loadModel(jsonConnector, this);
        	    	this.sections.add(connectorModel);
    	    	}
    			break;
    		default:
    			// skip
    		}
    	}
	
		super.loadModel(jsonSection, this);
    	
    	logger.debug(() -> String.format("Shape model has %d cells and %d sections",
    			this.cells.size(), this.sections.size()));	// Java 8 Supplier lazily evaluated
		
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
	
	public double getLayoutX() throws TechnicalException, CellCalculationException {
		return getLayoutXProperty().get();
	}
	
	public void setLayoutX(double value) throws TechnicalException, CellCalculationException {
		this.getLayoutXProperty().set(value);
	}
	
	public DoubleProperty getLayoutYProperty() throws TechnicalException, CellCalculationException {
		
		CellModel cell = this.cells.get("LayoutY".toLowerCase());
		if (cell != null) {
			return cell.getDoubleProperty();
		} else {
			throw new TechnicalException("Cell with type 'LayoutY' not found");
		}
	}
	
	public double getLayoutY() throws TechnicalException, CellCalculationException {
		return getLayoutYProperty().get();
	}
	
	public void setLayoutY(double value) throws TechnicalException, CellCalculationException {
		this.getLayoutYProperty().set(value);
	}
	
	public DoubleProperty getCenterXProperty() throws TechnicalException, CellCalculationException {
		
		CellModel cell = this.cells.get("CenterX".toLowerCase());
		if (cell != null) {
			return cell.getDoubleProperty();
		} else {
			throw new TechnicalException("Cell with type 'CenterX' not found");
		}
	}
	
	public DoubleProperty getCenterYProperty() throws TechnicalException, CellCalculationException {
		
		CellModel cell = this.cells.get("CenterY".toLowerCase());
		if (cell != null) {
			return cell.getDoubleProperty();
		} else {
			throw new TechnicalException("Cell with type 'CenterY' not found");
		}
	}
	
	public DoubleProperty getWidthProperty() throws TechnicalException, CellCalculationException {
		
		CellModel cell = this.cells.get("Width".toLowerCase());
		if (cell != null) {
			return cell.getDoubleProperty();
		} else {
			throw new TechnicalException("Cell with type 'Width' not found");
		}
	}
	
	public DoubleProperty getHeightProperty() throws TechnicalException, CellCalculationException {
		
		CellModel cell = this.cells.get("Height".toLowerCase());
		if (cell != null) {
			return cell.getDoubleProperty();
		} else {
			throw new TechnicalException("Cell with type 'Height' not found");
		}
	}
	
	public void acceptChanges(ShapeModel model) {
		// TODO: Änderungen kopieren
	}
	
	/**
	 * Liefert die Sections vom Typ {@link ConnectorModel}.
	 * @return eine Liste der Connectors
	 */
	public List<ConnectorModel> getConnectors() {
		List<ConnectorModel> connectors = new ArrayList<>();
		for (SectionModel section : this.sections) {
			if (section instanceof ConnectorModel) {
				connectors.add((ConnectorModel)section);
			}
		}
		return connectors;
	}

	public static ShapeModel buildShapeFromTemplate(ShapeTemplate shapeTemplate, PageModel pageModel) {
		
		ShapeModel shapeModel = (ShapeModel) shapeTemplate;
		shapeModel.setParentModel(pageModel);
		pageModel.addSection(shapeModel);
		return shapeModel;
	}
}
