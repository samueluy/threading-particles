import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    private ArrayList<ParticleBatch> particleBatchList;
    private ArrayList<Wall> wallList = new ArrayList<>();

    private final int MIN_VELOCITY = 3;
    private final int MAX_VELOCITY = 30;

    private final int MAX_LOAD = 10;

    private long lastUpdateTime;

    private int frames;
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

                for (ParticleBatch batch: particleBatchList) {
                    ArrayList<Particle> particleList = batch.getParticles();
                    for (Particle particle : particleList) {
                        particle.draw(g);
                    }
                }

                // Draw walls
                for (Wall wall : wallList){
                    wall.draw(g);
                }

                // Update FPS label position dynamically
                fpsLabel.setBounds(getWidth() - 110, getHeight() - 30, 100, 20);

                // Run a function after paintComponent is done
                SwingUtilities.invokeLater(this::runFPSCounter);
            }

            private void runFPSCounter() {
                long now = System.currentTimeMillis();
                long elapsed = now - lastUpdateTime;

                frames++;

                if (elapsed >= 500) { // 0.5 seconds
                    final int fps = (int) ((frames * 1000) / elapsed);
                    SwingUtilities.invokeLater(() -> fpsLabel.setText("FPS: " + Math.min(fps, 144)));

                    frames = 0;
                    lastUpdateTime = now;
                }

                try {
                    Thread.sleep(Math.max(1, (1000 / 144) - (System.currentTimeMillis() - now)));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            }
        };
        particlePanel.setPreferredSize(new Dimension(1280, 720)); // Main -> width is - input panel width
        particlePanel.setBackground(Color.BLACK);
        add(particlePanel, BorderLayout.CENTER);

        // Input Panel
        inputPanel = new JPanel();
        inputPanel.setPreferredSize(new Dimension(404, 720)); // Main -> width is - input panel width
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

                        addParticlesWithConstantStartPointAndVelocity(n, x, y, velocity, startTheta, endTheta);
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

                        addParticlesWithConstantStartPointAndAngle(n, x, y, theta, startVelocity, endVelocity);
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

        //Initializing the batch list
        particleBatchList = new ArrayList<ParticleBatch>();
        ParticleBatch tempPb = new ParticleBatch();
        tempPb.start();
        particleBatchList.add(tempPb);

        // testwalls
//        wallList.add(new Wall(100, 700, 600, 100)); //Test angle facing bottom left and top right WORKING
//        wallList.add(new Wall(600, 10, 1200, 700)); // Test angle facing bottom right and top left WORKING
//        wallList.add(new Wall(600, 10, 600, 700)); //Straight vertical WORKING
//        wallList.add(new Wall(100, 300, 1000, 300)); //Straight horizontal WORKING

        // Start gamelogic thread
        new Thread(this::gameLoop).start();
    }

    private void gameLoop() {
        final double maxFramesPerSecond = 144.0;
        final long frameTime = (long) (1000 / maxFramesPerSecond);
        lastUpdateTime = System.currentTimeMillis();
        frames = 0;

        while (true) {
            long startTime = System.currentTimeMillis();

            // Your game update and render logic here
            SwingUtilities.invokeLater(particlePanel::repaint);


            long endTime = System.currentTimeMillis();
            long deltaTime = endTime - startTime;
            long sleepTime = frameTime - deltaTime;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }




    // Helper methods for batch particle addition
    private void addParticlesWithConstantVelocityAndAngle(int n, int startX, int endX, int startY, int endY, double theta, double velocity) {
        double totalDistance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        double increment = totalDistance / (n - 1);
        double unitVectorX = (endX - startX) / totalDistance;
        double unitVectorY = (endY - startY) / totalDistance;
        // Add particles with uniform distance
        double currentX = startX;
        double currentY = startY;


        int remainingCount = n;


        // Add particles to existing batches that are not full yet
        if (!particleBatchList.isEmpty()) {
            for (ParticleBatch batch : particleBatchList) {
                if (batch.isFull())
                    break;
                else {
                    ArrayList<Particle> pList = new ArrayList<>();
                    int numNeeded = MAX_LOAD - batch.getNumParticles(); // Number of particles to be added to this batch

                    // If the number of available space in batch is more than the remaining count to add, then add only what is remaining
                    if (numNeeded > remainingCount) {
                        numNeeded = remainingCount;
                        remainingCount = 0;
                    } else
                        remainingCount -= numNeeded; // Get the number of remaining

                    for (int i = 0; i < numNeeded; i++) {
                        pList.add(new Particle((int) Math.round(currentX), (int) Math.round(currentY), velocity, theta, wallList));
                        currentX += increment * unitVectorX;
                        currentY += increment * unitVectorY;
                    }

                    synchronized (particleListLock) {
                        // Add particles to current existing batch
                        batch.addNewParticles(pList);

                        // TEMP PRINT TODO: REMOVE AFTER TEST
                        //System.out.println("ADDED to existing batch particle num: " + pList.size());
                        // END TEMP PRINT

                        // Update particle system
                        //particlePanel.repaint();
                    }
                }
            }
        }
            // Add the remaining count to new batches if there are still
        while (remainingCount > 0) {
            ArrayList<Particle> xList = new ArrayList<>();
            ParticleBatch pb = new ParticleBatch();
            pb.start();



            for (int i = 0; i < MAX_LOAD; i++) {
                if (remainingCount > 0) {
                    xList.add(new Particle((int) Math.round(currentX), (int) Math.round(currentY), velocity, theta, wallList));
                    currentX += increment * unitVectorX;
                    currentY += increment * unitVectorY;
                    remainingCount--;

                    //System.out.println(i);
                } else
                    break;
            }
            synchronized (particleListLock) {
                // Add a new batch
                pb.clearParticles();
                pb.addNewParticles(xList);
                particleBatchList.add(pb);



                // TEMP PRINT TODO: REMOVE AFTER TEST
                //System.out.println("ADDED NEW BATCH with particle num: " + pb.getNumParticles());
                // END TEMP PRINT

                // Update particle system
                //particlePanel.repaint();
            }
        }

        // TEMP PRINT TODO: REMOVE AFTER TEST
//        System.out.println("THREAD NUM START");
//        for (ParticleBatch batch : particleBatchList)
//            System.out.println("Thread num: " + batch.getNumParticles());
//        System.out.println("THREAD NUM END");
        // END TEMP PRINT

        // Sort list
        Collections.sort(particleBatchList, new ParticleBatchComparator());

    }

    private void addParticlesWithConstantStartPointAndVelocity(int n, int x, int y, double velocity, double startTheta, double endTheta) {
        double dTheta = (endTheta - startTheta) / (double) n;
        double incTheta = startTheta;
        int remainingCount = n;


        // Add particles to existing batches that are not full yet

        if (!particleBatchList.isEmpty()) {
            for (ParticleBatch batch : particleBatchList) {
                if (batch.isFull())
                    break;
                else {
                    ArrayList<Particle> pList = new ArrayList<>(); // Clear list before adding particles
                    int numNeeded = MAX_LOAD - batch.getNumParticles(); // Number of particles to be added to this batch

                    // If the number of available space in batch is more than the remaining count to add, then add only what is remaining
                    if (numNeeded > remainingCount) {
                        numNeeded = remainingCount;
                        remainingCount = 0;
                    } else
                        remainingCount -= numNeeded; // Get the number of remaining

                    for (int i = 0; i < numNeeded; i++) {
                        pList.add(new Particle(x, y, velocity, incTheta, wallList));
                        incTheta += dTheta;
                    }

                    // Add particles to current existing batch
                    synchronized (particleListLock) {
                        batch.addNewParticles(pList);

                        // TEMP PRINT TODO: REMOVE AFTER TEST
                        //System.out.println("ADDED to existing batch particle num: " + pList.size());
                        // END TEMP PRINT
                    }
                }
            }
        }

        // Add the remaining count to new batches if there are still
        while (remainingCount > 0) {
            ArrayList<Particle> xList = new ArrayList<>();
            ParticleBatch pb = new ParticleBatch();
            pb.start();

            for (int i = 0; i < MAX_LOAD; i++) {
                if (remainingCount > 0) {
                    xList.add(new Particle(x, y, velocity, incTheta, wallList));
                    incTheta += dTheta;
                    remainingCount--;
                } else
                    break;
            }
            synchronized (particleListLock) {
                pb.clearParticles();
                pb.addNewParticles(xList);
                particleBatchList.add(pb);


                // TEMP PRINT TODO: REMOVE AFTER TEST
                //System.out.println("ADDED NEW BATCH with particle num: " + pb.getNumParticles());
                // END TEMP PRINT
            }
        }

        // TEMP PRINT TODO: REMOVE AFTER TEST
//        System.out.println("THREAD NUM START");
//        for (ParticleBatch batch : particleBatchList)
//            System.out.println("Thread num: " + batch.getNumParticles());
//        System.out.println("THREAD NUM END");
        // END TEMP PRINT

        // Sort list
        Collections.sort(particleBatchList, new ParticleBatchComparator());

        // Update particle system
        //particlePanel.repaint();

    }

    private void addParticlesWithConstantStartPointAndAngle(int n, int x, int y, double theta, double startVelocity, double endVelocity) {
        double dVelocity = (endVelocity - startVelocity) / (double) n;
        double incVelo = startVelocity;
        int remainingCount = n;

        // Add particles to existing batches that are not full yet
        if (!particleBatchList.isEmpty()) {
            for (ParticleBatch batch : particleBatchList) {
                if (batch.isFull())
                    break;
                else {
                    ArrayList<Particle> pList = new ArrayList<>(); // Clear list before adding particles
                    int numNeeded = MAX_LOAD - batch.getNumParticles(); // Number of particles to be added to this batch

                    // If the number of available space in batch is more than the remaining count to add, then add only what is remaining
                    if (numNeeded > remainingCount) {
                        numNeeded = remainingCount;
                        remainingCount = 0;
                    } else
                        remainingCount -= numNeeded; // Get the number of remaining

                    for (int i = 0; i < numNeeded; i++) {
                        pList.add(new Particle(x, y, incVelo, theta, wallList));
                        incVelo += dVelocity;
                    }

                    synchronized (particleListLock) {
                        // Add particles to current existing batch
                        batch.addNewParticles(pList);

                        // TEMP PRINT TODO: REMOVE AFTER TEST
                        //System.out.println("ADDED to existing batch particle num: " + pList.size());
                        // END TEMP PRINT
                    }
                }
            }
        }

        // Add the remaining count to new batches if there are still
        while (remainingCount > 0) {
            ArrayList<Particle> xList = new ArrayList<>();
            ParticleBatch pb = new ParticleBatch();
            pb.start();

            for (int i = 0; i < MAX_LOAD; i++) {
                if (remainingCount > 0) {
                    xList.add(new Particle(x, y, incVelo, theta, wallList));
                    incVelo += dVelocity;
                    remainingCount--;
                } else
                    break;
            }

            synchronized (particleListLock) {
                // Add a new batch
                pb.clearParticles();
                pb.addNewParticles(xList);
                particleBatchList.add(pb);

                // TEMP PRINT TODO: REMOVE AFTER TEST
                //System.out.println("ADDED NEW BATCH with particle num: " + pb.getNumParticles());
                // END TEMP PRINT
            }

        }

        // TEMP PRINT TODO: REMOVE AFTER TEST
//        System.out.println("THREAD NUM START");
//        for (ParticleBatch batch : particleBatchList)
//            System.out.println("Thread particle num: " + batch.getNumParticles());
//        System.out.println("THREAD NUM END");
        // END TEMP PRINT

        // Sort list
        Collections.sort(particleBatchList, new ParticleBatchComparator());

        // Update particle system
        //particlePanel.repaint();

    }

    private void addParticles(int x, int y, double theta, double velocity) {
        // Create array of particles
        ArrayList<Particle> pList = new ArrayList<>();
        pList.add(new Particle(x, y, velocity, theta, wallList));

        // Add particle to an existing batch that is not full yet
        if (!particleBatchList.isEmpty() && !particleBatchList.get(0).isFull()) {
            for (ParticleBatch pb : particleBatchList) {
                if (pb.isFull())
                    break;
                else
                    synchronized (particleListLock) {
                        pb.addNewParticles(pList);

                        // TEMP PRINT TODO: REMOVE AFTER TEST
                        //System.out.println("ADDED to existing batch particle num: " + pList.size());
                        // END TEMP PRINT
                    }
            }
        } else {
            synchronized (particleListLock) {
                ParticleBatch pb = new ParticleBatch();
                particleBatchList.add(pb);
                pb.start();
                pb.addNewParticles(pList);

                // TEMP PRINT TODO: REMOVE AFTER TEST
                //System.out.println("ADDED NEW BATCH with particle num: " + pb.getNumParticles());
                // END TEMP PRINT
            }
        }

        // TEMP PRINT TODO: REMOVE AFTER TEST
//        System.out.println("THREAD NUM START");
//        for (ParticleBatch batch : particleBatchList)
//            System.out.println("Thread particle num: " + batch.getNumParticles());
//        System.out.println("THREAD NUM END");
        // END TEMP PRINT


        // Sort list
        Collections.sort(particleBatchList, new ParticleBatchComparator());

        // Update particle system
        //particlePanel.repaint();
    }

    private void addToInputPanel(String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Adding border
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

class ParticleBatchComparator implements Comparator<ParticleBatch> {
    @Override
    public int compare(ParticleBatch batch1, ParticleBatch batch2) {
        return Integer.compare(batch1.getNumParticles(), batch2.getNumParticles());
    }
}

