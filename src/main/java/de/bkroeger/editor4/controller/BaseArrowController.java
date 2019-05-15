package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.model.IArrowModel;
import de.bkroeger.editor4.view.IArrowView;

/**
 * <p>
 * This controller manages the behaviour of an arrow, a line between shapes.
 * </p>
 * 
 * @author bk
 */
public class BaseArrowController implements IArrowController {

    protected IArrowView view;

    @Override
    public IArrowView getView() {
        return view;
    }

    protected IArrowModel model;

    @Override
    public IArrowModel getModel() {
        return model;
    }

}