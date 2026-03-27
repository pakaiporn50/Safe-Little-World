import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class HospitalMemoryPanel extends JPanel {
    private String savedName;
    private String[] texts = {
            "(เสียงเครื่องวัดหัวใจดังสม่ำเสมอ... ติ๊ด... ติ๊ด...)",
            "เรา : สุดท้ายก็มานอนโรงบาลคนเดียว",
            "เรา : ต้องเข้ามหาลัยให้ได้",
            "เรา : (น้ำตาค่อยๆ ไหล)",
            "เรา : สำลี มาหาเราหน่อย",
            "เรา : (หมาชื่อสำลี สัญญานะจะพยายามดูแลสำลีตลอด)",
            "เรา : เราขอโทษที่ดูแลแกได้ไม่ดี เราขอโทษ แกอดทนหน่อยนะเดี๋ยวก็หาย"
    };
    private int currentTextIndex = 0;
    private String displayedText = "";
    private int charIndex = 0;
    private Timer charTimer;
    private JButton skipBtn;

    public HospitalMemoryPanel(String name) {
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
            goToScene6();
        });

        add(skipBtn); // เพิ่มปุ่มลงในหน้าจอ

        charTimer = new Timer(100, e -> {
            if (currentTextIndex < texts.length) {
                String fullText = texts[currentTextIndex];
                if (charIndex < fullText.length()) {
                    displayedText += fullText.charAt(charIndex);
                    charIndex++;
                    repaint();
                } else {
                    charTimer.stop();
                    int pauseTime = texts[currentTextIndex].contains("คนเดียว") ? 2500 : 2000;

                    Timer pauseTimer = new Timer(pauseTime, ev -> {
                        currentTextIndex++;
                        if (currentTextIndex < texts.length) {
                            displayedText = "";
                            charIndex = 0;
                            charTimer.start();
                        } else {
                            goToScene6();
                        }
                        ((Timer)ev.getSource()).stop();
                    });
                    pauseTimer.start();
                }
            }
        });
        charTimer.start();
    }


    private void goToScene6() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame != null) {
            GamePanel nextGame = new GamePanel();
            nextGame.scene = 6; // ไปหาเต่า
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

        // เน้นประโยค 'คนเดียว' ให้เด่นขึ้นด้วยสีเทาจางๆ สื่อถึงความอ้างว้าง
        if (currentTextIndex < texts.length && texts[currentTextIndex].contains("คนเดียว")) {
            g2.setColor(new Color(180, 180, 180));
        } else if (currentTextIndex < texts.length && texts[currentTextIndex].contains("สำลี")) {
            g2.setColor(new Color(200, 230, 255)); // สีฟ้าอ่อนตอนนึกถึงสำลี
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