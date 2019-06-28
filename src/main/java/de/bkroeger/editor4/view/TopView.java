package de.bkroeger.editor4.view;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TopView extends VBox implements IView {

	public TopView() {
		super();
		Label label = new Label("Top");
		this.getChildren().add(label);
	}
}
