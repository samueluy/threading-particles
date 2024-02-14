import java.awt.*;
import java.util.ArrayList;

public class ParticleBatch extends Thread {
    // Particles assigned to this thread
    private ArrayList<Particle> particles = new ArrayList<>();

    public ParticleBatch(ArrayList<Particle> particles) {
        this.particles = particles;
    }

    public ArrayList<Particle> getParticles() {
        return particles;
    }

    public int getNumParticles() {
        return particles.size();
    }

    @Override
    public void run() {

    }

}
