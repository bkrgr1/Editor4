package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.model.IModel;
import de.bkroeger.editor4.runtime.EditorRuntime;
import de.bkroeger.editor4.runtime.NavigatorRuntime;

/**
 * 
 *
 * @author berthold.kroeger@gmx.de
 */
public class NavigatorController extends BaseController implements IController {
	
	/**
	 * 
	 * @param model
	 */
	public NavigatorController(EditorModel model) {
		
		this.model = (IModel) model;
	}

	/**
	 * 
	 * @param editorRuntime
	 * @return das {@link EditorRuntime Ergebnis}
	 */
	public NavigatorRuntime buildView(EditorRuntime editorRuntime) {
		
		NavigatorRuntime result = new NavigatorRuntime(editorRuntime);
		result.setController(this);
		result.setModel((EditorModel) model);
		
//		TopView topView = new TopView(this.model);
//		result.setView(topView);
		
		return result;
	}

}
