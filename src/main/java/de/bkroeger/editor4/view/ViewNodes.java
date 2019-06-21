package de.bkroeger.editor4.view;

import java.util.List;

import javafx.scene.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewNodes {

	private Node primaryNode;
	
	private List<Node> connectors;
}
