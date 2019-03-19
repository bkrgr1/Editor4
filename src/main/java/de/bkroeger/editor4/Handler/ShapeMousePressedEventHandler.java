package de.bkroeger.editor4.Handler;

import de.bkroeger.editor4.model.ShapeEventData;
import de.bkroeger.editor4.view.IShapeView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Dieser EventHandler wird aufgerufen, wenn die Mouse über dem Pfeil gedrückt
 * wird. Die aktuelle Position der Mouse wird gespeichert.
 *
 * @author bk
 */
public class ShapeMousePressedEventHandler implements EventHandler<MouseEvent> {

    @SuppressWarnings("unused")
    private IShapeView shapeView;
    private ShapeEventData eventData;

    public ShapeMousePressedEventHandler(IShapeView shapeView, ShapeEventData eventData) {
        this.shapeView = shapeView;
        this.eventData = eventData;
    }

    @Override
    public void handle(MouseEvent event) {

        eventData.setMouseX(new Double(event.getSceneX()));
        eventData.setMouseY(new Double(event.getSceneY()));

        event.consume();
    }
}
