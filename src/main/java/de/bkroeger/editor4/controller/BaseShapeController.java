package de.bkroeger.editor4.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.Handler.ShapeMouseEnteredEventHandler;
import de.bkroeger.editor4.Handler.ShapeMouseExitedEventHandler;
import de.bkroeger.editor4.model.IModel;
import de.bkroeger.editor4.view.BaseShapeView;
import de.bkroeger.editor4.view.IView;
import javafx.scene.image.ImageView;

/**
 * <p>This controller manages the common behaviour of all 2D shapes.</p>
 * 
 * @author berthold.kroeger@gmx.de
 */
public class BaseShapeController implements IShapeController {

    @SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(BaseShapeController.class.getName());

    protected BaseShapeView view;

    @Override
    public IView getView() {
        return view;
    }

    protected Double mouseX = new Double(0.0);
    protected Double mouseY = new Double(0.0);

    protected IModel model;

    @Override
    public IModel getModel() {
        return model;
    }

    @Override
    public List<ImageView> getConnectors() {
        return view.getConnectors();
    }

    protected IController parentController;

    public BaseShapeController(IModel model, IController parentController) {
        this.model = model;
        this.parentController = parentController;
    }
    
    protected void setMouseHandler() {
		view.setOnMouseEntered(new ShapeMouseEnteredEventHandler(view));	
		view.setOnMouseExited(new ShapeMouseExitedEventHandler(view));
    }
}