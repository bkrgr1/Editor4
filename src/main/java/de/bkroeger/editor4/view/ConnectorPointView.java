package de.bkroeger.editor4.view;

import de.bkroeger.editor4.model.ConnectorModel;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ConnectorPointView extends Pane {
	
	private ConnectorModel model;
	public ConnectorModel getModel() { return model; }
	
	public ConnectorPointView(ConnectorModel model) {
		this.model = model;
	}

	public void setHighlight(boolean highlighting) {
		
		if (highlighting) {
			this.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
		} else {
			this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		}
	}
}
