import javax.swing.*;
import java.awt.*;

public class ParticleSystemApp extends JFrame {

    private JPanel particlePanel;
    private JPanel inputPanel;
    private JTextField xField, yField, thetaField, velocityField;
    private JTextField x1Field, y1Field, x2Field, y2Field;
    private JButton submitParticleButton, submitWallButton;

    private JLabel fpsLabel;

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

        // Start a thread to update FPS
        new Thread(this::runFPSCounter).start();
    }

    private void runFPSCounter() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 60D; // 60 ticks per second

        int frames = 0;
        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            boolean shouldRender = false;

            while (delta >= 1) {
                // Update logic here (if any)
                delta -= 1;
                shouldRender = true;
            }

            if (shouldRender) {
                frames++;
                // Render logic here (update your particle system)
            }

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
