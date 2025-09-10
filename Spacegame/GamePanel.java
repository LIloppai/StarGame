import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    // Game components
    private Timer timer;
    private Player player;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private List<Explosion> explosions;
    private List<Star> stars;
    private int score = 0;
    private int highScore = 0;
    private int lives = 3;
    private Random random;
    
    // Game states
    public enum GameState { MENU, PLAYING, GAME_OVER, PAUSED }
    private GameState gameState = GameState.MENU;
    
    // Images
    private BufferedImage playerImg, enemyImg, bulletImg, enemyBulletImg;
    
    // Colors
    private Color[] starColors = {Color.WHITE, Color.CYAN, Color.YELLOW, Color.LIGHT_GRAY};
    
    // Fonts
    private Font titleFont, scoreFont, menuFont;
    
    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        
        // Initialize game objects
        player = new Player(400, 500);
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        explosions = new ArrayList<>();
        stars = new ArrayList<>();
        random = new Random();
        
        // Create images
        createImages();
        
        // Create fonts
        titleFont = new Font("Arial", Font.BOLD, 48);
        scoreFont = new Font("Arial", Font.BOLD, 20);
        menuFont = new Font("Arial", Font.PLAIN, 24);
        
        // Create background stars
        for (int i = 0; i < 100; i++) {
            stars.add(new Star(random.nextInt(800), random.nextInt(600), 
                              random.nextInt(3) + 1, 
                              starColors[random.nextInt(starColors.length)]));
        }
        
        createEnemies();
        
        timer = new Timer(16, this);
        timer.start();
    }
    
    private void createImages() {
        // Create player ship
        playerImg = new BufferedImage(40, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = playerImg.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw a sleek spaceship
        int[] xPoints = {20, 0, 40, 20};
        int[] yPoints = {0, 30, 30, 0};
        g2d.setColor(Color.CYAN);
        g2d.fillPolygon(xPoints, yPoints, 4);
        g2d.setColor(Color.WHITE);
        g2d.drawPolygon(xPoints, yPoints, 4);
        
        // Draw engine glow
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(15, 25, 10, 10);
        g2d.dispose();
        
        // Create enemy ship
        enemyImg = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        g2d = enemyImg.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(Color.RED);
        g2d.fillOval(0, 0, 30, 30);
        g2d.setColor(Color.ORANGE);
        g2d.fillOval(5, 5, 20, 20);
        g2d.dispose();
        
        // Create bullets
        bulletImg = new BufferedImage(4, 12, BufferedImage.TYPE_INT_ARGB);
        g2d = bulletImg.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(Color.CYAN);
        g2d.fillRect(0, 0, 4, 12);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(1, 1, 2, 10);
        g2d.dispose();
        
        enemyBulletImg = new BufferedImage(4, 12, BufferedImage.TYPE_INT_ARGB);
        g2d = enemyBulletImg.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, 4, 12);
        g2d.setColor(Color.ORANGE);
        g2d.fillRect(1, 1, 2, 10);
        g2d.dispose();
    }
    
    private void createEnemies() {
        enemies.clear();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 10; col++) {
                enemies.add(new Enemy(50 + col * 70, 50 + row * 60, row));
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw background stars
        for (Star star : stars) {
            star.draw(g2d);
        }
        
        // Draw game elements based on state
        switch (gameState) {
            case MENU:
                drawMenu(g2d);
                break;
            case PLAYING:
            case PAUSED:
                drawGame(g2d);
                if (gameState == GameState.PAUSED) {
                    drawPauseScreen(g2d);
                }
                break;
            case GAME_OVER:
                drawGame(g2d);
                drawGameOverScreen(g2d);
                break;
        }
    }
    
    private void drawMenu(Graphics2D g2d) {
        // Draw title
        g2d.setColor(Color.CYAN);
        g2d.setFont(titleFont);
        String title = "SPACE DEFENDERS";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (getWidth() - titleWidth) / 2, 150);
        
        // Draw menu options
        g2d.setColor(Color.WHITE);
        g2d.setFont(menuFont);
        g2d.drawString("Press ENTER to Start", 300, 250);
        g2d.drawString("Press H for Help", 300, 300);
        g2d.drawString("High Score: " + highScore, 300, 350);
    }
    
    private void drawGame(Graphics2D g2d) {
        // Draw player
        if (playerImg != null) {
            player.draw(g2d, playerImg);
        }
        
        // Draw bullets
        for (Bullet bullet : bullets) {
            if (bulletImg != null && enemyBulletImg != null) {
                bullet.draw(g2d);
            }
        }
        
        // Draw enemies
        for (Enemy enemy : enemies) {
            if (enemyImg != null) {
                enemy.draw(g2d, enemyImg);
            }
        }
        
        // Draw explosions
        for (int i = 0; i < explosions.size(); i++) {
            Explosion explosion = explosions.get(i);
            explosion.draw(g2d);
            if (explosion.isFinished()) {
                explosions.remove(i);
                i--;
            }
        }
        
        // Draw UI
        drawUI(g2d);
    }
    
    private void drawUI(Graphics2D g2d) {
        // Draw score
        g2d.setColor(Color.WHITE);
        g2d.setFont(scoreFont);
        g2d.drawString("Score: " + score, 20, 30);
        
        // Draw lives
        g2d.drawString("Lives: " + lives, 20, 60);
        
        // Draw high score
        g2d.drawString("High Score: " + highScore, 650, 30);
    }
    
    private void drawPauseScreen(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.setColor(Color.YELLOW);
        g2d.setFont(titleFont);
        String pauseText = "GAME PAUSED";
        int textWidth = g2d.getFontMetrics().stringWidth(pauseText);
        g2d.drawString(pauseText, (getWidth() - textWidth) / 2, 300);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(menuFont);
        g2d.drawString("Press P to Resume", 300, 350);
    }
    
    private void drawGameOverScreen(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.setColor(Color.RED);
        g2d.setFont(titleFont);
        String gameOverText = "GAME OVER";
        int textWidth = g2d.getFontMetrics().stringWidth(gameOverText);
        g2d.drawString(gameOverText, (getWidth() - textWidth) / 2, 250);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(menuFont);
        g2d.drawString("Final Score: " + score, 320, 300);
        g2d.drawString("High Score: " + highScore, 320, 330);
        g2d.drawString("Press R to Restart", 320, 380);
        g2d.drawString("Press M for Menu", 320, 410);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameState != GameState.PLAYING) return;
        
        updateStars();
        updateBullets();
        updateEnemies();
        updateExplosions();
        checkCollisions();
        checkGameState();
        
        repaint();
    }
    
    private void updateStars() {
        for (Star star : stars) {
            star.update();
            if (star.getY() > getHeight()) {
                star.setY(0);
                star.setX(random.nextInt(getWidth()));
            }
        }
    }
    
    private void updateBullets() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.update();
            
            // Remove bullets that go off screen
            if (bullet.getY() < -20 || bullet.getY() > getHeight() + 20) {
                bullets.remove(i);
                i--;
            }
        }
    }
    
    private void updateEnemies() {
        // Move enemies
        for (Enemy enemy : enemies) {
            enemy.update(getWidth());
        }
        
        // Occasionally make enemies shoot
        if (random.nextInt(100) < 3 && enemies.size() > 0) {
            Enemy shooter = enemies.get(random.nextInt(enemies.size()));
            bullets.add(new Bullet(shooter.getX(), shooter.getY() + 15, false, bulletImg, enemyBulletImg));
        }
    }
    
    private void updateExplosions() {
        for (int i = 0; i < explosions.size(); i++) {
            Explosion explosion = explosions.get(i);
            explosion.update();
        }
    }
    
    private void checkCollisions() {
        // Check for bullet-enemy collisions
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            
            if (bullet.isPlayerBullet()) {
                for (int j = 0; j < enemies.size(); j++) {
                    Enemy enemy = enemies.get(j);
                    if (Math.abs(bullet.getX() - enemy.getX()) < 20 && Math.abs(bullet.getY() - enemy.getY()) < 20) {
                        bullets.remove(i);
                        explosions.add(new Explosion(enemy.getX(), enemy.getY()));
                        enemies.remove(j);
                        score += (4 - enemy.getRow()) * 10; // More points for higher rows
                        i--;
                        break;
                    }
                }
            } else {
                // Check if enemy bullet hit player
                if (Math.abs(bullet.getX() - player.getX()) < 20 && Math.abs(bullet.getY() - player.getY()) < 20) {
                    bullets.remove(i);
                    explosions.add(new Explosion(player.getX(), player.getY()));
                    lives--;
                    i--;
                    
                    if (lives <= 0) {
                        gameState = GameState.GAME_OVER;
                        if (score > highScore) {
                            highScore = score;
                        }
                    }
                }
            }
        }
    }
    
    private void checkGameState() {
        // Check if all enemies are defeated
        if (enemies.size() == 0) {
            if (score > highScore) {
                highScore = score;
            }
            gameState = GameState.GAME_OVER;
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (gameState == GameState.MENU) {
            if (key == KeyEvent.VK_ENTER) {
                startNewGame();
            } else if (key == KeyEvent.VK_H) {
                // Show help (you could implement this)
                JOptionPane.showMessageDialog(this, 
                    "Controls:\n- Arrow Keys: Move\n- Space: Shoot\n- P: Pause/Resume\n- R: Restart\n- M: Menu", 
                    "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (gameState == GameState.PLAYING) {
            if (key == KeyEvent.VK_LEFT) {
                player.moveLeft();
            } else if (key == KeyEvent.VK_RIGHT) {
                player.moveRight(getWidth());
            } else if (key == KeyEvent.VK_SPACE) {
                bullets.add(new Bullet(player.getX(), player.getY() - 15, true, bulletImg, enemyBulletImg));
            } else if (key == KeyEvent.VK_P) {
                gameState = GameState.PAUSED;
            }
        } else if (gameState == GameState.PAUSED) {
            if (key == KeyEvent.VK_P) {
                gameState = GameState.PLAYING;
            }
        } else if (gameState == GameState.GAME_OVER) {
            if (key == KeyEvent.VK_R) {
                startNewGame();
            } else if (key == KeyEvent.VK_M) {
                gameState = GameState.MENU;
            }
        }
    }
    
    private void startNewGame() {
        player = new Player(400, 500);
        bullets.clear();
        explosions.clear();
        score = 0;
        lives = 3;
        
        createEnemies();
        
        gameState = GameState.PLAYING;
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
}