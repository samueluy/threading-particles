import java.awt.*;
import java.util.Random;

public class Particle {
    private final int size = 5;

    private int x, y; // coordinates

    private Color color;

    private double velocity;
    private double theta;

    // Screen size for boundary detection
    private static final int SCREEN_WIDTH = 680;
    private static final int SCREEN_HEIGHT = 700;

    private static final Random random = new Random(); // for generating random color

    public Particle(int x, int y, double velocity, double theta){
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.theta = Math.toRadians(theta);
        this.color = getRandomLightColor();
    }

    public void update() {
        // Update position based on velocity and direction
        x += velocity * Math.cos(theta);
        y += velocity * Math.sin(theta);

        // Collision detection and response
        if (x <= 0 || x >= SCREEN_WIDTH) {
            theta = Math.PI - theta; // Reflect horizontally
            x = Math.max(0, Math.min(x, SCREEN_WIDTH)); // Keep within bounds
        }
        if (y <= 0 || y >= SCREEN_HEIGHT) {
            theta = -theta; // Reflect vertically
            y = Math.max(0, Math.min(y, SCREEN_HEIGHT)); // Keep within bounds
        }
    }

    public void draw (Graphics g){
        g.setColor(color);
        g.fillRect(x, y, size, size);
    }

    public static Color getRandomLightColor(){
        int r = 128 + random.nextInt(128); // Red
        int g = 128 + random.nextInt(128); // Green
        int b = 128 + random.nextInt(128); // Blue

        return new Color(r, g, b);
    }
}
