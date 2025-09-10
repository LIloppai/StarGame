import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy {
    private int x, y;
    private int row;
    private int direction = 1;
    private int speed = 1;
    
    public Enemy(int x, int y, int row) {
        this.x = x;
        this.y = y;
        this.row = row;
    }
    
    public void update(int screenWidth) {
        x += direction * speed;
        
        // Change direction if hitting edges
        if (x < 30 || x > screenWidth - 30) {
            direction *= -1;
            y += 20;
        }
    }
    
    public void draw(Graphics2D g2d, BufferedImage img) {
        if (img != null) {
            g2d.drawImage(img, x - 15, y - 15, null);
        } else {
            // Fallback drawing if image is null
            g2d.setColor(Color.RED);
            g2d.fillRect(x - 15, y - 10, 30, 20);
        }
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getRow() {
        return row;
    }
}