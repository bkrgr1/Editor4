package de.bkroeger.editor4.Handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.view.BaseShapeView;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Dieser EventHandler wird ausgeführt, wenn die Mouse den Arrow verlässt. In
 * diesem Fall wird der Default-Cursor wieder angezeigt.
 *
 * @author bk
 */
public class ShapeMouseExitedEventHandler implements EventHandler<MouseEvent> {

    private static final Logger logger = LogManager.getLogger(ShapeMouseExitedEventHandler.class.getName());

    private BaseShapeView shapeView;

    public ShapeMouseExitedEventHandler(BaseShapeView shapeView) {
        this.shapeView = shapeView;
    }

    @Override
    public void handle(MouseEvent event) {
        logger.debug("Mouse exited shape");
        ((Node) shapeView).getScene().setCursor(Cursor.DEFAULT);
        shapeView.setSelected(false);
        event.consume();
    }
}
