import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ParticleSystemApp extends JFrame {

    private final Object particleListLock = new Object();

    // GUI attributes
    private JPanel particlePanel;
    private JPanel inputPanel;
    private JTextField xField, yField, thetaField, velocityField;
    private JTextField x1Field, y1Field, x2Field, y2Field;
    private JButton submitParticleButton, submitWallButton;

    private JLabel fpsLabel;

    private ArrayList<Particle> particleList = new ArrayList<Particle>();
    public ParticleSystemApp() {
        setTitle("Particle System App");
        setSize(1440, 1080); // The window itself
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Particle System Panel
        particlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Draw particles
                for (Particle particle : particleList){
                    particle.draw(g);
                }

                // Update FPS label position dynamically
                fpsLabel.setBounds(getWidth() - 110, getHeight() - 30, 100, 20);
            }
        };
        particlePanel.setPreferredSize(new Dimension(1040, 1080)); // Main
        particlePanel.setBackground(Color.BLACK);
        add(particlePanel, BorderLayout.CENTER);

        // Input Panel
        inputPanel = new JPanel();
        inputPanel.setPreferredSize(new Dimension(400, 1080));
        inputPanel.setLayout(new GridLayout(10, 2));

        xField = new JTextField();
        yField = new JTextField();
        thetaField = new JTextField();
        velocityField = new JTextField();
        x1Field = new JTextField();
        y1Field = new JTextField();
        x2Field = new JTextField();
        y2Field = new JTextField();
        submitParticleButton = new JButton("Add Particle");
        submitWallButton = new JButton("Add Wall");

        // Method to add a label with border
        addToInputPanel("X:", xField);
        addToInputPanel("Y:", yField);
        addToInputPanel("Theta:", thetaField);
        addToInputPanel("Velocity:", velocityField);
        addToInputPanel("Wall X1:", x1Field);
        addToInputPanel("Wall Y1:", y1Field);
        addToInputPanel("Wall X2:", x2Field);
        addToInputPanel("Wall Y2:", y2Field);
        inputPanel.add(submitParticleButton);
        inputPanel.add(submitWallButton);

        add(inputPanel, BorderLayout.EAST);

        // FPS Label
        fpsLabel = new JLabel("FPS: 0");
        fpsLabel.setForeground(Color.WHITE); // Set text color to white for visibility
        fpsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        particlePanel.setLayout(null); // Use null layout to manually position components
        particlePanel.add(fpsLabel);



        // Particle initialization
        // test particles
        particleList.add(new Particle(100, 100, 5, 45));
        particleList.add(new Particle(150, 150, 8, 50));

        // Start a thread to update FPS
        new Thread(this::runFPSCounter).start();
        // Start gamelogic thread
        new Thread(this::gameLoop).start();

    }

    private void gameLoop() {
        long lastTime = System.nanoTime();
        double nsPerUpdate = 1000000000D / 60D; // 60 updates per second

        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerUpdate;
            lastTime = now;

            while (delta >= 1) {
                synchronized (particleListLock) {
                    // Update particle positions
                    for (Particle particle : particleList) {
                        particle.update();
                    }
                }
                delta -= 1;
            }

            SwingUtilities.invokeLater(particlePanel::repaint);

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void runFPSCounter() {
        int frames = 0;
        long lastTimer = System.currentTimeMillis();

        while (true) {
            frames++;

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                int finalFrames = frames;
                SwingUtilities.invokeLater(() -> fpsLabel.setText("FPS: " + finalFrames));
                frames = 0;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void addToInputPanel(String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adding border
        inputPanel.add(label);
        inputPanel.add(textField);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ParticleSystemApp app = new ParticleSystemApp();
            app.setVisible(true);
        });
    }
}
