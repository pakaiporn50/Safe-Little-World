import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PhoneCallPanel extends JPanel {
    private String savedName;
    private String[] texts = {
            "(เสียงสัญญาณโทรศัพท์... ตู๊ด... ตู๊ด...)",
            "พ่อ : พ่อเป็นคนกลางนะ ลูกต้องเข้าใจพ่อด้วย",
            "พ่อ : พ่อช่วยอะไรมากไม่ได้หรอก อยู่กับน้าเขาไปก่อนนะ",
            "(เงียบไปพักใหญ่ ก่อนจะกดโทรหาอีกเบอร์)",
            "แม่ : แม่ขอโทษนะ แม่ช่วยลูกได้แค่นี้จริงๆ",
            "แม่ : อดทนหน่อยนะลูก ไว้แม่พร้อมกว่านี้แม่จะมารับนะ",

    };
    private int currentTextIndex = 0;
    private String displayedText = "";
    private int charIndex = 0;
    private Timer charTimer;
    private JButton skipBtn;
    public PhoneCallPanel(String name) {
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
            goToScene5();
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
                    Timer pauseTimer = new Timer(2000, ev -> {
                        currentTextIndex++;
                        if (currentTextIndex < texts.length) {
                            displayedText = "";
                            charIndex = 0;
                            charTimer.start();
                        } else {
                            goToScene5(); // จบแล้วไปหา กระต่าย (ฉาก 5)
                        }
                        ((Timer)ev.getSource()).stop();
                    });
                    pauseTimer.start();
                }
            }
        });
        charTimer.start();
    }

    private void goToScene5() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame != null) {
            GamePanel nextGame = new GamePanel();
            nextGame.scene = 5; // ตั้งค่าให้ไปฉากที่ 5 (กระต่าย)
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

        // ใช้สีขาวนวลๆ สื่อถึงความเศร้าและผิดหวัง
        g2.setColor(new Color(220, 220, 220));
        g2.setFont(new Font("Tahoma", Font.BOLD, 22));

        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(displayedText)) / 2;
        int y = getHeight() / 2;
        g2.drawString(displayedText, x, y);
    }
}