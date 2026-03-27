import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MemoryPanel extends JPanel {
    private String savedName; // สำหรับเก็บชื่อที่ส่งมาจาก GamePanel
    private String[] texts = {
            "พ่อ : มึงทำได้แค่นี้หรอว่ะ ! ",
            "(เสียงฟาดหนังสือโดนหน้าอย่างแรง)",
            "เรา : (นิ่ง)"
    };
    private int currentTextIndex = 0;
    private String displayedText = "";
    private int charIndex = 0;
    private Timer charTimer;
    private JButton skipBtn;

    // แก้ Constructor ให้รับชื่อมาด้วย
    public MemoryPanel(String name) {
        this.savedName = name;
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
            goToScene2();
        });

        add(skipBtn); // เพิ่มปุ่มลงในหน้าจอ
        charTimer = new Timer(80, e -> {
            if (currentTextIndex < texts.length) {
                String fullText = introTexts(); // เรียกใช้ข้อความตามลำดับ
                if (charIndex < fullText.length()) {
                    displayedText += fullText.charAt(charIndex);
                    charIndex++;
                    repaint();
                } else {
                    charTimer.stop();
                    Timer pauseTimer = new Timer(1500, ev -> {
                        currentTextIndex++;
                        if (currentTextIndex < texts.length) {
                            displayedText = "";
                            charIndex = 0;
                            charTimer.start();
                        } else {
                            goToScene2();
                        }
                        ((Timer)ev.getSource()).stop();
                    });
                    pauseTimer.start();
                }
            }
        });
        charTimer.start();
    }

    // Helper สำหรับดึงข้อความ
    private String introTexts() {
        return texts[currentTextIndex];
    }

    private void goToScene2() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame != null) {
            GamePanel nextGame = new GamePanel();
            nextGame.scene = 3; // ไปหาฉากกบ (ฉากที่ 3)
            nextGame.playerName = this.savedName; // ส่งชื่อกลับไปด้วย ชื่อจะได้ไม่หาย
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
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Tahoma", Font.BOLD, 24));
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(displayedText)) / 2;
        int y = getHeight() / 2;
        g2.drawString(displayedText, x, y);
    }
}