package eu.devclub.talkmood;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TalkMood extends JPanel {
    public static final String URL_PREFIX = "http://talkmood.appspot.com";

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" HH:mm:ss ");
    private long showEventsSince;
    private JLabel likeCount;
    private JLabel timeLabel;

    public TalkMood() {
        init();
    }

    private void init() {
        likeCount = new JLabel("?", loadLikeIcon(), JLabel.LEFT);
        likeCount.setFont(new Font("Helvetica", Font.BOLD, 30));
        likeCount.setOpaque(false);
        likeCount.setForeground(Color.WHITE);
        likeCount.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(likeCount);

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
        timeLabel.setOpaque(false);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateTime(timeLabel);
        add(timeLabel);

        addPopupMenu(this);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.BLACK);

        new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateTime(timeLabel);
                if (timeLabel.getTopLevelAncestor() instanceof Window)
                    ((Window)timeLabel.getTopLevelAncestor()).pack();
            }
        }).start();

        new Timer(2000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateLikeCount(likeCount);
            }
        }).start();
    }

    private void addPopupMenu(Container frame) {
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
            if (votes == null || votes.trim().isEmpty()) votes = "?";
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

    protected ImageIcon loadLikeIcon() {
        URL url = getClass().getResource("/like.png");
        ImageIcon icon = new ImageIcon(url);
        icon = new ImageIcon(icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        return icon;
    }
}
