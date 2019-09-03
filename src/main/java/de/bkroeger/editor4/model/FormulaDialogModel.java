package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.controller.FormulaDialogController;
import de.bkroeger.editor4.functions.ConstantDef;
import de.bkroeger.editor4.functions.FunctionDef;
import de.bkroeger.editor4.functions.IFuncVarConst;
import de.bkroeger.editor4.functions.NullDef;
import de.bkroeger.editor4.functions.VariableDef;
import de.bkroeger.editor4.services.FunctionService;
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
	
	private static final String VARIABLE_PREFIX = "${";

	private static final Logger logger = LogManager.getLogger(FormulaDialogModel.class.getName());
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	/**
	 * Die Formel in String form
	 */
	private String formula;
	public void setFormula(String value) {
		parseFormula(value, formulaTree);
	}
	
	/**
	 * Das Model des Kontextes
	 */
	private IModel context;

	private FunctionService functionService;
	
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
	@SuppressWarnings("rawtypes")
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
	
	public FormulaDialogModel(String formula, IModel context, FunctionService functionService) {
		this.formula = formula;
		this.context = context;
		this.functionService = functionService;
		
		// die übergebene Formel zerlegen
		if (formula != null) {
			parseFormula(formula, formulaTree);
		}
		
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
	
	/**========================================================================
	 * Public methods
	 *=======================================================================*/
	
	/**========================================================================
	 * Private methods
	 *=======================================================================*/

	void parseFormula(String formula, TreeNode<IFuncVarConst> node) {
		
		logger.debug("Formula = "+formula);
		
		// Formel in Teile zerlegen, die durch Komma getrennt sind
		// dabei aber beachten, dass keine Kommas innerhalb von Klammern berücksichtigt werden
		List<String> formParts = getParts(formula);
		
		List<FunctionDef> functions = functionService.getFunctions();
		
		FunctionDef def;
		
		for (String formPart : formParts) {
			logger.debug("Parse formula part '"+formPart+"'");
			
			// Beginnt die Formel mit einer Variablen?
			if (formPart.startsWith(VARIABLE_PREFIX)) {
				
				// Variable prüfen
				parseVariable(formula, node);
				
			// beginnt die Formel mit einem Funktionsaufruf?
			} else if ((def = isAFunction(formPart, functions)) != null) {
				
				// Funktionsaufruf bearbeiten
				parseFunction(formPart, def, node);
				
			// oder ist es eine Konstante?
			} else {
				
				// Konstante prüfen
				parseConstant(formPart, node);
			}
		}
	}

	private List<String> getParts(String formula) {
		List<String> formParts = new ArrayList<>();
		String part = "";
		int parenthesis = 0;
		for (int i=0; i<formula.length(); i++) {
			
			String x = formula.substring(i,i+1);
			if (",".equals(x)) {
				if (parenthesis == 0) {
					formParts.add(part);
					part = "";
				} else {
					part += x;
				}
			} else if ("(".equals(x)) {
				parenthesis++;
				part += x;
			} else if (")".equals(x)) {
				parenthesis--;
				part += x;
			} else {
				part += x;
			}
		}
		formParts.add(part);
		return formParts;
	}

	private void parseVariable(String formula, TreeNode<IFuncVarConst> node) {
		
		// TODO: Varialbe prüfen
		VariableDef<String> def = new VariableDef<>(formula, null);
		node.addChild(new TreeNode<IFuncVarConst>(def));
	}

	private void parseFunction(String formula, FunctionDef functionDef, TreeNode<IFuncVarConst> node) {
		
		int p = functionDef.getName().length();
		String value = formula.substring(p+1, formula.length()-1);
		TreeNode<IFuncVarConst> child = new TreeNode<>(functionDef);
		node.addChild(child);
		parseFormula(value, child);
	}

	private void parseConstant(String formula, TreeNode<IFuncVarConst> node) {
		
		if (formula.startsWith("'")) {
			String s = formula.substring(1, formula.length()-1);
			ConstantDef<String> def = new ConstantDef<>(s);
			node.addChild(new TreeNode<IFuncVarConst>(def));
			
		} else {
			try {
				Double d = Double.parseDouble(formula);
				ConstantDef<Double> def = new ConstantDef<>(d);
				node.addChild(new TreeNode<IFuncVarConst>(def));
				
			} catch(Exception e) {
				node.addChild(new TreeNode<IFuncVarConst>(new NullDef()));
			}
		}		
	}

	private FunctionDef isAFunction(String formula, List<FunctionDef> functions) {
		
		int p = formula.indexOf("(");
		if (p <= 0) return null;
		String funcName = formula.substring(0,p);
		
		for (FunctionDef def : functions) {
			if (funcName.equalsIgnoreCase(def.getName())) {
				return def;
			}
		}
		return null;
	}
}
