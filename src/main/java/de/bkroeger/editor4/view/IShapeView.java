package de.bkroeger.editor4.view;

import java.util.List;

import javafx.scene.image.ImageView; 

public interface IShapeView extends IView {
	
	public List<ImageView> getConnectors();

	public void setSelected(boolean isSelected);
}
