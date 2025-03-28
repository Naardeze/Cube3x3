package cube3x3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import javafx.geometry.Point3D;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Cube3x3 extends JPanel {
    final private static Color BLUE = Color.blue;
    final private static Color RED = Color.red;
    final private static Color ORANGE = Color.orange;
    final private static Color YELLOW = Color.yellow;
    final private static Color WHITE = Color.white;
    final private static Color GREEN = Color.green;
    
    final private static double MIN = (double) -Axis.INDEX.length % 2 / 2;
    final private static double PLUS = MIN + Side.PLUS.ordinal();
    final private static double BORDER = 0.0375;
    
    final private static int ROTATION = 100;

    private byte camera = 9;
    
    final private ArrayList<Box> box = new ArrayList();

    final private Square[][][][] piece = new Square[][][][] {{{{new Square(BLUE, Axis.X), new Square(YELLOW, Axis.Y), new Square(ORANGE, Axis.Z)}, //000
            {new Square(BLUE, Axis.X), new Square(YELLOW, Axis.Y)}, //001
            {new Square(BLUE, Axis.X), new Square(YELLOW, Axis.Y), new Square(RED, Axis.Z)}}, //002
            {{new Square(BLUE, Axis.X), new Square(ORANGE, Axis.Z)}, //010
                {new Square(BLUE, Axis.X)}, //011
                {new Square(BLUE, Axis.X), new Square(RED, Axis.Z)}}, //012
            {{new Square(BLUE, Axis.X), new Square(WHITE, Axis.Y), new Square(ORANGE, Axis.Z)}, //020
                {new Square(BLUE, Axis.X), new Square(WHITE, Axis.Y)}, //021
                {new Square(BLUE, Axis.X), new Square(WHITE, Axis.Y), new Square(RED, Axis.Z)}}}, //022
        {{{new Square(YELLOW, Axis.Y), new Square(ORANGE, Axis.Z)}, //100
            {new Square(YELLOW, Axis.Y)}, //101
            {new Square(YELLOW, Axis.Y), new Square(RED, Axis.Z)}}, //102
            {{new Square(ORANGE, Axis.Z)}, //110
                {}, //111
                {new Square(RED, Axis.Z)}}, //112
            {{new Square(WHITE, Axis.Y), new Square(ORANGE, Axis.Z)}, //120
                {new Square(WHITE, Axis.Y)}, //121
                {new Square(WHITE, Axis.Y), new Square(RED, Axis.Z)}}}, //122
        {{{new Square(GREEN, Axis.X), new Square(YELLOW, Axis.Y), new Square(ORANGE, Axis.Z)}, //200
            {new Square(GREEN, Axis.X), new Square(YELLOW, Axis.Y)}, //201
            {new Square(GREEN, Axis.X), new Square(YELLOW, Axis.Y), new Square(RED, Axis.Z)}}, //202
            {{new Square(GREEN, Axis.X), new Square(ORANGE, Axis.Z)}, //210
                {new Square(GREEN, Axis.X)}, //211
                {new Square(GREEN, Axis.X), new Square(RED, Axis.Z)}}, //212
            {{new Square(GREEN, Axis.X), new Square(WHITE, Axis.Y), new Square(ORANGE, Axis.Z)}, //220
                {new Square(GREEN, Axis.X), new Square(WHITE, Axis.Y)}, //221
                {new Square(GREEN, Axis.X), new Square(WHITE, Axis.Y), new Square(RED, Axis.Z)}}}}; //222
    
    private Cube3x3() {
        Box centerBox = new Box();
        
        box.add(centerBox);
        
        setBackground(Color.darkGray);
        setPreferredSize(new Dimension(200, 200));
        
        addMouseListener(new MouseAdapter() {
            boolean ok = true;
            
            private void drag(MouseMotionAdapter mouseMotionAdapter) {
                addMouseMotionListener(mouseMotionAdapter);
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        removeMouseListener(this);
                        removeMouseMotionListener(mouseMotionAdapter);
                    }
                });
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (Math.hypot(e.getX() - getWidth() / 2, e.getY() - getHeight() / 2) > Math.min(getWidth(), getHeight()) / camera * Axis.INDEX.length) {
                    drag(new MouseMotionAdapter() {
                        Point3D[] rotation3D = centerBox.getRotation3D().clone();
                        Point point = e.getPoint();

                        @Override
                        public void mouseDragged(MouseEvent e) {
                            for (int horizontal = (int) ((double) (e.getX() - point.x) / Math.min(getWidth(), getHeight()) * ROTATION), vertical = (int) ((double) (e.getY() - point.y) / Math.min(getWidth(), getHeight()) * ROTATION), i = 0; i < rotation3D.length; i++) {
                                centerBox.getRotation3D()[i] = Axis.X.rotate(Axis.Z.rotate(rotation3D[i], horizontal), vertical);
                            }

                            repaint();
                        }
                    });
                } else if (ok) {                
                    loop : for (Index x : centerBox.getIndex(Axis.X)) {
                        for (Index y : centerBox.getIndex(Axis.Y)) {
                            for (Index z : centerBox.getIndex(Axis.Z)) {
                                for (Square square : piece[x.getValue()][y.getValue()][z.getValue()]) {
                                    test : {
                                        for (int i = 0; i < square.npoints; i++) {
                                            if (!Square.isClockWise(new int[] {e.getX(), square.xpoints[i], square.xpoints[(i + 1) % square.xpoints.length]}, new int[] {e.getY(), square.ypoints[i], square.ypoints[(i + 1) % square.ypoints.length]})) {
                                                break test;
                                            }
                                        }

                                        drag(new MouseMotionAdapter() {
                                            Index[] index = {x, y, z};

                                            @Override
                                            public void mouseDragged(MouseEvent e) {
                                                for (int i = 0; i < square.npoints; i++) {
                                                    if (Square.isClockWise(new int[] {e.getX(), square.xpoints[(i + 1) % square.xpoints.length], square.xpoints[i]}, new int[] {e.getY(), square.ypoints[(i + 1) % square.ypoints.length], square.ypoints[i]})) {
                                                        removeMouseMotionListener(this);

                                                        Axis pressedAxis = square.getAxis();
                                                        Side pressedSide = (Side) index[pressedAxis.ordinal()];

                                                        Axis axis = new Axis[] {pressedAxis.getHorizontal(pressedSide), pressedAxis.getVertical(pressedSide)}[i % Side.values().length];

                                                        if (index[axis.ordinal()] instanceof Side) {
                                                            Side side = (Side) index[axis.ordinal()];
                                                            Box faceBox = new Box(axis, side, centerBox.getRotation3D());

                                                            centerBox.setIndex(axis, new Index[][] {{Index.MIDDLE, Side.PLUS}, {Side.MIN, Index.MIDDLE}}[side.ordinal()]);
                                                            box.add(Boolean.compare(new int[] {-1, 1}[side.ordinal()] * centerBox.getRotation3D()[axis.ordinal()].getY() < 0, false), faceBox);

                                                            int direction = ((2 - axis.ordinal() + pressedAxis.ordinal()) % Axis.values().length + i % Side.values().length + i / Side.values().length + pressedSide.ordinal() + side.ordinal()) % Side.values().length;

                                                            new Thread() {
                                                                @Override
                                                                public void run() {
                                                                    ok = false;
                                                                    
                                                                    for (int step = new int[] {1, -1}[(axis.ordinal() + side.ordinal() + direction) % Side.values().length], degree = step; degree != step * 90; degree += step) {
                                                                        faceBox.setRotation3D(new Point3D[] {Box.rotation3D(centerBox.getRotation3D(), axis.rotate(Axis.X.getPoint3D(), degree)), Box.rotation3D(centerBox.getRotation3D(), axis.rotate(Axis.Y.getPoint3D(), degree)), Box.rotation3D(centerBox.getRotation3D(), axis.rotate(Axis.Z.getPoint3D(), degree))});

                                                                        repaint();

                                                                        try {
                                                                            Thread.sleep(3);
                                                                        } catch (Exception ex) {}
                                                                    }

                                                                    Axis horizontal = axis.getHorizontal(side);
                                                                    Axis vertical = axis.getVertical(side);

                                                                    Square[][][] layer = new Square[Axis.INDEX.length][Axis.INDEX.length][];

                                                                    Index[] index = new Index[Axis.values().length];

                                                                    index[axis.ordinal()] = side;

                                                                    for (Index row : faceBox.getIndex(vertical)) {
                                                                        for (Index column : faceBox.getIndex(horizontal)) {
                                                                            index[vertical.ordinal()] = row;
                                                                            index[horizontal.ordinal()] = column;

                                                                            layer[row.getValue()][column.getValue()] = piece[index[Axis.X.ordinal()].getValue()][index[Axis.Y.ordinal()].getValue()][index[Axis.Z.ordinal()].getValue()];
                                                                        }
                                                                    }

                                                                    for (Index row : faceBox.getIndex(vertical)) {
                                                                        for (Index column : faceBox.getIndex(horizontal)) {
                                                                            for (Square square : layer[row.getValue()][column.getValue()]) {
                                                                                if (square.getAxis() == horizontal) {
                                                                                    square.setAxis(vertical);
                                                                                } else if (square.getAxis() == vertical) {
                                                                                    square.setAxis(horizontal);
                                                                                }
                                                                            }

                                                                            index[horizontal.ordinal()] = Axis.INDEX[new int[] {2 - row.getValue(), row.getValue()}[direction]];
                                                                            index[vertical.ordinal()] = Axis.INDEX[new int[] {column.getValue(), 2 - column.getValue()}[direction]];

                                                                            piece[index[Axis.X.ordinal()].getValue()][index[Axis.Y.ordinal()].getValue()][index[Axis.Z.ordinal()].getValue()] = layer[row.getValue()][column.getValue()];
                                                                        }
                                                                    }

                                                                    box.remove(faceBox);             
                                                                    centerBox.setIndex(axis, Axis.INDEX);

                                                                    repaint();
                                                                    
                                                                    ok = true;
                                                                }
                                                            }.start();
                                                        }

                                                        break;
                                                    }
                                                }
                                            }
                                        });

                                        break loop;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Box box : box) {
            for (Index x : box.getIndex(Axis.X)) {
                for (Index y : box.getIndex(Axis.Y)) {
                    for (Index z : box.getIndex(Axis.Z)) {
                        Index[] index = {x, y, z};
                        
                        for (Square square : piece[index[Axis.X.ordinal()].getValue()][index[Axis.Y.ordinal()].getValue()][index[Axis.Z.ordinal()].getValue()]) {
                            Axis axis = square.getAxis();
                            Side side = (Side) index[axis.ordinal()];
                            
                            for (int i = 0; i < square.npoints; i++) {
                                double[] p0 = {x.getValue(), y.getValue(), z.getValue()};
                                
                                p0[axis.ordinal()] += new double[] {MIN, PLUS}[side.ordinal()];
                                p0[axis.getHorizontal(side).ordinal()] += new double[] {MIN + BORDER, PLUS - BORDER}[(i % Side.values().length + i / Side.values().length) % Side.values().length];
                                p0[axis.getVertical(side).ordinal()] += new double[] {MIN + BORDER, PLUS - BORDER}[i / Side.values().length];
                                
                                Point3D point3D = Box.rotation3D(box.getRotation3D(), new Point3D(-Axis.INDEX.length / 2, -Axis.INDEX.length / 2, -Axis.INDEX.length / 2).add(p0[Axis.X.ordinal()], p0[Axis.Y.ordinal()], p0[Axis.Z.ordinal()]));
                                
                                double distance = Math.min(getWidth(), getHeight()) / Math.hypot(camera + point3D.getY(), Math.hypot(point3D.getX(), point3D.getZ()));
                                
                                square.xpoints[i] = (int) (getWidth() / 2 + distance * point3D.getX());
                                square.ypoints[i] = (int) (getHeight() / 2 - distance * point3D.getZ());
                            }
                            
                            if (Square.isClockWise(square.xpoints, square.ypoints)) {
                                g.setColor(square.getColor());
                                g.fillPolygon(square);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new Cube3x3());
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    
}
