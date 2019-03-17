package de.bkroeger.editor4.view;

import java.util.List;

public interface IArrowView extends IView {

	public void setSelected(boolean selected);

	public List<IConnector> getConnectors();
}
