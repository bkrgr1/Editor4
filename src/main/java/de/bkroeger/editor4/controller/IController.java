package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.model.IModel;
import de.bkroeger.editor4.view.IView;

/**
 * <p>
 * This controller controls the behaviour of a diagram file.
 * </p>
 * 
 * @author bk
 */
public interface IController {

	public IView getView();

    public IModel getModel();
}