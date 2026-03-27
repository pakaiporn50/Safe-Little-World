import javax.swing.*;
import java.awt.*;

public class VideoPanel extends JPanel {
    String name;
    JLabel label;

    public VideoPanel(String playerName) {
        this.name = playerName;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // 🎬 1. เริ่มต้น: แสดงแอนิเมชันอันแรก (Endgood.gif) แบบเต็มจอ 800x600
        java.net.URL img1 = getClass().getResource("/images/Endgood.gif");
        if (img1 != null) {
            // ใช้ ImageIcon ตรงๆ จาก URL เลย ไม่ต้องผ่าน Image scaled
            label = new JLabel(new ImageIcon(img1));
            add(label, BorderLayout.CENTER);
        }
        // ⏳ 2. ตั้งเวลาเปลี่ยนจากคลิปที่ 1 เป็นคลิปที่ 2 (สมมติ 5 วินาที)
        Timer timer1 = new Timer(8000, e -> {

            // 🎵 3. หยุดเพลงเก่า และรอ 0.8 วินาทีค่อยเริ่มเพลงใหม่ (ตามที่คุณตั้งไว้)
            MusicPlayer.stop();
            Timer songDelay = new Timer(1000, ev -> {
                MusicPlayer.play("/images/goodsong.wav");
                ((Timer)ev.getSource()).stop();
            });
            songDelay.start();

            // 🎬 4. เปลี่ยนเป็นคลิปที่ 2 (Endgood2.gif) แบบเต็มจอ 800x600
            java.net.URL img2 = getClass().getResource("/images/Endgood2.gif");
            if (img2 != null) {
                ImageIcon icon2 = new ImageIcon(img2);
                Image scaled2 = icon2.getImage().getScaledInstance(800, 600, Image.SCALE_DEFAULT);
                label.setIcon(new ImageIcon(scaled2));
            }

            revalidate();
            repaint();

            // ⏳ 5. ตั้งเวลาสุดท้าย: พอคลิปที่สองเล่นจบ (42 วินาที) ให้ไปหน้าจบ
            Timer timer2 = new Timer(42000, ev -> {
                MusicPlayer.stop(); // หยุดเพลงตอนจบเกม
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                if (topFrame != null) {
                    topFrame.setContentPane(new StayEndingPanel(this.name));
                    topFrame.revalidate();
                    topFrame.repaint();
                }
            });
            timer2.setRepeats(false);
            timer2.start();

            ((Timer)e.getSource()).stop(); // หยุด Timer ตัวแรก
        });

        timer1.setRepeats(false);
        timer1.start();
    }
}