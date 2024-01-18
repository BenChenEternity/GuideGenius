import javax.swing.*;
import java.awt.*;


public class Administrator extends JFrame {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 360;

    private SavePath savePathDialog;
    private GenerateMap generateMapDialog;
    private SetTraffic setTrafficDialog;

    public Administrator() {
        super("Administrator");
        setSize(WIDTH, HEIGHT);
        setResizable(false);

        // Create the dialog objects
        savePathDialog = new SavePath();
        generateMapDialog = new GenerateMap();
        setTrafficDialog = new SetTraffic();

        // Create the panel for the buttons
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Create and add the label
        JLabel label = new JLabel("Administrator", SwingConstants.CENTER);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 24));
        getContentPane().add(label, BorderLayout.NORTH);

        // Add the buttons to the panel
        JButton generateMapButton = new JButton("Generate Map");
        generateMapButton.setPreferredSize(new Dimension(200, 40));
        JButton mapFileSettingsButton = new JButton("Map File Settings");
        mapFileSettingsButton.setPreferredSize(new Dimension(200, 40));
        JButton setTrafficButton = new JButton("Set Traffic");
        setTrafficButton.setPreferredSize(new Dimension(200, 40));
        buttonPanel.add(generateMapButton);
        buttonPanel.add(mapFileSettingsButton);
        buttonPanel.add(setTrafficButton);

        // Add the panel to the frame
        getContentPane().add(buttonPanel, BorderLayout.CENTER);

        // Center the frame on the screen
        setLocationRelativeTo(null);

        // Add the action listeners for the buttons
        generateMapButton.addActionListener(e -> {
            generateMapDialog.setVisible(true);
            situation.setRoadMap(Main.GUI.VMP.GLst);
        });
        mapFileSettingsButton.addActionListener(e -> savePathDialog.setVisible(true));
        setTrafficButton.addActionListener(e -> setTrafficDialog.setVisible(true));
    }
}


class GenerateMap extends JFrame {

    private final JTextField textField1;
    private final JTextField textField2;
    private final JTextField textField3;
    private final JTextField textField4;
    private final JTextField textField5;
    private final JTextField textField6;

    public GenerateMap() {
        super("Generate Map");

        // create panel and set layout
        JPanel panel = new JPanel(new GridBagLayout());

        textField1 = new JTextField("100000", 15);
        textField2 = new JTextField("1000", 15);
        textField3 = new JTextField("40", 15);
        textField4 = new JTextField("200", 15);
        textField5 = new JTextField("30", 15);
        textField6 = new JTextField("5", 15);

        JButton submitBtn = new JButton("Submit");
        JButton setDefaultBtn = new JButton("Set Default");

        submitBtn.addActionListener(e -> {
            try {
                Main.Loading.setVisible(true);
                int H = Integer.parseInt(textField1.getText());
                int distP = Integer.parseInt(textField2.getText());
                int Try = Integer.parseInt(textField3.getText());
                KMeans.KTown = Integer.parseInt(textField4.getText());
                KMeans.KCity = Integer.parseInt(textField5.getText());
                KMeans.KProvince = Integer.parseInt(textField6.getText());
                hashmap Hash = poisson.Sample((int) (1.5 * H), H, distP, Try);
                VirtualMap VoidNewVMP = new VirtualMap((int) (1.5 * H), H);
                Main.GUI.UpdateVirtualMap(VoidNewVMP, PointDouble.toPoint(Hash.PD), Hash.GL, distP);
                KMeans.DivideArea(Main.GUI.VMP.P);
                region.Init();
                region.setTownsName();
                region.setCitiesName();
                region.setProvincesName();
                region.Dispose();
                JOptionPane.showMessageDialog(null, "Map generated and adapted");
                Main.Loading.setVisible(false);
                UI.LogPrintln("[Admin] Generate a Map\nWidth: " + Main.GUI.VMP.W_VirtualMap + "\nHeight: " + Main.GUI.VMP.H_VirtualMap + "\nDistance between: " + Main.GUI.VMP.dist + "\nPoints: " + Main.GUI.VMP.P.length + "\nRetry: " + Try + "\nTowns: " + KMeans.KTown + "\nCities: " + KMeans.KCity + "\nProvinces: " + KMeans.KProvince + "\n");
                try {
                    Main.GUI.repaint_sight();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                this.setVisible(false);
            } catch (Exception NE) {
                JOptionPane.showMessageDialog(null, "Invalid value.");
            }


        });

        setDefaultBtn.addActionListener(e -> {
            textField1.setText("100000");
            textField2.setText("1000");
            textField3.setText("40");
            textField4.setText("200");
            textField5.setText("30");
            textField6.setText("5");
        });

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.gridx = 0;
        labelConstraints.gridy = GridBagConstraints.RELATIVE;
        labelConstraints.insets = new Insets(10, 10, 10, 50);

        GridBagConstraints textFieldConstraints = new GridBagConstraints();
        textFieldConstraints.anchor = GridBagConstraints.WEST;
        textFieldConstraints.gridx = 1;
        textFieldConstraints.gridy = GridBagConstraints.RELATIVE;

        panel.add(new JLabel("Height of the map:"), labelConstraints);
        panel.add(textField1, textFieldConstraints);
        panel.add(new JLabel("Distance between two points:"), labelConstraints);
        panel.add(textField2, textFieldConstraints);
        panel.add(new JLabel("Retry times:"), labelConstraints);
        panel.add(textField3, textFieldConstraints);
        panel.add(new JLabel("Count of towns:"), labelConstraints);
        panel.add(textField4, textFieldConstraints);
        panel.add(new JLabel("Count of cities:"), labelConstraints);
        panel.add(textField5, textFieldConstraints);
        panel.add(new JLabel("Count of provinces"), labelConstraints);
        panel.add(textField6, textFieldConstraints);

        panel.add(submitBtn, textFieldConstraints);
        panel.add(setDefaultBtn, textFieldConstraints);

        add(panel);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);
        pack();
    }
}


class SavePath extends JFrame {
    public SavePath() {
        JPanel panel = new JPanel(new BorderLayout());

        // Save/Load buttons panel
        JPanel saveLoadPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JButton saveMapButton = new JButton("Save Map");
        saveMapButton.setPreferredSize(new Dimension(160, 40));
        saveLoadPanel.add(saveMapButton);
        JButton loadMapButton = new JButton("Load Map");
        loadMapButton.setPreferredSize(new Dimension(160, 40));
        saveLoadPanel.add(loadMapButton);
        panel.add(saveLoadPanel, BorderLayout.CENTER);

        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel infoLabel = new JLabel("Save path:");
        JTextField infoField = new JTextField("./map.bin", 20);
        JButton defaultButton = new JButton("Set default");
        defaultButton.addActionListener(e -> {
            infoField.setText("./map.bin");
        });
        infoPanel.add(infoLabel);
        infoPanel.add(infoField);
        infoPanel.add(defaultButton);
        panel.add(infoPanel, BorderLayout.NORTH);

        // Submit button
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(e -> {
            String path = infoField.getText();
            try {
                Main.IOController.setPath(path);
                infoField.setText(path);
                JOptionPane.showMessageDialog(null, "Set file IO path:\n" + MapStorage.Path);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid path");
            }
        });

        loadMapButton.addActionListener(e -> {
            Main.Loading.setVisible(true);
            MapStorage.Path = infoField.getText();
            VirtualMap VP = Main.IOController.Input();
            if (VP == null) {
                JOptionPane.showMessageDialog(null, "No map was found, or the map file has been damaged.");
            } else {
                Main.GUI.VMP = VP;
                situation.RoadMap = VP.GLst;
                JOptionPane.showMessageDialog(null, "Load map done from path:\n" + MapStorage.Path);
                UI.LogPrintln("[Admin] Load a Map\nWidth: " + Main.GUI.VMP.W_VirtualMap + "\nHeight: " + Main.GUI.VMP.H_VirtualMap + "\nDistance between: " + Main.GUI.VMP.dist + "\nPoints: " + Main.GUI.VMP.P.length + "\nTowns: " + KMeans.KTown + "\nCities: " + KMeans.KCity + "\nProvinces: " + KMeans.KProvince + "\n");
                Main.Loading.setVisible(false);
                try {
                    Main.GUI.repaint_sight();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        saveMapButton.addActionListener(e -> {
            MapStorage.Path = infoField.getText();
            try {
                Main.IOController.Output();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Map saved to path:\n" + MapStorage.Path);
        });

        getContentPane().add(panel);
        setTitle("Save options");
        setSize(400, 150);
        setLocationRelativeTo(null);
    }

}

class SetTraffic extends JFrame {

    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton submitButton;

    public SetTraffic() {
        super("traffic settings");
        textField1 = new JTextField(20);
        textField1.setText("50");
        textField2 = new JTextField(20);
        textField2.setText("20");
        textField3 = new JTextField(20);
        textField3.setText("20");
        textField4 = new JTextField(20);
        textField4.setText("10");
        textField5 = new JTextField(20);
        textField5.setText("2000");
        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                int fastValue = Integer.parseInt(textField1.getText());
                int slowValue = Integer.parseInt(textField2.getText());
                int congestionValue = Integer.parseInt(textField3.getText());
                int jamValue = Integer.parseInt(textField4.getText());
                int timeSpanValue = Integer.parseInt(textField5.getText());
                situation.setTrafficSituation(fastValue, slowValue, congestionValue, jamValue);
                situation.setTimeSpan(timeSpanValue);
                JOptionPane.showMessageDialog(null, "situation set applied");
            } catch (Exception NE) {
                JOptionPane.showMessageDialog(null, "Invalid value.");
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("fast:"), c);
        c.gridx = 1;
        panel.add(textField1, c);

        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("slow:"), c);
        c.gridx = 1;
        panel.add(textField2, c);

        c.gridx = 0;
        c.gridy = 2;
        panel.add(new JLabel("congestion:"), c);
        c.gridx = 1;
        panel.add(textField3, c);

        c.gridx = 0;
        c.gridy = 3;
        panel.add(new JLabel("jam:"), c);
        c.gridx = 1;
        panel.add(textField4, c);

        c.gridx = 0;
        c.gridy = 4;
        panel.add(new JLabel("time span:"), c);
        c.gridx = 1;
        panel.add(textField5, c);

        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        panel.add(submitButton, c);

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
    }
}





