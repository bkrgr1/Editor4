package de.bkroeger.editor4.Handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.view.IShapeView;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Dieser EventHandler wird ausgeführt, wenn die Mouse über dem Arrow ist. In
 * diesem Fall, wird der Cursor in einen Hand-Cursor geändert.
 *
 * @author bk
 */
public class ShapeMouseEnteredEventHandler implements EventHandler<MouseEvent> {

    private static final Logger logger = LogManager.getLogger(ShapeMouseEnteredEventHandler.class.getName());

    private IShapeView shapeView;

    public ShapeMouseEnteredEventHandler(IShapeView shapeView) {
        this.shapeView = shapeView;
    }

    @Override
    public void handle(MouseEvent event) {
        logger.debug("Mouse entered shape");
        ((Node) shapeView).getScene().setCursor(Cursor.HAND);
        shapeView.setSelected(true);
        event.consume();
    }
}
