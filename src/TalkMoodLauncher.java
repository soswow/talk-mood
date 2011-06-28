import com.sun.awt.AWTUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

public class TalkMoodLauncher extends ComponentAdapter {

    public static void main(String[] args) throws Exception {
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setFocusableWindowState(false);
        frame.setAlwaysOnTop(true);
        frame.addComponentListener(new TalkMoodLauncher());

        new TalkMood().start(frame);

        if (AWTUtilities.isTranslucencySupported(AWTUtilities.Translucency.TRANSLUCENT))
            AWTUtilities.setWindowOpacity(frame, 0.5f);
        makeRoundedCorners(frame);

        frame.setVisible(true);
    }

    private static void makeRoundedCorners(Window w) {
        if (AWTUtilities.isTranslucencySupported(AWTUtilities.Translucency.PERPIXEL_TRANSPARENT))
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
