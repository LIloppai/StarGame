import javax.swing.*;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Enhanced Space Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            GamePanel gamePanel = new GamePanel();
            frame.add(gamePanel);
            
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}