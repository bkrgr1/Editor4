package de.bkroeger.editor4.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.PathModel;
import de.bkroeger.editor4.view.PathView;

/**
 * <p>Dieser Controller steuert einen Path.</p>
 * 
 * @author berthold.kroeger@gmx.de
 */
public class PathController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PathController.class.getName());
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/

	/**========================================================================
	 * Constructors
	 *=======================================================================*/
	
	/**
	 * Constructor
	 * @param pathModel das {@link PathModel}
	 * @throws TechnicalException
	 */
    public PathController(PathModel pathModel) throws TechnicalException {
        super();
        this.model = pathModel; 
        if (pathModel == null) throw new TechnicalException("Path model is NULL");
    }
	
	/**========================================================================
	 * Public methods
	 *=======================================================================*/

    /**
     * <p>Erstellt einen {@link PathView Grafik-Pfad} aus den Daten des {@link PathModel}.</p>
     * @param shapeResult ein {@link ControllerResult} mit den Details des Parent-Views
     * @return ein {@link ControllerResult} mit den Details des Path-Views
     * @throws TechnicalException
     * @throws CellCalculationException
     * @throws InputFileException
     */
	public ControllerResult buildView(ControllerResult shapeResult) 
			throws TechnicalException, CellCalculationException, InputFileException {
		
		ControllerResult result = new ControllerResult(this);
		
		// Pfad zeichnen
		PathView pathView = new PathView(this).draw();
    	result.setView(pathView);
			
    	// when user clicks on PathView
    	pathView.setOnMouseClicked(event -> {
    		logger.trace("Clicked on path view");
    	});
    	
		return result;
	}
}
