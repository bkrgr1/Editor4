package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.model.IArrowModel;
import de.bkroeger.editor4.view.IArrowView;

public interface IArrowController {

	public IArrowView getView();
	
	public IArrowModel getModel();
}
