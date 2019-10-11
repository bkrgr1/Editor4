package de.bkroeger.editor4.runtime;

import de.bkroeger.editor4.controller.IController;
import de.bkroeger.editor4.model.IModel;
import de.bkroeger.editor4.view.IView;

public interface IRuntime {
	
	public IRuntime getParent();

	public IModel getModel();
	
	public IView getView();
	
	public IController getController();
}
