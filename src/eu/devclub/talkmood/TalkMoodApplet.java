package eu.devclub.talkmood;

import javax.swing.*;

public class TalkMoodApplet extends JApplet {
    @Override
    public void init() {
        add(new TalkMood());
        this.setSize(120, 60);
    }
}
