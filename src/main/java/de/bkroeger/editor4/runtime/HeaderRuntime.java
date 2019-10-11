package de.bkroeger.editor4.runtime;

import de.bkroeger.editor4.controller.TopController;
import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.view.TopView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HeaderRuntime implements IRuntime {
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	private IRuntime parent;
	public IRuntime getParent() { return this.parent; }

	private EditorModel model;
	/**
	 * Der verantwortliche Controller
	 */
	private TopController controller;
	
	/**
	 * Die generierte View
	 */
	private TopView view;

	public HeaderRuntime(IRuntime parent) {
		this.parent = parent;
	}

}
