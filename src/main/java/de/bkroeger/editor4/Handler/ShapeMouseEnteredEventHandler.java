package de.bkroeger.editor4.Handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.view.BaseShapeView;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

/**
 * Dieser EventHandler wird ausgeführt, wenn die Mouse über einem 2D-Shape ist. In
 * diesem Fall, wird der Cursor in einen Hand-Cursor geändert.
 *
 * @author bk
 */
public class ShapeMouseEnteredEventHandler implements EventHandler<MouseEvent> {

    private static final Logger logger = LogManager.getLogger(ShapeMouseEnteredEventHandler.class.getName());

    private BaseShapeView shapeView;

    public ShapeMouseEnteredEventHandler(BaseShapeView shapeView){
        this.shapeView = shapeView;
    }

    @Override
    public void handle(MouseEvent event) {
        logger.debug("Mouse entered shape");
        shapeView.getScene().setCursor(Cursor.HAND);
        shapeView.setMouseOver(true);
        event.consume();
    }
}
