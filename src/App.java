import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.GradientPaint;

// =================
class FlappySparrow extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 736;
    int boardHeight = 414;

    //images
    Image backgroundImg;
    Image sparrowImg;
    Image topPipeImg;
    Image bottomPipeImg;
    Image gameOverImg1;
    Image gameOverImg2;
    
    // Current game over image for animation
    Image currentGameOverImg = null;
    Timer gameOverAnimationTimer;
    boolean showGameOverImg1 = true;
    int gameOverAnimationCounter = 0;

    //sparrow class
    int sparrowX = boardWidth/8;
    int sparrowY = boardHeight/2;
    int sparrowWidth = 80;
    int sparrowHeight = 60;

    class Sparrow {
        int x = sparrowX;
        int y = sparrowY;
        int width = sparrowWidth;
        int height = sparrowHeight;
        Image img;

        Sparrow(Image img) {
            this.img = img;
        }
    }

    //pipe class
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 80;
    int pipeHeight = 512;
    
    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    //game logic
    Sparrow sparrow;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;
    
    // Flap related variables
    int flapStrength = -8;
    int maxFallSpeed = 10;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipeTimer;
    boolean gameOver = false;
    boolean gameOverAudioPlayed = false;
    double score = 0;
    
    // Audio variables
    Clip backgroundMusic;
    Clip gameOverSound;
    boolean audioInitialized = false;

    FlappySparrow() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        // Load all images
        loadImages();
        
        // Initialize audio
        initializeAudio();
        
        // Initialize game over animation timer
        gameOverAnimationTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Toggle between the two game over images
                showGameOverImg1 = !showGameOverImg1;
                currentGameOverImg = showGameOverImg1 ? gameOverImg1 : gameOverImg2;
                gameOverAnimationCounter++;
                
                // Stop animation after game over audio finishes (approx 2 seconds = 4 toggles)
                if (gameOverAnimationCounter >= 4) {
                    gameOverAnimationTimer.stop();
                }
                repaint();
            }
        });

        //sparrow
        sparrow = new Sparrow(sparrowImg);
        pipes = new ArrayList<Pipe>();

        //place pipes timer
        placePipeTimer = new Timer(1600, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipeTimer.start();
        
        //game timer
        gameLoop = new Timer(1000/50, this);
        gameLoop.start();
    }
    
    private void loadImages() {
        try {
            // Use relative file paths instead of getResource()
            System.out.println("Loading images from graphics/ folder...");
            
            // Debug: show current directory
            File currentDir = new File(".");
            System.out.println("Current directory: " + currentDir.getAbsolutePath());
            
            // Check if graphics folder exists
            File graphicsDir = new File("graphics");
            System.out.println("Graphics folder exists: " + graphicsDir.exists());
            if (graphicsDir.exists()) {
                System.out.println("Files in graphics/:");
                for (String file : graphicsDir.list()) {
                    System.out.println("  - " + file);
                }
            }
            
            // Load images using ImageIcon with file paths
            backgroundImg = new ImageIcon("graphics/flappysparrowbg.png").getImage();
            sparrowImg = new ImageIcon("graphics/flappysparrow.png").getImage();
            topPipeImg = new ImageIcon("graphics/toppipe.png").getImage();
            bottomPipeImg = new ImageIcon("graphics/bottompipe.png").getImage();
            gameOverImg1 = new ImageIcon("graphics/GameOver1.png").getImage();
            gameOverImg2 = new ImageIcon("graphics/GameOver2.png").getImage();
            
            // Check if images loaded successfully
            System.out.println("Background image loaded: " + (backgroundImg != null));
            System.out.println("Sparrow image loaded: " + (sparrowImg != null));
            
        } catch (Exception e) {
            System.out.println("Error loading images: " + e.getMessage());
            System.out.println("Creating default images...");
            // Fallback colors if images fail to load
            backgroundImg = createDefaultBackgroundImage();
            sparrowImg = createDefaultSparrowImage();
            gameOverImg1 = createDefaultGameOverImage(Color.RED);
            gameOverImg2 = createDefaultGameOverImage(Color.ORANGE);
            topPipeImg = createDefaultPipeImage(Color.GREEN.darker());
            bottomPipeImg = createDefaultPipeImage(Color.GREEN.darker());
        }
    }
    
    private Image createDefaultBackgroundImage() {
        BufferedImage img = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        // Create a sky gradient
        GradientPaint gradient = new GradientPaint(0, 0, new Color(135, 206, 235), 
                                                   0, boardHeight, new Color(255, 255, 255));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, boardWidth, boardHeight);
        
        // Draw some clouds
        g2d.setColor(Color.WHITE);
        g2d.fillOval(100, 50, 60, 40);
        g2d.fillOval(120, 40, 70, 50);
        g2d.fillOval(300, 80, 80, 40);
        g2d.fillOval(320, 70, 60, 50);
        
        // Draw ground
        g2d.setColor(new Color(34, 139, 34)); // Forest green
        g2d.fillRect(0, boardHeight - 50, boardWidth, 50);
        
        g2d.dispose();
        return img;
    }
    
    private Image createDefaultSparrowImage() {
        BufferedImage img = new BufferedImage(sparrowWidth, sparrowHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        // Body
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(0, 0, sparrowWidth, sparrowHeight);
        
        // Wing
        g2d.setColor(Color.ORANGE);
        g2d.fillArc(sparrowWidth/2, sparrowHeight/3, sparrowWidth/2, sparrowHeight/2, 0, 180);
        
        // Eye
        g2d.setColor(Color.BLACK);
        g2d.fillOval(sparrowWidth/3, sparrowHeight/3, sparrowWidth/5, sparrowHeight/5);
        
        // Beak
        g2d.setColor(Color.ORANGE.darker());
        Polygon beak = new Polygon();
        beak.addPoint(sparrowWidth - 10, sparrowHeight/2);
        beak.addPoint(sparrowWidth, sparrowHeight/2 - 5);
        beak.addPoint(sparrowWidth, sparrowHeight/2 + 5);
        g2d.fillPolygon(beak);
        
        g2d.dispose();
        return img;
    }
    
    private Image createDefaultGameOverImage(Color color) {
        BufferedImage img = new BufferedImage(sparrowWidth, sparrowHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        // Background
        g2d.setColor(color);
        g2d.fillRect(0, 0, sparrowWidth, sparrowHeight);
        
        // Border
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, sparrowWidth-1, sparrowHeight-1);
        
        // Text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("GAME", 10, sparrowHeight/2 - 10);
        g2d.drawString("OVER", 10, sparrowHeight/2 + 10);
        
        g2d.dispose();
        return img;
    }
    
    private Image createDefaultPipeImage(Color color) {
        BufferedImage img = new BufferedImage(pipeWidth, pipeHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        // Pipe color
        g2d.setColor(color);
        g2d.fillRect(0, 0, pipeWidth, pipeHeight);
        
        // Pipe details
        g2d.setColor(color.darker());
        g2d.fillRect(0, 0, 10, pipeHeight); // Left edge
        g2d.fillRect(pipeWidth-10, 0, 10, pipeHeight); // Right edge
        
        // Pipe rim
        g2d.setColor(color.darker().darker());
        g2d.fillRect(0, 0, pipeWidth, 15);
        g2d.fillRect(0, pipeHeight-15, pipeWidth, 15);
        
        g2d.dispose();
        return img;
    }
    
    private void initializeAudio() {
        try {
            System.out.println("Loading audio files...");
            
            // Check if audios folder exists
            File audioDir = new File("audios");
            System.out.println("Audio folder exists: " + audioDir.exists());
            if (audioDir.exists()) {
                System.out.println("Files in audios/:");
                for (String file : audioDir.list()) {
                    System.out.println("  - " + file);
                }
            }
            
            // Load background music
            File bgAudioFile = new File("audios/bgAudio.wav");
            System.out.println("Looking for bgAudio.wav at: " + bgAudioFile.getAbsolutePath());
            System.out.println("File exists: " + bgAudioFile.exists());
            
            if (bgAudioFile.exists()) {
                AudioInputStream bgAudioStream = AudioSystem.getAudioInputStream(bgAudioFile);
                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(bgAudioStream);
                
                // Load game over sound
                File gameOverFile = new File("audios/gameOver.wav");
                System.out.println("Looking for gameOver.wav at: " + gameOverFile.getAbsolutePath());
                System.out.println("File exists: " + gameOverFile.exists());
                
                if (gameOverFile.exists()) {
                    AudioInputStream gameOverStream = AudioSystem.getAudioInputStream(gameOverFile);
                    gameOverSound = AudioSystem.getClip();
                    gameOverSound.open(gameOverStream);
                    
                    audioInitialized = true;
                    
                    // Start background music in a loop
                    backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
                    backgroundMusic.start();
                    System.out.println("Audio initialized successfully!");
                } else {
                    System.out.println("Warning: gameOver.wav not found in audios/ folder");
                    audioInitialized = false;
                }
            } else {
                System.out.println("Warning: bgAudio.wav not found in audios/ folder");
                audioInitialized = false;
            }
            
        } catch (Exception e) {
            System.out.println("Error loading audio files: " + e.getMessage());
            System.out.println("Game will continue without audio.");
            audioInitialized = false;
        }
    }
    
    void placePipes() {
        // Calculate random position for the gap center
        int minGapY = boardHeight/4 + sparrowHeight;
        int maxGapY = 3 * boardHeight/4 - sparrowHeight;
        int gapCenterY = minGapY + random.nextInt(maxGapY - minGapY);
        
        // Larger opening space for easier passage
        int openingSpace = boardHeight/2;
        
        // Calculate top pipe bottom position (gap starts after top pipe)
        int topPipeBottomY = gapCenterY - openingSpace/2;
        
        Pipe topPipe = new Pipe(topPipeImg);
        // Top pipe goes from top of screen to just above the gap
        topPipe.y = topPipeBottomY - pipeHeight;
        pipes.add(topPipe);
    
        Pipe bottomPipe = new Pipe(bottomPipeImg);
        // Bottom pipe goes from bottom of gap to bottom of screen
        bottomPipe.y = gapCenterY + openingSpace/2;
        pipes.add(bottomPipe);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //background
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);

        //pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        // Draw sparrow or game over animation
        if (!gameOver) {
            // Normal sparrow
            g.drawImage(sparrowImg, sparrow.x, sparrow.y, sparrow.width, sparrow.height, null);
        } else {
            // Game over animation
            if (currentGameOverImg != null) {
                g.drawImage(currentGameOverImg, sparrow.x, sparrow.y, sparrow.width, sparrow.height, null);
            } else {
                // Fallback to regular sparrow if animation not started
                g.drawImage(sparrowImg, sparrow.x, sparrow.y, sparrow.width, sparrow.height, null);
            }
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Press SPACE to restart", 10, boardHeight - 20);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move() {
        //sparrow
        velocityY += gravity;
        
        if (velocityY > maxFallSpeed) {
            velocityY = maxFallSpeed;
        }
        
        sparrow.y += velocityY;
        sparrow.y = Math.max(sparrow.y, 0);

        //pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && sparrow.x > pipe.x + pipe.width) {
                score += 0.5;
                pipe.passed = true;
            }

            if (collision(sparrow, pipe)) {
                gameOver = true;
            }
        }

        if (sparrow.y > boardHeight) {
            gameOver = true;
        }
    }

    boolean collision(Sparrow a, Pipe b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        
        // Check for game over and trigger audio/animation
        if (gameOver && !gameOverAudioPlayed) {
            // Stop background music
            if (audioInitialized && backgroundMusic.isRunning()) {
                backgroundMusic.stop();
            }
            
            // Play game over sound
            if (audioInitialized) {
                try {
                    gameOverSound.setFramePosition(0); // Rewind to start
                    gameOverSound.start();
                } catch (Exception ex) {
                    System.out.println("Error playing game over sound: " + ex.getMessage());
                }
            }
            
            // Start game over animation
            currentGameOverImg = gameOverImg1; // Start with first image
            showGameOverImg1 = true;
            gameOverAnimationCounter = 0;
            gameOverAnimationTimer.start();
            
            // Stop game timers
            placePipeTimer.stop();
            gameLoop.stop();
            
            gameOverAudioPlayed = true;
        }
        
        repaint();
    }  

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameOver) {
                velocityY = flapStrength;
            } else {
                // Check if game over audio has finished playing
                if (gameOverAudioPlayed && (!audioInitialized || !gameOverSound.isRunning())) {
                    restartGame();
                }
            }
        }
    }
    
    private void restartGame() {
        // Reset game state
        sparrow.y = sparrowY;
        velocityY = 0;
        pipes.clear();
        gameOver = false;
        gameOverAudioPlayed = false;
        score = 0;
        
        // Stop game over animation
        gameOverAnimationTimer.stop();
        currentGameOverImg = null;
        
        // Restart background music from beginning
        if (audioInitialized) {
            try {
                // Make sure game over sound is stopped
                if (gameOverSound.isRunning()) {
                    gameOverSound.stop();
                }
                
                // Restart background music
                backgroundMusic.setFramePosition(0);
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundMusic.start();
            } catch (Exception ex) {
                System.out.println("Error restarting background music: " + ex.getMessage());
            }
        }
        
        // Restart game timers
        gameLoop.start();
        placePipeTimer.start();
        
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}

// =================

public class App {    
    public static void main(String[] args) throws Exception {
        System.out.println("Starting Flappy Sparrow...");
        System.out.println("Working directory: " + System.getProperty("user.dir"));
        
        int boardWidth = 736;
        int boardHeight = 414;

        JFrame frame = new JFrame("Flappy Sparrow");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappySparrow flappySparrow = new FlappySparrow();
        frame.add(flappySparrow);
        frame.pack();
        flappySparrow.requestFocus();
        frame.setVisible(true);
    }
}