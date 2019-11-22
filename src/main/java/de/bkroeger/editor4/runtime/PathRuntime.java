package de.bkroeger.editor4.runtime;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.controller.PathController;
import de.bkroeger.editor4.controller.PathElemController;
import de.bkroeger.editor4.model.PathElementModel;
import de.bkroeger.editor4.model.PathModel;
import de.bkroeger.editor4.view.GroupView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PathRuntime extends BaseRuntime implements IRuntime {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(PathRuntime.class.getName());

	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private IRuntime parent;
	public IRuntime getParent() { return this.parent; }

	private PathModel model;
	/**
	 * Der verantwortliche Controller
	 */
	private PathController controller;
	
	/**
	 * Die generierte View
	 */
	private GroupView view;
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/
	
	public PathRuntime() { }

	public PathRuntime(IRuntime parent) {
		this.parent = parent;
	}

	private List<PathElemRuntime> pathElements = new ArrayList<>();
	public void addPathElem(PathElemRuntime value) {
		pathElements.add(value);
	}
		
	/**========================================================================
	 * Public methods
	 *=======================================================================*/

	public void init() {
		
		for (PathElementModel elementModel : this.model.getElements()) {
			
	    	PathElemRuntime elementRuntime = applicationContext.getBean(PathElemRuntime.class);
	    	PathElemController elementController = applicationContext.getBean(PathElemController.class);
	    	elementRuntime.setParent(this);
	    	elementRuntime.setModel(elementModel);
	    	elementRuntime.setController(elementController);
	    	elementRuntime.init();
		}
	}
}
