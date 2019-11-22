package de.bkroeger.editor4.model;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javafx.application.Application.Parameters;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
@ToString(callSuper=true)
@Component
public class EditorModel extends BaseModel implements IModel {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(EditorModel.class.getName());
	
	private static final int DEFAULT_PANEL_WIDTH = 1200;
	private static final int DEFAULT_PANEL_HEIGHT = 800;
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	private int panelWidth;
	
	private int panelHeight;
	
	private String panelWidthString;
	public void setPanelWidthString(String value) {
		try {
			panelWidth = Integer.parseInt(value);
		} catch(NumberFormatException e) {
			panelWidth = DEFAULT_PANEL_WIDTH;
		}
	}
	
	private String panelHeightString;
	public void setPanelHeigthString(String value) {
		try {
			panelHeight = Integer.parseInt(value);
		} catch(NumberFormatException e) {
			panelHeight = DEFAULT_PANEL_HEIGHT;
		}
	}
	
	/**
	 * Die Aufrufparameter
	 */
	private Parameters parameters;
	
	/**
	 * <p>Flag, ob autoConnect eingeschaltet ist.</p>
	 * <p>Default-Wert = true</p>
	 */
	private final BooleanProperty autoConnect = new SimpleBooleanProperty(this, "autoConnect", true);
	public final boolean isAutoConnect() { return autoConnect.get(); }
	public final void setAutoConnect(boolean value) { 
	    autoConnect.set(value);
	}
	
	//================================================
	
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
	
	//==================================================
	
	/**
	 * Flag, ob das Connector-Tool ausgewählt wurde.</p>
	 * <p>Default-Wert = false</p>
	 */
	private final BooleanProperty orthogonalLine = new SimpleBooleanProperty(this, "ortogonalLine", true);
	public final boolean isOrthogonalLine() { return orthogonalLine.get(); }
	public final void setOrthogonalLine(boolean value) { 
	    orthogonalLine.set(value);
	}
	
	/**
	 * Flag, ob das Text-Tool ausgewählt wurde.</p>
	 * <p>Default-Wert = false</p>
	 */
	private final BooleanProperty curvedLine= new SimpleBooleanProperty(this, "curvedLine", false);
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
	
	private final StringProperty title = new SimpleStringProperty(this, "title", "Editor 4");
	public final String getTitle() { return title.get(); }
	public final void setTitle(String value) { title.set(value); }
	public StringProperty getTitleProperty() { return title; }
	
	public LineType getLineType() {
		if (isStraightLine()) return LineType.STRAIGHT;
		if (isOrthogonalLine()) return LineType.ORTHOGONAL;
		if (isCurvedLine()) return LineType.CURVED;
		return LineType.NONE;
	}
	
	//=================================================
	
	private Map<LineType, ShapeTemplate> lineTemplates = new EnumMap<>(LineType.class);
	public Map<LineType, ShapeTemplate> getLineTemplates() {
		
		if (lineTemplates.isEmpty()) {
			
			// Templates laden, die immer vorhanden sind
			loadGlobalLineTemplates();
			
			// Templates laden, die gewählten Template-Sets laden
			loadCurrentLineTemplates();
		}
		return lineTemplates;
	}
	
	private Map<String, ShapeTemplate> shapeTemplates = new HashMap<>();
	public Map<String, ShapeTemplate> getShapeTemplates() {
		
		if (shapeTemplates.isEmpty()) {
			
			// Templates laden, die immer vorhanden sind
			loadGlobalShapeTemplates();
			
			// Templates laden, die gewählten Template-Sets laden
			loadCurrentShapeTemplates();
		}
		return shapeTemplates;
	}
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/
	
	public EditorModel() { 
		super(ModelType.Editor);
	}

	
	/**========================================================================
	 * Public methods
	 *=======================================================================*/
	
	/**========================================================================
	 * Private methods
	 *=======================================================================*/
	
	private void loadGlobalLineTemplates() {
		
		lineTemplates.put(LineType.ORTHOGONAL, LineTemplate.buildOrthogonalLineTemplate());
		lineTemplates.put(LineType.STRAIGHT,  LineTemplate.buildStraightLineTemplate());
		lineTemplates.put(LineType.CURVED, LineTemplate.buildCurvedLineTemplate());
	}
	
	private void loadCurrentLineTemplates() {
		throw new UnsupportedOperationException();
	}
	
	private void loadGlobalShapeTemplates() {
		throw new UnsupportedOperationException();
	}
	
	private void loadCurrentShapeTemplates() {
		throw new UnsupportedOperationException();
	}
}
