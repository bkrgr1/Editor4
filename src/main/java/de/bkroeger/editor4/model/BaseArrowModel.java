package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 * <p>
 * This is the base model for all arrows.
 * </p>
 * 
 * @author bk
 */
public class BaseArrowModel implements IArrowModel {

    protected String title;

    @Override
    public String getTitle() {
        return title;
    }

    protected DoubleProperty x1Property;

    @Override
    public DoubleProperty x1Property() {
        return x1Property;
    }

    @Override
    public DoubleProperty xProperty() {
        return x1Property;
    }

    protected DoubleProperty y1Property;

    @Override
    public DoubleProperty y1Property() {
        return y1Property;
    }

    @Override
    public DoubleProperty yProperty() {
        return y1Property;
    }

    protected DoubleProperty x2Property;

    @Override
    public DoubleProperty x2Property() {
        return x2Property;
    }

    protected DoubleProperty y2Property;

    @Override
    public DoubleProperty y2Property() {
        return y2Property;
    }

    protected DoubleProperty rotateProperty;

    @Override
    public DoubleProperty rotateProperty() {
        return rotateProperty;
    }

    @Override
    public ArrowType getArrowType() {
        return ArrowType.straightArrow;
    }

    protected ObjectProperty<LineEndingType> lineStartTypeProperty = new SimpleObjectProperty<LineEndingType>(
            LineEndingType.None);

    @Override
    public LineEndingType getLineStartType() {
        return lineStartTypeProperty.get();
    }

    public ObjectProperty<LineEndingType> lineStartTypeProperty() {
        return lineStartTypeProperty;
    }

    protected ObjectProperty<LineEndingType> lineEndTypeProperty = new SimpleObjectProperty<LineEndingType>(
            LineEndingType.None);

    @Override
    public LineEndingType getLineEndType() {
        return lineEndTypeProperty.get();
    }

    public ObjectProperty<LineEndingType> lineEndTypeProperty() {
        return lineEndTypeProperty;
    }

    protected ObjectProperty<Color> colorProperty = new SimpleObjectProperty<Color>(Color.BLACK);

    @Override
    public Color getColor() {
        return colorProperty.get();
    }

    public ObjectProperty<Color> colorProperty() {
        return colorProperty;
    }

    protected DoubleProperty strokeWidthProperty = new SimpleDoubleProperty(1.0);

    @Override
    public DoubleProperty strokeWidthProperty() {
        return strokeWidthProperty;
    }

    protected ObjectProperty<LineStrokeType> strokeTypeProperty;

    @Override
    public LineStrokeType getStrokeType() {
        return strokeTypeProperty.get();
    }

    public ObjectProperty<LineStrokeType> strokeTypeProperty() {
        return strokeTypeProperty;
    }

    protected List<IConnectorModel> connectorModels = new ArrayList<>();

    @Override
    public List<IConnectorModel> getConnectorModels() {
        return this.connectorModels;
    }

    /**
     * Constructor
     * 
     * @param x1     x position of start point
     * @param y1     y position of start point
     * @param x2     x position of end point
     * @param y2     y position of end point
     * @param rotate rotation angle in degree
     */
    public BaseArrowModel(double x1, double y1, double x2, double y2, double rotate) {
        this.x1Property = new SimpleDoubleProperty(x1);
        this.y1Property = new SimpleDoubleProperty(y1);
        this.x2Property = new SimpleDoubleProperty(x2);
        this.y2Property = new SimpleDoubleProperty(y2);
        this.rotateProperty = new SimpleDoubleProperty(rotate);
    }
}