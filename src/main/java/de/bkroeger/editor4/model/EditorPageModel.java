package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.List;

public class EditorPageModel extends BaseModel {
	
	private String title = "Editor4";
	public String getTitle() { return this.title; }
	
	private List<IShapeModel> shapeModels = new ArrayList<>();
	public List<IShapeModel> getShapeModels() { return this.shapeModels; }
	public void addShapeModel(IShapeModel model) {
		this.shapeModels.add(model);
	}
		
	private List<IArrowModel> arrowModels = new ArrayList<>();
	public List<IArrowModel> getArrowModels() { return this.arrowModels; }
	public void addArrowModel(IArrowModel model) {
		this.arrowModels.add(model);
	}

	public EditorPageModel() {
		super();
	}
}
