public class situation extends Thread {
    static int TimeSpan;
    static GList RoadMap;

    static int free;
    static int slow;
    static int congestion;
    static int jam;

    situation() {
        situation.free = 50;
        situation.slow = 20;
        situation.congestion = 20;
        situation.jam = 10;
        situation.TimeSpan = 2000;
    }

    static void setRoadMap(GList list) {
        situation.RoadMap = list;
    }

    static void setTimeSpan(int milliseconds) {
        situation.TimeSpan = milliseconds;
    }

    public static void setTrafficSituation(int f, int s, int c, int j) {
        situation.free = f;
        situation.slow = s;
        situation.congestion = c;
        situation.jam = j;
    }

    public void run() {
        try {
            while (true) {
                if (UI.value_z <= JPaint.ThresholdPolygonS) {

                    sleep(TimeSpan);
                    if (situation.RoadMap != null) {
                        RoadMap.InitRandomColor(free, slow, congestion, jam);
                    }
                    Main.GUI.repaint_sight();

                } else {
                    sleep(TimeSpan);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
