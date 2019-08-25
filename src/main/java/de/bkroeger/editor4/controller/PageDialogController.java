package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.model.PageDialogModel;
import de.bkroeger.editor4.view.PageDialogView;

public class PageDialogController implements IController {
	
	private PageDialogModel model;

	public PageDialogController(PageDialogModel model) {
		this.model = model;
	}
	
	public PageDialogView buildView() {
		
		PageDialogView dialog = new PageDialogView(model);
		return dialog;
	}
}
