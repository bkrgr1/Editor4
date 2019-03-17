package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.model.IModel;
import de.bkroeger.editor4.view.IView;
import lombok.Data;

/**
 * <p>
 * This controller controls the behaviour of a diagram file.
 * </p>
 * <p>
 * A diagram file supports following actions:
 * </p>
 * <ul>
 * </ul>
 * 
 * @author bk
 */
@Data
public class BaseController implements IController {

    protected IModel model;

    protected IView view;

    public IModel getModel() { return model; }

    public IView getView() { return view; }
}