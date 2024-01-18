import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class JImageButton extends JButton {
    public JImageButton(String path){
        ImageIcon ico=new ImageIcon(Objects.requireNonNull(importImage.Get(path)));
        setIcon(ico);
        setPreferredSize(new Dimension(ico.getIconWidth(),ico.getIconHeight()));
    }
}
class importImage {
    public static Image Get(String path) {
        try {
            InputStream ImageInput;
            URL U = Main.class.getResource(path);
            assert U != null;
            ImageInput = U.openStream();
            Image img= ImageIO.read(ImageInput);
            ImageInput.close();
            return img;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

