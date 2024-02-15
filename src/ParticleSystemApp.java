import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// TODO:

public class ParticleSystemApp extends JFrame {

    private final Object particleListLock = new Object();

    // GUI attributes
    private JPanel particlePanel;
    private JPanel inputPanel;
    private JTextField startXField, startYField, endXField, endYField, startThetaField, endThetaField, startVelocityField, endVelocityField, nField;
    private JTextField x1Field, y1Field, x2Field, y2Field;
    private JButton submitParticleButton, submitWallButton;

    private JComboBox<String> batchOptions;

    private JLabel fpsLabel;

    private final int MIN_VELOCITY = 3;
    private final int MAX_VELOCITY = 30;

    private ArrayList<Particle> particleList = new ArrayList<Particle>();
    private ArrayList<Wall> wallList = new ArrayList<>();
    public ParticleSystemApp() {
        setTitle("Particle System App");
        setSize(1700, 760); // The window itself
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        getContentPane().setBackground(Color.GRAY); // Set the default window color

        // Particle System Panel
        particlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Draw particles
                for (Particle particle : particleList){
                    particle.draw(g);
                }

                // Draw walls
                for (Wall wall : wallList){
                    wall.draw(g);
                }

                // Update FPS label position dynamically
                fpsLabel.setBounds(getWidth() - 110, getHeight() - 30, 100, 20);
            }
        };
        particlePanel.setPreferredSize(new Dimension(1280, 720)); // Main -> width is - input panel width
        particlePanel.setBackground(Color.BLACK);
        add(particlePanel, BorderLayout.CENTER);

        // Input Panel
        inputPanel = new JPanel();
        inputPanel.setPreferredSize(new Dimension(404, 720));
        inputPanel.setLayout(new GridLayout(13, 2));


        // Particle Input
        nField = new JTextField();
        startXField = new JTextField();
        startYField = new JTextField();
        endXField = new JTextField();
        endYField = new JTextField();
        startThetaField = new JTextField();
        endThetaField = new JTextField();
        startVelocityField = new JTextField();
        endVelocityField = new JTextField();
        submitParticleButton = new JButton("Add Particle");

        // Wall Input
        x1Field = new JTextField();
        y1Field = new JTextField();
        x2Field = new JTextField();
        y2Field = new JTextField();
        submitWallButton = new JButton("Add Wall");

        // Batch options to the input panel
        String[] batchOptionsArray = {"Default", "Constant Velocity and Angle", "Constant Start Point and Velocity", "Constant Start Point and Angle"};
        JComboBox<String> batchOptions = new JComboBox<>(batchOptionsArray);
        batchOptions.setSelectedItem("Default"); // Set default option
        inputPanel.add(new JLabel("Options"));
        inputPanel.add(batchOptions);

        // INITIAL INPUT PANEL
        addToInputPanel("X:", startXField);
        addToInputPanel("Y:", startYField);
        addToInputPanel("Theta:", startThetaField);
        addToInputPanel("Velocity:", startVelocityField);
        addToInputPanel("Wall X1:", x1Field);
        addToInputPanel("Wall Y1:", y1Field);
        addToInputPanel("Wall X2:", x2Field);
        addToInputPanel("Wall Y2:", y2Field);
        inputPanel.add(submitParticleButton);
        inputPanel.add(submitWallButton);

        // Action listener for the JComboBox
        batchOptions.addActionListener(e -> {
            String selectedOption = (String) batchOptions.getSelectedItem();
            inputPanel.removeAll(); // Clear previous components

            switch (selectedOption) {
                case "Constant Velocity and Angle":
                    inputPanel.add(new JLabel("Options"));
                    inputPanel.add(batchOptions);
                    addToInputPanel("Number of Particles (n):", nField);
                    addToInputPanel("Start X:", startXField);
                    addToInputPanel("Start Y:", startYField);
                    addToInputPanel("End X:", endXField);
                    addToInputPanel("End Y:", endYField);
                    addToInputPanel("Theta:", startThetaField);
                    addToInputPanel("Velocity:", startVelocityField);

                    // Method to add a label with border
                    addToInputPanel("Wall X1:", x1Field);
                    addToInputPanel("Wall Y1:", y1Field);
                    addToInputPanel("Wall X2:", x2Field);
                    addToInputPanel("Wall Y2:", y2Field);
                    inputPanel.add(submitParticleButton);
                    inputPanel.add(submitWallButton);
                    break;
                case "Constant Start Point and Velocity":
                    inputPanel.add(new JLabel("Options"));
                    inputPanel.add(batchOptions);

                    addToInputPanel("Number of Particles (n):", nField);
                    addToInputPanel("X:", startXField);
                    addToInputPanel("Y:", startYField);
                    addToInputPanel("Start Theta:", startThetaField);
                    addToInputPanel("End Theta:", endThetaField); // Reusing velocityField for endTheta
                    addToInputPanel("Velocity:", startVelocityField); // Reusing x1Field for velocity

                    // Method to add a label with border
                    addToInputPanel("Wall X1:", x1Field);
                    addToInputPanel("Wall Y1:", y1Field);
                    addToInputPanel("Wall X2:", x2Field);
                    addToInputPanel("Wall Y2:", y2Field);
                    inputPanel.add(submitParticleButton);
                    inputPanel.add(submitWallButton);
                    break;
                case "Constant Start Point and Angle":
                    inputPanel.add(new JLabel("Options"));
                    inputPanel.add(batchOptions);

                    addToInputPanel("Number of Particles (n):", nField);
                    addToInputPanel("X:", startXField);
                    addToInputPanel("Y:", startYField);
                    addToInputPanel("Theta:", startThetaField);
                    addToInputPanel("Start Velocity:", startVelocityField); // Reusing x1Field for velocity
                    addToInputPanel("End Velocity:", endVelocityField); // Reusing x1Field for endVelocity

                    // Method to add a label with border
                    addToInputPanel("Wall X1:", x1Field);
                    addToInputPanel("Wall Y1:", y1Field);
                    addToInputPanel("Wall X2:", x2Field);
                    addToInputPanel("Wall Y2:", y2Field);
                    inputPanel.add(submitParticleButton);
                    inputPanel.add(submitWallButton);
                    break;
                case "Default":
                    inputPanel.add(new JLabel("Options"));
                    inputPanel.add(batchOptions);

                    addToInputPanel("X:", startXField);
                    addToInputPanel("Y:", startYField);
                    addToInputPanel("Theta:", startThetaField);
                    addToInputPanel("Velocity:", startVelocityField);

                    // Method to add a label with border
                    addToInputPanel("Wall X1:", x1Field);
                    addToInputPanel("Wall Y1:", y1Field);
                    addToInputPanel("Wall X2:", x2Field);
                    addToInputPanel("Wall Y2:", y2Field);
                    inputPanel.add(submitParticleButton);
                    inputPanel.add(submitWallButton);
                default:
                    break;
            }

            inputPanel.revalidate(); // Update layout
            inputPanel.repaint(); // Redraw panel
        });



        // Particle input handling
        submitParticleButton.addActionListener(e -> {
            try {
                int startX, endX, startY, endY, x, y, n;
                double theta, velocity, startTheta, endTheta, startVelocity, endVelocity;

                switch (batchOptions.getSelectedIndex()) {
                    case 0: // Default
                        try {
                            x = Integer.parseInt(startXField.getText());
                            y = Integer.parseInt(startYField.getText());
                            theta = Double.parseDouble(startThetaField.getText());
                            velocity = Double.parseDouble(startVelocityField.getText());

                            // Validate inputs (e.g., check if coordinates are within the window bounds)
                            if (x < 0 || x > particlePanel.getWidth() || y < 0 || y > particlePanel.getHeight()) {
                                JOptionPane.showMessageDialog(this, "Invalid coordinates!");
                                return;
                            }
                            if (theta < 0 || theta > 360) {
                                JOptionPane.showMessageDialog(this, "Theta must be between 0 and 360 degrees!");
                                return;
                            }
                            if (velocity < 0) {
                                JOptionPane.showMessageDialog(this, "Velocity must be non-negative!");
                                return;
                            }
                            if (velocity < MIN_VELOCITY) {
                                JOptionPane.showMessageDialog(this, "Velocity will be adjusted to " + MIN_VELOCITY);
                                velocity = MIN_VELOCITY;
                            }
                            if (velocity > MAX_VELOCITY) {
                                JOptionPane.showMessageDialog(this, "Velocity will be adjusted to " + MAX_VELOCITY);
                                velocity = MAX_VELOCITY;
                            }

                            addParticles(x, y, theta, velocity);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Invalid input format!");
                        }
                        break;

                    case 1: // Constant Velocity and Angle
                        // Retrieve start and end points
                        n = Integer.parseInt(nField.getText());
                        startX = Integer.parseInt(startXField.getText());
                        endX = Integer.parseInt(endXField.getText());
                        startY = Integer.parseInt(startYField.getText());
                        endY = Integer.parseInt(endYField.getText());
                        theta = Double.parseDouble(startThetaField.getText());
                        velocity = Double.parseDouble(startVelocityField.getText());

                        // Validate inputs (e.g., check if coordinates are within the window bounds)
                        if (startX < 0 || startX > particlePanel.getWidth() || startY < 0 || startY > particlePanel.getHeight() ||
                                endX < 0 || endX > particlePanel.getWidth() || endY < 0 || endY > particlePanel.getHeight()) {
                            JOptionPane.showMessageDialog(this, "Invalid coordinates!");
                            return;
                        }
                        if (theta < 0 || theta > 360) {
                            JOptionPane.showMessageDialog(this, "Theta must be between 0 and 360 degrees!");
                            return;
                        }
                        if (velocity < 0) {
                            JOptionPane.showMessageDialog(this, "Velocity must be non-negative!");
                            return;
                        }
                        if (velocity < MIN_VELOCITY) {
                            JOptionPane.showMessageDialog(this, "Velocity will be adjusted to " + MIN_VELOCITY);
                            velocity = MIN_VELOCITY;
                        }
                        if (velocity > MAX_VELOCITY) {
                            JOptionPane.showMessageDialog(this, "Velocity will be adjusted to " + MAX_VELOCITY);
                            velocity = MAX_VELOCITY;
                        }

                        addParticlesWithConstantVelocityAndAngle(n, startX, endX, startY, endY, theta, velocity);
                        break;
                    case 2: // Constant Start Point and Velocity
                        // Retrieve start angle and end angle
                        n = Integer.parseInt(nField.getText());
                        x = Integer.parseInt(startXField.getText());
                        y = Integer.parseInt(startYField.getText());
                        startTheta = Double.parseDouble(startThetaField.getText());
                        endTheta = Double.parseDouble(endThetaField.getText());
                        velocity = Double.parseDouble(startVelocityField.getText());

                        // Validate inputs (e.g., check if coordinates are within the window bounds)
                        if (x < 0 || x > particlePanel.getWidth() || y < 0 || y > particlePanel.getHeight()) {
                            JOptionPane.showMessageDialog(this, "Invalid coordinates!");
                            return;
                        }
                        if (startTheta < 0 || startTheta > 360 || endTheta < 0 || endTheta > 360) {
                            JOptionPane.showMessageDialog(this, "Theta must be between 0 and 360 degrees!");
                            return;
                        }
                        if (velocity < 0) {
                            JOptionPane.showMessageDialog(this, "Velocity must be non-negative!");
                            return;
                        }
                        if (velocity < MIN_VELOCITY) {
                            JOptionPane.showMessageDialog(this, "Velocity will be adjusted to " + MIN_VELOCITY);
                            velocity = MIN_VELOCITY;
                        }
                        if (velocity > MAX_VELOCITY) {
                            JOptionPane.showMessageDialog(this, "Velocity will be adjusted to " + MAX_VELOCITY);
                            velocity = MAX_VELOCITY;
                        }

                        addParticlesWithConstantStartPointAndAngle(n, x, y, velocity, startTheta, endTheta);
                        break;
                    case 3: // Constant Start Point and Angle
                        // Retrieve start velocity and end velocity
                        n = Integer.parseInt(nField.getText());
                        x = Integer.parseInt(startXField.getText());
                        y = Integer.parseInt(startYField.getText());
                        theta = Double.parseDouble(startThetaField.getText());
                        startVelocity = Double.parseDouble(startVelocityField.getText());
                        endVelocity = Double.parseDouble(endVelocityField.getText());

                        if (x < 0 || x > particlePanel.getWidth() || y < 0 || y > particlePanel.getHeight()) {
                            JOptionPane.showMessageDialog(this, "Invalid coordinates!");
                            return;
                        }
                        if (theta < 0 || theta > 360) {
                            JOptionPane.showMessageDialog(this, "Theta must be between 0 and 360 degrees!");
                            return;
                        }
                        if (startVelocity < 0 || endVelocity < 0) {
                            JOptionPane.showMessageDialog(this, "Velocity must be non-negative!");
                            return;
                        }
                        if (startVelocity < MIN_VELOCITY || endVelocity < MIN_VELOCITY) {
                            JOptionPane.showMessageDialog(this, "Minimum velocity should only be " + MIN_VELOCITY);
                            return;
                        }
                        if (startVelocity > MAX_VELOCITY || endVelocity > MAX_VELOCITY) {
                            JOptionPane.showMessageDialog(this, "Maximum velocity should only be " + MAX_VELOCITY);
                            return;
                        }

                        addParticlesWithConstantStartPointAndVelocity(n, x, y, theta, startVelocity, endVelocity);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Invalid batch option selected.");
                        break;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input format!");
            }
        });

        // Wall handling
        submitWallButton.addActionListener(e -> {
            try {
                int x1 = Integer.parseInt(x1Field.getText());
                int y1 = Integer.parseInt(y1Field.getText());
                int x2 = Integer.parseInt(x2Field.getText());
                int y2 = Integer.parseInt(y2Field.getText());

                // Validate inputs (e.g., check if coordinates are within the window bounds)
                if (x1 < 0 || x1 > particlePanel.getWidth() || y1 < 0 || y1 > particlePanel.getHeight() ||
                        x2 < 0 || x2 > particlePanel.getWidth() || y2 < 0 || y2 > particlePanel.getHeight()) {
                    JOptionPane.showMessageDialog(this, "Invalid wall coordinates!");
                    return;
                }

                // Create and add wall
                synchronized (particleListLock) {
                    wallList.add(new Wall(x1, y1, x2, y2));
                }

                // Update particle system
                particlePanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input format!");
            }
        });

        add(inputPanel, BorderLayout.EAST);

        // FPS Label
        fpsLabel = new JLabel("FPS: 0");
        fpsLabel.setForeground(Color.WHITE); // Set text color to white for visibility
        fpsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        particlePanel.setLayout(null); // Use null layout to manually position components
        particlePanel.add(fpsLabel);

        // Particle initialization

        // test particles
        Particle particle1 = new Particle(100, 100, 12, 0, wallList);
        Particle particle2 = new Particle(400, 150, 8, 60, wallList);
        Particle particle3 = new Particle(600, 150, 8, 45, wallList);
        Particle particle4 = new Particle(1000, 100, 8, 80, wallList);
        particleList.add(particle1);
        particleList.add(particle2);
        particleList.add(particle3);
        particleList.add(particle4);
        particle1.start();
        particle2.start();
        particle3.start();
        particle4.start();


        // testwalls
        //wallList.add(new Wall(100, 700, 600, 100)); //Test angle facing bottom left and top right WORKING
        //wallList.add(new Wall(600, 10, 1200, 700)); // Test angle facing bottom right and top left WORKING
        //wallList.add(new Wall(600, 10, 600, 700)); //Straight vertical WORKING
        wallList.add(new Wall(100, 300, 1000, 300)); //Straight horizontal WORKING

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

//            while (delta >= 1) {
//                synchronized (particleListLock) {
//                    // Update particle positions
//                    for (Particle particle : particleList) {
//                        //particle.update();
//                    }
//                }
//                delta -= 1;
//            }

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
        long lastTime = System.nanoTime(); // Use nanoTime for higher precision
        double nsPerUpdate = 1000000000D / 60D; // 60 updates per second

        while (true) {
            long now = System.nanoTime();
            long elapsed = now - lastTime;
            lastTime = now;

            frames++;

            if (elapsed >= nsPerUpdate) {
                double seconds = elapsed / 1000000000.0; // Convert nanoseconds to seconds
                int fps = (int) (frames / seconds); // Calculate FPS

                SwingUtilities.invokeLater(() -> fpsLabel.setText("FPS: " + fps));

                frames = 0; // Reset frame count
                lastTime = System.nanoTime(); // Reset last update time
            }

            try {
                Thread.sleep(1); // Adjust sleep time as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    // Helper methods for batch particle addition
    private void addParticlesWithConstantVelocityAndAngle(int n, int startX, int endX, int startY, int endY, double theta, double velocity) {
        //TODO: Change it to create a thread batch of particles

        double totalDistance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        double increment = totalDistance / (n - 1);
        double unitVectorX = (endX - startX) / totalDistance;
        double unitVectorY = (endY - startY) / totalDistance;

        // Add particles with uniform distance
        double currentX = startX;
        double currentY = startY;

        for (int i = 0; i < n; i++) {
            synchronized (particleListLock) {
                Particle particle = new Particle((int) Math.round(currentX), (int) Math.round(currentY), velocity, theta, wallList);
                particleList.add(particle);
                particle.start();
            }
            currentX += increment* unitVectorX;
            currentY += increment * unitVectorY;
        }

        // Update particle system
        particlePanel.repaint();
    }

    private void addParticlesWithConstantStartPointAndAngle(int n, int startX, int startY, double velocity, double startTheta, double endTheta) {
        //TODO: Change it to create a thread batch of particles
        double dTheta = (endTheta - startTheta) / (double) n;
        double incTheta = startTheta;

        for (int i = 0; i < n; i++) {
            synchronized (particleListLock) {
                Particle particle = new Particle(startX, startY, velocity, incTheta, wallList);
                particleList.add(particle);
                particle.start();
            }
            incTheta += dTheta;
        }
        // Update particle system
        particlePanel.repaint();
    }

    private void addParticlesWithConstantStartPointAndVelocity(int n, int startX, int startY, double theta, double startVelocity, double endVelocity) {
        //TODO: Change it to create a thread batch of particles
        double dVelocity = (endVelocity - startVelocity) / (double) n;
        double incVelo = startVelocity;

        for (int i = 0; i < n; i++) {
            synchronized (particleListLock) {
                Particle particle = new Particle(startX, startY, incVelo, theta, wallList);
                particleList.add(particle);
                particle.start();
            }
            incVelo += dVelocity;
        }
        // Update particle system
        particlePanel.repaint();
    }

    private void addToInputPanel(String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Adding border
        inputPanel.add(label);
        inputPanel.add(textField);
    }

    private void addParticles(int x, int y, double theta, double velocity) {
        //TODO: Change it to create one batch thread of one

        // Create and add particle
        synchronized (particleListLock) {
            Particle particle = new Particle(x, y, velocity, theta, wallList);
            particleList.add(particle);
            particle.start();
        }

        // Update particle system
        particlePanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ParticleSystemApp app = new ParticleSystemApp();
            app.setVisible(true);
        });
    }
}
