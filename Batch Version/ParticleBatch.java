import java.awt.*;
import java.util.ArrayList;

public class ParticleBatch extends Thread {
    // Particles assigned to this thread
    private ArrayList<Particle> particles = new ArrayList<>();

    private int numParticles;

    private final int MAX_LOAD = 20;

    public ParticleBatch(ArrayList<Particle> particles) {
        this.particles = particles;
        this.numParticles = particles.size();
    }

    public ArrayList<Particle> getParticles() {
        return particles;
    }

    public int getNumParticles() {
        return particles.size();
    }

    public boolean isFull() {
        return particles.size() == MAX_LOAD;
    }

    public void addNewParticles(ArrayList<Particle> newParticles) {
        particles.addAll(newParticles);
        numParticles += newParticles.size();
    }

    @Override
    public void run() {
        while(true)
        {
            for (Particle particle: particles)
                particle.update();

            try
            {
                Thread.sleep(20);
            }
            catch(Exception e){}
        }
    }

}
