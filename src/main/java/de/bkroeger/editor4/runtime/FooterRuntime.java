package de.bkroeger.editor4.runtime;

import de.bkroeger.editor4.controller.FooterController;
import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.view.FooterView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FooterRuntime implements IRuntime {
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	private IRuntime parent;
	public IRuntime getParent() { return this.parent; }

	private EditorModel model;
	/**
	 * Der verantwortliche Controller
	 */
	private FooterController controller;
	
	/**
	 * Die generierte View
	 */
	private FooterView view;

	public FooterRuntime(IRuntime parent) {
		this.parent = parent;
	}

}
