package de.bkroeger.editor4.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.Handler.MouseMovedCommand;
import de.bkroeger.editor4.Handler.MousePressedCommand;
import de.bkroeger.editor4.Handler.MouseReleasedCommand;
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
public class PathController extends BaseController implements IMouseHandlerData {

	private static final Logger logger = LogManager.getLogger(PathController.class.getName());
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	private double mouseX;
	public double getMouseX() { return mouseX; }
	public void setMouseX(double value) { this.mouseX = value; }
	private double mouseY;
	public double getMouseY() { return mouseY; }
	public void setMouseY(double value) { this.mouseY = value; }
	
	private MousePressedCommand mousePressedCommand;
	private MouseMovedCommand mouseMovedCommand;
	private MouseReleasedCommand mouseReleasedCommand;

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

		mousePressedCommand = new MousePressedCommand(this);
		mouseMovedCommand = new MouseMovedCommand(this, pathModel);
		mouseReleasedCommand = new MouseReleasedCommand(this, pathModel);
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
    	
    	// when user presses a mouse key over the PathView
    	pathView.setOnMousePressed(event -> {
    		mousePressedCommand.execute(event);
    	});
    	
    	// when user moves the PathView with key pressed
    	pathView.setOnMouseMoved(event -> {
    		mouseMovedCommand.execute(event);
    	});
    	
    	// when user releases the mouse key
    	pathView.setOnMouseReleased(event -> {
    		mouseReleasedCommand.execute(event);
    	});
    	
		return result;
	}
}
