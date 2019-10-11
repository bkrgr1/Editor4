package de.bkroeger.editor4.runtime;

import java.util.ArrayList;
import java.util.List;

import de.bkroeger.editor4.controller.FileController;
import de.bkroeger.editor4.model.FileModel;
import de.bkroeger.editor4.view.TabPaneView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CenterRuntime implements IRuntime {
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	/**
	 * Die Parent-Runtime-Struktur
	 */
	private IRuntime parent;
	public IRuntime getParent() { return this.parent; }

	/**
	 * Das Datenmodel für die angezeigt Datei
	 */
	private FileModel model;
	
	/**
	 * Der verantwortliche Controller
	 */
	private FileController controller;
	
	/**
	 * Die generierte View
	 */
	private TabPaneView view;
	
	/**
	 * Die Map der angezeigten Seiten
	 */
	private List<PageRuntime> pages = new ArrayList<>();
	public void addPageRuntime(PageRuntime pageRuntime) {
		pages.add(pageRuntime);
	}

	public CenterRuntime(IRuntime parent) {
		this.parent = parent;
	}
}
