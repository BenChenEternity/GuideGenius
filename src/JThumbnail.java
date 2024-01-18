import javax.swing.*;
import java.awt.*;


public class JThumbnail extends JPanel {

    int X;
    int Y;
    int W;
    int H;

    JThumbnail(){
        this.setBackground(Color.white);
        this.setOpaque(false);
        this.X=45;
        this.Y=30;
        this.W=90;
        this.H=60;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d=(Graphics2D)g;
        Stroke stroke=new BasicStroke(3.0f);
        g2d.setStroke(stroke);
        g2d.setPaint(Color.green);
        g2d.drawRect(this.X, this.Y, this.W, this.H);
    }

    public void setPos(int x,int y,int w,int h){
        this.X=x;
        this.Y=y;
        this.W=w;
        this.H=h;
    }

}
