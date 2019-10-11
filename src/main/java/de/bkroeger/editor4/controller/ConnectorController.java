package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.model.ConnectorModel;
import de.bkroeger.editor4.view.ConnectorView;

public class ConnectorController {
	
	private ConnectorModel model;
	private ConnectorView view;
	
	public ConnectorController(ConnectorModel model) {
		this.model = model;
	}

	public void buildView() {
		
		this.view = new ConnectorView(this.model);
	}
}
