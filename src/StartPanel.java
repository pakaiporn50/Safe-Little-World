import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*;
import java.io.InputStream;


public class StartPanel extends JPanel {
    Clip clip;

    public StartPanel() {

        this.setLayout(null);

        // GIF Background
        JLabel bgLabel = new JLabel(
                new ImageIcon(getClass().getResource("/images/Backopen.gif"))
        );
        bgLabel.setBounds(0, 0, 800, 600);
        this.add(bgLabel);
        bgLabel.setLayout(null);
        // 🏷️ ชื่อเกม (รูป)
        System.out.println(getClass().getResource("/images/name_game.png"));
        JLabel title = new JLabel(
                new ImageIcon(getClass().getResource("/images/name_game.png"))
        );
        title.setBounds(0, 0, 800, 600);


// ❗ ใส่ลงใน bgLabel เหมือนปุ่ม
        bgLabel.add(title);


        // Start Button
        JButton startButton = new JButton(
                new ImageIcon(getClass().getResource("/images/Button.png"))
        );
        startButton.setBounds(-50, 100, 300, 800);

        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);

        startButton.addActionListener(e -> {

            clip.stop();

            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

            topFrame.setContentPane(new AnimationPanel());
            topFrame.revalidate();
            topFrame.repaint();
        });

        bgLabel.add(startButton);
        bgLabel.add(title);
        playMusic("/images/opengame.wav");
    }
    public void playMusic(String soundFile) {//หาไฟล์เสียง
        try {
            InputStream audioSrc = getClass().getResourceAsStream(soundFile);
            InputStream bufferedIn = new java.io.BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // เล่นวน
        } catch (Exception e) {
            e.printStackTrace();
        }
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }

}