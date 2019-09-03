package de.bkroeger.editor4.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Service;

import de.bkroeger.editor4.functions.FunctionDef;
import de.bkroeger.editor4.functions.IFunction;

@Service
public class FunctionService {
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	@Autowired
	private ApplicationContext appContext;
  	
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
		BeanDefinitionRegistry bdr = new SimpleBeanDefinitionRegistry();
		ClassPathBeanDefinitionScanner s = new ClassPathBeanDefinitionScanner(bdr);
	
		TypeFilter tf = new AssignableTypeFilter(IFunction.class);
		s.addIncludeFilter(tf);
		s.scan("de.bkroeger.editor4.functions");       
		String[] beans = bdr.getBeanDefinitionNames();
		for (int i=0; i<beans.length; i++) {
			BeanDefinition bd = bdr.getBeanDefinition(beans[i]);
			String name = bd.getBeanClassName();
			if (name.endsWith("Function")) {
				int p = name.lastIndexOf(".");
				name = name.substring(p+1);
				IFunction func = appContext.getBean(name, IFunction.class);
				functions.add(new FunctionDef(name, func));
//				logger.debug("Function "+name);
			}
		}
	}
}
