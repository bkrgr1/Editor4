package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Model of a PathElement</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
@ToString(callSuper=true)
public class PathElementModel extends BaseModel {

	private static final Logger logger = LogManager.getLogger(PathElementModel.class.getName());
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/

	protected List<CellModel> cells = new ArrayList<>();
    /**
     * Art des PathElement
     */
	private PathElementType elemType;
	
	public DoubleProperty getXProperty() throws TechnicalException, CellCalculationException {
		
		return getDoubleCellProperty("X");
	}
	
	public DoubleProperty getYProperty() throws TechnicalException, CellCalculationException {
		
		return getDoubleCellProperty("Y");
	}
	
	public DoubleProperty getRadiusXProperty() throws TechnicalException, CellCalculationException {
		
		return getDoubleCellProperty("radiusX");
	}
	
	public DoubleProperty getRadiusYProperty() throws TechnicalException, CellCalculationException {
		
		return getDoubleCellProperty("radiusY");
	}
	
	public BooleanProperty getLargeArcFlagProperty() throws TechnicalException, CellCalculationException {
		return null;
		
//		CellModel cell = this.cells.get("largeArcFlag");
//		if (cell != null) {
//			return cell.getBooleanProperty();
//		} else {
//			return new SimpleBooleanProperty(false);
//		}
	}
	
	public BooleanProperty getSweepFlagProperty() throws TechnicalException, CellCalculationException {
		return null;
		
//		CellModel cell = this.cells.get("sweepFlag");
//		if (cell != null) {
//			return cell.getBooleanProperty();
//		} else {
//			return new SimpleBooleanProperty(false);
//		}
	}
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/
	
	public PathElementModel() {
		super(ModelType.PathElement);
	}
	
	/**
	 * Constructor
	 * @param pt
	 */
	public PathElementModel(PathElementType pt) {
		super(ModelType.PathElement);
		this.elemType = pt;
	}

	public PathElementModel(PathElementModel model) {
		super(ModelType.PathElement);
		this.elemType = model.elemType;
	}

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
