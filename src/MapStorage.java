import java.awt.*;
import java.io.*;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Vector;

public class MapStorage {
    static String Path;
    static VirtualMap Map;
    static DataOutputStream DOS;
    static BufferedReader BFR;
    static Point[] P;
    static GList GLst;

    void setPath(String Path) {
        MapStorage.Path = Path;
    }

    VirtualMap Input() {
        VirtualMap VMP;
        String check;
        region.TownBorder = new Vector<>();
        region.TownAreaColor = new Vector<>();
        region.CityBorder = new Vector<>();
        region.CityAreaColor = new Vector<>();
        region.ProvinceBorder = new Vector<>();
        region.ProvinceAreaColor = new Vector<>();
        P = null;
        GLst = new GList();
        int H;
        int r;
        try {
            BFR = new BufferedReader(new FileReader(Path));

            check = "size = ";
            String Line = BFR.readLine();
            if (Line.indexOf(check) == 0) {
                H = Integer.parseInt(Line.substring(check.length()));
                VMP = new VirtualMap((int) (1.5 * H), H);
            } else {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            check = "DistP = ";
            Line = BFR.readLine();
            if (Line.indexOf(check) == 0) {
                r = Integer.parseInt(Line.substring(check.length()));
                VMP.dist = r;
            } else {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            int pLen;
            check = "pLength = ";
            Line = BFR.readLine();
            if (Line.indexOf(check) == 0) {
                pLen = Integer.parseInt(Line.substring(check.length()));
            } else {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            if (!Objects.equals(BFR.readLine(), "p = [")) {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            P = new Point[pLen];
            for (int i = 0; i < pLen; i++) {
                Line = BFR.readLine();
                if (Line.contains(",")) {
                    String[] Params = Line.split(",");
                    P[i] = new Point();
                    P[i].index = Integer.parseInt(Params[0]);
                    P[i].x = Integer.parseInt(Params[1]);
                    P[i].y = Integer.parseInt(Params[2]);
                    P[i].name = Params[3];
                    P[i].town = Integer.parseInt(Params[4]);
                    P[i].city = Integer.parseInt(Params[5]);
                    P[i].province = Integer.parseInt(Params[6]);
                } else {
                    ExceptionProcess();
                    //file data damaged
                    return null;
                }
            }

            if (!Objects.equals(BFR.readLine(), "]")) {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            int TLen;
            check = "TLength = ";
            Line = BFR.readLine();
            if (Line.indexOf(check) == 0) {
                TLen = Integer.parseInt(Line.substring(check.length()));
            } else {
                ExceptionProcess();
                //file data damaged
                return null;
            }
            KMeans.KTown = TLen;

            if (!Objects.equals(BFR.readLine(), "towns = [")) {
                ExceptionProcess();
                //file data damaged
                return null;
            }
            //towns documents

            region.Towns = new Point[TLen];
            for (int i = 0; i < TLen; i++) {
                Line = BFR.readLine();
                if (Line.contains(",")) {
                    String[] params = Line.split(",");
                    region.Towns[i] = new Point();
                    region.Towns[i].x = Integer.parseInt(params[0]);
                    region.Towns[i].y = Integer.parseInt(params[1]);
                    region.Towns[i].name = params[2];
                    region.Towns[i].town = Integer.parseInt(params[3]);
                } else {
                    ExceptionProcess();
                    //file data damaged
                    return null;
                }
            }

            if (!Objects.equals(BFR.readLine(), "]")) {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            int CLen;
            check = "CLength = ";
            Line = BFR.readLine();
            if (Line.indexOf(check) == 0) {
                CLen = Integer.parseInt(Line.substring(check.length()));
            } else {
                ExceptionProcess();
                //file data damaged
                return null;
            }
            KMeans.KCity = CLen;

            if (!Objects.equals(BFR.readLine(), "cities = [")) {
                ExceptionProcess();
                //file data damaged
                return null;
            }
            //towns documents

            region.Cities = new Point[CLen];
            for (int i = 0; i < CLen; i++) {
                Line = BFR.readLine();
                if (Line.contains(",")) {
                    String[] params = Line.split(",");
                    region.Cities[i] = new Point();
                    region.Cities[i].x = Integer.parseInt(params[0]);
                    region.Cities[i].y = Integer.parseInt(params[1]);
                    region.Cities[i].name = params[2];
                    region.Cities[i].city = Integer.parseInt(params[3]);
                } else {
                    ExceptionProcess();
                    //file data damaged
                    return null;
                }
            }

            if (!Objects.equals(BFR.readLine(), "]")) {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            int PLen;
            check = "PLength = ";
            Line = BFR.readLine();
            if (Line.indexOf(check) == 0) {
                PLen = Integer.parseInt(Line.substring(check.length()));
            } else {
                ExceptionProcess();
                //file data damaged
                return null;
            }
            KMeans.KProvince = PLen;

            if (!Objects.equals(BFR.readLine(), "provinces = [")) {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            region.Provinces = new Point[PLen];
            for (int i = 0; i < PLen; i++) {
                Line = BFR.readLine();
                if (Line.contains(",")) {
                    String[] params = Line.split(",");
                    region.Provinces[i] = new Point();
                    region.Provinces[i].x = Integer.parseInt(params[0]);
                    region.Provinces[i].y = Integer.parseInt(params[1]);
                    region.Provinces[i].name = params[2];
                    region.Provinces[i].province = Integer.parseInt(params[3]);
                } else {
                    ExceptionProcess();
                    //file data damaged
                    return null;
                }
            }


            //towns documents

            if (!Objects.equals(BFR.readLine(), "]")) {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            int TALen;
            check = "TALength = ";
            Line = BFR.readLine();
            if (Line.indexOf(check) == 0) {
                TALen = Integer.parseInt(Line.substring(check.length()));
            } else {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            if (!Objects.equals(BFR.readLine(), "TownArea = [")) {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            for (int i = 0; i < TALen; i++) {
                Line = BFR.readLine();

                String[] Part = Line.split(";");

                String[] XPoints = Part[0].split(",");
                String[] YPoints = Part[1].split(",");

                Point[] PTemp = new Point[XPoints.length];
                for (int j = 0; j < XPoints.length; j++) {
                    PTemp[j] = new Point(Integer.parseInt(XPoints[j]), Integer.parseInt(YPoints[j]));
                }

                region.TownBorder.add(PTemp);

                String[] RGB = Part[2].split(",");

                region.TownAreaColor.add(new Color(Integer.parseInt(RGB[0]), Integer.parseInt(RGB[1]), Integer.parseInt(RGB[2])));
            }

            if (!Objects.equals(BFR.readLine(), "]")) {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            int CALen;
            check = "CALength = ";
            Line = BFR.readLine();
            if (Line.indexOf(check) == 0) {
                CALen = Integer.parseInt(Line.substring(check.length()));
            } else {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            if (!Objects.equals(BFR.readLine(), "CityArea = [")) {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            for (int i = 0; i < CALen; i++) {
                Line = BFR.readLine();

                String[] Part = Line.split(";");

                String[] XPoints = Part[0].split(",");
                String[] YPoints = Part[1].split(",");

                Point[] PTemp = new Point[XPoints.length];
                for (int j = 0; j < XPoints.length; j++) {
                    PTemp[j] = new Point(Integer.parseInt(XPoints[j]), Integer.parseInt(YPoints[j]));
                }

                region.CityBorder.add(PTemp);

                String[] RGB = Part[2].split(",");

                region.CityAreaColor.add(new Color(Integer.parseInt(RGB[0]), Integer.parseInt(RGB[1]), Integer.parseInt(RGB[2])));
            }


            if (!Objects.equals(BFR.readLine(), "]")) {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            int PALen;
            check = "PALength = ";
            Line = BFR.readLine();
            if (Line.indexOf(check) == 0) {
                PALen = Integer.parseInt(Line.substring(check.length()));
            } else {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            if (!Objects.equals(BFR.readLine(), "ProvinceArea = [")) {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            for (int i = 0; i < PALen; i++) {
                Line = BFR.readLine();

                String[] Part = Line.split(";");

                String[] XPoints = Part[0].split(",");
                String[] YPoints = Part[1].split(",");

                Point[] PTemp = new Point[XPoints.length];
                for (int j = 0; j < XPoints.length; j++) {
                    PTemp[j] = new Point(Integer.parseInt(XPoints[j]), Integer.parseInt(YPoints[j]));
                }

                region.ProvinceBorder.add(PTemp);

                String[] RGB = Part[2].split(",");

                region.ProvinceAreaColor.add(new Color(Integer.parseInt(RGB[0]), Integer.parseInt(RGB[1]), Integer.parseInt(RGB[2])));
            }

            if (!Objects.equals(BFR.readLine(), "]")) {
                ExceptionProcess();
                //file data damaged
                return null;
            }


            if (!Objects.equals(BFR.readLine(), "v = [")) {
                ExceptionProcess();
                //file data damaged
                return null;
            }

            for (int i = 0; i < pLen; i++) {
                Line = BFR.readLine();
                if (Line.length() == 0) {
                    GLst.list.get(i).PHost = P[i];
                } else {
                    String[] singleRoad = Line.split(";+");
                    for(String road:singleRoad){
                        String[] info=road.split(",");
                        MapStorage.GLst.add2List(getPointByIndex(i), getPointByIndex(Integer.parseInt(info[0])),Integer.parseInt(info[1]));
                    }
                }
            }

            if (!Objects.equals(BFR.readLine(), "]")) {
                ExceptionProcess();
                //file data damaged
                return null;
            }


            BFR.close();
        } catch (Exception e) {
            ExceptionProcess();
            return null;
            //stream exception
        }

        Main.GUI.UpdateVirtualMap(VMP, P, GLst, r);
        return VMP;
    }

    private static Point getPointByIndex(int ind) {
        if (ind == -1) {
            return null;
        }
        for (Point p : MapStorage.P) {
            if (p.index == ind) {
                return new Point(p);
            }
        }
        return null;
    }

    void ExceptionProcess() {

    }

    void Output() throws Exception {
        DOS = new DataOutputStream(new FileOutputStream(Path));
        DOS.write(("size = " + Map.H_VirtualMap + "\n").getBytes());
        DOS.write(("DistP = " + Map.dist + "\n").getBytes());

        DOS.write(("pLength = " + Map.P.length + "\n").getBytes());
        DOS.write(("p = [\n").getBytes());
        for (int i = 0; i < Map.P.length; i++) {
            DOS.write((Map.P[i].index + "," + Map.P[i].x + "," + Map.P[i].y + "," + Map.P[i].name + "," + Map.P[i].town + "," + Map.P[i].city + "," + Map.P[i].province + "," + "\n").getBytes());
        }
        DOS.write(("]\n").getBytes());

        DOS.write(("TLength = " + region.Towns.length + "\n").getBytes());
        DOS.write(("towns = [\n").getBytes());
        for (int i = 0; i < region.Towns.length; i++) {
            DOS.write((region.Towns[i].x + "," + region.Towns[i].y + "," + region.Towns[i].name + "," + region.Towns[i].town + "," + "\n").getBytes());
        }
        DOS.write(("]\n").getBytes());

        DOS.write(("CLength = " + region.Cities.length + "\n").getBytes());
        DOS.write(("cities = [\n").getBytes());
        for (int i = 0; i < region.Cities.length; i++) {
            DOS.write((region.Cities[i].x + "," + region.Cities[i].y + "," + region.Cities[i].name + "," + region.Cities[i].city + "," + "\n").getBytes());
        }
        DOS.write(("]\n").getBytes());

        DOS.write(("PLength = " + region.Provinces.length + "\n").getBytes());
        DOS.write(("provinces = [\n").getBytes());
        for (int i = 0; i < region.Provinces.length; i++) {
            DOS.write((region.Provinces[i].x + "," + region.Provinces[i].y + "," + region.Provinces[i].name + "," + region.Provinces[i].province + "," + "\n").getBytes());
        }
        DOS.write(("]\n").getBytes());


        DOS.write(("TALength = " + region.TownBorder.size() + "\n").getBytes());
        DOS.write(("TownArea = [\n").getBytes());

        for (int i = 0; i < region.TownBorder.size(); i++) {
            for (int j = 0; j < region.TownBorder.get(i).length - 1; j++) {
                DOS.write((region.TownBorder.get(i)[j].x + ",").getBytes());
            }
            DOS.write((region.TownBorder.get(i)[region.TownBorder.get(i).length - 1].x + ";").getBytes());


            for (int j = 0; j < region.TownBorder.get(i).length - 1; j++) {
                DOS.write((region.TownBorder.get(i)[j].y + ",").getBytes());
            }
            DOS.write((region.TownBorder.get(i)[region.TownBorder.get(i).length - 1].y + ";").getBytes());

            DOS.write((region.TownAreaColor.get(i).getRed() + "," + region.TownAreaColor.get(i).getGreen() + "," + region.TownAreaColor.get(i).getBlue() + "\n").getBytes());

        }
        DOS.write(("]\n").getBytes());

        DOS.write(("CALength = " + region.CityBorder.size() + "\n").getBytes());

        DOS.write(("CityArea = [\n").getBytes());

        for (int i = 0; i < region.CityBorder.size(); i++) {
            for (int j = 0; j < region.CityBorder.get(i).length - 1; j++) {
                DOS.write((region.CityBorder.get(i)[j].x + ",").getBytes());
            }
            DOS.write((region.CityBorder.get(i)[region.CityBorder.get(i).length - 1].x + ";").getBytes());


            for (int j = 0; j < region.CityBorder.get(i).length - 1; j++) {
                DOS.write((region.CityBorder.get(i)[j].y + ",").getBytes());
            }
            DOS.write((region.CityBorder.get(i)[region.CityBorder.get(i).length - 1].y + ";").getBytes());

            DOS.write((region.CityAreaColor.get(i).getRed() + "," + region.CityAreaColor.get(i).getGreen() + "," + region.CityAreaColor.get(i).getBlue() + "\n").getBytes());

        }


        DOS.write(("]\n").getBytes());

        DOS.write(("PALength = " + region.ProvinceBorder.size() + "\n").getBytes());

        DOS.write(("ProvinceArea = [\n").getBytes());

        for (int i = 0; i < region.ProvinceBorder.size(); i++) {
            for (int j = 0; j < region.ProvinceBorder.get(i).length - 1; j++) {
                DOS.write((region.ProvinceBorder.get(i)[j].x + ",").getBytes());
            }
            DOS.write((region.ProvinceBorder.get(i)[region.ProvinceBorder.get(i).length - 1].x + ";").getBytes());


            for (int j = 0; j < region.ProvinceBorder.get(i).length - 1; j++) {
                DOS.write((region.ProvinceBorder.get(i)[j].y + ",").getBytes());
            }
            DOS.write((region.ProvinceBorder.get(i)[region.ProvinceBorder.get(i).length - 1].y + ";").getBytes());

            DOS.write((region.ProvinceAreaColor.get(i).getRed() + "," + region.ProvinceAreaColor.get(i).getGreen() + "," + region.ProvinceAreaColor.get(i).getBlue() + "\n").getBytes());

        }


        DOS.write(("]\n").getBytes());

        this.saveRoad();
        DOS.close();
    }

    public void saveRoad() throws Exception {
        DOS.write(("v = [\n").getBytes());
        Object O = this.getClass().getDeclaredConstructor().newInstance();
        Method printN = this.getClass().getMethod("printNode", GLNode.class);
        Method printL = this.getClass().getMethod("printEndLine");
        GList.foreach(Map.GLst, O, null, O, printN, O, printL);
        DOS.write(("]\n").getBytes());
    }

    public void printNode(GLNode Node) throws IOException {
        DOS.write((Node.P.index + "," + Node.volume + ";").getBytes());
    }

    public void printEndLine() throws IOException {
        DOS.write(("\n").getBytes());
    }


}
