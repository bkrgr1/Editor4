package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.view.TopView;

public class TopController extends BaseController implements IController {

	public ControllerResult buildView(ControllerResult controllerResult) {
		
		ControllerResult result = new ControllerResult();
		result.setController(this);
		
		TopView topView = new TopView();
		result.setView(topView);
		
		return result;
	}

}
