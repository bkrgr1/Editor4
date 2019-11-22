package de.bkroeger.editor4.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.PathModel;
import de.bkroeger.editor4.runtime.EditorRuntime;
import de.bkroeger.editor4.runtime.PathElemRuntime;
import de.bkroeger.editor4.view.PathElemView;
import de.bkroeger.editor4.view.PathView;

/**
 * <p>Dieser Controller steuert einen Path.</p>
 * 
 * @author berthold.kroeger@gmx.de
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PathElemController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PathElemController.class.getName());
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/

	/**========================================================================
	 * Constructors
	 *=======================================================================*/
	
	public PathElemController() { }
	
	/**
	 * Constructor
	 * @param pathModel das {@link PathModel}
	 * @throws TechnicalException
	 */
    public PathElemController(PathModel pathModel) throws TechnicalException {
        super();
        this.model = pathModel; 
        if (pathModel == null) throw new TechnicalException("Path model is NULL");
    }
	
	/**========================================================================
	 * Public methods
	 *=======================================================================*/

    /**
     * <p>Erstellt einen {@link PathView Grafik-Pfad} aus den Daten des {@link PathModel}.</p>
     * @param shapeResult ein {@link EditorRuntime} mit den Details des Parent-Views
     * @return ein {@link EditorRuntime} mit den Details des Path-Views
     * @throws TechnicalException
     * @throws CellCalculationException
     * @throws InputFileException
     */
	public PathView buildView(PathElemRuntime runtime) 
			throws TechnicalException, CellCalculationException, InputFileException {
		
		// Pfad zeichnen
		PathElemView pathView = new PathElemView(this).draw();
			
    	// when user clicks on PathView
    	pathView.setOnMouseClicked(event -> {
    		logger.trace("Clicked on path view");
    	});
    	
		return pathView;
	}
}
