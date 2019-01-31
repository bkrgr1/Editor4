package de.bkroeger.editor4.controller;

import java.util.List;

import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.model.IArrowModel;
import de.bkroeger.editor4.model.IShapeModel;
import de.bkroeger.editor4.view.EditorPane;
import javafx.scene.Node;

/**
 * Controller
 * @author bk
 */
public class EditorController {
	
	private EditorModel model;
	public EditorModel getModel() { return this.model; }
	private EditorPane view;
	public EditorPane getView() { return this.view; }
	
	private List<Node> connectors;

	/**
	 * Constructor
	 * @param model
	 * @param view
	 */
	public EditorController(EditorModel model, EditorPane view) {
		
		this.model = model;
		this.view = view;
		
		for (IShapeModel editorShapeModel : model.getShapeModels()) {
			
			IShapeController shapeCtrl = ShapeControllerFactory.getController(editorShapeModel, this);
			
			this.view.getChildren().add(shapeCtrl.getView());
			
			connectors = shapeCtrl.getConnectors();
		}
		
		for (IArrowModel editorArrowModel : model.getArrowModels()) {
			
			IArrowController arrowCtrl = ArrowControllerFactory.getController(editorArrowModel, this, connectors);
			
			this.view.getChildren().add(arrowCtrl.getView());
		}
	}
}
