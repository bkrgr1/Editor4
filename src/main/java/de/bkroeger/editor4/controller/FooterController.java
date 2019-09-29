package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.model.IModel;
import de.bkroeger.editor4.view.TopView;

/**
 * 
 *
 * @author berthold.kroeger@gmx.de
 */
public class FooterController extends BaseController implements IController {
	
	/**
	 * 
	 * @param model
	 */
	public FooterController(EditorModel model) {
		
		this.model = (IModel) model;
	}

	/**
	 * 
	 * @param controllerResult
	 * @return das {@link ControllerResult Ergebnis}
	 */
	public ControllerResult buildView(ControllerResult controllerResult) {
		
		ControllerResult result = new ControllerResult();
		result.setController(this);
		
//		TopView topView = new TopView(this.model);
//		result.setView(topView);
		
		return result;
	}

}
