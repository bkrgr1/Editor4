package de.bkroeger.editor4.view;

import java.util.ArrayList;
import java.util.List;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.model.ConnectorModel;
import de.bkroeger.editor4.model.ShapeModel;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * <p>Dieses GroupView ist die Basis für jedes 2D-Shape.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
public class GroupView extends Pane implements IView {
	
	private static final double CONNECTORY = 5.0;
	private static final double CONNECTORX = 5.0;
	
	private List<ConnectorPointView> connectors = new ArrayList<>();
	public List<ConnectorPointView> getConnectors() {
		return connectors;
	}

	/**
	 * <p>Constructor</p>
	 * <p>Erzeugt ein Pane, in dem das Shape gezeichnet wird.</p>
	 * @param model ein {@link ShapeModel}
	 */
	public GroupView(ShapeModel model) {
		super();
		
//		// TEST TEST TEST
//		this.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	/**
	 * <p>Die Konnectoren für ein 2D-Shape zeichnen.</p>
	 * <p>Ein ConnectorView ist ein Pane mit einem diagonalen Kreuz in Blau.</p>
	 * 
	 * @param connectors Liste der {@link ConnectorModel}s für die Konnectoren
	 * @return Liste der ConnectorViews
	 * @throws CellCalculationException 
	 */
	public List<ConnectorPointView> buildConnectorViews(List<ConnectorModel> connectorModels) 
			throws CellCalculationException {
		
		this.connectors.clear();
		// die Connector-Points zeichnen
		for (ConnectorModel connectorModel : connectorModels) {
			
			ConnectorPointView connectorPane = new ConnectorPointView(connectorModel);
			connectorPane.setPrefSize(CONNECTORX * 2.0, CONNECTORY * 2.0);
			connectorPane.layoutXProperty().bind(Bindings.subtract(connectorModel.getXProperty(), CONNECTORX));
			connectorPane.layoutYProperty().bind(Bindings.subtract(connectorModel.getYProperty(), CONNECTORY));
			
			Path connectorPath = new Path();
			MoveTo moveTo1 = new MoveTo();
			moveTo1.setX(0.0);
			moveTo1.setY(0.0);
			LineTo lineTo1 = new LineTo();
			lineTo1.setX(CONNECTORX * 2.0);
			lineTo1.setY(CONNECTORY * 2.0);
			MoveTo moveTo2 = new MoveTo();
			moveTo2.setX(CONNECTORX * 2.0);
			moveTo2.setY(0.0);
			LineTo lineTo2 = new LineTo();
			lineTo2.setX(0.0);
			lineTo2.setY(CONNECTORY * 2.0);			
			connectorPath.getElements().addAll(moveTo1, lineTo1, moveTo2, lineTo2);
			connectorPath.setStroke(Color.BLUE);
			
			connectorPane.getChildren().add(connectorPath);
			
			connectorPane.setVisible(false);
			
			this.connectors.add(connectorPane);
		}

		return this.connectors;
	}

	public void showConnectorPoints(boolean visible) {
		for (Node path : this.connectors) {
			path.setVisible(visible);
		}
	}
}
