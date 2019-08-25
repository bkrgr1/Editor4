package de.bkroeger.editor4.view;

import java.util.List;

import javafx.scene.image.ImageView;

public interface IArrowView extends IView {

	public void setSelected(boolean selected);

	public List<ImageView> getConnectors();
}
