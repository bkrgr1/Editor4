package de.bkroeger.editor4.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.model.IModel;
import javafx.scene.shape.Path;

/**
 * <p>This class supports 2D-Shape controllers.</p>
 * 
 * @author berthold.kroeger@gmx.de
 */
public class BaseShapeController {

    @SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(BaseShapeController.class.getName());

    protected Path view;

    protected Double mouseX = new Double(0.0);
    protected Double mouseY = new Double(0.0);

    protected IModel model;

    protected IController parentController;

    public BaseShapeController(IModel model, IController parentController) {
        this.model = model;
        this.parentController = parentController;
    }
    
    protected void setMouseHandler() {
//		view.setOnMouseEntered(new ShapeMouseEnteredEventHandler(view));	
//		view.setOnMouseExited(new ShapeMouseExitedEventHandler(view));
    }
}