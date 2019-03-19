package de.bkroeger.editor4.controller;

import java.util.List;

import de.bkroeger.editor4.view.IConnector;

public interface IShapeController extends IController {

	public List<IConnector> getConnectors();
}
