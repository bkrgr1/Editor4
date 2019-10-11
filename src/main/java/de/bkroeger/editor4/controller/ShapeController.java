package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.SectionModel;
import de.bkroeger.editor4.runtime.ShapeRuntime;
import de.bkroeger.editor4.view.GroupView;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>Dies ist die Basisklasse für Shape-Controller.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
public class ShapeController extends BaseController {
	
	private SectionModel shapeModel;

	/**
	 * Constructor with Model
	 * @param shapeModel
	 */
	public ShapeController(SectionModel shapeModel) {
        super();
        this.model = shapeModel; 
	}
    
    public GroupView buildView(ShapeRuntime shapeRuntime) 
    		throws TechnicalException, InputFileException, CellCalculationException {	
    	throw new TechnicalException("Has to be overridden by sub classes.");
    }
}
