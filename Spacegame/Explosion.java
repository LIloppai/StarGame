import java.awt.*;
import java.awt.Composite;

public class Explosion {
    private int x, y;
    private int radius = 5;
    private int maxRadius = 30;
    private boolean growing = true;
    
    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void update() {
        if (growing) {
            radius += 2;
            if (radius >= maxRadius) {
                growing = false;
            }
        } else {
            radius -= 2;
        }
    }
    
    public void draw(Graphics2D g2d) {
        float alpha = (float) radius / maxRadius;
        if (!growing) alpha = 1 - alpha;
        
        Composite originalComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(Color.ORANGE);
        g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(x - radius/2, y - radius/2, radius, radius);
        g2d.setComposite(originalComposite);
    }
    
    public boolean isFinished() {
        return !growing && radius <= 0;
    }
}