import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StepMotherPanel extends JPanel {
    private String savedName;
    private String[] texts = {
            "พ่อ : พ่อต้องไปทำงานต่างประเทศก่อนนะ อยู่คนเดียวได้ไหม ? ",
            "เรา : (หนูเลือกอะไรได้ด้วยหรอ)",
            "แม่เลี้ยง : นี่กูต้องไปรับไปส่งมึงที่โรงเรียนทุกวันเลยนะ ! ",
            "แม่เลี้ยง : ตั้งแต่มาอยู่เนี่ย มึงเคยเรียกกูว่าแม่สักคำไหม ? ",
            "แม่เลี้ยง : วันๆ เอาแต่นอนตายอยู่ในห้อง ไม่หยิบจับทำอะไรสักอย่าง!",

    };
    private int currentTextIndex = 0;
    private String displayedText = "";
    private int charIndex = 0;
    private Timer charTimer;
    private JButton skipBtn;
    public StepMotherPanel(String name) {
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
            goToScene4();
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
                    Timer pauseTimer = new Timer(2000, ev -> {
                        currentTextIndex++;
                        if (currentTextIndex < texts.length) {
                            displayedText = "";
                            charIndex = 0;
                            charTimer.start();
                        } else {
                            goToScene4();
                        }
                        ((Timer)ev.getSource()).stop();
                    });
                    pauseTimer.start();
                }
            }
        });
        charTimer.start();
    }

    private void goToScene4() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame != null) {
            GamePanel nextGame = new GamePanel();
            nextGame.scene = 4; // ไปหายีราฟ
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

        // ✨ เช็คว่าถ้าประโยคขึ้นต้นด้วย "แม่เลี้ยง" ให้ใช้สีแดง
        if (currentTextIndex < texts.length && texts[currentTextIndex].startsWith("แม่เลี้ยง")) {
            g2.setColor(new Color(255, 50, 50)); // สีแดง
        } else {
            g2.setColor(Color.WHITE); // พ่อกับเราใช้สีขาวปกติ
        }

        g2.setFont(new Font("Tahoma", Font.BOLD, 22));
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(displayedText)) / 2;
        int y = getHeight() / 2;
        g2.drawString(displayedText, x, y);
    }
}