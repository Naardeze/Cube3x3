package cube3x3;

import javafx.geometry.Point3D;

public enum Axis {
    X(new Point3D(1.0, 0.0, 0.0)) {
        @Override
        public Point3D rotate(Point3D point3D, int degree) {
            return new Point3D(point3D.getX(), point3D.getY() * Math.cos(Math.toRadians(degree)) - point3D.getZ() * Math.sin(Math.toRadians(degree)), point3D.getY() * Math.sin(Math.toRadians(degree)) + point3D.getZ() * Math.cos(Math.toRadians(degree)));
        }
    }, Y(new Point3D(0.0, 1.0, 0.0)) {
        @Override
        public Point3D rotate(Point3D point3D, int degree) {
            return new Point3D(point3D.getX() * Math.cos(Math.toRadians(degree)) - point3D.getZ() * Math.sin(Math.toRadians(degree)), point3D.getY(), point3D.getX() * Math.sin(Math.toRadians(degree)) + point3D.getZ() * Math.cos(Math.toRadians(degree)));
        }
    }, Z(new Point3D(0.0, 0.0, 1.0)) {
        @Override
        public Point3D rotate(Point3D point3D, int degree) {
            return new Point3D(point3D.getX() * Math.cos(Math.toRadians(degree)) - point3D.getY() * Math.sin(Math.toRadians(degree)), point3D.getX() * Math.sin(Math.toRadians(degree)) + point3D.getY() * Math.cos(Math.toRadians(degree)), point3D.getZ());
        }
    };
    
    final private Point3D point3D;
    
    Axis(Point3D point3D) {
        this.point3D = point3D;
    }
    
    public Point3D getPoint3D() {
        return point3D;
    }
    
    final public static Index[] INDEX = {Side.MIN, Index.MIDDLE, Side.PLUS};
    
    public abstract Point3D rotate(Point3D point3D, int degree);
    
    public Axis getHorizontal(Side side) {
        return values()[(ordinal() + 1 + side.ordinal()) % values().length];
    }
    
    public Axis getVertical(Side side) {
        return values()[(ordinal() + 2 - side.ordinal()) % values().length];
    }
    
}
