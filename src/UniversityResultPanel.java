import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UniversityResultPanel extends JPanel {
    private String savedName;
    private String[] texts = {
            "(หน้าจอคอมพิวเตอร์ : 'ยินดีด้วย! คุณผ่านการคัดเลือก')",
            "เรา : (น้ำตาค่อยๆไหล สะอื้นจนตัวสั่นอยู่คนเดียว)",
            "เรา : (กดพิมพ์ข้อความส่งหาแม่)",
            "เรา : [ส่งข้อความ] : แม่หนูสอบติดมหาลัยแล้วนะ",
            "(เวลาผ่านไปเนิ่นนาน จนหน้าจอโทรศัพท์สว่างขึ้นอีกครั้ง)",
            "แม่ : เก่งมากลูก ขอบคุณนะที่ไม่เกเรและตั้งใจเรียน",
            "แม่ : 'คนเก่งของมี้'",
            "เรา : (ในที่สุดฉันก็จะไปจากบ้านหลังนี้ได้จริงๆ แล้วใช่ไหม)"
    };
    private int currentTextIndex = 0;
    private String displayedText = "";
    private int charIndex = 0;
    private Timer charTimer;
    private JButton skipBtn;

    public UniversityResultPanel(String name) {
        this.savedName = name;
        setBackground(new Color(10, 15, 20));
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
            goToFinalScene();
        });

        add(skipBtn); // เพิ่มปุ่มลงในหน้าจอ

        charTimer = new Timer(90, e -> {
            if (currentTextIndex < texts.length) {
                String fullText = texts[currentTextIndex];
                if (charIndex < fullText.length()) {
                    displayedText += fullText.charAt(charIndex);
                    charIndex++;
                    repaint();
                } else {
                    charTimer.stop();
                    // หน่วงเวลาให้นานขึ้นในจังหวะรอแม่ตอบ และจังหวะคนเก่งของมี้
                    int pauseTime = (currentTextIndex == 4 || currentTextIndex == 6) ? 3500 : 2200;

                    Timer pauseTimer = new Timer(pauseTime, ev -> {
                        currentTextIndex++;
                        if (currentTextIndex < texts.length) {
                            displayedText = "";
                            charIndex = 0;
                            charTimer.start();
                        } else {
                            goToFinalScene();
                        }
                        ((Timer)ev.getSource()).stop();
                    });
                    pauseTimer.start();
                }
            }
        });
        charTimer.start();
    }

    private void goToFinalScene() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame != null) {
            GamePanel nextGame = new GamePanel();
            nextGame.scene = 7;
            nextGame.playerName = this.savedName;
            nextGame.playerX = 50;
            topFrame.setContentPane(nextGame);
            topFrame.revalidate();
            topFrame.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // การตั้งค่าสีตามบทสนทนา
        if (currentTextIndex == 0) {
            g2.setColor(new Color(150, 255, 150)); // สีเขียวประกาศผล
        } else if (currentTextIndex == 3) {
            g2.setColor(new Color(150, 200, 255)); // สีฟ้า (เราส่งข้อความ)
        } else if (currentTextIndex == 5 || currentTextIndex == 6) {
            g2.setColor(new Color(255, 200, 200)); // สีชมพูอ่อน (แม่ตอบกลับมา)
        } else {
            g2.setColor(Color.WHITE);
        }

        g2.setFont(new Font("Tahoma", Font.BOLD, 22));
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(displayedText)) / 2;
        int y = getHeight() / 2;
        g2.drawString(displayedText, x, y);
    }
}