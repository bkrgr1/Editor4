package de.bkroeger.editor4.runtime;

import de.bkroeger.editor4.controller.NavigatorController;
import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.view.NavigatorView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NavigatorRuntime implements IRuntime {
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	private IRuntime parent;
	public IRuntime getParent() { return this.parent; }

	private EditorModel model;
	/**
	 * Der verantwortliche Controller
	 */
	private NavigatorController controller;
	
	/**
	 * Die generierte View
	 */
	private NavigatorView view;

	public NavigatorRuntime(IRuntime parent) {
		this.parent = parent;
	}
}
