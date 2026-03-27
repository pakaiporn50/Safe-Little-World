import javax.swing.*;
import java.awt.*;

public class BackEndingPanel extends JPanel {

    int textY = 600;
    String[] lines = {
            "คุณไม่ตื่นแล้วนะ",
            "ชีวิตที่ผ่านมามันจบลงที่นี่",
            "ถ้ายังมีความเป็นไปได้อื่นรออยู่ข้างนอกนั่นล่ะ?",
            "คงจะดีถ้าแกไม่ต้องรู้สึกเจ็บปวดแบบนี้อีก",
            "หลับฝันให้สบายในโลกที่ไม่มีจริงนี้เถอะ"
    };

    Image currentGif;
    Timer animTimer;
    boolean videoChanged = false;

    public BackEndingPanel() {
        setPreferredSize(new Dimension(800, 600));
        setLayout(null);
        setBackground(Color.BLACK);

        // 🎵 1. เริ่มเล่นเพลงฉากจบเพียงเพลงเดียว (Fade In ให้ดูละมุน)
        MusicPlayer.stop(); // หยุดเพลงจากฉากก่อนหน้า
        MusicPlayer.fadeIn("/images/Sadsong.wav", -10.0f, 4000);

        // 🎬 2. โหลดวิดีโอแรก
        loadVideo("/images/Endsad.gif");

        // ⏳ 3. Timer สำหรับตัวหนังสือลอย และเช็คจังหวะเปลี่ยนวิดีโอ
        animTimer = new Timer(30, e -> {
            textY -= 1;

            // ✨ เปลี่ยนแค่วิดีโอ แต่เพลงเดิมยังเล่นต่อเนิ่นนาน...
            if (textY < 250 && !videoChanged) {
                videoChanged = true;
                loadVideo("/images/Endsad2.gif"); // เปลี่ยนเป็นวิดีโอตัวที่สอง
            }

            if (textY < -400) {
                animTimer.stop();
                showRestartButton();
            }
            repaint();
        });
        animTimer.start();
    }

    private void loadVideo(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            currentGif = new ImageIcon(imgURL).getImage();
        }
        repaint();
    }

    private void showRestartButton() {
        JButton restartBtn = new JButton("กลับไปเริ่มต้นใหม่");
        restartBtn.setBounds(300, 300, 200, 40);
        restartBtn.setFocusable(false);
        restartBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        restartBtn.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) {
                MusicPlayer.stop(); // หยุดเพลงเมื่อกดกลับหน้าหลัก
                topFrame.setContentPane(new StartPanel());
                topFrame.revalidate();
            }
        });
        this.add(restartBtn);
        this.revalidate();
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // วาด GIF ปัจจุบัน
        if (currentGif != null) {
            g2.drawImage(currentGif, 0, 0, getWidth(), getHeight(), this);
        }

        // วาด Credit Roll
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Tahoma", Font.BOLD, 26));
        FontMetrics fm = g2.getFontMetrics();
        int currentY = textY;

        for (String line : lines) {
            int textWidth = fm.stringWidth(line);
            int x = (getWidth() - textWidth) / 2;

            g2.setColor(new Color(0, 0, 0, 180));
            g2.drawString(line, x + 2, currentY + 2);

            g2.setColor(Color.WHITE);
            g2.drawString(line, x, currentY);
            currentY += 60;
        }
    }
}