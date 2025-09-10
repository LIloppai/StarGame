import java.awt.*;

public class Star {
    private int x, y;
    private int size;
    private Color color;
    private int speed;
    
    public Star(int x, int y, int size, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
        this.speed = size; // Bigger stars move faster
    }
    
    public void update() {
        y += speed;
    }
    
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval(x, y, size, size);
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
}