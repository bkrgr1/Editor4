package de.bkroeger.editor4.runtime;

import de.bkroeger.editor4.controller.PathElemController;
import de.bkroeger.editor4.model.PathModel;
import de.bkroeger.editor4.view.PathView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PathElemRuntime implements IRuntime {
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	private IRuntime parent;
	public IRuntime getParent() { return this.parent; }

	private PathModel model;
	/**
	 * Der verantwortliche Controller
	 */
	private PathElemController controller;
	
	/**
	 * Die generierte View
	 */
	private PathView view;

	public PathElemRuntime(IRuntime parent) {
		this.parent = parent;
	}
}
