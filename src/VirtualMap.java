import java.lang.reflect.Method;
import java.util.*;

public class VirtualMap {
    int W_VirtualMap;
    int H_VirtualMap;
    int dist;
    Point[] P;
    GList GLst;
    ArrayList<ArrayList<Integer>> locations;
    int X_Sight;
    int W_Thumbnail;
    int W_Sight;
    int W_Window;
    int Y_Sight;
    int H_Thumbnail;
    int H_Sight;
    int H_Window;
    int X_Vision;
    int Y_Vision;
    int W_Vision;
    int H_Vision;

    public void initLocations() {
        locations = new ArrayList<>();
        Random R = new Random();
        for (Point p : P) {
            ArrayList<Integer> PointLocations = new ArrayList<>();
            if (R.nextDouble() <= .05) {//Hospital
                PointLocations.add(0);
            }
            if (R.nextDouble() <= .1) {//School
                PointLocations.add(1);
            }
            if (R.nextDouble() <= .03) {//Tourist Attraction
                PointLocations.add(2);
            }
            if (R.nextDouble() <= .6) {//Hotel
                PointLocations.add(3);
            }
            if (R.nextDouble() <= .1) {//Gas Station
                PointLocations.add(4);
            }
            if (R.nextDouble() <= .2) {//Bus Station
                PointLocations.add(5);
            }
            if (R.nextDouble() <= .05) {//Metro Station
                PointLocations.add(6);
            }
            if (R.nextDouble() <= .5) {//Parking log
                PointLocations.add(7);
            }
            if (R.nextDouble() <= .1) {//Bank
                PointLocations.add(8);
            }
            if (R.nextDouble() <= .7) {//Shopping mall
                PointLocations.add(9);
            }
            locations.add(PointLocations);
        }
    }

    VirtualMap(int W, int H) {
        this.W_VirtualMap = W;
        this.H_VirtualMap = H;
    }

    void Init(Point[] p, GList GLst) {
        this.P = Arrays.copyOf(p, p.length);
        this.GLst = GLst;
    }

    void setParams(int X_Sight, int W_Thumbnail, int W_Sight, int W_Window, int Y_Sight, int H_Thumbnail, int H_Sight, int H_Window) {
        this.X_Sight = X_Sight;
        this.W_Thumbnail = W_Thumbnail;
        this.W_Sight = W_Sight;
        this.W_Window = W_Window;
        this.Y_Sight = Y_Sight;
        this.H_Thumbnail = H_Thumbnail;
        this.H_Sight = H_Sight;
        this.H_Window = H_Window;

        this.X_Vision = this.X_Sight * this.W_VirtualMap / this.W_Thumbnail;
        this.Y_Vision = this.Y_Sight * this.H_VirtualMap / this.H_Thumbnail;
        this.W_Vision = this.W_Sight * this.W_VirtualMap / this.W_Thumbnail;
        this.H_Vision = this.H_Sight * this.H_VirtualMap / this.H_Thumbnail;
    }

    hashmapInt getPointsLinesInVision() {
        Vector<Point> points_left = new Vector<>();
        for (Point point : this.P) {
            if ((this.X_Vision - 2 * this.dist < point.x) && (point.x < this.X_Vision + this.W_Vision + 2 * this.dist) && (this.Y_Vision - 2 * this.dist < point.y) && (point.y < this.Y_Vision + this.H_Vision + 2 * this.dist)) {
                points_left.add(new Point(point.x, point.y, point.index, point.name));
            }
        }
        GList lines_left = new GList(this.GLst);
        lines_left.list.removeIf(gls -> (gls == null) || (!FindP(points_left, gls.PHost.index)));
        return new hashmapInt(points_left.toArray(new Point[0]), lines_left);
    }


    private boolean FindP(Vector<Point> VP, int index) {
        for (Point point : VP) {
            if (point.index == index) {
                return true;
            }
        }
        return false;
    }

    public Point transform(Point Pt) {
        Point P = new Point();
        P.x = (int) ((((double) (Pt.x * this.W_Thumbnail - this.X_Sight * this.W_VirtualMap) * (this.W_Window - 260))) / (this.W_Sight * this.W_VirtualMap));
        P.y = (int) ((((double) (Pt.y * this.H_Thumbnail - this.Y_Sight * this.H_VirtualMap) * (this.H_Window - 260))) / (this.H_Sight * this.H_VirtualMap));
        P.index = Pt.index;
        P.name = Pt.name;
        return P;
    }

    Point[] transform(Point[] Pts) {
        int S = Pts.length;
        Point[] P;
        P = new Point[S];
        for (int i = 0; i < S; i++) {
            P[i] = new Point();
            P[i].x = (int) ((((double) (Pts[i].x * this.W_Thumbnail - this.X_Sight * this.W_VirtualMap) * (this.W_Window - 260))) / (this.W_Sight * this.W_VirtualMap));
            P[i].y = (int) ((((double) (Pts[i].y * this.H_Thumbnail - this.Y_Sight * this.H_VirtualMap) * (this.H_Window - 260))) / (this.H_Sight * this.H_VirtualMap));
            P[i].index = Pts[i].index;
            P[i].name = Pts[i].name;
        }
        return P;
    }

    Point[] transform(Vector<Point> Pts) {
        int S = Pts.size();
        Point[] P;
        P = new Point[S];
        for (int i = 0; i < S; i++) {
            P[i] = new Point();
            P[i].x = (int) ((((double) (Pts.get(i).x * this.W_Thumbnail - this.X_Sight * this.W_VirtualMap) * (this.W_Window - 260))) / (this.W_Sight * this.W_VirtualMap));
            P[i].y = (int) ((((double) (Pts.get(i).y * this.H_Thumbnail - this.Y_Sight * this.H_VirtualMap) * (this.H_Window - 260))) / (this.H_Sight * this.H_VirtualMap));
            P[i].index = Pts.get(i).index;
            P[i].name = Pts.get(i).name;
        }
        return P;
    }

    GList transform(GList List) throws Exception {
        Method processing = this.getClass().getMethod("transform", Point.class);
        GList.ForEach(List, processing);
        return List;
    }

    public ArrayList<Point> findClosestPoints(int x, int y, int k) {
        PriorityQueue<Point> pq = new PriorityQueue<>(k, Comparator.comparingInt(p -> -getDistance(p, x, y)));


        for (Point point : P) {
            pq.offer(point);
            if (pq.size() > k) {
                pq.poll();
            }
        }

        ArrayList<Point> result = new ArrayList<>(k);
        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }
        Collections.reverse(result);
        return result;
    }

    private int getDistance(Point p, int x, int y) {
        return (int) Math.sqrt(((double) (p.x - x) * (p.x - x) + (double) (p.y - y) * (p.y - y)));
    }
}



