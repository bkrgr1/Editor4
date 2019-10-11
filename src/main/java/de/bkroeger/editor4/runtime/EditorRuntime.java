package de.bkroeger.editor4.runtime;

import java.util.HashMap;
import java.util.Map;

import de.bkroeger.editor4.controller.EditorController;
import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.view.EditorView;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Basis-Runtime-Datenstruktur auf Editor-Ebene.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
@ToString
public class EditorRuntime implements IRuntime {
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	private IRuntime parent = null;
	public IRuntime getParent() { return this.parent; }

	/**
	 * Die Daten für diese Ebene
	 */
	private EditorModel model;
	
	/**
	 * Der verantwortliche Controller
	 */
	private EditorController controller;
	
	/**
	 * Die generierte View
	 */
	private EditorView view;
	
	/**
	 * Eine Map der View-Teile
	 */
	private Map<String, IRuntime> viewParts = new HashMap<>();
	public void putViewPart(String key, IRuntime value) {
		viewParts.put(key, value);
	}
	
	/**
	 * Liefert das Title-Property
	 * @return ein StringProperty
	 */
	public StringProperty getTitleProperty() {
		if (controller != null) {
			return controller.getTitleProperty();
		} else return null;
	}
		
	/**========================================================================
	 * Constructors
	 *=======================================================================*/

	public EditorRuntime() { }

	public EditorRuntime(IRuntime parent) {
		this.parent = parent;
	}

	public EditorRuntime(EditorController controller) {
    	this.controller = controller;
	}
	
	public EditorRuntime(EditorModel model, EditorController controller) {
		this.model = model;
		this.controller = controller;
	}
}
