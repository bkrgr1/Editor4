package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.model.IModel;
import de.bkroeger.editor4.runtime.EditorRuntime;
import de.bkroeger.editor4.runtime.FooterRuntime;

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
	 * @return das {@link EditorRuntime Ergebnis}
	 */
	public FooterRuntime buildView(EditorRuntime editorRuntime) {
		
		FooterRuntime result = new FooterRuntime(editorRuntime);
		result.setController(this);
		result.setModel((EditorModel) model);
		
//		TopView topView = new TopView(this.model);
//		result.setView(topView);
		
		return result;
	}

}
