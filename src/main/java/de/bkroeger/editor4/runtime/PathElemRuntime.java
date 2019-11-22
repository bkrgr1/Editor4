package de.bkroeger.editor4.runtime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.controller.PathElemController;
import de.bkroeger.editor4.model.PathElementModel;
import de.bkroeger.editor4.view.PathElemView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PathElemRuntime extends BaseRuntime implements IRuntime {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(PathElemRuntime.class.getName());

	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	@Autowired
	private ApplicationContext applicationContext;

	private PathElementModel model;
	
	/**
	 * Der verantwortliche Controller
	 */
	private PathElemController controller;
	
	/**
	 * Die generierte View
	 */
	private PathElemView view;
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/
	
	public PathElemRuntime() { }

	public PathElemRuntime(IRuntime parent) {
		this.parent = parent;
	}
	
	/**========================================================================
	 * Public methods
	 *=======================================================================*/

	public void init() {
		// nothing to do
	}
}
