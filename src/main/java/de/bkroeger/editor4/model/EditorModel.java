package de.bkroeger.editor4.model;

import java.util.List;

import org.json.simple.JSONObject;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Die Klasse EditorModel enthält alle zentralen Daten der Editor4-Anwendung.</p>
 * <p>Diese Klasse ist ein Singleton, d.h. es gibt nur eine Instanz hiervon, die
 * mit der Klassenmethode {@link EditorModel#getEditorModel()} abgerufen werden kann.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
@ToString
public class EditorModel implements IModel {
	
	private static EditorModel editorModel;
	
	/**
	 * <p>Der Controller für EditorModel ist privat.</p>
	 */
	private EditorModel() { }
	
	/**
	 * <p>Liefert die einzige Instanz von EditorModel.</p>
	 * <p>Wenn noch keine Instanz erstellt wurde, wird sie mit dem privaten Constructor erstellt.</p>
	 * @return das einzige EditorModel
	 */
	public static EditorModel getEditorModel() {
		if (editorModel == null) editorModel = new EditorModel();
		return editorModel;
	}
	
	/**
	 * <p>Flag, ob autoConnect eingeschaltet ist.</p>
	 * <p>Default-Wert = true</p>
	 */
	private final BooleanProperty autoConnect = new SimpleBooleanProperty(this, "autoConnect", true);
	public final boolean isAutoConnect() { return autoConnect.get(); }
	public final void setAutoConnect(boolean value) { 
	    autoConnect.set(value);
	}
	
	/**
	 * Flag, ob das Pointer-Tool ausgewählt wurde.</p>
	 * <p>Default-Wert = true</p>
	 */
	private final BooleanProperty toolPointer = new SimpleBooleanProperty(this, "toolPointer", true);
	public final boolean isToolPointer() { return toolPointer.get(); }
	public final void setToolPointer(boolean value) { 
	    toolPointer.set(value);
	}
	
	/**
	 * Flag, ob das Connector-Tool ausgewählt wurde.</p>
	 * <p>Default-Wert = false</p>
	 */
	private final BooleanProperty toolConnector = new SimpleBooleanProperty(this, "toolConnector", false);
	public final boolean isToolConnector() { return toolConnector.get(); }
	public final void setToolConnector(boolean value) { 
	    toolConnector.set(value);
	}
	
	/**
	 * Flag, ob das Text-Tool ausgewählt wurde.</p>
	 * <p>Default-Wert = false</p>
	 */
	private final BooleanProperty toolText= new SimpleBooleanProperty(this, "toolText", false);
	public final boolean isToolText() { return toolText.get(); }
	public final void setToolText(boolean value) { 
	    toolText.set(value);
	}
	
	/**
	 * Flag, ob das Connector-Tool ausgewählt wurde.</p>
	 * <p>Default-Wert = false</p>
	 */
	private final BooleanProperty ortogonalLine = new SimpleBooleanProperty(this, "ortogonalLine", false);
	public final boolean isOrtogonalLine() { return ortogonalLine.get(); }
	public final void setOrtogonalLine(boolean value) { 
	    ortogonalLine.set(value);
	}
	
	/**
	 * Flag, ob das Text-Tool ausgewählt wurde.</p>
	 * <p>Default-Wert = false</p>
	 */
	private final BooleanProperty curvedLine= new SimpleBooleanProperty(this, "curvedLine", true);
	public final boolean isCurvedLine() { return curvedLine.get(); }
	public final void setCurvedLine(boolean value) { 
	    curvedLine.set(value);
	}
	
	/**
	 * Flag, ob das Pointer-Tool ausgewählt wurde.</p>
	 * <p>Default-Wert = true</p>
	 */
	private final BooleanProperty straightLine = new SimpleBooleanProperty(this, "Straight line", false);
	public final boolean isStraightLine() { return straightLine.get(); }
	public final void setStraightLine(boolean value) { 
	    straightLine.set(value);
	}

	@Override
	public SectionModel loadModel(JSONObject jsonSection, IModel parentSection)
			throws TechnicalException, InputFileException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CellModel getCellByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SectionModel> selectSections(SectionModelType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SectionModel getSection(SectionModelType location) throws InputFileException, CellCalculationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IModel calculate(boolean firstRound) throws CellCalculationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SectionModel traverseSectionsUp(String name) throws CellCalculationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IModel getParentModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SectionModel> searchSections(String sectionName) {
		// TODO Auto-generated method stub
		return null;
	}
}
