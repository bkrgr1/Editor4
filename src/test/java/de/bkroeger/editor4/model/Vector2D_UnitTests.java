package de.bkroeger.editor4.model;

import static org.junit.Assert.assertNotNull;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Vector2D_UnitTests {

    private static final double DELTA = 1e-15;

    @Test
    public void whenConstructingVector_thenSuccess() {

        DoubleProperty x1Property = new SimpleDoubleProperty(100.0);
        DoubleProperty y1Property = new SimpleDoubleProperty(100.0);
        DoubleProperty x2Property = new SimpleDoubleProperty(200.0);
        DoubleProperty y2Property = new SimpleDoubleProperty(150.0);
        Vector2D vector = new Vector2D(x1Property, y1Property, x2Property, y2Property);
        assertNotNull("vector not defined", vector);
        assertEquals("Invalid horizontal length", 100.0, (double) vector.getA(), DELTA);
        double b = vector.getB();
        double c = vector.getC();
        System.out.println("C="+c);
        assertEquals("Invalid vertical length", -50.0, b, DELTA);
        assertEquals("Invalid c", 111.803399, c, 0.0001);
    }
}