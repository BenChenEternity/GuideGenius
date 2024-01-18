public class KMeans {
    static int CenterPointUpdateTimes;
    static int KTown;
    static int KCity;
    static int KProvince;

    public static Point[] kMeans(Point[] points, int numClusters, int Category) {
        if (CenterPointUpdateTimes <= 0) {
            CenterPointUpdateTimes = 50;
        }
        // 初始化k个簇中心，可以随机选择numClusters个点作为中心
        Point[] centers = new Point[numClusters];
        for (int i = 0; i < numClusters; i++) {
            centers[i] = points[i * points.length / numClusters];
            switch (Category) {
                case 0: {
                    centers[i].town = i;
                    break;
                }
                case 1: {
                    centers[i].city = i;
                    break;
                }
                case 2: {
                    centers[i].province = i;
                    break;
                }
            }
        }

        for (int i = 0; i < CenterPointUpdateTimes; i++) {
            // 将每个点分配到最近的簇中
            for (Point p : points) {
                double minDist = Double.MAX_VALUE;
                for (int j = 0; j < numClusters; j++) {
                    double dist = euclideanDist(p, centers[j]);
                    if (dist < minDist) {
                        minDist = dist;
                        switch (Category) {
                            case 0: {
                                p.town = j;
                                break;
                            }
                            case 1: {
                                p.city = j;
                                break;
                            }
                            case 2: {
                                p.province = j;
                                break;
                            }
                        }
                    }
                }
            }

            // 更新每个簇的中心点位置
            for (int j = 0; j < numClusters; j++) {
                int count = 0;
                double sumX = 0, sumY = 0;
                for (Point p : points) {
                    switch (Category) {
                        case 0: {
                            if (p.town == j) {
                                count++;
                                sumX += p.x;
                                sumY += p.y;
                            }
                            break;
                        }
                        case 1: {
                            if (p.city == j) {
                                count++;
                                sumX += p.x;
                                sumY += p.y;
                            }
                            break;
                        }
                        case 2: {
                            if (p.province == j) {
                                count++;
                                sumX += p.x;
                                sumY += p.y;
                            }
                            break;
                        }
                    }

                }
                if (count > 0) {
                    Point newCenter = new Point();
                    newCenter.x = (int) Math.round(sumX / count);
                    newCenter.y = (int) Math.round(sumY / count);
                    switch (Category) {
                        case 0: {
                            newCenter.town = j;
                            break;
                        }
                        case 1: {
                            newCenter.city = j;
                            break;
                        }
                        case 2: {
                            newCenter.province = j;
                            break;
                        }
                    }

                    if (!newCenter.equals(centers[j])) {
                        centers[j] = newCenter;
                    }
                }
            }
        }
        return centers;
    }

    private static double euclideanDist(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    public static void DivideArea(Point[] points) {
        Point[] Towns;
        Towns = kMeans(points, KTown, 0);
        Point[] Cities;
        Cities = kMeans(points, KCity, 1);
        Point[] Provinces;
        Provinces = kMeans(points, KProvince, 2);
        region.Towns = Towns;
        region.Cities = Cities;
        region.Provinces = Provinces;
        region.TownBorder = ConvexHull.getTownBorderPoints(points);
        region.CityBorder = ConvexHull.getCityBorderPoints(points);
        region.ProvinceBorder = ConvexHull.getProvinceBorderPoints(points);
    }
}





