import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class IntroPanel extends JPanel {
    private String[] introTexts = {
            "แม่ : แล้วใครจะดูแลลูก",
            "พ่อ : มึงจะไปไหนก็ไป กูจะดูแลเอง ! ",
            "เรา : (ได้ยินอยู่ทุกคำ)"
    };
    private int currentTextIndex = 0;
    private String displayedText = "";
    private int charIndex = 0;
    private Timer charTimer;
    private JButton skipBtn;

    public IntroPanel() { // 👈 ไม่ต้องรับชื่อแล้ว เพราะใช้แค่ตอนเริ่มเกม
        setBackground(Color.BLACK);
        setLayout(null);
        // 2. สร้างปุ่ม Skip และตั้งค่าตำแหน่ง
        skipBtn = new JButton("SKIP >>");
        skipBtn.setBounds(650, 500, 100, 35); // ปรับ x, y ตามความเหมาะสม
        skipBtn.setFocusable(false);
        skipBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
        skipBtn.setBackground(new Color(60, 60, 60));
        skipBtn.setForeground(Color.WHITE);

        // 3. ใส่คำสั่งเมื่อกดปุ่ม (ให้หยุด Timer และไปฉาก 6 ทันที)
        skipBtn.addActionListener(e -> {
            if (charTimer != null) charTimer.stop();
            goToGame();
        });

        add(skipBtn); // เพิ่มปุ่มลงในหน้าจอ
        charTimer = new Timer(100, e -> {
            if (currentTextIndex < introTexts.length) {
                String fullText = introTexts[currentTextIndex];
                if (charIndex < fullText.length()) {
                    displayedText += fullText.charAt(charIndex);
                    charIndex++;
                    repaint();
                } else {
                    charTimer.stop();
                    Timer pauseTimer = new Timer(1500, ev -> {
                        currentTextIndex++;
                        if (currentTextIndex < introTexts.length) {
                            displayedText = "";
                            charIndex = 0;
                            charTimer.start();
                        } else {
                            goToGame();
                        }
                        ((Timer)ev.getSource()).stop();
                    });
                    pauseTimer.start();
                }
            }
        });
        charTimer.start();
    }

    private void goToGame() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame != null) {
            // 🎵 เริ่มเพลงหน้า GamePanel (แมวส้ม)
            MusicPlayer.play("/images/start game.wav");
            topFrame.setContentPane(new GamePanel());
            topFrame.revalidate();
            topFrame.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Tahoma", Font.BOLD, 24));

        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(displayedText)) / 2;
        int y = getHeight() / 2;
        g2.drawString(displayedText, x, y);
    }
}