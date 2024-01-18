import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class LoadingWindow extends JFrame {
    private final JLabel loadingLabel;
    public LoadingWindow() {
        setTitle("Loading");
        setSize(300, 100);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loadingLabel = new JLabel("Loading", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(loadingLabel);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> System.exit(0));
        add(cancelButton, "South");
        Timer timer = new Timer(500, new ActionListener() {
            int count = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                loadingLabel.setText("Loading" + ".".repeat(Math.max(0, count % 4)));
                count++;
            }
        });
        timer.start();
        setVisible(true);
    }
}
