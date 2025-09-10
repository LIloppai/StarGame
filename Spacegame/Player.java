import java.awt.*;
import java.awt.image.BufferedImage;

public class Player {
    private int x, y;
    private int speed = 6;
    
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void moveLeft() {
        if (x > 20) {
            x -= speed;
        }
    }
    
    public void moveRight(int screenWidth) {
        if (x < screenWidth - 20) {
            x += speed;
        }
    }
    
    public void draw(Graphics2D g2d, BufferedImage img) {
        if (img != null) {
            g2d.drawImage(img, x - 20, y - 15, null);
        } else {
            // Fallback drawing if image is null
            g2d.setColor(Color.GREEN);
            g2d.fillRect(x - 15, y - 10, 30, 20);
        }
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}