package de.bkroeger.editor4.view;

import de.bkroeger.editor4.model.ConnectorModel;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class ConnectorView extends Path {
	
	private ConnectorModel model;
	public ConnectorModel getModel() { return this.model; }
	
	public ConnectorView(ConnectorModel model) {
		super();
		this.model = model;
		
		// MoveTo: X - HalfX; Y - HalfY
		MoveTo moveTo1 = new MoveTo();
		// LineTo: X + HalfX; Y + HalfY
		LineTo lineTo1 = new LineTo();
		// MoveTo: X - HalfX; Y + HalfY
		MoveTo moveTo2 = new MoveTo();
		// LineTo: X + HalfX; Y - HalfY
		LineTo lineTo2 = new LineTo();
				
		this.getElements().addAll(moveTo1, lineTo1, moveTo2, lineTo2);
		
		this.setVisible(false);
	}
}
