package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.LineType;
import de.bkroeger.editor4.model.ShapeModel;
import de.bkroeger.editor4.runtime.ShapeRuntime;
import de.bkroeger.editor4.view.GroupView;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class Shape1DController extends ShapeController {

	public Shape1DController(ShapeRuntime runtime) {
		super(runtime);
	}
    
	@Override
    public GroupView buildView(ShapeRuntime shapeRuntime) 
    		throws TechnicalException, InputFileException, CellCalculationException {	
    	
    	GroupView groupView = new GroupView(shapeRuntime.getModel());
    	
    	ShapeModel shapeModel = shapeRuntime.getModel();
    	
    	if (shapeModel.getLineType() == LineType.ORTHOGONAL) {
    		// TODO: in View auslagern
    		
	    	Path path = new Path();
	    	
	    	MoveTo moveTo1 = new MoveTo();
	    	moveTo1.setX(0.0);
	    	moveTo1.setY(0.0);
	    	
	    	// TODO: bei orthogonalen Linien müssen mindestens zwei Segmente gebildet werden
	    	
	    	LineTo lineTo1 = new LineTo();
	    	lineTo1.xProperty().bind(shapeModel.getWidthProperty());
	    	lineTo1.yProperty().bind(shapeModel.getHeightProperty());
	    	
	    	path.getElements().addAll(moveTo1, lineTo1);
	    	
	    	groupView.getChildren().add(path);
    	}
    	
    	return groupView;
    }
}
