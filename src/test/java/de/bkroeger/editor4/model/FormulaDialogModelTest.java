package de.bkroeger.editor4.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import de.bkroeger.editor4.functions.ConcatFunction;
import de.bkroeger.editor4.functions.FunctionDef;
import de.bkroeger.editor4.functions.IFuncVarConst;
import de.bkroeger.editor4.functions.IntFunction;
import de.bkroeger.editor4.functions.TextFunction;
import de.bkroeger.editor4.services.FunctionService;
import de.bkroeger.editor4.utils.TreeNode;

public class FormulaDialogModelTest {

	private static final Logger logger = LogManager.getLogger(FormulaDialogModelTest.class.getName());
	
	private static final String FORMULA1 = "concat('Page ',text(int(${PageNo})))";

    @Test
    public void whenParseFormula_thenSuccess() {
    	
    	FunctionService functionService = new FunctionService(buildFunctionList());
    	PageModel pageModel = new PageModel();
    	FormulaDialogModel model = new FormulaDialogModel(FORMULA1, pageModel, functionService);
    	TreeNode<IFuncVarConst> node = model.getFormulaTree();
    	
    	logger.debug("Node="+node.toString());
    	
    	assertEquals("Zu wenige Children", 1, node.getChildren().size());
    	assertNotEquals("Keine grand children", 0, node.getChildren().get(0).getChildren().size());
    	assertEquals("Zu wenig Grandchildren", 2, node.getChildren().get(0).getChildren().size());
    }

	private List<FunctionDef> buildFunctionList() {
		List<FunctionDef> functions = new ArrayList<>();
		functions.add(new FunctionDef("concat", new ConcatFunction()));
		functions.add(new FunctionDef("text", new TextFunction()));
		functions.add(new FunctionDef("int", new IntFunction()));
		return functions;
	}
}
