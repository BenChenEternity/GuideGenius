import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.Image.SCALE_AREA_AVERAGING;

public class JThumbnailBG extends JLabel {
    JThumbnailBG(JPaint panel) {
        Dimension ImgSz = panel.getSize();
        BufferedImage image = new BufferedImage(ImgSz.width,
                ImgSz.height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.createGraphics();
        panel.paintOutput(g);
        Icon ico = new ImageIcon(image.getScaledInstance(160, 120, SCALE_AREA_AVERAGING));
        this.setIcon(ico);
        this.setVisible(true);
        this.setLayout(null);
        this.setVerticalAlignment(SwingConstants.TOP);
    }
}
