package de.bkroeger.editor4.model;

import org.json.simple.JSONObject;

import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;

public interface IModel {
	
	public SectionModel loadModel(JSONObject jsonSection, IModel parentSection) 
			throws TechnicalException, InputFileException;
	
	public CellModel getCell(String name);
}