import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet {
    private int x, y;
    private boolean isPlayerBullet;
    private int speed = 10;
    private BufferedImage bulletImg;
    private BufferedImage enemyBulletImg;
    
    public Bullet(int x, int y, boolean isPlayerBullet, BufferedImage bulletImg, BufferedImage enemyBulletImg) {
        this.x = x;
        this.y = y;
        this.isPlayerBullet = isPlayerBullet;
        this.bulletImg = bulletImg;
        this.enemyBulletImg = enemyBulletImg;
    }
    
    public void update() {
        if (isPlayerBullet) {
            y -= speed;
        } else {
            y += speed/2;
        }
    }
    
    public void draw(Graphics2D g2d) {
        if (isPlayerBullet && bulletImg != null) {
            g2d.drawImage(bulletImg, x - 2, y - 12, null);
        } else if (!isPlayerBullet && enemyBulletImg != null) {
            g2d.drawImage(enemyBulletImg, x - 2, y - 12, null);
        } else {
            // Fallback drawing if images are null
            g2d.setColor(isPlayerBullet ? Color.CYAN : Color.RED);
            g2d.fillRect(x - 2, y - 6, 4, 12);
        }
    }
    
    public boolean isPlayerBullet() {
        return isPlayerBullet;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}