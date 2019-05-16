package de.bkroeger.editor4.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.model.IModel;
import de.bkroeger.editor4.view.IConnector;
import de.bkroeger.editor4.view.IShapeView;
import de.bkroeger.editor4.view.IView;

/**
 * <p>
 * This controller manages the behaviour of a shape.
 * </p>
 * 
 * @author bk
 */
public class BaseShapeController implements IShapeController {

    @SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(BaseShapeController.class.getName());

    protected IShapeView view;

    @Override
    public IView getView() {
        return view;
    }

    @SuppressWarnings("deprecation")
	protected Double mouseX = new Double(0.0);
    @SuppressWarnings("deprecation")
	protected Double mouseY = new Double(0.0);

    protected IModel model;

    @Override
    public IModel getModel() {
        return model;
    }

    @Override
    public List<IConnector> getConnectors() {
        return view.getConnectors();
    }

    protected IController parentController;

    public BaseShapeController(IModel model, IController parentController) {
        this.model = model;
        this.parentController = parentController;
    }
}