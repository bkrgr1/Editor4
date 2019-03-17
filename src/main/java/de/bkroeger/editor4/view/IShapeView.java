package de.bkroeger.editor4.view;

import java.util.List;

public interface IShapeView extends IView {
	
	public List<IConnector> getConnectors();

	public void setSelected(boolean isSelected);
}
