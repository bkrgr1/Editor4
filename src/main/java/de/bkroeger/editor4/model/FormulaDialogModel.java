package de.bkroeger.editor4.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.functions.FunctionDef;
import de.bkroeger.editor4.functions.IFuncVarConst;
import de.bkroeger.editor4.functions.VariableDef;
import de.bkroeger.editor4.utils.TreeNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Das Datenmodell für den {@link FormulaDialogController}.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
@ToString
public class FormulaDialogModel {
	
	private static final Logger logger = LogManager.getLogger(FormulaDialogModel.class.getName());
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	/**
	 * Die Formel in String form
	 */
	private String formula;
	
	/**
	 * Das Model des Kontextes
	 */
	private IModel context;
	
	/**
	 * Die Liste der Funktionskategorien
	 */
	private ObservableList<String> categories = 
		    FXCollections.observableArrayList(
		        "All functions",
		        "Text functions",
		        "Numeric functions"
		    );
	
	/**
	 * Die Liste der {@link FunctionDef Funktionsdefinitionen} pro Funktionskategorie.
	 */
	private ObservableList<FunctionDef> functions = 
		    FXCollections.observableArrayList(

		    );
	
	/**
	 * Die Liste der Sections
	 */
	private ObservableList<String> sections = 
		    FXCollections.observableArrayList(
		    );
	
	/**
	 * Die Liste der {@link VariableDef Variablen} pro Section
	 */
	private ObservableList<VariableDef> variables = 
		    FXCollections.observableArrayList(
		    );
	
	/**
	 * Baumstruktur der Formel
	 */
	private TreeNode<IFuncVarConst> formulaTree = new TreeNode<>();
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/
	
	public FormulaDialogModel(String formula, IModel context) {
		this.formula = formula;
		this.context = context;
		
		// die übergebene Formel zerlegen
		
		
		// das Kontextmodell übernehmen 
		if (context instanceof PageModel) {
			sections.add("Page");
			sections.add("File");
		} else if (context instanceof ShapeModel) {
			sections.add("Shape");
			sections.add("Page");
			sections.add("File");
		} else {
			logger.warn("Invalid context object: "+context.getClass().getSimpleName());
		}
	}

}
