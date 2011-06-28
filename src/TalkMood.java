import com.sun.awt.AWTUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TalkMood extends ComponentAdapter {
    public static final String URL_PREFIX = "http://talkmood.appspot.com";

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" HH:mm:ss ");
    private long showEventsSince;
    private JLabel likeCount;
    private JLabel timeLabel;

    public static void main(String[] args) throws Exception {
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        new TalkMood().start();
    }

    private void start() {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);

        likeCount = new JLabel("0", loadLikeIcon(), JLabel.LEFT);
        likeCount.setFont(new Font("Helvetica", Font.BOLD, 30));
        likeCount.setOpaque(false);
        likeCount.setForeground(Color.WHITE);
        likeCount.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(likeCount);

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
        timeLabel.setOpaque(false);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateTime(timeLabel);
        frame.add(timeLabel);

        addPopupMenu(frame);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.getContentPane().setBackground(Color.BLACK);
        frame.addComponentListener(new TalkMood());
        if (AWTUtilities.isTranslucencySupported(AWTUtilities.Translucency.TRANSLUCENT))
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

    private void addPopupMenu(JFrame frame) {
        final JPopupMenu menu = new JPopupMenu();
        JMenuItem item = new JMenuItem("Reset");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetVotes();
            }
        });
        menu.add(item);

        item = new JMenuItem("Quit");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(item);

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private void updateTime(JLabel timeLabel) {
        timeLabel.setText(simpleDateFormat.format(new Date()));
    }

    private void updateLikeCount(JLabel likeCount) {
        try {
            URL url = new URL(URL_PREFIX + "/do/vote/num/from/" + showEventsSince);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String votes = reader.readLine();
            if (votes == null || votes.trim().isEmpty()) votes = "0";
            likeCount.setText(votes);
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetVotes() {
        try {
            URL url = new URL(URL_PREFIX + "/do/events/mark");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            System.out.println(reader.readLine());
            reader.close();
            showEventsSince = System.currentTimeMillis();
            likeCount.setText("0");
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(likeCount, e.toString());
        }
    }

    private void makeRoundedCorners(Window w) {
        AWTUtilities.setWindowShape(w, new RoundRectangle2D.Float(0, 0, w.getWidth(), w.getHeight(), 7, 7));
    }

    @Override
    public void componentResized(ComponentEvent e) {
        Window w = (Window) e.getComponent();
        makeRoundedCorners(w);
//        w.getGraphicsConfiguration().getDevice().set
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
