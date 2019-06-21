package de.bkroeger.editor4.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.Handler.ShapeMouseDraggedEventHandler;
import de.bkroeger.editor4.Handler.ShapeMousePressedEventHandler;
import de.bkroeger.editor4.Handler.ShapeMouseReleasedEventHandler;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.DefaultConnectorModel;
import de.bkroeger.editor4.model.IModel;
import de.bkroeger.editor4.model.IShapeModel;
import de.bkroeger.editor4.model.RectangleShapeModel;
import de.bkroeger.editor4.model.ShapeEventData;
import de.bkroeger.editor4.view.IView;
import de.bkroeger.editor4.view.RectangleShapeView;
import de.bkroeger.editor4.view.ViewNodes;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

/**
 * <p>This controller manages the behaviour of a Rectangle shape.</p>
 * <p>Behaviour:</p>
 * <ul>
 * <li>Mouse entered - shape is selected</li>
 * <li>Mouse exited - shape is deselected</li>
 * <li>Mouse pressed - shape is selected and position stored</li>
 * <li>Mouse dragged - shape is moved with mouse</li>
 * <li>Mouse released - shape is moved to position and deselected</li>
 * </ul>
 * @author berthold.kroeger@gmx.de
 */
public class RectangleShapeController extends BaseShapeController implements IShapeController {

	private static final Logger logger = LogManager.getLogger(RectangleShapeController.class.getName());

	private List<IConnectorController> connectorControllers = new ArrayList<>();

	/**
	 * <p>Dies erzeugt einen Controller für ein RectangleShape.</p>
	 * <p>Als erstes wird das Shape von der View-Klasse gezeichnet,
	 * dann die Mouse-Handler definiert.</p>
	 * 
	 * @param model the model for this controller
	 * @param parentController parent controller or null
	 * @throws TechnicalException 
	 */
	public RectangleShapeController(IShapeModel model, PageController parentController) 
			throws TechnicalException {
		super(model, parentController);

		logger.debug("Creating shape controller...");

		if (!(model instanceof RectangleShapeModel)) {
			throw new TechnicalException("Model is invalid for RectangleShape");
		}
		RectangleShapeModel rectModel = (RectangleShapeModel) model;

		ViewNodes viewNodes = RectangleShapeView.draw(rectModel);

		// EventHandler zuordnen
		super.setMouseHandler();
		this.setMouseHandler();

		logger.debug("Shape controller created for shape 'Rectangle'");
	}
	
	protected void setMouseHandler() {
		ShapeEventData eventData = new ShapeEventData((IShapeModel) model);
		view.setOnMousePressed(new ShapeMousePressedEventHandler(view, eventData));
		view.setOnMouseDragged(new ShapeMouseDraggedEventHandler(view, eventData));
		view.setOnMouseReleased(new ShapeMouseReleasedEventHandler(view, eventData));
	}

    @Override
    public IView getView() {
        return view;
    }

    @Override
    public IModel getModel() {
        return model;
    }

    @Override
    public List<ImageView> getConnectors() {
        return view.getConnectors();
    }

}
