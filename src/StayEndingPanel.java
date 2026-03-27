import javax.swing.*;
import java.awt.*;

public class StayEndingPanel extends JPanel {
    int textY = 600;
    String name;
    String[] lines;
    Image backgroundImage;

    public StayEndingPanel(String playerName) {
        this.name = playerName;



        lines = new String[]{
                "ขอโทษนะ...",
                "ถ้าสิ่งที่ฉันกำลังเขียนต่อไปนี้ จะทำให้เธอร้องไห้",
                "เลิกกดดันตัวเอง เลิกคาดหวังตัวเองมากไปแล้วนะ",
                "เลิกโทษตัวเองในสิ่งที่ผิดพลาด",
                "เลิกเอาตัวเองไปเปรียบเทียบกับคนอื่น",
                "ทั้งที่เธอเก่งและมีความสามารถในแบบของเธอ",
                "หัดชมตัวเองในทุกความสำเร็จ แม้จะเล็กน้อย",
                "ฉันรู้ว่าเธอเหนื่อยและสู้มาตลอด",
                "เก่งมากนะ เธอทำได้ดีแล้ว",
                "เธอจะดีพอสำหรับตัวเองเสมอ",
                "เพิ่งเกิดมาครั้งแรก เพิ่งใช้ชีวิตครั้งแรก",
                "ถ้าจะพลาดบ้าง มันก็ไม่แปลกเลย",
                "อย่ากดดันตัวเองเลยนะ " + this.name
        };

        java.net.URL bgURL = getClass().getResource("/images/back6.gif");
        if (bgURL != null) {
            backgroundImage = new ImageIcon(bgURL).getImage();
        }

        Timer timer = new Timer(20, e -> {
            textY -= 1;
            if (textY < -800) {
                showRestartButton();
                ((Timer)e.getSource()).stop();
            }
            repaint();
        });
        timer.start();
    }

    private void showRestartButton() {
        this.setLayout(null); // ตั้งค่า Layout ก่อนเพิ่มปุ่ม
        JButton btn = new JButton("กลับสู่หน้าหลัก");
        btn.setBounds(300, 300, 200, 40);
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setFocusable(false);
        btn.addActionListener(e -> {
            MusicPlayer.stop(); // หยุดเพลงเมื่อกลับหน้าหลัก
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) {
                topFrame.setContentPane(new StartPanel());
                topFrame.revalidate();
            }
        });
        this.add(btn);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (backgroundImage != null) g2.drawImage(backgroundImage, 0, 0, 800, 600, this);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Leelawadee UI", Font.BOLD, 22));
        FontMetrics fm = g2.getFontMetrics();

        int currentY = textY;
        for (String line : lines) {
            int x = (800 - fm.stringWidth(line)) / 2;

            // ✨ ปรับสีตัวอักษร: ใช้เงาสีดำ และตัวหนังสือสีขาว จะทำให้อ่านง่ายกว่าบน GIF ครับ
            g2.setColor(new Color(0, 0, 0, 150)); // เงาสีดำ
            g2.drawString(line, x + 1, currentY + 1);

            g2.setColor(Color.WHITE); // ตัวหนังสือสีขาว
            g2.drawString(line, x, currentY);

            currentY += 50;
        }
    }
}