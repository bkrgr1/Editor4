package de.bkroeger.editor4.model;

import java.util.List;

import org.json.simple.JSONObject;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;

public interface IModel {
	
	public SectionModel loadModel(JSONObject jsonSection, IModel parentSection) 
			throws TechnicalException, InputFileException;
	
	public CellModel getCell(String name);
	
	public List<SectionModel> selectSections(SectionModelType type);

	public SectionModel getSection(SectionModelType location) 
			throws InputFileException, CellCalculationException;

	public IModel calculate() throws CellCalculationException;
}