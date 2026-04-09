import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.net.URL;

public class MusicPlayer {
    public static Clip musicClip;

    public static void play(String path) {
        try {
            stop();
            URL url = MusicPlayer.class.getResource(path);
            if (url == null) return;
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            musicClip = AudioSystem.getClip();
            musicClip.open(audio);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }
    public static void playSFX(String path) {//เสียงเอฟเฟก แต่ไม่ได้ใช้555555555
        try {
            URL url = MusicPlayer.class.getResource(path);
            if (url == null) return;
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            Clip sfxClip = AudioSystem.getClip();
            sfxClip.open(audio);
            sfxClip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }


    public static void stop() {
        if (musicClip != null) {
            musicClip.stop();
            musicClip.close();
            musicClip = null;
        }
    }

    public static void fadeIn(String path, float targetVolume, int durationMs) {
        try {
            URL url = MusicPlayer.class.getResource(path);
            if (url == null) return;
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

                //ดึงค่าที่เครื่องนี้ยอมรับได้มาเช็คก่อน
                float maxAllowed = gainControl.getMaximum(); // ปกติประมาณ 6.0
                float minAllowed = gainControl.getMinimum(); // ปกติประมาณ -80.0

                // ⚠️ ป้องกันไม่ให้ตั้งค่าเกิน Max/Min ของเครื่อง
                final float finalTarget = Math.min(targetVolume, maxAllowed);
                final float startVol = Math.max(-40.0f, minAllowed);

                gainControl.setValue(startVol);
                clip.start();

                int steps = 25;
                int delay = durationMs / steps;
                final float increment = (finalTarget - startVol) / steps;

                Timer fadeInTimer = new Timer(delay, new java.awt.event.ActionListener() {
                    float currentVolume = startVol;
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        currentVolume += increment;

                        // ตรวจสอบความปลอดภัยก่อน setValue
                        if (currentVolume >= finalTarget) {
                            gainControl.setValue(finalTarget);
                            ((Timer)e.getSource()).stop();
                        } else if (currentVolume <= maxAllowed && currentVolume >= minAllowed) {
                            gainControl.setValue(currentVolume);
                        }
                    }
                });
                fadeInTimer.start();
            } else {
                clip.start(); // ถ้าเครื่องไม่รองรับการปรับเสียง ก็ให้เล่นไปเฉยๆ
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}