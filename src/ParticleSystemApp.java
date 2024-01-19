import javax.swing.*;
import java.awt.*;

public class ParticleSystemApp extends JFrame {

    private JPanel particlePanel;
    private JPanel inputPanel;
    private JTextField xField, yField, thetaField, velocityField;
    private JTextField x1Field, y1Field, x2Field, y2Field;
    private JButton submitParticleButton, submitWallButton;

    public ParticleSystemApp() {
        setTitle("Particle System App");
        setSize(1440, 1080); // The window itself
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Particle System Panel
        particlePanel = new JPanel();
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
