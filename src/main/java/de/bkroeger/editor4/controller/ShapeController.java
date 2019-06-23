package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.model.SectionModel;

public class ShapeController extends BaseController {
	
	private SectionModel shapeModel;

	public ShapeController(SectionModel shapeModel) {
        super();
        this.model = shapeModel; 
	}
    
    public ControllerResult buildView(ControllerResult parentController) {
    	
    	ControllerResult controllerResult = new ControllerResult();
    	controllerResult.setController(this);

        // create a drawing canvas
//        ShapeView shapeView = new ShapeView();
//        this.view = shapeView;
//        controllerResult.setView(this.view);
    	
		return controllerResult;
    }
}
