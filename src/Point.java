import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Point {
    int x;
    int y;
    int index;
    int town;
    int city;
    int province;
    String name;

    Point() {
        this.x = -1;
        this.y = -1;
    }

    Point(int x, int y, int index) {
        this.x = x;
        this.y = y;
        this.index = index;
    }

    Point(int x, int y, int index, String name) {
        this.x = x;
        this.y = y;
        this.index = index;
        this.name = name;
    }

    Point(Point P) {
        this.x = P.x;
        this.y = P.y;
        this.town = P.town;
        this.city = P.city;
        this.province = P.province;
        this.index = P.index;
        this.name = P.name;
    }

    Point(PointDouble PB) {
        this.x = (int) PB.x;
        this.y = (int) PB.y;
        this.index = PB.index;
        this.name = PB.name;
    }

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    static double dist(Point A, Point B) {
        return Math.sqrt((A.x - B.x) * (A.x - B.x) + (A.y - B.y) * (A.y - B.y));
    }

    static Point[] randomPoints(int x_bound, int y_bound, int Point_counts) {
        Random R = new Random();
        Point[] P = new Point[Point_counts];
        for (int i = 0; i < Point_counts; i++) {
            P[i] = new Point();
            P[i].x = R.nextInt(x_bound);
            P[i].y = R.nextInt(y_bound);
        }
        return P;
    }

    static void show_scatter(Point[] PArray) {
        final double[] XMax = {PArray[0].x};
        final double[] YMax = {PArray[0].y};
        final double[] XMin = {PArray[0].x};
        final double[] YMin = {PArray[0].y};
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel jpanel = new JPanel() {
            @Override
            public void paint(Graphics graphics) {
                super.paint(graphics);
                graphics.setColor(Color.red);

                for (Point point : PArray) {
                    if (point.x > XMax[0]) {
                        XMax[0] = point.x;
                    }
                    if (point.y > YMax[0]) {
                        YMax[0] = point.y;
                    }
                    if (point.x < XMin[0]) {
                        XMin[0] = point.x;
                    }
                    if (point.y < YMin[0]) {
                        YMin[0] = point.y;
                    }
                    graphics.fillOval((int) (point.x - XMin[0] + 5), (int) (point.y - YMin[0] + 5), 10, 10);
                    graphics.drawString(point.index + "," + point.name + "," + point.town + "," + point.city + "," + point.province, point.x, point.y);
                }
            }
        };
        jFrame.add(jpanel);
        jFrame.setSize((int) (XMax[0] - XMin[0] + 10), (int) (YMax[0] - YMin[0] + 10));
        jFrame.setVisible(true);
    }

    static void show_scatter(PointDouble[] PArray) {
        final double[] XMax = {PArray[0].x};
        final double[] YMax = {PArray[0].y};
        final double[] XMin = {PArray[0].x};
        final double[] YMin = {PArray[0].y};
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel jpanel = new JPanel() {
            @Override
            public void paint(Graphics graphics) {
                super.paint(graphics);
                graphics.setColor(Color.red);

                for (PointDouble point : PArray) {
                    if (point.x > XMax[0]) {
                        XMax[0] = point.x;
                    }
                    if (point.y > YMax[0]) {
                        YMax[0] = point.y;
                    }
                    if (point.x < XMin[0]) {
                        XMin[0] = point.x;
                    }
                    if (point.y < YMin[0]) {
                        YMin[0] = point.y;
                    }
                    graphics.fillOval((int) (point.x - XMin[0] + 5), (int) (point.y - YMin[0] + 5), 10, 10);
                }
            }
        };
        jFrame.add(jpanel);
        jFrame.setSize((int) (XMax[0] - XMin[0] + 10), (int) (YMax[0] - YMin[0] + 10));
        jFrame.setVisible(true);
    }
}

class PointDouble {
    double x;
    double y;

    int index;

    String name;

    PointDouble(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public PointDouble(Point point) {
        this.x = point.x;
        this.y = point.y;
        this.index = point.index;
        this.name = point.name;
    }


    static Point[] toPoint(PointDouble[] PD) {
        Point[] P;
        P = new Point[PD.length];
        for (int i = 0; i < PD.length; i++) {
            P[i] = new Point();
            P[i].x = (int) PD[i].x;
            P[i].y = (int) PD[i].y;
            P[i].index = PD[i].index;
            P[i].name = PD[i].name;
        }
        return P;
    }

    static double dist(PointDouble A, PointDouble B) {
        return Math.sqrt((A.x - B.x) * (A.x - B.x) + (A.y - B.y) * (A.y - B.y));
    }

}

