import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        JFrame window = new JFrame("Safe Little World");
        StartPanel startPanel = new StartPanel();

        window.add(startPanel);
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}