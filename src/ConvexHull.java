import java.awt.*;
import java.util.*;
import java.util.List;

public class ConvexHull {

    public static Vector<Point[]> getTownBorderPoints(Point[] points) {
        HashMap<Integer, Vector<Point>> map = new HashMap<>();
        region.TownAreaColor = new Vector<>();

        for (Point point : points) {
            int category = point.town;
            if (!map.containsKey(category)) {
                map.put(category, new Vector<>());
            }
            map.get(category).add(point);
        }

        Random rand = new Random();
        Vector<Point[]> result = new Vector<>();
        for (Integer key : map.keySet()) {
            Vector<Point> vector = map.get(key);
            Point[] array = vector.toArray(new Point[0]);
            result.add(convexHull(array));
            region.TownAreaColor.add(new Color(255 - rand.nextInt(151), 255 - rand.nextInt(151), 255 - rand.nextInt(151)));
        }
        return result;
    }

    public static Vector<Point[]> getCityBorderPoints(Point[] points) {
        HashMap<Integer, Vector<Point>> map = new HashMap<>();
        region.CityAreaColor = new Vector<>();

        for (Point point : points) {
            int category = point.city;
            if (!map.containsKey(category)) {
                map.put(category, new Vector<>());
            }
            map.get(category).add(point);
        }

        Random rand = new Random();
        Vector<Point[]> result = new Vector<>();
        for (Integer key : map.keySet()) {
            Vector<Point> vector = map.get(key);
            Point[] array = vector.toArray(new Point[0]);
            result.add(convexHull(array));
            region.CityAreaColor.add(new Color(255 - rand.nextInt(151), 255 - rand.nextInt(151), 255 - rand.nextInt(151)));
        }
        return result;
    }

    public static Vector<Point[]> getProvinceBorderPoints(Point[] points) {
        HashMap<Integer, Vector<Point>> map = new HashMap<>();
        region.ProvinceAreaColor = new Vector<>();

        for (Point point : points) {
            int category = point.province;
            if (!map.containsKey(category)) {
                map.put(category, new Vector<>());
            }
            map.get(category).add(point);
        }

        Random rand = new Random();
        Vector<Point[]> result = new Vector<>();
        for (Integer key : map.keySet()) {
            Vector<Point> vector = map.get(key);
            Point[] array = vector.toArray(new Point[0]);
            result.add(convexHull(array));
            region.ProvinceAreaColor.add(new Color(255 - rand.nextInt(151), 255 - rand.nextInt(151), 255 - rand.nextInt(151)));
        }
        return result;
    }

    private static int cross(Point a, Point b, Point c) {
        return (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y);
    }

    private static Point[] convexHull(Point[] points) {
        if (points.length == 1) {
            return points;
        }
        Arrays.sort(points, (a, b) -> a.x == b.x ? a.y - b.y : a.x - b.x);
        List<Point> lower = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            while (lower.size() >= 2 && cross(lower.get(lower.size() - 2), lower.get(lower.size() - 1), points[i]) <= 0) {
                lower.remove(lower.size() - 1);
            }
            lower.add(points[i]);
        }
        List<Point> upper = new ArrayList<>();
        for (int i = points.length - 1; i >= 0; i--) {
            while (upper.size() >= 2 && cross(upper.get(upper.size() - 2), upper.get(upper.size() - 1), points[i]) <= 0) {
                upper.remove(upper.size() - 1);
            }
            upper.add(points[i]);
        }
        List<Point> hull = new ArrayList<>(lower);
        hull.addAll(upper.subList(1, upper.size() - 1));
        return hull.toArray(new Point[0]);
    }
}

