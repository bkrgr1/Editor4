package de.bkroeger.editor4.calculation;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.functions.ColorFunction;
import de.bkroeger.editor4.functions.ConcatFunction;
import de.bkroeger.editor4.functions.FunctionDef;
import de.bkroeger.editor4.functions.IntFunction;
import de.bkroeger.editor4.functions.TextFunction;
import de.bkroeger.editor4.model.ICell;
import de.bkroeger.editor4.model.ICellChangedListener;
import de.bkroeger.editor4.model.IModel;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>Der CalculatorService hilft bei der Berechnung der Formeln einer {@link ICell Zelle}.</p>
 * <p>Die Methode {@link #scanFormula(String)} zerlegt die Formel in einen Funktionsbaum.
 * Dabei sind die Funktionen die Knoten und die Konstanten sind die Blätter.</p>
 * <p>Dabei wird versucht, die benannten Variablen in der Hierarchie zu finden
 * (Dies setzt voraus, dass bereits die gesamte Objekthierarchie eingelesen ist).</p>
 * <p>Wenn die Zelle zur Variablen gefunden wird, wird ein {@link ICellChangedListener}
 * registriert, der dafür sorgt, dass Änderungen am Wert der referenzierten Zelle
 * zur Neuberechnung der Formel führen.</p>
 * <p>Die Methode {@link #calculate(ICell)} berechnet den Wert des Funktionsbaumes.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
public class CalculatorService<T> implements ICalculator<T> {

	private static final Logger logger = LogManager.getLogger(CalculatorService.class.getName());
	
	private static final String VARIABLE_PREFIX = "${";
	private static final char VARIABLE_SUFFIX = '}';
	private static final char OPEN_PAREN = '(';
	private static final char CLOSE_PAREN = ')'; 
	private static final String APOST = "'";
	
	/**
	 * Array der Funktionsdefinitionen
	 */
	private static final FunctionDef[] funcDefs = new FunctionDef[] {
			new FunctionDef("concat", new ConcatFunction()), 
			new FunctionDef("color", new ColorFunction()), 
			new FunctionDef("text", new TextFunction()), 
			new FunctionDef("int", new IntFunction())
			};
	
	/**
	 * Formel in eine Baumstruktur umwandeln und Variablen suchen.
	 * @param formula eine Formel
	 * @param die {@link ICell Zelle}, die die Formel enthält
	 * @return ein {@link FormulaTree Formel-Baum}
	 * @throws CellCalculationException 
	 */
	@Override
	public FormulaTree scanFormula(String formula, ICell targetCell) 
			throws CellCalculationException {
		
		try {
			logger.debug(() -> "Scan formula="+formula);
			
			FormulaTree tree =  new FormulaTree(scanString(formula, targetCell));
			logger.debug(() -> "FormulaTree="+tree.toString());
			
			return tree;
			
		} catch(Exception e) {
			throw new CellCalculationException(e.getMessage(), e);
		}
	}

	/**
	 * Berechnet den Wert der Formel.
	 * @param cell eine {@link ICell Zelle}
	 * @return eine Wert vom Typ T
	 */
	@Override
	public T calculate(ICell targetCell) throws CellCalculationException {
		
		// ist eine Formel vorhanden?
		if (targetCell.getFormula().isEmpty()) {
			throw new CellCalculationException("Formula is empty");
		}	
		
		// TODO: Wert berechnen
		
		return null;	
	}
	
	/**
	 * Analysiere einen Teilstring.
	 * @param eine Formel
	 * @param die {@link ICell Zelle}, die die Formel enthält
	 * @return eine Liste mit {@link IFormulaNode Formel-Knoten}
	 * @throws CellCalculationException 
	 */
	private List<IFormulaNode> scanString(String formula, ICell targetCell) 
			throws CellCalculationException {
		
		String form = formula;
		List<IFormulaNode> nodes = new ArrayList<>();
		do {
			NodeDef nodeDef = null;
			
			// Ist es eine Funktion?
			FunctionDef def = startsWithFunction(form);
			if (def != null) {
				
				// dies ist eine Formel
				nodeDef = scanFunction(form, def, targetCell);
		
			// Ist dies eine Variable?
			} else if (form.startsWith(VARIABLE_PREFIX)) {
				
				// dies ist eine Variable
				nodeDef = scanVariable(form, targetCell);
				
			} else {
				
				// dies ist eine Konstante
				nodeDef = scanConstant(form);
			}
			
			nodes.add(nodeDef.getNode());
			// den restlichen Teil der Formel ermitteln
			form = form.substring(Math.min(form.length(), nodeDef.getLength()));			
			// folgende Komma und Spaces entfernen
			form = form.trim();
			if (form.startsWith(",")) {
				form = form.substring(1);
				form = form.trim();
			}
			
		} while (form.length() > 0);
		return nodes;
	}
	
	/**
	 * Prüft, ob die Formel mit dem Namen einer Funktion beginnt.
	 * @param formula eine Formel
	 * @return eine {@link FunctionDef Funktionsdefinition}
	 */
	private FunctionDef startsWithFunction(String formula) {
		
		for (FunctionDef def : funcDefs) {
			// beginnt die Formel mit dem Namen der Funktion und einer Klammer
			if (formula.startsWith(def.getName()+"(")) {
				return def;
			}
		}
		return null;
	}
	
	/**
	 * Analysiert Funktionsaufrufe.
	 * @param formula eine Formel
	 * @param def eine {@link FunctionDef Funktionsdefinition}
	 * @return eine {@link NodeDef Knotendefinition}
	 * @throws CellCalculationException 
	 */
	private NodeDef scanFunction(String formula, FunctionDef def, ICell targetCell) 
			throws CellCalculationException {
		
		NodeDef nd = new NodeDef();
		IFormulaNode node = new FunctionNode(def);
		
		// die Formel innerhalb der Funktionsklammern ermitteln
		String subFormula = extractSubFormula(formula);
		// und analysieren
		List<IFormulaNode> nodes = scanString(subFormula, targetCell);
		node.getChildNodes().addAll(nodes);
		
		nd.setNode(node);
		nd.setLength(def.getName().length() + subFormula.length() + 2);
		return nd;
	}
	
	/**
	 * Analysiert Konstanten
	 * @param formula eine Formel
	 * @return eine {@link NodeDef Knotendefinition}
	 */
	private NodeDef scanConstant(String formula) {
		
		NodeDef nd = new NodeDef();
		IFormulaNode node = null;
		
		// beginnt die Formel mit einem Apostrophe?
		if (formula.startsWith(APOST)) {
			StringBuilder sb = new StringBuilder();
			int delta = 1;
			for (int p=1; p<formula.length(); p++) {
				String x = formula.substring(p,p+1);
				if (APOST.equals(x)) {
					// ist das nächste Zeichen auch ein Apostrophe?
					if ((p+1) <formula.length() && APOST.equals(formula.substring(p+1,p+2))) {
						sb.append("'");
					} else {
						delta += 1;
						break;
					}
				} else {
					sb.append(x);
				}
			}
			node = new StringConstantNode(sb.toString());
			nd.setLength(sb.length()+delta);
			
		} else {
			String[] parts = formula.split("s*[,]{1}s*");
			String form = parts[0];
			nd.setLength(form.length());
			try {
				// ist dies eine Zahl?
				Double d = Double.parseDouble(form);
				node = new DoubleConstantNode(d);
				
			} catch(NumberFormatException e) {
				if (form.equalsIgnoreCase("true") || form.equalsIgnoreCase("false")) {
					node = new BooleanConstantNode(Boolean.parseBoolean(formula));
					
				} else {
					
					// die ganze Formel als String betrachten
					node = new StringConstantNode(form);
				}
			}
		}
		
		nd.setNode(node);
		return nd;
	}
	
	/**
	 * Analysiert eine Variable
	 * @param formula eine Formel
	 * @param die {@link ICell Zelle}, die die Formel enthält
	 * @return eine {@link NodeDef Knotendefinition}
	 * @throws CellCalculationException 
	 */
	private NodeDef scanVariable(String formula, ICell targetCell) 
			throws CellCalculationException {
		
		NodeDef nd = new NodeDef();
		VariableNode node = new VariableNode();
		node.setTargetCell(targetCell);
		nd.setNode(node);
		
		// Namen der Variablen extrahieren
		String name = extractVariableName(formula);
		node.setName(name);
		// Zelle mit diesem Namen suchen
		ICell sourceCell = searchVariableCell(name, targetCell);
		node.setSourceCell(sourceCell);
		// CellChangedListener registrieren
		
		nd.setLength(VARIABLE_PREFIX.length() + 1 + name.length());
		return nd;
	}
	
	/**
	 * Sucht in der Datenhierarchie nach der Variablen.
	 * @param name der zusammengesetzte Name der Variablen
	 * @return die {@link ICell Zelle} mit dem Wert
	 * @throws CellCalculationException 
	 */
	private ICell searchVariableCell(String name, ICell targetCell) 
			throws CellCalculationException {
		
		IModel model = targetCell.getModel();
		String[] parts = name.split(".");
		if (parts.length > 1) {
			// suche das Objekt mit dem angegebenen Namen in der
			// übergeordneten Hierarchie
			while (model != null) {
				if (model.getNameU().equals(parts[0])) {
					break;
				} else {
					model = model.getParentModel();
				}
			}
			if (model == null) {
				// TODO
				throw new CellCalculationException("Objekt %s not found");
			}
			// untergeordnete Objekte suchen
			for (int i=1; i<parts.length-2; i++) {
				
				// ist dort ein untergeordnetes Objekt mit dem Namen
				boolean found = false;
				for (IModel m : model.getChildModels()) {
					if (m.getNameU().equals(parts[i])) {
						model = m;
						found = true;
						break;
					}
				}
				if (!found) {
					// TODO
					throw new CellCalculationException("Model for name %s not found.");
				}
			}
			// Zelle im Model suchen
			ICell sourceCell = (ICell) model.getCellByName(parts[parts.length-1]);
			if (sourceCell == null) {
				// TODO
				throw new CellCalculationException("Cell %s not found");
			}
		
		} else {
			// TODO: suche die Zelle in dem Objekt, zu dem die Target-Zelle gehört
		}
		return null;
	}

	/**
	 * Extrahiert den symbolischen Pfadnamen einer Variablen.
	 * Der Name muss in "${" und "}" eingeschlossen sein.
	 * @param formula eine Formel
	 * @return ein symbolischer Name
	 */
	private String extractVariableName(String formula) {
		
		StringBuilder sb = new StringBuilder();
		int p = VARIABLE_PREFIX.length();
		for (; p<formula.length(); p++) {
			
			char x = formula.charAt(p);
			if (x == VARIABLE_SUFFIX) {
				break;
			} else
				sb.append(x);
		}
		return sb.toString();
	}

	/**
	 * Extrahiert einen String aus der Formel, der bei der ersten
	 * öffnenden Klammer beginnt und bis zur passenden schliessenden
	 * Klammer reicht.
	 * @param formula eine Formel
	 * @return die Teile der Formel innerhalb der Klammern
	 */
	private String extractSubFormula(String formula) {
		
		StringBuilder sb = new StringBuilder();
		int p = formula.indexOf(OPEN_PAREN);
		int cnt = 0;
		for (p = p+1; p<formula.length(); p++) {
			
			char x = formula.charAt(p);
			if (x == OPEN_PAREN) { 
				cnt++; 
				sb.append(x);
			}
			else if (x == CLOSE_PAREN) {
				if (cnt > 0) {
					cnt--;
					sb.append(x);
				} else {
					break;
				}
			} else
				sb.append(x);
		}
		return sb.toString();
	}
	
	//=========================================================================
	
	@Getter
	@Setter
	private class NodeDef {
		private IFormulaNode node;
		private int length;
	}
}
