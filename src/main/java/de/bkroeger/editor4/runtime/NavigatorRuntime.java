package de.bkroeger.editor4.runtime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.controller.NavigatorController;
import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.view.NavigatorView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NavigatorRuntime implements IRuntime {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(NavigatorRuntime.class.getName());

	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	private IRuntime parent;
	public IRuntime getParent() { return this.parent; }

	private EditorModel model;
	/**
	 * Der verantwortliche Controller
	 */
	private NavigatorController controller;
	
	/**
	 * Die generierte View
	 */
	private NavigatorView view;

	public NavigatorRuntime(IRuntime parent) {
		this.parent = parent;
	}
}
