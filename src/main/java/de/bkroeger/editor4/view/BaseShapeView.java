package de.bkroeger.editor4.view;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class BaseShapeView extends Pane implements IView  {
	
	protected Shape javaFXShape;
	public Shape getJavaFXShape() { return javaFXShape; }

	protected List<ImageView> connectors = new ArrayList<>();
	public List<ImageView> getConnectors() { return connectors; }
	
	protected List<Node> resizeIcons = new ArrayList<>();
	public List<Node> getResizeIcons() { return resizeIcons; }

	private double currentStrokeWidth;

	/**
	 * 
	 * @param isSelected
	 */
	public void setSelected(boolean isSelected) {

		if (isSelected) {
			
		} else {
			
		}
	}
	
	/**
	 * 
	 * @param isMouseOver
	 */
	public void setMouseOver(boolean isMouseOver) {
		
		if (isMouseOver) {
			// die Stroke-Stärke merken
			currentStrokeWidth = javaFXShape.getStrokeWidth();
			// Rahmen doppelt so fett anzeigen
			javaFXShape.setStrokeWidth(currentStrokeWidth * 2.0);
			// show all resize icons
			for (Node icon : resizeIcons) {
				icon.setVisible(true);
			}
		} else {
			// wieder die vorherige Stroke-Stärke verwenden
			javaFXShape.setStrokeWidth(currentStrokeWidth);
			// hide all resize icons
			for (Node icon : resizeIcons) {
				icon.setVisible(false);
			}
		}
	}
}
