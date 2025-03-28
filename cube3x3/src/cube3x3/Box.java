package cube3x3;

import javafx.geometry.Point3D;

public class Box {
    final private Index[][] index = new Index[Axis.values().length][];
    private Point3D[] rotation3D;
    
    public Box() {
        for (int i = 0; i < index.length; i++) {
            index[i] = Axis.INDEX;
        }
        
        rotation3D = new Point3D[] {Axis.X.getPoint3D(), Axis.Y.getPoint3D(), Axis.Z.getPoint3D()};
    }
    
    public Box(Axis axis, Side side, Point3D[] rotation3D) {
        index[axis.ordinal()] = new Index[] {side};
        index[axis.getHorizontal(side).ordinal()] = Axis.INDEX;
        index[axis.getVertical(side).ordinal()] = Axis.INDEX;
        
        this.rotation3D = rotation3D;
    }
    
    public Index[] getIndex(Axis axis) {
        return index[axis.ordinal()];
    }
    
    public void setIndex(Axis axis, Index[] index) {
        this.index[axis.ordinal()] = index;
    }
    
    public Point3D[] getRotation3D() {
        return rotation3D;
    }
    
    public void setRotation3D(Point3D[] rotation3D) {
        this.rotation3D = rotation3D;
    }
    
    public static Point3D rotation3D(Point3D[] r3, Point3D point3D) {
        return new Point3D(point3D.getX() * r3[Axis.X.ordinal()].getX() + point3D.getY() * r3[Axis.Y.ordinal()].getX() + point3D.getZ() * r3[Axis.Z.ordinal()].getX(), point3D.getX() * r3[Axis.X.ordinal()].getY() + point3D.getY() * r3[Axis.Y.ordinal()].getY() + point3D.getZ() * r3[Axis.Z.ordinal()].getY(), point3D.getX() * r3[Axis.X.ordinal()].getZ() + point3D.getY() * r3[Axis.Y.ordinal()].getZ() + point3D.getZ() * r3[Axis.Z.ordinal()].getZ());
    }
    
}
