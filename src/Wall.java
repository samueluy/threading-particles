import java.awt.*;

public class Wall {
    private int x1, y1, x2, y2;
    private final int CANVAS_HEIGHT = 720;
    private final int CANVAS_WIDTH = 720;

    public Wall(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawLine(x1, CANVAS_HEIGHT - y1, x2, CANVAS_HEIGHT - y2);
    }

    public boolean intersects(int particleX, int particleY, int particleSizeX, int particleSizeY) {
        // Calculate the line segment vectors
        int dX1 = x2 - x1;
        int dY1 = y2 - y1;
        int dX2 = particleX - x1;
        int dY2 = particleY - y1;

        // Calculate the dot products
        int dot1 = dX1 * dX2 + dY1 * dY2;
        int dot2 = dX2 * dX2 + dY2 * dY2;

        // Calculate the projection parameter
        float parameter = -1;
        if (dot2 != 0) {
            parameter = (float) dot1 / dot2;
        }

        // Calculate the nearest point on the line segment
        float nearestX, nearestY;
        if (parameter < 0) {
            nearestX = x1;
            nearestY = y1;
        } else if (parameter > 1) {
            nearestX = x2;
            nearestY = y2;
        } else {
            nearestX = x1 + parameter * dX2;
            nearestY = y1 + parameter * dY2;
        }

        // Check if the nearest point is within the boundaries of the wall segment
        boolean withinXBounds = nearestX >= Math.min(x1, x2) && nearestX <= Math.max(x1, x2);
        boolean withinYBounds = nearestY >= Math.min(y1, y2) && nearestY <= Math.max(y1, y2);

        // Check if the nearest point is within the boundaries of the particle
        boolean withinParticleBounds = nearestX >= particleX && nearestX <= particleX + particleSizeX &&
                nearestY >= particleY && nearestY <= particleY + particleSizeY;

        return withinXBounds && withinYBounds && withinParticleBounds;
    }
}
