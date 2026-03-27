import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.AlphaComposite;

public class GamePanel extends JPanel implements KeyListener {

    // --- Variables ---
    Image player, playerFront, playerBack;
    Image background1, background2, background3, background4, background5, background6, background7;
    Image cat, frog, giraffe, rabbit, turtle, capybara;

    float alpha = 0f;
    Timer fadeTimer;

    int playerX = 100, playerY = 300;
    int speed = 6;
    int scene = 1;

    // NPC Positions
    int catX = 600, catY = 250, frogX = 400, frogY = 350, giraffeX = 500, giraffeY = 150;
    int rabbitX = 350, rabbitY = 380, turtleX = 200, turtleY = 400, capybaraX = 500, capybaraY = 350;

    // State Flags
    boolean talking = false;
    boolean nearCat = false, nearFrog = false, nearGiraffe = false, nearRabbit = false, nearTurtle = false, nearCapybara = false;
    int questionIndex = 0;
    String playerName = "";

    // ระบบ Choice
    boolean showGiraffeChoice = false;
    int giraffeChoiceIndex = 0;
    boolean showEndChoice = false;
    int endChoiceIndex = 0;

    // ---------- บทสนทนา ----------
    String[] catDialogue = { "เรา: ที่นี่ที่ไหน?", "แมวส้ม: สวัสดี ฉันชื่อแมวส้ม", "แมวส้ม: คุณชื่ออะไรหรอ?", "แมวส้ม: ยินดีที่ได้รู้จักนะ ", "แมวส้ม: ตอนนี้เราอยู่ในโลกของความฝันนะ", "แมวส้ม: รู้ใช่ไหมว่าโลกความจริงมันโหดร้าย", "แมวส้ม: มาอยู่ที่นี่ต่อดีกว่า" };
    String[] frogDialogue = { "กบ: เธอยังกล้าสบตาตัวเองในกระจกอยู่ไหม?", "กบ: ทุกครั้งที่เธอบอกคนอื่นว่า 'ไม่เป็นไร'", "กบ: ข้างในนั้น มันแตกสลายไปจนนับชิ้นไม่ถ้วนแล้วใช่ไหม?", "กบ: ไหนลองบอกความจริงที่เธอ 'ขยะแขยง' ที่สุดมาสักเรื่องสิ", "กบ: หึ ก็น่าสมเพชดีนะ", "กบ: เดินต่อไปสิ ไปดูว่าความฝันนี้จะหลอกให้เธอมีความสุขได้นานแค่ไหน" };
    String[] giraffeDialogue = { "ยีราฟ: เดินมาไกลขนาดนี้ เพื่ออะไรกันล่ะ?", "ยีราฟ: มองลงมาจากที่สูงแบบนี้ ฉันเห็นแต่ความว่างเปล่าในแววตาเธอ", "ยีราฟ: " + playerName + " เธอยัง อยากเดินไปต่ออีกหรอ ? " };
    String[] rabbitDialogue = { "กระต่าย: ดอกไม้ที่นี่ มันสวยสู้ดอกไม้ในโลกความจริงของเธอได้ไหม?", "กระต่าย: ฉันเห็นเธอมองมาที่ฉัน เธออยากจะกอดฉันเหมือนตอนเด็กๆ หรือเปล่า?", "กระต่าย: " + playerName + " มี 'คำพูด' ไหนที่เธออยากได้ยินที่สุดไหม?", "กระต่าย: เป็นคำที่ฟังดูอบอุ่นจังเลยนะ", "กระต่าย: อยู่ที่นี่นานอีกหน่อยสิ ดอกไม้กำลังจะบานเพื่อเธอแล้วล่ะ" };
    String[] turtleDialogue = { "เต่า: จะรีบไปไหนหรอ?", "เต่า: ทุกคนที่วิ่งผ่านฉันไป สุดท้ายก็ต้องไปหยุดพักที่ไหนสักแห่งอยู่ดี", "เต่า: ปลายทางที่เธอพยายามวิ่งไปหาแทบตาย มันมีอยู่จริง หรือเธอแค่หลอกตัวเอง?", "เต่า: ค่อยๆ เดินเถอะ เพราะไม่ว่าจะถึงช้าหรือเร็ว ความเจ็บปวดก็รอเธออยู่เท่าเดิม" };
    String[] capyDialogue = {
            "คาปิบารา: เดินมาจนถึงจุดสิ้นสุดของความฝันแล้วนะ",
            "คาปิบารา: โลกข้างนอกนั่นใจร้ายกับเธอมากเลยใช่ไหม?",
            "คาปิบารา: " + playerName + "เธอแบกความเจ็บปวดมามากพอแล้ว",
            "คาปิบารา: จะหลับฝันอยู่ที่นี่ตลอดไป หรือจะตื่นไปเผชิญหน้ากับความจริงอีกครั้ง?"
    };

    String[] currentQuestions;
    JTextField answerField;

    public GamePanel() {
        setLayout(null); setFocusable(true); addKeyListener(this);
        SwingUtilities.invokeLater(() -> requestFocusInWindow());

        background1 = loadImage("/images/back1.jpg"); background2 = loadImage("/images/back2.png");
        background3 = loadImage("/images/back3.jpg"); background4 = loadImage("/images/back4.png");
        background5 = loadImage("/images/back5.gif"); background6 = loadImage("/images/back6.gif");
        background7 = loadImage("/images/back7 (2).jpg");

        playerFront = loadImage("/images/main front.png"); playerBack = loadImage("/images/main back.png");
        player = playerFront;
        cat = loadImage("/images/cat.gif"); frog = loadImage("/images/frog.gif");
        giraffe = loadImage("/images/giraffe.gif"); rabbit = loadImage("/images/rabbit.gif");
        turtle = loadImage("/images/turtle.gif"); capybara = loadImage("/images/capybara (2).gif");

        answerField = new JTextField();
        answerField.setBounds(250, 500, 300, 40);
        answerField.setFont(new Font("Tahoma", Font.PLAIN, 20));
        answerField.setVisible(false);
        add(answerField);

        answerField.addActionListener(e -> {
            if (scene == 1 && questionIndex == 2) {
                playerName = answerField.getText();
                catDialogue[3] = "แมวส้ม: ยินดีที่ได้รู้จักนะ " + playerName + "!";
                giraffeDialogue[2] = "ยีราฟ: " + playerName + "เธอยัง 'อยากเดินไปต่ออีกหรอ'?";
                rabbitDialogue[2] = "กระต่าย: " + playerName + " มี 'คำพูด' ไหนที่เธออยากได้ยินที่สุดไหม?";
                capyDialogue[2] = "คาปิบารา: " + playerName + " เธอแบกความเจ็บปวดมามากพอแล้ว";
                closeTextField();
            } else if (scene == 3 && questionIndex == 3) {
                answerField.setText(""); closeTextField();
            } else if (scene == 5 && questionIndex == 2) {
                String wish = answerField.getText();
                rabbitDialogue[3] = "กระต่าย: '" + wish + "' เป็นคำที่ฟังดูอบอุ่นจังเลยนะ";
                answerField.setText(""); closeTextField();
            }
        });
    }

    private void closeTextField() {
        answerField.setVisible(false); questionIndex++;
        this.requestFocusInWindow(); repaint();
    }

    private Image loadImage(String path) {
        java.net.URL url = getClass().getResource(path);
        return (url == null) ? null : new ImageIcon(url).getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (scene == 1) g.drawImage(background1, 0, 0, 800, 600, null);
        if (scene == 2) g.drawImage(background2, 0, 0, 800, 600, null);
        if (scene == 3) { g.drawImage(background3, 0, 0, 800, 600, null); g.setColor(new Color(20, 0, 40, 40)); g.fillRect(0, 0, 800, 600); }
        if (scene == 4) g.drawImage(background4, 0, 0, 800, 600, null);
        if (scene == 5) { g.drawImage(background5, 0, 0, 800, 600, null); g.setColor(new Color(255, 182, 193, 20)); g.fillRect(0, 0, 800, 600); }
        if (scene == 6) g.drawImage(background6, 0, 0, 800, 600, null);
        if (scene == 7) { g.drawImage(background7, 0, 0, 800, 600, null); g.setColor(new Color(100, 150, 200, 30)); g.fillRect(0, 0, 800, 600); }

        checkCollision();

        if (scene == 1) g.drawImage(cat, catX, catY, 100, 100, null);
        if (scene == 3) g.drawImage(frog, frogX, frogY, 80, 80, null);
        if (scene == 4) g.drawImage(giraffe, giraffeX, giraffeY, 150, 250, null);
        if (scene == 5) g.drawImage(rabbit, rabbitX, rabbitY, 80, 80, null);
        if (scene == 6) g.drawImage(turtle, turtleX, turtleY, 100, 60, null);
        if (scene == 7) g.drawImage(capybara, capybaraX, capybaraY, 120, 100, null);

        g.setColor(Color.WHITE);
        if (scene == 1 && nearCat && !talking) g.drawString("Press E to talk", catX, catY - 10);
        if (scene == 3 && nearFrog && !talking) g.drawString("Press E to talk", frogX, frogY - 10);
        if (scene == 4 && nearGiraffe && !talking) g.drawString("Press E to talk", giraffeX + 40, giraffeY - 10);
        if (scene == 5 && nearRabbit && !talking) g.drawString("Press E to talk", rabbitX, rabbitY - 10);
        if (scene == 6 && nearTurtle && !talking) g.drawString("Press E to talk", turtleX, turtleY - 10);
        if (scene == 7 && nearCapybara && !talking) g.drawString("Press E to talk", capybaraX, capybaraY - 10);

        g.drawImage(player, playerX, playerY, 150, 150, null);

        if (talking && currentQuestions != null && questionIndex < currentQuestions.length) {
            // วาดกล่องข้อความเหมือนเดิม
            g.setColor(new Color(0, 0, 0, 220));
            g.fillRect(50, 420, 700, 130);
            g.setColor(Color.WHITE);
            g.drawRect(50, 420, 700, 130);

            g.setFont(new Font("Leelawadee UI", Font.BOLD, 18)); // แนะนำฟอนต์นี้ สระสวยกว่า

            String text = currentQuestions[questionIndex];
            int x = 80;
            int y = 465;
            int maxWidth = 640; // ความกว้างสูงสุดที่ยอมให้ข้อความยาวได้ก่อนขึ้นบรรทัดใหม่

            // เรียกใช้ฟังก์ชันช่วยวาดข้อความแบบตัดคำ
            drawWrappedText(g, text, x, y, maxWidth);
        }


        if (showGiraffeChoice) {
            g.setColor(giraffeChoiceIndex == 0 ? Color.WHITE : new Color(50, 50, 50, 200)); g.fillRect(200, 250, 180, 50);
            g.setColor(giraffeChoiceIndex == 0 ? Color.BLACK : Color.WHITE); g.drawString("ไปต่อ", 265, 282);
            g.setColor(giraffeChoiceIndex == 1 ? Color.WHITE : new Color(50, 50, 50, 200)); g.fillRect(420, 250, 180, 50);
            g.setColor(giraffeChoiceIndex == 1 ? Color.BLACK : Color.WHITE); g.drawString("เหนื่อยแล้ว", 460, 282);
        }

        if (showEndChoice) {
            g.setFont(new Font("Tahoma", Font.BOLD, 20));
            g.setColor(endChoiceIndex == 0 ? Color.WHITE : new Color(30, 30, 30, 220)); g.fillRect(200, 250, 180, 60);
            g.setColor(endChoiceIndex == 0 ? Color.BLACK : Color.WHITE); g.drawString("หลับฝันต่อไป", 235, 287);
            g.setColor(endChoiceIndex == 1 ? Color.WHITE : new Color(30, 30, 30, 220)); g.fillRect(420, 250, 180, 60);
            g.setColor(endChoiceIndex == 1 ? Color.BLACK : Color.WHITE); g.drawString("ออกจากฝัน", 460, 287);
        }

        Graphics2D g2d = (Graphics2D) g;
        float curAlpha = Math.min(1.0f, Math.max(0.0f, alpha));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, curAlpha));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 800, 600);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        if (!talking) {
            g2d.setColor(new Color(0, 0, 0, 100));//เงา
            g2d.fillRoundRect(10, 10, 250, 40, 10, 10);
            g2d.setColor(Color.WHITE);

            g2d.drawString("control: [↑][↓][←][→] Move | [E] Talk", 25, 35);
        }
    }
    private void drawWrappedText(Graphics g, String text, int x, int y, int maxWidth) {
        FontMetrics fm = g.getFontMetrics();
        String[] words = text.split(" "); // แยกด้วยช่องว่าง (ถ้ามี)
        StringBuilder line = new StringBuilder();

        // สำหรับภาษาไทยที่ไม่มีช่องว่าง เราจะเช็คทีละตัวอักษร
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            line.append(c);

            // ถ้าความกว้างของบรรทัดปัจจุบัน เกิน maxWidth ให้วาดแล้วขึ้นบรรทัดใหม่
            if (fm.stringWidth(line.toString()) > maxWidth) {
                // ถอยกลับไป 1 ตัวอักษรเพื่อวาด
                line.setLength(line.length() - 1);
                g.drawString(line.toString(), x, y);

                line.setLength(0); // ล้างบรรทัดเก่า
                line.append(c);    // เอาตัวที่เกินมาเริ่มบรรทัดใหม่
                y += fm.getHeight() + 5; // ขยับ Y ลงมา (ระยะห่างบรรทัด)
            }
        }
        // วาดเศษที่เหลือบรรทัดสุดท้าย
        g.drawString(line.toString(), x, y);
    }

    private void checkCollision() {
        Rectangle pRect = new Rectangle(playerX + 50, playerY + 50, 50, 50);
        if (scene == 1) nearCat = pRect.intersects(new Rectangle(catX, catY, 100, 100));
        if (scene == 3) nearFrog = pRect.intersects(new Rectangle(frogX, frogY, 80, 80));
        if (scene == 4) nearGiraffe = pRect.intersects(new Rectangle(giraffeX, giraffeY, 150, 250));
        if (scene == 5) nearRabbit = pRect.intersects(new Rectangle(rabbitX, rabbitY, 80, 80));
        if (scene == 6) nearTurtle = pRect.intersects(new Rectangle(turtleX, turtleY, 100, 60));
        if (scene == 7) nearCapybara = pRect.intersects(new Rectangle(capybaraX, capybaraY, 120, 100));
    }

    private void transitionToNextScene() {
        if (fadeTimer != null && fadeTimer.isRunning()) return;
        fadeTimer = new Timer(20, e -> {
            alpha += 0.05f;
            if (alpha >= 1f) {
                alpha = 1f; scene++; playerX = 50;
                ((Timer)e.getSource()).stop();
                Timer fadeIn = new Timer(20, ev -> {
                    alpha -= 0.05f;
                    if (alpha <= 0f) { alpha = 0f; ((Timer)ev.getSource()).stop(); }
                    repaint();
                });
                fadeIn.start();
            }
            repaint();
        });
        fadeTimer.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // --- 1. จัดการเรื่อง Choice และการข้าม (Skip) ---
        if (showGiraffeChoice) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) giraffeChoiceIndex = 0;
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) giraffeChoiceIndex = 1;
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                showGiraffeChoice = false;
                if (giraffeChoiceIndex == 1) {
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    topFrame.setContentPane(new StartPanel()); topFrame.revalidate(); topFrame.repaint();
                } else {
                    currentQuestions = new String[]{"ยีราฟ: งั้นก็จงแบกความเหนื่อยล้านั่นเดินต่อไป"};
                    questionIndex = 0;
                }
            }
            repaint(); return;
        }

        if (showEndChoice) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) endChoiceIndex = 0;
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) endChoiceIndex = 1;
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                showEndChoice = false; talking = false;
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                if (topFrame != null) {
                    MusicPlayer.stop();
                    if (endChoiceIndex == 0) topFrame.setContentPane(new BackEndingPanel());
                    else topFrame.setContentPane(new VideoPanel(playerName));
                    topFrame.revalidate(); topFrame.repaint();
                }
            }
            repaint(); return;
        }

        // --- 2. จัดการบทสนทนา (Enter เพื่อเลื่อน / S เพื่อ Skip) ---
        if (talking) {
            // ปุ่ม S สำหรับ Skip
            if (e.getKeyCode() == KeyEvent.VK_S) {
                // ถ้าเป็นฉากแมวและยังไม่มีชื่อ ห้าม Skip (ต้องให้พิมพ์ชื่อก่อน)
                if (scene == 1 && playerName.isEmpty()) {
                    questionIndex = 2; // กระโดดไปช็อตถามชื่อเลย
                    answerField.setVisible(true);
                    answerField.requestFocusInWindow();
                } else {
                    talking = false;
                    handleSkipToNextScene(); // เรียกฟังก์ชันข้ามฉาก
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (scene == 1 && questionIndex == 2) {
                    answerField.setVisible(true); answerField.requestFocusInWindow();
                } else if (scene == 3 && questionIndex == 3) {
                    answerField.setVisible(true); answerField.requestFocusInWindow();
                } else if (scene == 5 && questionIndex == 2) {
                    answerField.setVisible(true); answerField.requestFocusInWindow();
                } else if (scene == 4 && questionIndex == 2 && !showGiraffeChoice) {
                    showGiraffeChoice = true;
                } else if (scene == 7 && questionIndex == 3 && !showEndChoice) {
                    showEndChoice = true;
                } else {
                    questionIndex++;
                    if (questionIndex >= currentQuestions.length) {
                        talking = false;
                        handleSkipToNextScene(); // จบการสนทนาปกติ
                    }
                }
            }
            repaint();
            return; // คุยอยู่ห้ามเดินต่อ
        }

        // --- 3. ระบบการเคลื่อนที่และล็อคขอบจอ ---
        int nextX = playerX;
        int nextY = playerY;

        if (e.getKeyCode() == KeyEvent.VK_UP) { nextY -= speed; player = playerBack; }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) { nextY += speed; player = playerFront; }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) { nextX -= speed; }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) { nextX += speed; }

        // กฎการล็อคขอบ (Boundary)
        if (nextX < 0) nextX = 0;
        if (nextY < 200) nextY = 200;
        if (nextY > 450) nextY = 450;

        if (nextX > 750) {
            if (scene < 7) {
                transitionToNextScene();
                return;
            } else {
                nextX = 750;
            }
        }

        playerX = nextX;
        playerY = nextY;

        // --- 4. ปุ่ม E เพื่อเริ่มคุย ---
        if (e.getKeyCode() == KeyEvent.VK_E) {
            if (nearCat && scene == 1) { talking = true; currentQuestions = catDialogue; questionIndex = 0; }
            else if (nearFrog && scene == 3) { talking = true; currentQuestions = frogDialogue; questionIndex = 0; }
            else if (nearGiraffe && scene == 4) { talking = true; currentQuestions = giraffeDialogue; questionIndex = 0; }
            else if (nearRabbit && scene == 5) { talking = true; currentQuestions = rabbitDialogue; questionIndex = 0; }
            else if (nearTurtle && scene == 6) { talking = true; currentQuestions = turtleDialogue; questionIndex = 0; }
            else if (nearCapybara && scene == 7) { talking = true; currentQuestions = capyDialogue; questionIndex = 0; }
        }
        repaint();
    }

    // ฟังก์ชันช่วยย้ายฉาก (เรียกใช้ตอนคุยจบ หรือ กด Skip)
    private void handleSkipToNextScene() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame == null) return;

        if (scene == 1) topFrame.setContentPane(new MemoryPanel(this.playerName));
        else if (scene == 3) topFrame.setContentPane(new StepMotherPanel(this.playerName));
        else if (scene == 4) topFrame.setContentPane(new PhoneCallPanel(this.playerName));
        else if (scene == 5) topFrame.setContentPane(new HospitalMemoryPanel(this.playerName));
        else if (scene == 6) topFrame.setContentPane(new UniversityResultPanel(this.playerName));

        topFrame.revalidate();
        topFrame.repaint();
    }
    private void handleDialogueEnd() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame == null) return;

        // เช็คว่าตอนนี้อยู่ฉากไหน แล้วส่งไปฉากถัดไปตามลำดับ
        if (scene == 1) {
            topFrame.setContentPane(new MemoryPanel(this.playerName));
        } else if (scene == 3) {
            topFrame.setContentPane(new StepMotherPanel(this.playerName));
        } else if (scene == 4) {
            topFrame.setContentPane(new PhoneCallPanel(this.playerName));
        } else if (scene == 5) {
            topFrame.setContentPane(new HospitalMemoryPanel(this.playerName));
        } else if (scene == 6) {
            topFrame.setContentPane(new UniversityResultPanel(this.playerName));
        }

        topFrame.revalidate();
        topFrame.repaint();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}