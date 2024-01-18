import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class JPaint extends JPanel {
    Point[] P;
    GList Lst;
    static int ThresholdPT;
    static int ThresholdTC;
    static int ThresholdCP;
    static int ThresholdPolygonS;
    static int ThresholdPolygonB;
    static ArrayList<Point> HighLightedRoute;
    static ArrayList<Point> HighLightedPoint;

    void setShowPoint(Point[] POINTS, GList GL) {
        this.P = POINTS;
        this.Lst = GL;
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.Paint(g);
    }

    public void paintOutput(Graphics g) {
        this.Paint(g);
    }

    public void Paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (P != null) {
            if (UI.value_z <= JPaint.ThresholdPolygonS) {
                for (int i = 0; i < region.TownBorder.size(); i++) {
                    Point[] PT = Main.GUI.VMP.transform(region.TownBorder.get(i));
                    Color currentColor = region.TownAreaColor.get(i);
                    g2d.setColor(currentColor);
                    int[] x = new int[PT.length];
                    int[] y = new int[PT.length];
                    for (int j = 0; j < PT.length; j++) {
                        x[j] = PT[j].x;
                        y[j] = PT[j].y;
                    }
                    g2d.fillPolygon(x, y, PT.length);
                }
                g2d.setColor(Color.BLACK);
            } else {
                if (UI.value_z <= JPaint.ThresholdPolygonB) {
                    for (int i = 0; i < region.CityBorder.size(); i++) {
                        Point[] PT = Main.GUI.VMP.transform(region.CityBorder.get(i));
                        Color currentColor = region.CityAreaColor.get(i);
                        g2d.setColor(currentColor);
                        int[] x = new int[PT.length];
                        int[] y = new int[PT.length];
                        for (int j = 0; j < PT.length; j++) {
                            x[j] = PT[j].x;
                            y[j] = PT[j].y;
                        }
                        g2d.fillPolygon(x, y, PT.length);
                    }
                    g2d.setColor(Color.BLACK);
                } else {

                    for (int i = 0; i < region.ProvinceBorder.size(); i++) {
                        Point[] PT = Main.GUI.VMP.transform(region.ProvinceBorder.get(i));
                        Color currentColor = region.ProvinceAreaColor.get(i);
                        g2d.setColor(currentColor);
                        int[] x = new int[PT.length];
                        int[] y = new int[PT.length];
                        for (int j = 0; j < PT.length; j++) {
                            x[j] = PT[j].x;
                            y[j] = PT[j].y;
                        }
                        g2d.fillPolygon(x, y, PT.length);
                    }
                    g2d.setColor(Color.BLACK);

                }
            }

            if (UI.value_z <= JPaint.ThresholdPT) {
                for (Point point : this.P) {
                    this.drawPoint(point.x, point.y, point.name, g2d);
                }
                if (this.Lst != null) {
                    GLNode GN;
                    for (int i = 0; i < this.Lst.list.size(); i++) {
                        if (this.Lst.list.get(i) != null) {
                            GN = this.Lst.list.get(i).GLS;
                        } else {
                            continue;
                        }
                        if (GN != null) {
                            assert this.P != null;
                            this.road(this.P[i].x, this.P[i].y, GN.P.x, GN.P.y, GN.situation, g2d);
                            while (GN.next != null) {
                                GN = GN.next;
                                this.road(this.P[i].x, this.P[i].y, GN.P.x, GN.P.y, GN.situation, g2d);
                            }
                        }
                    }
                }
            } else {
                if (UI.value_z < JPaint.ThresholdTC) {
                    for (Point p : P) {
                        this.town(p.x, p.y, g2d);
                        if (p.name != null) {
                            g2d.drawString(p.name, p.x, p.y - 10);
                        }
                    }
                } else {
                    if (UI.value_z < JPaint.ThresholdCP) {
                        for (Point p : P) {
                            this.city(p.x, p.y, g2d);
                            if (p.name != null) {
                                g2d.drawString(p.name, p.x, p.y - 10);
                            }
                        }
                    } else {

                        for (Point p : P) {
                            this.province(p.x, p.y, g2d);
                            if (p.name != null) {
                                g2d.drawString(p.name, p.x, p.y - 10);
                            }
                        }
                    }
                }

            }
            if (HighLightedRoute != null) {
                g2d.setColor(new Color(65, 105, 225));
                g2d.setStroke(new BasicStroke(8.0f));
                for (int i = 0; i < HighLightedRoute.size() - 1; i++) {
                    Point PStart = new Point(Main.GUI.VMP.transform(HighLightedRoute.get(i)));
                    Point PEnd = new Point(Main.GUI.VMP.transform(HighLightedRoute.get(i + 1)));
                    g2d.drawLine(PStart.x, PStart.y, PEnd.x, PEnd.y);
                }
            }
            if (HighLightedPoint != null) {
                g2d.setColor(new Color(205, 0, 0));
                for(int i=0;i<HighLightedPoint.size();i++){
                    Point PCurrent=new Point(Main.GUI.VMP.transform(HighLightedPoint.get(i)));
                    g2d.drawOval(PCurrent.x - 2, PCurrent.y - 2, 4, 4);
                }
            }
        }
        g2d.dispose();
    }

    void province(int x, int y, Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(5.0f));
        g2d.fillOval(x - 5, y - 5, 10, 10);
        g2d.drawOval(x - 10, y - 10, 20, 20);
    }

    void city(int x, int y, Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.fillOval(x - 5, y - 5, 10, 10);
        g2d.drawOval(x - 7, y - 7, 14, 14);
    }

    void town(int x, int y, Graphics2D g2d) {
        g2d.fillOval(x - 5, y - 5, 10, 10);
    }

    void drawPoint(int x, int y, String name, Graphics2D g2d) {
        if (UI.value_z < 15) {
            g2d.setColor(Color.black);
            g2d.drawOval(x - 5, y - 5, 10, 10);
            if (name != null) {
                g2d.drawString(name, x, y - 10);
            }
        } else {
            if (UI.value_z < 30) {
                g2d.drawOval(x - 2, y - 2, 4, 4);
                if (name != null) {
                    g2d.drawString(name, x, y - 10);
                }
            }
        }
    }

    void road(int x1, int y1, int x2, int y2, int situation, Graphics2D g2d) {
        switch (situation) {
            case 0: {
                g2d.setColor(Color.green);
                break;
            }
            case 1: {
                g2d.setColor(Color.yellow);
                break;
            }
            case 2: {
                g2d.setColor(Color.red);
                break;
            }
            case 3: {
                g2d.setColor(new Color(153, 0, 0));
                break;
            }
        }
        g2d.setStroke(new BasicStroke(5.0f));
        g2d.drawLine(x1, y1, x2, y2);
    }

}

