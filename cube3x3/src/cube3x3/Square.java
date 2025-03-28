package cube3x3;

import java.awt.Color;
import java.awt.Polygon;

public class Square extends Polygon {
    final private static int NPOINTS = 4;
    
    final private Color color;
    private Axis axis;
    
    public Square(Color color, Axis axis) {
        this.color = color;
        this.axis = axis;
        
        npoints = NPOINTS;
        
        xpoints = new int[npoints];
        ypoints = new int[npoints];
    }
    
    public Color getColor() {
        return color;
    }
    
    public Axis getAxis() {
        return axis;
    }
    
    public void setAxis(Axis axis) {
        this.axis = axis;
    }
    
    public static boolean isClockWise(int[] x, int[] y) {
        return (x[2] - x[1]) * (y[0] - y[1]) - (x[0] - x[1]) * (y[2] - y[1]) > 0;        
    }
    
}
