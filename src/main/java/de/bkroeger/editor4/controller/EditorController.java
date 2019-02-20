package de.bkroeger.editor4.controller;

import java.util.ArrayList;
import java.util.List;

import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.model.IArrowModel;
import de.bkroeger.editor4.model.IShapeModel;
import de.bkroeger.editor4.view.EditorPane;
import javafx.scene.text.Text;

/**
 * Controller
 * @author bk
 */
public class EditorController {
	
	private EditorModel model;
	public EditorModel getModel() { return this.model; }
	
	private EditorPane view;
	public EditorPane getView() { return this.view; }
	
	private List<IShapeController> shapeControllers = new ArrayList<>();
	
	private List<IArrowController> arrowControllers = new ArrayList<>();

	/**
	 * Constructor
	 * @param model
	 * @param view
	 */
	public EditorController(EditorModel model, EditorPane view) {
		
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
