import javax.swing.*;

public class TalkMoodApplet extends JApplet {
    @Override
    public void init() {
        new TalkMood().start(this.getContentPane());
        this.setSize(120, 60);
    }
}
