import javax.swing.*;
import java.util.ArrayList;
import java.awt.Graphics;

public class ParticleBatchRenderer extends Thread{
    private ArrayList<Particle> particleList;

    private int numParticles;

    private final Graphics g;

    public ParticleBatchRenderer(ArrayList<Particle> particleList, int numParticles, Graphics g) {
        this.particleList = particleList;
        this.numParticles = numParticles;
        this.g = g;
    }

    public void addNewParticles(ArrayList<Particle> newParticles) {
        particleList.addAll(newParticles);
        numParticles += newParticles.size();
    }

    @Override
    public void run() {
        while(true) {
            for (Particle particle: particleList) {
                particle.draw(g);
            }
        }
    }


}
