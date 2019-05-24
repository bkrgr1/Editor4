package de.bkroeger.editor4.view;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class BaseShapeView extends Pane implements IView  {

	protected List<ImageView> connectors = new ArrayList<>();
	public List<ImageView> getConnectors() { return connectors; }

	public void setSelected(boolean isSelected) {

		if (isSelected) {
			
		} else {
			
		}
	}
	
	public void setMouseOver(boolean isMouseOver) {
		
		if (isMouseOver) {
			// zeigt einen gestrichelten Rahmen des Shapes
			this.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.DOTTED, 
					new CornerRadii(10), new BorderWidths(1))));
		} else {
			this.setBorder(null);
		}
	}
}
