package de.bkroeger.editor4.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.bkroeger.editor4.controller.PageController;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.view.PageView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PageRuntime implements IRuntime {
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	private IRuntime parent;
	public IRuntime getParent() { return this.parent; }

	private PageModel model;
	/**
	 * Der verantwortliche Controller
	 */
	private PageController controller;
	
	/**
	 * Die generierte View
	 */
	private PageView view;
	
	private Map<UUID, ShapeRuntime> shapes = new HashMap<>();
	public void putShape(UUID key, ShapeRuntime value) {
		shapes.put(key, value);
	}

	public PageRuntime(IRuntime parent) {
		this.parent = parent;
	}
}
