import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Particle {
    private final int size = 5;

    private ArrayList<Wall> walls = new ArrayList<>();

    private int x, y; // coordinates

    private Color color;

    private double velocity;
    private double theta;

    // Screen size for boundary detection
    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;

    private static final Random random = new Random(); // for generating random color

    public Particle(int x, int y, double velocity, double theta, ArrayList<Wall> walls){
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.theta = Math.toRadians(theta);
        this.color = getRandomLightColor();
        this.walls = walls;
    }

    public void update() {
        // Update position based on velocity and direction
        x += velocity * Math.cos(theta);
        y += velocity * Math.sin(theta);

        // Collision detection and response
        for (Wall wall : walls) {
            if (wall.intersects(x, y, size)) {
                checkInclineCollision(wall);
                try {
                    Thread.sleep(10); // Sleep for specified time
                } catch (InterruptedException e) {
                    e.printStackTrace(); // Handle interruption exception if needed
                }
            }
        }

        // For screen bounds
        if (x <= 0 || x >= SCREEN_WIDTH) {
            theta = Math.PI - theta; // Reflect horizontally
            x = Math.max(0, Math.min(x, SCREEN_WIDTH)); // Keep within bounds
        }
        if (y <= 0 || y >= SCREEN_HEIGHT) {
            theta = -theta; // Reflect vertically
            y = Math.max(0, Math.min(y, SCREEN_HEIGHT)); // Keep within bounds
        }
    }
    void checkInclineCollision(Wall wall){
        // Calculate the particle's velocity
        float vX = (float) (velocity * Math.cos(theta));
        float vY = (float) (velocity * Math.sin(theta));

        // Calculate the normal vector of the wall
        float cosine = (float) Math.cos(wall.getRotAngle() + Math.PI / 2);
        float sine = (float) Math.sin(wall.getRotAngle() + Math.PI / 2);

        // Calculate the dot product of velocity and wall normal
        float dotProduct = vX * cosine+ vY * sine;

        // Calculate the reflected velocity components
        float reflectedVx = vX - 2 * dotProduct * cosine;
        float reflectedVy = vX - 2 * dotProduct * sine;

        // update particle location and angle
        x += 2 * reflectedVx;
        y += 2 * reflectedVy;
        theta = Math.atan2(reflectedVy, reflectedVx);
    }


    public static Color getRandomLightColor(){
        int r = 128 + random.nextInt(128); // Red
        int g = 128 + random.nextInt(128); // Green
        int b = 128 + random.nextInt(128); // Blue

        return new Color(r, g, b);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public void draw (Graphics g){
        g.setColor(color);
        g.fillRect(x, SCREEN_HEIGHT - y, size, size);
    }
}
