import java.awt.*;
import java.util.Random;

public class Particle {
    private final int size = 5;
    private int x, y; // coordinates

    private static final Random random = new Random(); // for generating random color

    public Particle(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void draw (Graphics g){
        g.setColor(getRandomLightColor());
        g.fillRect(x, y, size, size);
    }

    public static Color getRandomLightColor(){
        int r = 128 + random.nextInt(128); // Red
        int g = 128 + random.nextInt(128); // Green
        int b = 128 + random.nextInt(128); // Blue

        return new Color(r, g, b);
    }
}
