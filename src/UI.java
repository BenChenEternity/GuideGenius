import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

import static java.lang.Math.floor;

public class UI extends JFrame {
    Sep_Log Log_Window;
    JScrollPane SP_Log;
    JTextArea Log;
    JLabel MainTitle;
    static Administrator AdminPanel;
    JLabel Board;
    JPaint Map;
    VirtualMap VMP;
    JThumbnail Thumbnail;
    JSlider zoom;
    static int value_z = 50;
    JSlider vertical;
    static int value_y = 50;
    JSlider horizontal;
    static int value_x = 50;
    int W_border;
    int H_border;
    int W_sight;
    int H_sight;
    JThumbnailBG thumbnailBG;
    hashmap Hash;
    JImageButton LogWindowBtn;
    JImageButton OpenAdminUI;
    JImageButton RequestInfo;
    JImageButton newNav;

    public UI(String TITLE) {
        super(TITLE);
    }

    public void UpdateVirtualMap(VirtualMap VMP_void, Point[] P, GList GLst, int distP) {
        Main.GUI.VMP = VMP_void;
        Main.GUI.VMP.Init(P, GLst);
        situation.RoadMap = GLst;
        Main.GUI.VMP.dist = distP;
        MapStorage.P = P;
        MapStorage.GLst = GLst;
        MapStorage.Map = Main.GUI.VMP;
    }

    public void open() {
        Main.IOController = new MapStorage();
        Main.IOController.setPath("./map.bin");
        Log_Window = new Sep_Log("Log Window", this);
        AdminPanel = new Administrator();

        setIconImage(importImage.Get("/icon/icon.png"));

        setTitle("Guide Genius");
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setSize(800, 620);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addComponentListener(new Monitor_Component(this));
        this.Log = new JTextArea();
        this.SP_Log = new JScrollPane(Log);
        this.SP_Log.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.SP_Log.setBounds(30, 400, 690, 100);
        this.Log.setBackground(Color.white);
        this.Log.setEditable(false);
        this.Log.setVisible(true);
        this.Log.setLineWrap(true);
        this.Log.setWrapStyleWord(true);
        this.Log.setFont(new Font("Comic Sans", Font.BOLD, 25));

        LogWindowBtn = new JImageButton("/icon/BtnExp.png");
        LogWindowBtn.addActionListener(e -> {
            UI.this.SP_Log.setVisible(false);
            UI.this.Log_Window.setVisible(true);
            UI.this.LogWindowBtn.setVisible(false);
            UI.this.Log_Window.Log.setText(UI.this.Log.getText());
        });

        OpenAdminUI = new JImageButton("/icon/BtnAdmin.png");
        OpenAdminUI.addActionListener(e -> {
            UI.AdminPanel.setVisible(true);
        });

        RequestInfo = new JImageButton("/icon/BtnInfo.png");
        RequestInfo.addActionListener(e -> {
            UI.LogPrintln("Information of the map:\nWidth: " + Main.GUI.VMP.W_VirtualMap + "\nHeight: " + Main.GUI.VMP.H_VirtualMap + "\nDistance between: " + Main.GUI.VMP.dist + "\nPoints: " + Main.GUI.VMP.P.length + "\nTowns: " + KMeans.KTown + "\nCities: " + KMeans.KCity + "\nProvinces: " + KMeans.KProvince + "\n");
        });

        newNav = new JImageButton("/icon/BtnNav.png");
        newNav.addActionListener(e -> {
            JFrame selectFrame = new JFrame();
            selectFrame.setLocationRelativeTo(null);
            selectFrame.setLayout(new BoxLayout(selectFrame.getContentPane(), BoxLayout.Y_AXIS));
            selectFrame.getContentPane().add(new JLabel(new ImageIcon(Objects.requireNonNull(importImage.Get("/icon/logo.png")))));
            JButton p2p = new JButton("Point to Point");
            p2p.addActionListener(E -> {
                JFrame p2pFrame = new JFrame();
                p2pFrame.setLocationRelativeTo(null);

                p2pFrame.getContentPane().setLayout(new GridBagLayout());

                GridBagConstraints cs = new GridBagConstraints();
                cs.anchor = GridBagConstraints.WEST;
                cs.gridx = 0;
                cs.gridy = GridBagConstraints.RELATIVE;
                cs.insets = new Insets(10, 10, 10, 50);

                GridBagConstraints textFieldConstraints = new GridBagConstraints();
                textFieldConstraints.anchor = GridBagConstraints.WEST;
                textFieldConstraints.gridx = 1;
                textFieldConstraints.gridy = GridBagConstraints.RELATIVE;
                textFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
                textFieldConstraints.weightx = 1;

                JTextField textFieldStart = new JTextField();
                JTextField textFieldDest = new JTextField();

                p2pFrame.getContentPane().add(new JLabel("Start point:"), cs);
                p2pFrame.getContentPane().add(textFieldStart, textFieldConstraints);
                p2pFrame.getContentPane().add(new JLabel("Destination:"), cs);
                p2pFrame.getContentPane().add(textFieldDest, textFieldConstraints);

                JButton findByDist = new JButton("Shortest path");
                findByDist.addActionListener(ee -> {
                    JPaint.HighLightedRoute = Main.GUI.VMP.GLst.findShortestPath(Integer.parseInt(textFieldStart.getText()), Integer.parseInt(textFieldDest.getText()));
                    try {
                        this.repaint_sight();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
                JButton findByTime = new JButton("Least time cost");
                findByTime.addActionListener(ee -> {
                    JPaint.HighLightedRoute = Main.GUI.VMP.GLst.findLeastCostPath(Integer.parseInt(textFieldStart.getText()), Integer.parseInt(textFieldDest.getText()));
                    try {
                        this.repaint_sight();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });

                p2pFrame.getContentPane().add(findByDist, textFieldConstraints);
                p2pFrame.getContentPane().add(findByTime, textFieldConstraints);

                p2pFrame.pack();
                p2pFrame.setVisible(true);
            });
            selectFrame.getContentPane().add(p2p);
            JButton Near = new JButton("Find N near Points");
            Near.addActionListener(E -> {
                JFrame nearFrame = new JFrame();
                nearFrame.setLocationRelativeTo(null);

                nearFrame.getContentPane().setLayout(new GridBagLayout());

                GridBagConstraints cs = new GridBagConstraints();
                cs.anchor = GridBagConstraints.WEST;
                cs.gridx = 0;
                cs.gridy = GridBagConstraints.RELATIVE;
                cs.insets = new Insets(10, 10, 10, 50);

                GridBagConstraints textFieldConstraints = new GridBagConstraints();
                textFieldConstraints.anchor = GridBagConstraints.WEST;
                textFieldConstraints.gridx = 1;
                textFieldConstraints.gridy = GridBagConstraints.RELATIVE;
                textFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
                textFieldConstraints.weightx = 1;

                JTextField textFieldX = new JTextField();
                JTextField textFieldY = new JTextField();
                JTextField textFieldN = new JTextField();

                nearFrame.getContentPane().add(new JLabel("X( no more than " + Main.GUI.VMP.W_VirtualMap + "):"), cs);
                nearFrame.getContentPane().add(textFieldX, textFieldConstraints);
                nearFrame.getContentPane().add(new JLabel("Y( no more than " + Main.GUI.VMP.H_VirtualMap + "):"), cs);
                nearFrame.getContentPane().add(textFieldY, textFieldConstraints);
                nearFrame.getContentPane().add(new JLabel("count of N:"), cs);
                nearFrame.getContentPane().add(textFieldN, textFieldConstraints);

                JButton submitBtn = new JButton("Submit");
                submitBtn.addActionListener(ee -> {
                    JPaint.HighLightedPoint = Main.GUI.VMP.findClosestPoints(Integer.parseInt(textFieldX.getText()), Integer.parseInt(textFieldY.getText()), Integer.parseInt(textFieldN.getText()));
                    try {
                        this.repaint_sight();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
                nearFrame.getContentPane().add(submitBtn, textFieldConstraints);

                nearFrame.pack();
                nearFrame.setVisible(true);
            });
            selectFrame.getContentPane().add(Near);
            JButton findPos = new JButton("Find Surroundings");
            findPos.addActionListener(E -> {
                new SquareButtonsFrame();
            });
            JButton checkRoadCondition = new JButton("Check road conditions");
            checkRoadCondition.addActionListener(E -> {
                JFrame checkFrame = new JFrame();
                checkFrame.setLocationRelativeTo(null);

                checkFrame.getContentPane().setLayout(new GridBagLayout());

                GridBagConstraints cs = new GridBagConstraints();
                cs.anchor = GridBagConstraints.WEST;
                cs.gridx = 0;
                cs.gridy = GridBagConstraints.RELATIVE;
                cs.insets = new Insets(10, 10, 10, 50);

                GridBagConstraints textFieldConstraints = new GridBagConstraints();
                textFieldConstraints.anchor = GridBagConstraints.WEST;
                textFieldConstraints.gridx = 1;
                textFieldConstraints.gridy = GridBagConstraints.RELATIVE;
                textFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
                textFieldConstraints.weightx = 1;

                JTextField textFieldA = new JTextField();
                JTextField textFieldB = new JTextField();

                checkFrame.getContentPane().add(new JLabel("index of Point A:"), cs);
                checkFrame.getContentPane().add(textFieldA, textFieldConstraints);
                checkFrame.getContentPane().add(new JLabel("index of Point B:"), cs);
                checkFrame.getContentPane().add(textFieldB, textFieldConstraints);

                JButton submitBtn = new JButton("Submit");
                submitBtn.addActionListener(ee -> {
                    UI.LogPrintln(Main.GUI.VMP.GLst.inspect(Integer.parseInt(textFieldA.getText()), Integer.parseInt(textFieldB.getText())));
                });
                checkFrame.getContentPane().add(submitBtn, textFieldConstraints);

                checkFrame.pack();
                checkFrame.setVisible(true);
            });
            selectFrame.getContentPane().add(findPos);
            selectFrame.getContentPane().add(checkRoadCondition);
            selectFrame.pack();
            selectFrame.setVisible(true);
        });

        this.VMP = Main.IOController.Input();
        if (this.VMP == null) {
            this.VMP = new VirtualMap(150000, 100000);
            this.Hash = poisson.Sample(150000, 100000, 1000, 40);
            this.VMP.Init(PointDouble.toPoint(Hash.PD), Hash.GL);
            KMeans.KTown = 200;
            KMeans.KCity = 30;
            KMeans.KProvince = 5;
            KMeans.DivideArea(this.VMP.P);
            this.VMP.dist = 1000;
            region.Init();
            region.setTownsName();
            region.setCitiesName();
            region.setProvincesName();
            region.Dispose();
        }

        situation.setRoadMap(this.VMP.GLst);

        this.Board = new JLabel();
        this.Board.setVisible(true);
        this.Board.setOpaque(true);
        this.Board.setBackground(Color.white);

        this.Map = new JPaint();
        JPaint.ThresholdPT = 15;
        JPaint.ThresholdTC = 50;
        JPaint.ThresholdCP = 85;
        JPaint.ThresholdPolygonS = 45;
        JPaint.ThresholdPolygonB = 70;
        this.Map.setOpaque(false);
        this.Map.setVisible(true);
        this.Map.setSize(540, 360);

        this.thumbnailBG = new JThumbnailBG(this.Map);
        this.thumbnailBG.setBounds(this.getWidth() - 220, 20, 180, 120);
        this.thumbnailBG.setBorder(new LineBorder(Color.BLACK));

        this.Thumbnail = new JThumbnail();
        this.Thumbnail.setVisible(true);
        this.Thumbnail.setBounds(this.getWidth() - 220, 20, 180, 120);
        this.W_border = this.Thumbnail.getWidth();
        this.H_border = this.Thumbnail.getHeight();

        this.zoom = new JSlider(JSlider.VERTICAL);
        this.zoom.setMajorTickSpacing(10);
        this.zoom.setMinorTickSpacing(5);
        this.zoom.setPaintTicks(true);
        this.zoom.setVisible(true);

        this.vertical = new JSlider(JSlider.VERTICAL);
        this.horizontal = new JSlider(JSlider.HORIZONTAL);

        this.MainTitle = new JLabel("Guide Genius");
        this.MainTitle.setVisible(true);
        this.MainTitle.setFont(new Font("Comic Sans", Font.BOLD, 30));
        this.MainTitle.setVerticalAlignment(SwingConstants.TOP);

        add(LogWindowBtn);
        add(OpenAdminUI);
        add(RequestInfo);
        add(newNav);
        add(SP_Log);

        add(Board);
        Board.add(this.Map);
        add(Thumbnail);
        add(thumbnailBG);
        add(zoom);
        add(vertical);
        add(horizontal);
        add(MainTitle);

        vertical.addChangeListener(event -> {
            value_y = vertical.getValue();
            try {
                this.repaint_sight();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        horizontal.addChangeListener(event -> {
            value_x = horizontal.getValue();
            try {
                this.repaint_sight();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        zoom.addChangeListener(event -> {
            value_z = 100 - zoom.getValue();
            if (value_z == 0) {
                value_z = 1;
            }
            try {
                this.repaint_sight();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        this.setFocusable(true);
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation() * 2;
                if (e.isShiftDown()) {
                    horizontal.setValue(horizontal.getValue() + notches);
                } else if (e.isControlDown()) {
                    zoom.setValue(zoom.getValue() - notches);
                } else {
                    vertical.setValue(vertical.getValue() - notches);
                }
            }
        };
        vertical.addMouseWheelListener(mouseAdapter);
        horizontal.addMouseWheelListener(mouseAdapter);
        zoom.addMouseWheelListener(mouseAdapter);
        this.addMouseWheelListener(mouseAdapter);

        this.setVisible(true);
        Main.Loading.setVisible(false);
    }

    void repaint_sight() throws Exception {
        this.W_sight = (int) floor((double) (W_border * value_z) / 100 * this.Map.getWidth() / 540);
        this.H_sight = (int) floor((double) (H_border * value_z) / 100 * this.Map.getHeight() / 360);
        int X_sight = (int) ((double) ((W_border - W_sight) * value_x) / 100);
        int Y_sight = (int) ((double) ((H_border - H_sight) * (100 - value_y)) / 100);
        this.Thumbnail.setPos(X_sight, Y_sight, W_sight, H_sight);
        this.Thumbnail.repaint();
        this.VMP.setParams(X_sight, W_border, W_sight, this.getWidth(), Y_sight, H_border, H_sight, this.getHeight());

        if (value_z <= JPaint.ThresholdPT) {
            hashmapInt PointsLinesInside = VMP.getPointsLinesInVision();
            Point[] PTS = this.VMP.transform(PointsLinesInside.PD);
            GList GLS = this.VMP.transform(PointsLinesInside.GL);
            this.Map.setShowPoint(PTS, GLS);
        } else {
            if (value_z < JPaint.ThresholdTC) {
                this.Map.P = this.VMP.transform(region.Towns);
                this.Map.Lst = null;
            } else {
                if (value_z < JPaint.ThresholdCP) {
                    this.Map.P = this.VMP.transform(region.Cities);
                    this.Map.Lst = null;
                } else {
                    this.Map.P = this.VMP.transform(region.Provinces);
                    this.Map.Lst = null;
                }
            }
        }

        this.Map.repaint();
        this.repaint();
    }

    private static class Monitor_Component implements ComponentListener {
        UI win;

        public Monitor_Component(UI Win) {
            this.win = Win;
        }

        @Override
        public void componentResized(ComponentEvent e) {
            Dimension WndSz = this.win.getSize();
            win.SP_Log.setBounds(30, WndSz.height - 150, WndSz.width - 100, 100);
            win.OpenAdminUI.setBounds(WndSz.width - 60, WndSz.height - 200, 48, 48);
            win.LogWindowBtn.setBounds(WndSz.width - 60, WndSz.height - 150, 48, 48);
            win.RequestInfo.setBounds(WndSz.width - 60, WndSz.height - 250, 48, 48);
            win.newNav.setBounds(WndSz.width - 60, WndSz.height - 300, 48, 48);
            win.Board.setBounds(30, 60, WndSz.width - 260, WndSz.height - 260);
            win.Map.setBounds(0, 0, WndSz.width - 260, WndSz.height - 260);
            win.thumbnailBG.setBounds(WndSz.width - 220, 20, 180, 120);
            win.Thumbnail.setBounds(WndSz.width - 220, 20, 180, 120);
            try {
                win.repaint_sight();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            win.zoom.setBounds(WndSz.width - 180, WndSz.height - 410, 50, 200);
            win.horizontal.setBounds(WndSz.width / 2 - 280, WndSz.height - 200, 400, 40);
            win.vertical.setBounds(WndSz.width - 220, WndSz.height - 410, 50, 200);
            win.MainTitle.setBounds(30, 15, 600, 150);
        }

        @Override
        public void componentMoved(ComponentEvent e) {

        }

        @Override
        public void componentShown(ComponentEvent e) {

        }

        @Override
        public void componentHidden(ComponentEvent e) {

        }
    }

    public static void LogPrintln(String content) {
        if (Main.GUI.Log_Window.isVisible()) {
            Main.GUI.Log_Window.Log.append(content + "\n");
        } else {
            Main.GUI.Log.append(content + "\n");
        }
    }

    class SquareButtonsFrame extends JFrame {
        public SquareButtonsFrame() {

            setTitle("Nearby");

            Main.GUI.VMP.initLocations();

            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);

            JLabel label = new JLabel("Your position:");

            JTextField textField = new JTextField();

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(label, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            panel.add(textField, gbc);

            gbc.gridwidth = 1;

            for (int i = 0; i < 10; i++) {
                JButton button = new JButton();
                button.setIcon(new ImageIcon(Objects.requireNonNull(importImage.Get("/icon/" + i + ".png"))));
                int finalI = i;
                button.addActionListener(e -> {
                    ArrayList<Point> available = new ArrayList<>();
                    Point userLocation = Main.GUI.VMP.P[Integer.parseInt(textField.getText())];
                    int k = 10;
                    while ((available.size() <= 5) && (k <= 100)) {
                        ArrayList<Point> nearPoints = Main.GUI.VMP.findClosestPoints(userLocation.x, userLocation.y, k);
                        available.clear();
                        for (Point nearPoint : nearPoints) {
                            if (Main.GUI.VMP.locations.get(nearPoint.index).contains(finalI)) {
                                available.add(nearPoint);
                            }
                        }
                        k += 5;
                    }
                    JPaint.HighLightedPoint = available;
                    try {
                        repaint_sight();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });

                gbc.gridx = i % 5;
                gbc.gridy = i / 5 + 2;
                panel.add(button, gbc);
            }
            add(panel);
            setVisible(true);
            setLocationRelativeTo(null);
            pack();
        }
    }
}


class Sep_Log extends JFrame {
    JScrollPane SP_Log;
    JTextArea Log;

    public Sep_Log(String TITLE, UI Main) {
        super(TITLE);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setSize(300, 700);
        this.addComponentListener(new Monitor_Component(this));
        this.Log = new JTextArea();
        this.SP_Log = new JScrollPane(Log);
        this.SP_Log.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.SP_Log.setBounds(10, 10, 270, 640);
        this.Log.setBackground(Color.white);
        this.Log.setEditable(false);
        this.Log.setVisible(true);
        this.Log.setLineWrap(true);//长度太长，自动换行
        this.Log.setWrapStyleWord(true);//换行不断词
        this.Log.setFont(new Font("Comic Sans", Font.BOLD, 25));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Sep_Log.this.setVisible(false);
                Main.SP_Log.setVisible(true);
                Main.LogWindowBtn.setVisible(true);

                Sep_Log.this.Log.setText(Main.Log.getText());
            }
        });

        this.add(SP_Log);
        this.setVisible(false);
    }


    private static class Monitor_Component implements ComponentListener {
        Sep_Log win;

        public Monitor_Component(Sep_Log Win) {
            this.win = Win;
        }

        @Override
        public void componentResized(ComponentEvent e) {
            Dimension d = this.win.getSize();
            win.SP_Log.setBounds(10, 10, d.width - 30, d.height - 60);
        }

        @Override
        public void componentMoved(ComponentEvent e) {

        }

        @Override
        public void componentShown(ComponentEvent e) {

        }

        @Override
        public void componentHidden(ComponentEvent e) {

        }
    }
}



