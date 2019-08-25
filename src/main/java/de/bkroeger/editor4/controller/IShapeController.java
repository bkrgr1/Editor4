package de.bkroeger.editor4.controller;

import java.util.List;

import de.bkroeger.editor4.model.ShapeModel;
import javafx.scene.image.ImageView;

public interface IShapeController extends IController {

	public List<ImageView> getConnectors();

	public ShapeModel getModel();
}
