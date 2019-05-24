package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.model.IModel;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.view.IView;
import de.bkroeger.editor4.view.NewPageDialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Dialog;

public class NewPageDialogController implements IController {

    private PageModel pageModel;

    @Override
    public IModel getModel() {
        return pageModel;
    }

    private NewPageDialog newPageDialog;

    @Override
    public IView getView() {
        return newPageDialog;
    }

    /**
     * <p>
     * Controller for the {@link NewPageDialog}
     * </p>
     * 
     * @author berthold
     */
    public NewPageDialogController(int maxPageNo) {

        this.newPageDialog = new NewPageDialog();

        this.pageModel = new PageModel();
        this.pageModel.setPageNo(maxPageNo + 1);

        ((Dialog<PageModel>) this.newPageDialog).setResultConverter((dialogButton) -> {
            ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonData.OK_DONE ? this.pageModel : null;
        });
    }
}