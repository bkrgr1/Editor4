package de.bkroeger.editor4.controller;

import java.util.ArrayList;
import java.util.List;

import de.bkroeger.editor4.model.EditorPageModel;
import de.bkroeger.editor4.model.IArrowModel;
import de.bkroeger.editor4.model.IShapeModel;
import de.bkroeger.editor4.view.EditorPane;
import lombok.Data;

/**
 * <p>
 * Controller for a document page.
 * </p>
 * 
 * @author bk
 */
@Data
public class EditorController {

	private EditorPageModel model;

	private EditorPane view;

	private List<IShapeController> shapeControllers = new ArrayList<>();

	private List<IArrowController> arrowControllers = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param model
	 * @param view
	 */
	public EditorController(EditorPageModel model, EditorPane view) {

		this.model = model;
		this.view = view;

		// alle Shapes zeichnen
		for (IShapeModel editorShapeModel : model.getShapeModels()) {

			IShapeController shapeCtrl = ShapeControllerFactory.getController(editorShapeModel, this);
			shapeControllers.add(shapeCtrl);

			this.view.getChildren().add(shapeCtrl.getView());
		}

		// alle Verbinder zeichnen
		for (IArrowModel editorArrowModel : model.getArrowModels()) {

			IArrowController arrowCtrl = ArrowControllerFactory.getController(editorArrowModel, this, shapeControllers);
			arrowControllers.add(arrowCtrl);

			this.view.getChildren().add(arrowCtrl.getView());
		}
	}
}
