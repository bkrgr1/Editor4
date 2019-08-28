package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.view.NewPageDialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Dialog;

public class NewPageDialogController implements IController {

    private PageModel pageModel;
    
    private NewPageDialog newPageDialog;
    public NewPageDialog getView() {
    	return newPageDialog;
    }

    /**
     * <p>
     * Controller for the {@link NewPageDialog}
     * </p>
     * 
     * @author berthold
     * @throws CellCalculationException 
     */
    public NewPageDialogController(int maxPageNo) throws CellCalculationException {

        this.newPageDialog = new NewPageDialog();

        this.pageModel = new PageModel();
        this.pageModel.setPageNo(maxPageNo + 1);

        ((Dialog<PageModel>) this.newPageDialog).setResultConverter((dialogButton) -> {
            ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonData.OK_DONE ? this.pageModel : null;
        });
    }
}