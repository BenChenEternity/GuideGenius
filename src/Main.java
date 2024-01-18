import java.util.ArrayList;

public class Main {
    static UI GUI;
    static MapStorage IOController;
    static situation Updater;
    static LoadingWindow Loading;

    public static void main(String[] args) {
//        JPaint.HighLightedRoute = new ArrayList<>();
//        JPaint.HighLightedRoute.add(new Point(100,200));
//        JPaint.HighLightedRoute.add(new Point(400,500));
//        JPaint.HighLightedRoute.add(new Point(700,350));
//
//        JPaint.HighLightedPoint = new ArrayList<>();
//        JPaint.HighLightedPoint.add(new Point(100,250));
//        JPaint.HighLightedPoint.add(new Point(400,550));
//       JPaint.HighLightedPoint.add(new Point(700,400));

        Loading = new LoadingWindow();
        GUI = new UI("WindowsTitle");
        GUI.open();
        MapStorage.Map = GUI.VMP;
        Updater = new situation();
        Updater.start();
    }
}
