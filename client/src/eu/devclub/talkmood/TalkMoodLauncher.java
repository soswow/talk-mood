package eu.devclub.talkmood;

import com.sun.awt.AWTUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

import static com.sun.awt.AWTUtilities.Translucency.PERPIXEL_TRANSPARENT;
import static com.sun.awt.AWTUtilities.Translucency.TRANSLUCENT;

public class TalkMoodLauncher extends ComponentAdapter {

    public static void main(String[] args) throws Exception {
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(false);
//        frame.setFocusableWindowState(false);
        frame.setAlwaysOnTop(true);
        frame.addComponentListener(new TalkMoodLauncher());

        TalkMood component = new TalkMood();
        frame.add(component);
        frame.pack();

        MoveMouseListener dragListener = new MoveMouseListener(component);
        component.addMouseListener(dragListener);
        component.addMouseMotionListener(dragListener);

        if (AWTUtilities.isTranslucencySupported(TRANSLUCENT))
            AWTUtilities.setWindowOpacity(frame, 0.5f);
        makeRoundedCorners(frame);

        frame.setVisible(true);
    }

    private static void makeRoundedCorners(Window w) {
        if (AWTUtilities.isTranslucencySupported(PERPIXEL_TRANSPARENT))
            AWTUtilities.setWindowShape(w, new RoundRectangle2D.Float(0, 0, w.getWidth(), w.getHeight(), 7, 7));
    }

    @Override
    public void componentResized(ComponentEvent e) {
        Window w = (Window) e.getComponent();
        makeRoundedCorners(w);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        w.setLocation((int)screenSize.getWidth() - w.getWidth() - 30, 30);
    }
}
