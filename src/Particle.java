import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Particle extends Thread{
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

    public void run() {
        while(true)
        {
            update();

            try
            {
                Thread.sleep(20);
            }
            catch(Exception e){}
        }
    }


    public void update() {
        // Update position based on velocity and direction
        x += velocity * Math.cos(theta);
        y += velocity * Math.sin(theta);

        // Collision detection and response
        for (Wall wall : walls) {
            if (wall.intersects(x, y, size)) {
                //System.out.println("INTERSECTS!");
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
        float dx_, dy_;
        // (dx, dy) is relative position of ball with respect to wall midpoints
        float dx = x - wall.getMidX();
        float dy = y - wall.getMidY();

        float cosine = (float) Math.cos(wall.getRotAngle());
        float sine = (float) Math.sin(wall.getRotAngle());

        System.out.println("Cosine :" + cosine);
        System.out.println("sine :" + sine);

        // Rotate relative position and velocity
        dx_ = cosine * dx + sine * dy;
        dy_ = cosine * dy - sine * dx;


        // Rotate back to restore original coordinate axis
        float dx_new = cosine * dx_ - sine * dy_;
        float dy_new = cosine * dy_ + sine * dx_;

        // Update particle properties
        x = (int) (wall.getMidX() + dx_new);
        y = (int) (wall.getMidY() + dy_new);
        theta = 2 * wall.getRotAngle() - theta;
    }

    public void draw (Graphics g){
        g.setColor(color);
        g.fillRect(x, SCREEN_HEIGHT - y, size, size);
    }

    public static Color getRandomLightColor(){
        int r = 128 + random.nextInt(128); // Red
        int g = 128 + random.nextInt(128); // Green
        int b = 128 + random.nextInt(128); // Blue

        return new Color(r, g, b);
    }
}
