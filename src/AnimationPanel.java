import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AnimationPanel extends JPanel {
    ImageIcon currentIcon;
    JButton skipBtn;
    Timer timerToAni2, nextSceneTimer;
    boolean isSwitched = false;

    public AnimationPanel() {
        this.setLayout(null);
        MusicPlayer.play("/images/intro_song.wav");
        currentIcon = new ImageIcon(getClass().getResource("/images/ani0.gif"));

        skipBtn = new JButton("Skip >>");
        skipBtn.setBounds(700, 20, 100, 30);
        skipBtn.setFocusable(false);
        skipBtn.addActionListener(e -> skipToIntro()); // เปลี่ยนชื่อเมธอดให้ชัดเจน
        this.add(skipBtn);

        timerToAni2 = new Timer(24000, e -> {
            if (!isSwitched) {
                currentIcon = new ImageIcon(getClass().getResource("/images/ani2.gif"));
                MusicPlayer.play("/images/start game.wav");
                repaint();
            }
        });
        timerToAni2.setRepeats(false);
        timerToAni2.start();

        nextSceneTimer = new Timer(25500, e -> skipToIntro());
        nextSceneTimer.setRepeats(false);
        nextSceneTimer.start();
    }

    private void skipToIntro() {
        if (isSwitched) return;
        isSwitched = true;

        if (timerToAni2 != null) timerToAni2.stop();
        if (nextSceneTimer != null) nextSceneTimer.stop();

        MusicPlayer.stop();

        JFrame topFrame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this);
        if (topFrame != null) {
            // ➡️ ส่งไปที่ฉากเฉลยปมพ่อแม่ก่อนเริ่มเกม
            topFrame.setContentPane(new IntroPanel());
            topFrame.revalidate();
            topFrame.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentIcon != null) {
            g.drawImage(currentIcon.getImage(), 0, 0, 800, 600, this);
        }
    }
}