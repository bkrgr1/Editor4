package de.bkroeger.editor4.runtime;

import java.util.ArrayList;
import java.util.List;

import de.bkroeger.editor4.controller.ShapeController;
import de.bkroeger.editor4.model.ShapeModel;
import de.bkroeger.editor4.view.GroupView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShapeRuntime implements IRuntime {
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	private IRuntime parent;
	public IRuntime getParent() { return this.parent; }

	private ShapeModel model;
	/**
	 * Der verantwortliche Controller
	 */
	private ShapeController controller;
	
	/**
	 * Die generierte View
	 */
	private GroupView view;

	public ShapeRuntime(IRuntime parent) {
		this.parent = parent;
	}

	private List<PathElemRuntime> pathElements = new ArrayList<>();
	public void addPathElem(PathElemRuntime value) {
		pathElements.add(value);
	}
}
