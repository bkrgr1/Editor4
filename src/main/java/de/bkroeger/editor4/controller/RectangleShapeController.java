package de.bkroeger.editor4.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.Handler.ShapeMouseDraggedEventHandler;
import de.bkroeger.editor4.Handler.ShapeMouseEnteredEventHandler;
import de.bkroeger.editor4.Handler.ShapeMouseExitedEventHandler;
import de.bkroeger.editor4.Handler.ShapeMousePressedEventHandler;
import de.bkroeger.editor4.Handler.ShapeMouseReleasedEventHandler;
import de.bkroeger.editor4.model.DefaultConnectorModel;
import de.bkroeger.editor4.model.IShapeModel;
import de.bkroeger.editor4.model.RectangleShapeModel;
import de.bkroeger.editor4.model.ShapeEventData;
import de.bkroeger.editor4.view.RectangleShapeView;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;

/**
 * <p>
 * This controller manages the behaviour of a Rectangle shape.
 * </p>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>Mouse entered - shape is selected</li>
 * <li>Mouse exited - shape is deselected</li>
 * <li>Mouse pressed - shape is selected and position stored</li>
 * <li>Mouse dragged - shape is moved with mouse</li>
 * <li>Mouse released - shape is moved to position and deselected</li>
 * 
 * @author bk
 */
public class RectangleShapeController extends BaseShapeController implements IShapeController {

	private static final Logger logger = LogManager.getLogger(RectangleShapeController.class.getName());

	private double additionalWidth = 15.0;
	private double additionalHeight = 15.0;

	private List<IConnectorController> connectorControllers = new ArrayList<>();

	/**
	 * <p>
	 * Constructor with model and parent controller.
	 * </p>
	 * 
	 * @param model            the model for this controller
	 * @param parentController parent controller or null
	 */
	public RectangleShapeController(IShapeModel model, PageController parentController) {
		super(model, parentController);

		logger.debug("Creating shape controller...");

		RectangleShapeModel rectModel = (RectangleShapeModel) model;
		view = new RectangleShapeView(rectModel.xProperty(), rectModel.yProperty(), rectModel.widthProperty(),
				rectModel.heightProperty());

		// die Konnectoren als Kreise zeichnen
		IConnectorController connectorController = null;
		DefaultConnectorModel connectorModel = null;

		// Konnektor-1: Mitte rechte Seite
		connectorModel = new DefaultConnectorModel(
				Bindings.add(rectModel.widthProperty(), additionalWidth),
				Bindings.add(Bindings.divide(rectModel.heightProperty(), 2.0), additionalHeight));
		connectorController = new ShapeConnectorController(connectorModel, this);
		connectorControllers.add(connectorController);

		// Konnektor-2: Mitte unten
		connectorModel = new DefaultConnectorModel(
			Bindings.add(Bindings.divide(rectModel.widthProperty(), 2.0), additionalWidth),
			Bindings.add(rectModel.heightProperty(), additionalHeight));
		connectorController = new ShapeConnectorController(connectorModel, this);
		connectorControllers.add(connectorController);
	
		// Konnektor-3: Mitte linke Seite
		connectorModel = new DefaultConnectorModel(
			Bindings.add(Bindings.multiply(rectModel.xProperty(), 0.0), additionalWidth),
			Bindings.add(Bindings.divide(rectModel.heightProperty(), 2.0), additionalHeight));
		connectorController = new ShapeConnectorController(connectorModel, this);
		connectorControllers.add(connectorController);

		// Konnektor-4: Mitte oben
		connectorModel = new DefaultConnectorModel(
			Bindings.add(Bindings.divide(rectModel.widthProperty(), 2.0), additionalWidth),
			Bindings.add(Bindings.multiply(rectModel.heightProperty(), 0.0), additionalHeight));
		connectorController = new ShapeConnectorController(connectorModel, this);
		connectorControllers.add(connectorController);

		ShapeEventData eventData = new ShapeEventData(model);
		// EventHandler zuordnen
		((Node) view).setOnMouseEntered(new ShapeMouseEnteredEventHandler(view));
		((Node) view).setOnMouseExited(new ShapeMouseExitedEventHandler(view));
		((Node) view).setOnMousePressed(new ShapeMousePressedEventHandler(view, eventData));
		((Node) view).setOnMouseDragged(new ShapeMouseDraggedEventHandler(view, eventData));
		((Node) view).setOnMouseReleased(new ShapeMouseReleasedEventHandler(view, eventData));

		logger.debug("Shape controller created for shape 'Rectangle'");
	}
}
