package de.bkroeger.editor4.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import de.bkroeger.editor4.view.IView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Datenstruktur für die Ergebnisse eines Controllers.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
@ToString
public class ControllerResult {
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/

	/**
	 * Der verantwortliche Controller
	 */
	private IController controller;
	
	/**
	 * Die generierte View
	 */
	private IView view;
	
	/**
	 * Eine Liste weiterer Nodes
	 */
	private List<Node> nodes = new ArrayList<>();
		
	/**========================================================================
	 * Constructors
	 *=======================================================================*/

	public ControllerResult() { }
	
	public ControllerResult(IController controller) {
    	this.controller = controller;
	}
}
