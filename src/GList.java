import java.lang.reflect.Method;
import java.util.*;

public class GList {
    Vector<GListSingle> list;

    public GList() {
        this.list = new Vector<>();
    }

    public GList(GList GLCopy) {
        this.list = new Vector<>();
        for (int i = 0; i < GLCopy.list.size(); i++) {
            this.list.add(new GListSingle(GLCopy.list.get(i)));
        }
    }

    public boolean contains(int ind1, int ind2) {
        if ((ind1 == -1) || (ind2 == -1)) {
            return false;
        }
        int indLarger;
        int indLess;
        if (ind1 >= ind2) {
            indLarger = ind1;
            indLess = ind2;
        } else {
            indLarger = ind2;
            indLess = ind1;
        }
        return this.list.get(indLess).contains(indLarger);
    }

    public void add2List(PointDouble A, PointDouble B, int volume) {
        this.add2List(new Point(A), new Point(B), volume);
    }

    public void add2List(Point A, Point B, int volume) {
        Point host;
        Point indexJ;
        int i;
        int j;
        if (A.index > B.index) {
            j = A.index;
            i = B.index;
            indexJ = A;
            host = B;
        } else {
            if (A.index < B.index) {
                i = A.index;
                j = B.index;
                indexJ = B;
                host = A;
            } else {
                return;
            }
        }
        if (j > this.list.size() - 1) {
            int LstSz = this.list.size();
            for (int k = 0; k < j - LstSz + 1; k++) {
                this.list.add(new GListSingle(new Point(-1, -1, this.list.size())));
            }
        }
        if (this.list.get(i).GLS == null) {
            GListSingle GS = new GListSingle(host);
            GS.add(indexJ, volume);
            this.list.setElementAt(GS, i);
        } else {
            this.list.get(i).add(indexJ, volume);
        }
    }


    public void print() throws Exception {
        Object O = this.getClass().getDeclaredConstructor().newInstance();
        Method printH = this.getClass().getMethod("printHost", Point.class);
        Method printN = this.getClass().getMethod("printNode", GLNode.class);
        Method printL = this.getClass().getMethod("printEndLine");
        foreach(this, O, printH, O, printN, O, printL);
    }

    public void printHost(Point PHost) {
        System.out.print(PHost.index + ":");
    }

    public void printNode(GLNode Node) {
        System.out.print("->" + Node.P.index);
    }

    public void printEndLine() {
        System.out.print("\n");
    }


    public static void ForEach(GList GLst, Method method) throws Exception {
        GLNode GN;
        for (int i = 0; i < GLst.list.size(); i++) {
            GLst.list.get(i).PHost = (Point) method.invoke(Main.GUI.VMP, GLst.list.get(i).PHost);
            if (GLst.list.get(i).GLS != null) {
                GN = GLst.list.get(i).GLS;
            } else {
                continue;
            }
            if (GN != null) {
                while (GN.next != null) {
                    GN.P = (Point) method.invoke(Main.GUI.VMP, GN.P);
                    GN = GN.next;
                }
                GN.P = (Point) method.invoke(Main.GUI.VMP, GN.P);
            }
        }

    }

    public static void foreach(GList GLst, Object objInstanceHost, Method methodHost, Object objInstanceNode, Method methodNode, Object objInstanceEndLine, Method methodEndLine) throws Exception {
        GLNode GN;
        boolean HostPro = true;
        boolean NodePro = true;
        boolean EndLinePro = true;
        if (methodHost == null) {
            HostPro = false;
        }
        if (methodNode == null) {
            NodePro = false;
        }
        if (methodEndLine == null) {
            EndLinePro = false;
        }
        for (int i = 0; i < GLst.list.size(); i++) {
            if (HostPro) {
                methodHost.invoke(objInstanceHost, GLst.list.get(i).PHost);
            }

            if (GLst.list.get(i).GLS != null) {
                GN = GLst.list.get(i).GLS;
            } else {
                if (EndLinePro) {
                    methodEndLine.invoke(objInstanceEndLine);
                }
                continue;
            }
            if (GN != null) {
                if (NodePro) {
                    methodNode.invoke(objInstanceNode, GN);
                }
                while (GN.next != null) {
                    GN = GN.next;
                    if (NodePro) {
                        methodNode.invoke(objInstanceNode, GN);
                    }
                }
            }
            if (EndLinePro) {
                methodEndLine.invoke(objInstanceEndLine);
            }
        }
    }

    void InitRandomColor(int free, int slow, int congestion, int jam) {
        int total = free + slow + congestion + jam;
        double f = (double) free / total;
        double s = (double) slow / total;
        double c = (double) congestion / total;
        GLNode GN;
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).GLS != null) {
                GN = this.list.get(i).GLS;
            } else {
                continue;
            }
            if (GN != null) {
                while (GN.next != null) {
                    GN.situation = getRandomTrafficState(f, s, c);
                    GN = GN.next;
                }
                GN.situation = getRandomTrafficState(f, s, c);
            }
        }
    }

    int getRandomTrafficState(double freePercentage, double slowPercentage, double congestionPercentage) {
        Random R = new Random();
        double RandomValue = R.nextDouble();
        if (RandomValue < freePercentage) {
            return 0;
        } else {
            if (RandomValue < freePercentage + slowPercentage) {
                return 1;
            } else {
                if (RandomValue < freePercentage + slowPercentage + congestionPercentage) {
                    return 2;
                } else {
                    return 3;
                }
            }
        }
    }

    public ArrayList<Point> findShortestPath(int start, int end) {
        Map<Integer, Integer> distances = new HashMap<>(); // 记录起点到各顶点的距离
        Map<Integer, Integer> predecessors = new HashMap<>(); // 记录各顶点的前驱顶点
        Queue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get)); // 优先队列（按距离排序）
        for (int i = 0; i < list.size(); i++) {
            distances.put(list.get(i).PHost.index, i == start ? 0 : Integer.MAX_VALUE);
            // 起点距离为0，其它顶点距离为无穷大
            predecessors.put(list.get(i).PHost.index, null); // 所有顶点的前驱顶点为null
            queue.add(list.get(i).PHost.index); // 将所有顶点加入队列
        }
        while (!queue.isEmpty()) {
            int indexX = queue.remove(); // 取出距离最短的顶点
            if (indexX == end) {
                break; // 终点已找到，结束搜索
            }
            GLNode CurrentNode = list.get(indexX).GLS;
            while (CurrentNode != null) {
                int indexY = list.get(CurrentNode.P.index).PHost.index;
                int dist = (int) (distances.get(indexX) + Point.dist(list.get(indexX).PHost, list.get(indexY).PHost));
                if (dist < distances.get(indexY)) { // 如果通过x到y的距离更短，则更新距离和前驱顶点
                    distances.put(indexY, dist);
                    predecessors.put(indexY, indexX);
                    queue.remove(indexY); // 更新后要重新插入y以保持优先队列排序
                    queue.add(indexY);
                }
                CurrentNode = CurrentNode.next;
            }
            ArrayList<Integer> restPoint = new ArrayList<>();
            for (int i = 0; i < indexX; i++) {
                GLNode CurNode = list.get(i).GLS;
                while (CurNode != null) {
                    if (CurNode.P.index == indexX) {
                        restPoint.add(list.get(i).PHost.index);
                    }
                    CurNode = CurNode.next;
                }
            }
            for (int n : restPoint) {
                int indexY = list.get(n).PHost.index;
                int dist = (int) (distances.get(indexX) + Point.dist(list.get(indexX).PHost, list.get(indexY).PHost));
                if (dist < distances.get(indexY)) { // 如果通过x到y的距离更短，则更新距离和前驱顶点
                    distances.put(indexY, dist);
                    predecessors.put(indexY, indexX);
                    queue.remove(indexY); // 更新后要重新插入y以保持优先队列排序
                    queue.add(indexY);
                }
            }
        }
        ArrayList<Point> path = new ArrayList<>();
        GListSingle current = list.get(end);
        while (predecessors.get(list.get(current.PHost.index).PHost.index) != null) { // 从终点回溯到起点
            path.add(current.PHost);
            current = list.get(predecessors.get(list.get(current.PHost.index).PHost.index));
        }
        path.add(list.get(start).PHost);
        Collections.reverse(path); // 反转得到正向路径
        return path;
    }

    public ArrayList<Point> findLeastCostPath(int start, int end) {
        Map<Integer, Integer> timecosts = new HashMap<>(); // 记录起点到各顶点的距离
        Map<Integer, Integer> predecessors = new HashMap<>(); // 记录各顶点的前驱顶点
        Queue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(timecosts::get)); // 优先队列（按距离排序）
        for (int i = 0; i < list.size(); i++) {
            timecosts.put(list.get(i).PHost.index, i == start ? 0 : Integer.MAX_VALUE);
            // 起点距离为0，其它顶点距离为无穷大
            predecessors.put(list.get(i).PHost.index, null); // 所有顶点的前驱顶点为null
            queue.add(list.get(i).PHost.index); // 将所有顶点加入队列
        }
        while (!queue.isEmpty()) {
            int indexX = queue.remove(); // 取出距离最短的顶点
            if (indexX == end) {
                break; // 终点已找到，结束搜索
            }
            GLNode CurrentNode = list.get(indexX).GLS;
            while (CurrentNode != null) {
                int indexY = list.get(CurrentNode.P.index).PHost.index;
                int dist = timecosts.get(indexX) + getTimeCost(CurrentNode.dist, CurrentNode.situation, CurrentNode.volume);
                if (dist < timecosts.get(indexY)) { // 如果通过x到y的距离更短，则更新距离和前驱顶点
                    timecosts.put(indexY, dist);
                    predecessors.put(indexY, indexX);
                    queue.remove(indexY); // 更新后要重新插入y以保持优先队列排序
                    queue.add(indexY);
                }
                CurrentNode = CurrentNode.next;
            }
            ArrayList<Integer> restPoint = new ArrayList<>();
            for (int i = 0; i < indexX; i++) {
                GLNode CurNode = list.get(i).GLS;
                while (CurNode != null) {
                    if (CurNode.P.index == indexX) {
                        restPoint.add(list.get(i).PHost.index);
                    }
                    CurNode = CurNode.next;
                }
            }
            for (int n : restPoint) {
                int indexY = list.get(n).PHost.index;
                int dist = timecosts.get(indexX) + getTimeCost(list.get(indexY).get(indexX).dist, list.get(indexY).get(indexX).situation, list.get(indexY).get(indexX).volume);
                if (dist < timecosts.get(indexY)) { // 如果通过x到y的距离更短，则更新距离和前驱顶点
                    timecosts.put(indexY, dist);
                    predecessors.put(indexY, indexX);
                    queue.remove(indexY); // 更新后要重新插入y以保持优先队列排序
                    queue.add(indexY);
                }
            }
        }
        ArrayList<Point> path = new ArrayList<>();
        GListSingle current = list.get(end);
        while (predecessors.get(list.get(current.PHost.index).PHost.index) != null) { // 从终点回溯到起点
            path.add(current.PHost);
            current = list.get(predecessors.get(list.get(current.PHost.index).PHost.index));
        }
        path.add(list.get(start).PHost);
        Collections.reverse(path); // 反转得到正向路径
        return path;
    }

    private int getTimeCost(int roadLen, int situation, int volume) {
        Random R = new Random();
        int carNum = 0;
        switch (situation) {
            case 0: {
                carNum = R.nextInt(volume / 2);
                break;
            }
            case 1: {
                carNum = R.nextInt(volume / 4) + volume / 2;
                break;
            }
            case 2: {
                carNum = R.nextInt((volume / 4) + volume * 3 / 4);
                break;
            }
            case 3: {
                carNum = R.nextInt(volume) + volume;
                break;
            }
        }
        if (carNum > volume) {
            return (int) (roadLen * (1 + 0.3 * (carNum / volume)));
        } else {
            return roadLen;
        }
    }

    public String inspect(int indexA, int indexB) {
        GLNode currentNode = list.get(indexA).GLS;
        while (currentNode != null) {
            if (currentNode.P.index == indexB) {
                return "Volume of the road: " + currentNode.volume + "  Distance between: " + currentNode.dist + "  Situation: " + getSituation(currentNode.situation);
            } else {
                currentNode = currentNode.next;
            }
        }
        currentNode = list.get(indexB).GLS;
        while (currentNode != null) {
            if (currentNode.P.index == indexA) {
                return "Volume of the road: " + currentNode.volume + "  Distance between: " + currentNode.dist + "  Situation: " + getSituation(currentNode.situation);
            } else {
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    private String getSituation(int state){
        switch (state){
            case 0:{
                return "Normal";
            }
            case 1:{
                return "Slow";
            }
            case 2:{
                return "Congested";
            }
            case 3:{
                return "Jammed";
            }
        }
        return null;
    }
}

class GListSingle {
    Point PHost;
    GLNode GLS;

    GListSingle(Point PHost) {
        this.GLS = null;
        this.PHost = PHost;
    }

    GListSingle(GListSingle gls) {
        this.PHost = new Point(gls.PHost);
        if (gls.GLS != null) {
            this.GLS = new GLNode(gls.GLS);
        } else {
            this.GLS = null;
        }
    }

    void add(Point P, int volume) {
        if (this.size() == 0) {
            this.GLS = new GLNode();
            this.GLS.next = null;
            this.GLS.P = P;
            this.GLS.dist = (int) Point.dist(this.PHost, this.GLS.P);
            this.GLS.volume = volume;
        } else {
            GLNode Pt = this.GLS;
            if (Pt.P.index == P.index) {
                return;
            }
            while (Pt.next != null) {
                Pt = Pt.next;
                if (Pt.P.index == P.index) {
                    return;
                }
            }
            Pt.next = new GLNode(P);
            Pt.next.dist = (int) Point.dist(this.PHost, P);
            Pt.next.volume = volume;
            Pt.next.next = null;
        }
    }

    int size() {
        if (this.GLS == null) {
            return 0;
        } else {
            int sz = 1;
            GLNode Pt = this.GLS;
            while (Pt.next != null) {
                sz++;
                Pt = Pt.next;
            }
            return sz;
        }
    }

    boolean contains(int index) {
        if (this.GLS != null) {
            GLNode GFinder = this.GLS;
            if (GFinder.P.index == index) {
                return true;
            }
            while (GFinder.next != null) {
                GFinder = GFinder.next;
                if (GFinder.P.index == index) {
                    return true;
                }
            }
        }
        return false;
    }

    GLNode get(int index) {
        GLNode GFinder = this.GLS;
        while (GFinder != null) {
            if (GFinder.P.index == index){
                return GFinder;
            }
            GFinder = GFinder.next;
        }
        return null;
    }
}


class GLNode {
    GLNode next;
    Point P;
    int dist;
    int situation;
    int volume;

    GLNode() {
        this.next = null;
        this.P = null;
    }

    GLNode(Point P) {
        this.next = null;
        this.P = P;
    }

    GLNode(GLNode N) {
        GLNode GFinder = N;
        GLNode GCreator = this;
        this.P = new Point(N.P);
        this.dist = N.dist;
        this.situation = N.situation;
        this.volume = N.volume;
        while (GFinder.next != null) {
            GFinder = GFinder.next;
            GCreator.next = new GLNode(GFinder);
            GCreator = GCreator.next;
            GCreator.situation = GFinder.situation;
            GCreator.volume = GFinder.volume;
            GCreator.dist = GFinder.dist;
            GCreator.P = new Point(GFinder.P);
        }
    }

}


