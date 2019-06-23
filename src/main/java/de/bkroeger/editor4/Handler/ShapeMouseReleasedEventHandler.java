package de.bkroeger.editor4.Handler;

import de.bkroeger.editor4.model.ShapeEventData;
import de.bkroeger.editor4.view.BaseShapeView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Dieser EventHandler wird aufgerufen, wenn die Mouse über dem Pfeil wieder
 * freigegeben wird. Wenn dies über einem Connector geschiet, wird der Pfeil an
 * den Connector gebunden.
 *
 * @author bk
 */
public class ShapeMouseReleasedEventHandler implements EventHandler<MouseEvent> {

    @SuppressWarnings("unused")
    private BaseShapeView arrow;
    private ShapeEventData eventData;

    public ShapeMouseReleasedEventHandler(BaseShapeView shapeView, ShapeEventData eventData) {
        this.arrow = shapeView;
        this.eventData = eventData;
    }

    @Override
    public void handle(MouseEvent event) {

        if (eventData.getMouseX() != null && eventData.getMouseY() != null) {

            double deltaX = event.getSceneX() - eventData.getMouseX();
            double deltaY = event.getSceneY() - eventData.getMouseY();

            eventData.setMouseX(eventData.getMouseX() + deltaX);
            eventData.setMouseY(eventData.getMouseY() + deltaY);

//            eventData.getModel().xProperty().set(eventData.getModel().xProperty().get() + deltaX);
//            eventData.getModel().yProperty().set(eventData.getModel().yProperty().get() + deltaY);

            eventData.setMouseX(null);
            eventData.setMouseY(null);

            event.consume();
        }
    }
}
