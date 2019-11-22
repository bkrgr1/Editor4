package de.bkroeger.editor4.controller;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.runtime.ShapeRuntime;
import de.bkroeger.editor4.view.GroupView;

/**
 * <p>Dies ist die Basisklasse für Shape-Controller.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShapeController extends BaseController {
		
	/**========================================================================
	 * Fields
	 *=======================================================================*/

	protected ShapeRuntime runtime;
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/

	public ShapeController() { }
	
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
