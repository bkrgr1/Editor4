package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.model.IModel;
import de.bkroeger.editor4.view.IView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Diese Klasse stellt gemeinsame Methoden für alle Controller zur Verfügung.</p>
 * <p>
 * 
 * @author bk
 */
@Getter
@Setter
@ToString
public class BaseController implements IController {

	/**
	 * Das Datenmodel
	 */
    protected IModel model;

    /**
     * Die View
     */
    protected IView view;

    /**
     * Liefert das Datenmodel
     * @return
     */
    public IModel getModel() { return model; }

    /**
     * Liefert die View
     * @return
     */
    public IView getView() { return view; }
}