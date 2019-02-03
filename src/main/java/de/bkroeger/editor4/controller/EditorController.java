package de.bkroeger.editor4.controller;

import java.util.ArrayList;
import java.util.List;

import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.model.IArrowModel;
import de.bkroeger.editor4.model.IShapeModel;
import de.bkroeger.editor4.view.EditorPane;

/**
 * Controller
 * @author bk
 */
public class EditorController {
	
	private EditorModel model;
	public EditorModel getModel() { return this.model; }
	private EditorPane view;
	public EditorPane getView() { return this.view; }
	
	private List<IShapeController> shapes = new ArrayList<>();
	
	private List<IArrowController> arrows = new ArrayList<>();

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
			shapes.add(shapeCtrl);
			
			this.view.getChildren().add(shapeCtrl.getView());
		}
		
		for (IArrowModel editorArrowModel : model.getArrowModels()) {
			
			IArrowController arrowCtrl = ArrowControllerFactory.getController(editorArrowModel, this, shapes);
			arrows.add(arrowCtrl);
			
			this.view.getChildren().add(arrowCtrl.getView());
		}
	}
}
