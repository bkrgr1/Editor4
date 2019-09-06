package de.bkroeger.editor4.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.bkroeger.editor4.functions.ColorFunction;
import de.bkroeger.editor4.functions.ConcatFunction;
import de.bkroeger.editor4.functions.FunctionDef;
import de.bkroeger.editor4.functions.IntFunction;
import de.bkroeger.editor4.functions.TextFunction;

@Service
public class FunctionService {
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	@Resource
	private ConcatFunction concatFunction;
	@Resource
	private TextFunction textFunction;
	@Resource
	private IntFunction intFunction;
	@Resource
	private ColorFunction colorFunction;
  	
  	private List<FunctionDef> functions;
  	public List<FunctionDef> getFunctions() { 
  		if (functions == null) {
  			loadFunctions();
  		}
  		return functions; 
  	}
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/

  	public FunctionService() { }
  	
  	public FunctionService(List<FunctionDef> functions) {
  		this.functions = functions;
  	}
	
	/**========================================================================
	 * Private methods
	 *=======================================================================*/

	/**
	 * Ermittelt alle Beans, die das Interface IFunction implementieren.
	 */
	private void loadFunctions() {
		
		functions = new ArrayList<FunctionDef>();
		functions.add(new FunctionDef("concat", concatFunction));
		functions.add(new FunctionDef("text", textFunction));
		functions.add(new FunctionDef("int", intFunction));
		functions.add(new FunctionDef("color", colorFunction));
//		BeanDefinitionRegistry bdr = new SimpleBeanDefinitionRegistry();
//		ClassPathBeanDefinitionScanner s = new ClassPathBeanDefinitionScanner(bdr);
//	
//		TypeFilter tf = new AssignableTypeFilter(IFunction.class);
//		s.addIncludeFilter(tf);
//		s.scan("de.bkroeger.editor4.functions");       
//		String[] beans = bdr.getBeanDefinitionNames();
//		for (int i=0; i<beans.length; i++) {
//			BeanDefinition bd = bdr.getBeanDefinition(beans[i]);
//			String name = bd.getBeanClassName();
//			if (name.endsWith("Function") && !name.endsWith("ConcatFunction")) {
//				int p = name.lastIndexOf(".");
//				name = name.substring(p+1);
//				IFunction func = appContext.getBean(name, IFunction.class);
//				functions.add(new FunctionDef(name, func));
////				logger.debug("Function "+name);
//			}
//		}
	}
}
