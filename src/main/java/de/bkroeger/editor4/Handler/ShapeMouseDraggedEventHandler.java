package de.bkroeger.editor4.Handler;

import de.bkroeger.editor4.model.ShapeEventData;
import de.bkroeger.editor4.view.BaseShapeView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Dieser EventHandler wird aufgerufen, wenn die Mouse mit dem Pfeil verschoben
 * wird. Dazu wird die Differenz zwischen der aktuellen Mouse-Position und der
 * gespeicherten Mouse-Position berechnet und der Pfeil entsprechend verschoben.
 * Die neue Position wird dann wieder gespeichert.
 *
 * @author bk
 */
public class ShapeMouseDraggedEventHandler implements EventHandler<MouseEvent> {

    @SuppressWarnings("unused")
    private BaseShapeView arrow;
    private ShapeEventData eventData;

    public ShapeMouseDraggedEventHandler(BaseShapeView shapeView, ShapeEventData eventData) {
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

            // Shape verschieben
            eventData.getModel().xProperty().set(eventData.getModel().xProperty().get() + deltaX);
            eventData.getModel().yProperty().set(eventData.getModel().yProperty().get() + deltaY);
        }

        event.consume();
    }
}
