# Particle System Simulator

A Java-based simulation for visualizing particle dynamics with custom barriers. This Swing application allows users to create particles with varied velocities and angles, place walls to influence particle motion, and observe the system's behavior in real-time. This application aims to showcase the principles of parallel computing and efficient thread management to simulate complex particle interactions and barrier influences.

In partial completion of the course **Distributed Computing (STDISCM)**.

## Technical Report
[STDISCM PS1 - TECHNICAL REPORT](https://docs.google.com/document/d/1AY1l3o3QcPs9698LnT-6zJ9dD6yGvpSHDfLO1c-ISac/edit?usp=sharing)


## Features 
- Multithreaded Simulation Engine
- Dynamic Barrier Creation
- Customizable Particle Properties
- Real-Time Visualization and Control
- Thread Synchronization
- Performance Optimization

## Prerequisites
- Java Development Kit (JDK) 8 or above.

## How to Run
- 1. Clone this repository
```git clone https://github.com/samueluy/threading-particles.git```
- 2. Navigate to the *src* directory 
```cd threading-particles/src```
- 3. Compile the Java file:
```javac ParticleSystemApp.java```
- 4. Execute the compiled class:
```java ParticleSystemApp```

## Usage
- **Particle Creation**: Use the input panel to set parameters like position, velocity, and angle for new particles.
- **Wall Placement**: Input coordinates to create walls that affect particle movement.
- **Simulation Control**: The application provides real-time feedback on the simulation's performance through an FPS counter.

## Classes

### `ParticleSystemApp`
The `ParticleSystemApp` class serves as the application's main entry point. It extends `JFrame` to create a graphical user interface (GUI) where the simulation occurs. This class initializes the simulation environment, including the setup of the particle panel where particles are drawn and animated, the input panel for user interaction to create particles and walls, and the FPS counter for performance tracking. It handles user inputs for creating particles with specific velocities, angles, and positions, as well as placing walls within the simulation area. The class also manages the simulation loop, ensuring smooth animation and updating of particle positions based on their interactions with the environment and walls.

### `Particle`
The `Particle` class models individual particles within the simulation. Each particle has properties such as position (`x`, `y`), velocity, direction (`theta`), and color. The class is responsible for updating the particle's position based on its current velocity and direction, detecting collisions with walls or the screen boundaries, and adjusting its trajectory accordingly.

Particles are drawn on the screen as colored squares, with their color randomly generated to add visual variety. The class includes methods for updating the particle's position (`update`), checking for and responding to collisions (`checkInclineCollision`), and drawing the particle on the GUI (`draw`).

- **Movement and Boundary Checking**: Automatically updates its position with each simulation tick, considering the velocity and direction. It also handles boundary collisions with the simulation area edges by reflecting the particle's direction.
- **Wall Collision Detection**: Identifies intersections with walls and calculates the new direction after collisions, simulating realistic bounce effects.
- **Visual Representation**: Each particle is represented by a square of a random light color, enhancing the visual appeal of the simulation.


### `ParticleBatch`
The `ParticleBatch` class manages a collection of particles as a single unit or batch, facilitating efficient updates and rendering within the simulation. It extends `Thread`, allowing each batch of particles to be updated concurrently, improving the performance of the simulation by leveraging multithreading.

- **Particle Management**: Holds a specific number of `Particle` objects, managing their lifecycle from creation to update.
- **Concurrency Handling**: Uses synchronization to ensure thread-safe operations on the particle list, mainly when updating particle positions and checking for collisions.
- **Performance Optimization**: By dividing particles into batches and processing each batch in a separate thread, the simulation can handle many particles more efficiently, reducing lag and improving the user experience.

- **Dynamic Particle Updates**: Each `ParticleBatch` independently updates the positions of its particles based on their velocity and direction, as well as handling collisions.
- **Load Management**: Limits the number of particles to a predefined maximum (`MAX_LOAD`), ensuring that each thread manages a manageable number of particles.
- **Batch Processing**: Allows for parallel processing of particle updates, significantly improving performance by utilizing multiple processor cores.


### `ParticleBatchRenderer`
The `ParticleBatchRenderer` class renders a collection of particles onto the graphical interface. It extends `Thread`, allowing the rendering process to be executed concurrently with the simulation's update cycles, thereby enhancing the performance and responsiveness of the particle system simulation.

- **Rendering Particles**: Iterates over a list of `Particle` objects and invokes their `draw` method to render them on the GUI.
- **Handling Particle Updates**: While its primary role is rendering, it can also add new particles to its list, ensuring the rendering loop includes any newly created particles.
- - **Concurrent Rendering**: By running the rendering process in a separate thread, the application can maintain a smooth frame rate even as the number of particles increases.
- **Dynamic Particle Management**: Supports adding new particles to the rendering list, allowing for dynamic changes in the particle system during runtime.
- **Direct Graphics Manipulation**: Utilizes a `Graphics` object passed during construction to draw particles directly onto the GUI component, enabling real-time visual updates.

### `ParticleBatchComparator`
The `ParticleBatchComparator` class implements the `Comparator` interface to define a custom comparison strategy for `ParticleBatch` objects. It is used to sort or order batches of particles based on their size, specifically the number of particles they contain.

- **Custom Sorting Logic**: Provides a mechanism to compare two `ParticleBatch` instances, facilitating sorting operations based on the number of particles in each batch.
- **Efficiency in Managing Batches**: By enabling the sorting of particle batches, the simulation can prioritize the processing or rendering of batches based on their load, potentially optimizing performance.


### `Wall`
The `Wall` class represents static obstacles within the simulation environment. These obstacles are used to interact with particles, influencing their trajectory upon collision. Each wall is defined by its endpoints (`x1`, `y1`, `x2`, `y2`) and has properties such as its midpoint and rotation angle, which are calculated upon instantiation.

- **Geometric Representation**: Walls are represented as line segments between two points in the simulation space. This simple yet effective representation allows for easy calculation of collisions and interactions with particles.
- **Collision Detection**: Includes a method for detecting intersections with particles. This method calculates the distance between a particle and the wall to determine if a collision has occurred.
- **Drawing**: Capable of rendering itself on the simulation canvas. Considering the canvas dimensions, it calculates the necessary coordinates to display the wall correctly within the GUI.

  ## Contributors

- Ayen Lim
- Samuel Uy
