package de.bkroeger.editor4.view;

import de.bkroeger.editor4.model.PageModel;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;

public class NewPageDialog extends Dialog<PageModel> implements IView {

    private GridPane grid;

    /**
     * <p>
     * A dialog to create a new diagram page.
     * </p>
     * 
     * @author berthold
     */
    public NewPageDialog() {

        this.setTitle("Create a new page");

        this.grid = new GridPane();
        this.getDialogPane().setContent(grid);

        // Cancel and OK button
        this.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        Button okButton = (Button) this.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("Create Page");
    }
}