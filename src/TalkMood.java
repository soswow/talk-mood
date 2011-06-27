import com.sun.awt.AWTUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TalkMood extends ComponentAdapter {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" HH:mm:ss ");

    public static void main(String[] args) throws MalformedURLException {
        new TalkMood().start();
    }

    private void start() {
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");

        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);

        final JLabel likeCount = new JLabel("0", loadLikeIcon(), JLabel.LEFT);
        likeCount.setFont(new Font("Helvetica", Font.BOLD, 30));
        likeCount.setOpaque(false);
        likeCount.setForeground(Color.WHITE);
        likeCount.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(likeCount);

        final JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
        timeLabel.setOpaque(false);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateTime(timeLabel);
        frame.add(timeLabel);

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.getContentPane().setBackground(Color.BLACK);
        frame.addComponentListener(new TalkMood());
        AWTUtilities.setWindowOpacity(frame, 0.5f);
        makeRoundedCorners(frame);
        frame.pack();
        frame.setFocusableWindowState(false);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);

        new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateTime(timeLabel);
                frame.pack();
                frame.toFront();
            }
        }).start();

        new Timer(2000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateLikeCount(likeCount);
            }
        }).start();
    }

    private void updateTime(JLabel timeLabel) {
        timeLabel.setText(simpleDateFormat.format(new Date()));
    }

    int votes;
    private void updateLikeCount(JLabel likeCount) {
        try {
            URL url = new URL("http://talkmood.appspot.com/");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            //likeCount.setText(reader.readLine());
            likeCount.setText(Integer.toString(votes++));
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeRoundedCorners(Window w) {
        AWTUtilities.setWindowShape(w, new RoundRectangle2D.Float(0, 0, w.getWidth(), w.getHeight(), 7, 7));
    }

    @Override
    public void componentResized(ComponentEvent e) {
        Window w = (Window) e.getComponent();
        makeRoundedCorners(w);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        w.setLocation((int)screenSize.getWidth() - w.getWidth() - 30, 30);
    }

    protected ImageIcon loadLikeIcon() {
        URL url = getClass().getResource("like.png");
        ImageIcon icon = new ImageIcon(url);
        icon = new ImageIcon(icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        return icon;
    }
}
