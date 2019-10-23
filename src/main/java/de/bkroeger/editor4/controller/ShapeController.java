package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
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
	
	protected ShapeRuntime runtime;

	/**
	 * Constructor with Model
	 * @param shapeModel
	 */
	public ShapeController(ShapeRuntime shapeRuntime) {
        super();
        this.runtime = shapeRuntime;
	}
    
    public GroupView buildView(ShapeRuntime shapeRuntime) 
    		throws TechnicalException, InputFileException, CellCalculationException {	
    	throw new TechnicalException("Has to be overridden by sub classes.");
    }
}
