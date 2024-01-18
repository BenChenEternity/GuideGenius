import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import static java.lang.Math.*;

class poisson {

    static double cell_size;
    static int[][] grid;

    static int rows;
    static int cols;

    static Vector<PointDouble> process;

    static Vector<PointDouble> result;


    public static hashmap Sample(int width, int height, double r, int retry) {
        poisson.cell_size = r / sqrt(2);
        poisson.cols = (int) Math.ceil(width / poisson.cell_size);
        poisson.rows = (int) Math.ceil(height / poisson.cell_size);
        poisson.grid = new int[poisson.rows][];
        for (int i = 0; i < poisson.rows; i++) {
            poisson.grid[i] = new int[poisson.cols];
            for (int j = 0; j < poisson.cols; j++) {
                poisson.grid[i][j] = -1;
            }
        }

        Random R = new Random();
        PointDouble p0 = new PointDouble(R.nextDouble() * width, R.nextDouble() * height);
        p0.name = "0";

        int col = col_of(p0.x);
        int row = row_of(p0.y);


        GList G = new GList();

        poisson.process = new Vector<>();
        poisson.result = new Vector<>();


        process.add(p0);
        result.add(p0);
        grid[row][col] = 0;


        while (!poisson.process.isEmpty()) {
            PointDouble p = poisson.process.get(poisson.process.size() - 1);
            boolean found = false;

            for (int i = 0; i < retry; i++) {
                double delta = R.nextDouble() * r + r;
                double angle = R.nextDouble() * PI * 2;
                PointDouble PNew = new PointDouble(p.x + delta * cos(angle), p.y + delta * sin(angle));
                if ((PNew.x < 0 || PNew.x >= width) || (PNew.y < 0 || PNew.y >= height)) {
                    continue;
                }

                col = col_of(PNew.x);
                row = row_of(PNew.y);
                if (poisson.grid[row][col] != -1) {
                    continue;
                }
                boolean dist_far = true;
                int rm = row_of(PNew.y - r);
                int RM = row_of(PNew.y + r);
                int cm = col_of(PNew.x - r);
                int CM = col_of(PNew.x + r);
                for (int k = cm; k <= CM; k++) {
                    if ((k < 0) || (k >= poisson.cols)) {
                        continue;
                    }
                    for (int l = rm; l <= RM; l++) {
                        if ((l < 0) || (l >= poisson.rows)) {
                            continue;
                        }
                        int IND = poisson.grid[l][k];
                        if (IND != -1) {
                            if (PointDouble.dist(poisson.result.get(IND), PNew) < r) {
                                dist_far = false;
                                break;
                            }
                        }
                    }
                    if (!dist_far) {
                        break;
                    }
                }

                if (dist_far) {
                    PNew.index = poisson.result.size();
                    PNew.name = String.valueOf(PNew.index);
                    G.add2List(poisson.process.get(poisson.process.size() - 1), PNew, roadRandom());
                    poisson.result.add(PNew);
                    poisson.process.add(PNew);
                    poisson.grid[row][col] = poisson.result.size() - 1;
                    found = true;

                    break;
                }
            }
            if (!found) {
                poisson.process.remove(poisson.process.size() - 1);
            }
        }

        poisson.LoopConstruct(G);
        G.InitRandomColor(50, 20, 20, 10);
        return new hashmap(poisson.result.toArray(new PointDouble[0]), G);
    }

    static Random RR = new Random();

    static int roadRandom() {
        return RR.nextInt(700) + 300;
    }

    static void LoopConstruct(GList GLst) {
        for (PointDouble p : poisson.result) {
            index2d rc = rc_of(p);
            GLst.list.get(p.index).PHost = new Point(p);
            if (rc.i - 1 >= 0) {
                for (int k = rc.j - 1; k <= rc.j + 1; k++) {
                    if ((k >= 0) && (k <= cols - 1)) {
                        if (!GLst.contains(grid[rc.i][rc.j], grid[rc.i - 1][k])) {
                            if ((getPointByIndex(grid[rc.i][rc.j]) != null) && (getPointByIndex(grid[rc.i - 1][k]) != null)) {
                                GLst.add2List(getPointByIndex(grid[rc.i][rc.j]), getPointByIndex(grid[rc.i - 1][k]), roadRandom());
                            }
                        }
                    }
                }
            }

            for (int k = rc.j - 1; k <= rc.j + 1; k++) {
                if (k == rc.j) {
                    continue;
                }
                if ((k >= 0) && (k <= poisson.cols - 1)) {
                    if (!GLst.contains(grid[rc.i][rc.j], grid[rc.i][k])) {
                        if ((getPointByIndex(grid[rc.i][rc.j]) != null) && (getPointByIndex(grid[rc.i][k]) != null)) {
                            GLst.add2List(getPointByIndex(grid[rc.i][rc.j]), getPointByIndex(grid[rc.i][k]), roadRandom());
                        }
                    }
                }
            }

            //3
            if (rc.i + 1 <= rows - 1) {
                for (int k = rc.j - 1; k <= rc.j + 1; k++) {
                    if ((k >= 0) && (k <= cols - 1)) {
                        if (!GLst.contains(grid[rc.i][rc.j], grid[rc.i + 1][k])) {
                            if ((getPointByIndex(grid[rc.i][rc.j]) != null) && (getPointByIndex(grid[rc.i + 1][k]) != null)) {
                                GLst.add2List(getPointByIndex(grid[rc.i][rc.j]), getPointByIndex(grid[rc.i + 1][k]), roadRandom());
                            }
                        }
                    }
                }
            }
        }
    }

    private static Point getPointByIndex(int ind) {
        if (ind == -1) {
            return null;
        }
        for (PointDouble p : result) {
            if (p.index == ind) {
                return new Point(p);
            }
        }
        return null;
    }

    static int row_of(double y) {
        return (int) floor(y / poisson.cell_size);
    }

    static int col_of(double x) {
        return (int) floor(x / poisson.cell_size);
    }

    static index2d rc_of(PointDouble P) {
        return new index2d((int) floor(P.y / poisson.cell_size), (int) floor(P.x / poisson.cell_size));
    }
}

class hashmap {
    PointDouble[] PD;
    GList GL;

    hashmap(PointDouble[] P, GList G) {
        this.PD = P;
        this.GL = G;
    }
}

class hashmapInt {
    Point[] PD;
    GList GL;

    hashmapInt(Point[] P, GList G) {
        this.PD = P;
        this.GL = G;
    }
}

