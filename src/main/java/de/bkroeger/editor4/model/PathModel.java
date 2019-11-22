package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import javafx.beans.property.DoubleProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Model of a shape path.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
@ToString(callSuper=true)
public class PathModel extends BaseModel implements IModel {

	private static final Logger logger = LogManager.getLogger(PathModel.class.getName());
		
	/**========================================================================
	 * Fields
	 *=======================================================================
	
	private List<CellModel> cells = new ArrayList<>();
		
	protected List<PathElemModel> elements = new ArrayList<>();

	public DoubleProperty getXProperty() throws CellCalculationException, TechnicalException {
		
		return getDoubleCellProperty("x");
	}
	
	public DoubleProperty getYProperty() throws CellCalculationException, TechnicalException {
		
		return getDoubleCellProperty("y");
	}

	/**========================================================================
	 * Constructors
	 *=======================================================================*/
	
	public PathModel() {
		super(ModelType.Path);
	}
	
	private PathType pathType;
	
	public PathModel(PathType pt) {
		super(ModelType.Path);
		this.pathType = pt;
	}
	
	public PathModel(PathModel model) {
		super(ModelType.Path);
		this.pathType = model.pathType;
	}
	
	protected List<CellModel> cells = new ArrayList<>();
	
	protected List<PathElementModel> elements = new ArrayList<>();
	
	/**========================================================================
	 * Public methods
	 *=======================================================================*/
	
	/**========================================================================
	 * Private methods
	 *=======================================================================*/

	private DoubleProperty getDoubleCellProperty(String cellName) 
			throws CellCalculationException, TechnicalException {
				return null;
		
//		CellModel cell = this.cells.get(cellName.toLowerCase());
//		if (cell != null) {
//			return cell.getDoubleProperty();
//		} else {
//			throw new TechnicalException("Cell with type '"+cellName+"' not found");
//		}
	}
}
