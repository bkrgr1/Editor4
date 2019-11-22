package de.bkroeger.editor4.model;

import java.util.List;
import java.util.Map;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;

public interface IModel {
	
	// List<ICell> cells
	/**
	 * Sucht nach einer {@link ICell Zelle} mit dem angegebenen Namen
	 * @param name NameU einer Zelle
	 * @return eine Zelle
	 */
	public ICell getCellByName(String name);
	
	// List<IModel> models
	/**
	 * Sucht nach einem {@link IModel Modell} mit dem angegebenen Typ.
	 * @param type der {@link ModelType Typ} des gesuchten Models
	 * @return Liste der ausgewählten Modelle
	 */
	public List<IModel> selectChildMotel(ModelType type);

	/**
	 * Liefert die Section mit dem angegebenen {@link ModelType Typ}
	 * @param location
	 * @return das gesuchte {@link IModel Modell}
	 * @throws InputFileException
	 * @throws CellCalculationException
	 */
	public IModel getSection(ModelType location) 
			throws InputFileException, CellCalculationException;

	/**
	 * Liefert alle Kinder-Modelle mit dem angegebenen NameU
	 * @param nameU
	 * @return die List der Modelle
	 */
	public List<IModel> searchSections(String nameU);
	
	/**
	 * Liefert die Liste der Kinder-Modelle
	 * @return eine Liste von Modellen
	 */
	public List<IModel> getChildModels();

	// ModelType modelType
	/**
	 * Gibt den {@link ModelType Modelltyp} zurück
	 * @return der Modelltyp
	 */
	public ModelType getModelType();

	/**
	 * Liefert das übergeordnete Modell
	 * @return das Modell
	 */
	public IModel getParentModel();
	
	/**
	 * Liefert den universellen Namen des Modells
	 * @return der Name
	 */
	public String getNameU();
	
//	/**
//	 * Lädt das Datenmodel aus der Datei
//	 * @param jsonSection
//	 * @param parentSection
//	 * @return das geladene Modell
//	 * @throws TechnicalException
//	 * @throws InputFileException
//	 */
//	public IModel loadModel(Map<String, Object> modelMap, IModel parentSection) 
//			throws TechnicalException, InputFileException;
	 

	public IModel calculate(boolean firstRound) throws CellCalculationException;
	
//	public IModel traverseSectionsUp(String name) throws CellCalculationException;

//	public List<IModel> loadSubModels(Map<String, Object> map, String string, IModel model);
}