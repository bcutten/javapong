/*
 * B Cutten
 * Started May 26, 2022
 * This JPanel will do all of the drawing for the game
 */
package pong;

/**
 *
 * @author cutten
 */
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DrawingSurface extends JPanel implements MouseListener, Runnable, ActionListener {

    private Thread animator;
    private final int DELAY = 25;
    private States gameState;
    private Font titleFont;
    private Font menuFont;
    private Cursor menuLeftCursor;
    private Cursor menuRightCursor;
    private int ticks = 0;
    private int loadTimer = 0;
    private int player1Score = 0;
    private int player2Score = 0;
    private int w, h;
    private Ball b;
    private Paddle player1, player2;

    

    //these are the different phases the game can be on
    enum States {
        LOADING,
        MAIN_MENU,
        PLAY_2P,
        PLAY_1P,
        PAUSE,
        P1_SCORED,
        P2_SCORED,
        GAME_OVER
    }

    public DrawingSurface() { //constructor for the panel

        gameState = States.LOADING;//start with the menu screen

        //attach the mouse listener to the panel and give it "focus"
        this.addMouseListener(this);
        this.setFocusable(true);
        addKeyListener(new TAdapter());
        this.requestFocus();

    }

    /**
     * This method loads in various images and fonts It's called from outside
     * the panel so that the size is correct
     */
    public void loadResources() {
        w = getSize().width;
        h = getSize().height;
        System.out.println(w + " " + h);
        loadFont();
        loadImages();
    }

    

    /**
     * This methods creates a custom font that's included with the game.
     */
    private void loadFont() {
        try {
            //http://www.java2s.com/Tutorials/Java/Graphics/Font/Create_font_from_true_type_font_ttf_file_in_Java.htm
            InputStream in = DrawingSurface.class.getResourceAsStream("font.ttf");
            Font dynamicFont = Font.createFont(Font.TRUETYPE_FONT, in);
            titleFont = dynamicFont.deriveFont(96f);
            menuFont = dynamicFont.deriveFont(32f);
            System.out.println("Font loaded");
        } catch (IOException | FontFormatException e) {
            System.out.println("Unable to load fonts");
            titleFont = new Font("Franklin Gothic Medium", Font.PLAIN, 96);
            menuFont = new Font("Franklin Gothic Medium", Font.PLAIN, 32);
        }

    }

    
    /**
     * Does the actual drawing, depending on which state the game is in
     * @param g - the graphics object to draw with
     */
    private void doDrawing(Graphics g) {
        //the Graphics2D class is the class that handles all the drawing
        //must be casted from older Graphics class in order to have access to some newer methods
        Graphics2D g2d = (Graphics2D) g;
        
        //check which game state we're in and call the appropriate draw method
        if (gameState == States.MAIN_MENU) {
            drawMainMenu(g2d);
        } else if (gameState == States.PLAY_2P) {
            drawGame(g2d);
        } else if (gameState == States.LOADING) {
            drawLoadScreen(g2d);
        } else if (gameState == States.P1_SCORED) {
            drawScoreScreen(g2d, "P1");
        } else if (gameState == States.P2_SCORED) {
            drawScoreScreen(g2d, "P2");
        } else if (gameState == States.PAUSE) {
            drawPauseScreen(g2d);
        } 

    }

    /**
     * This will do all of the drawing needed for the "scored" screen
     *
     * @param g2d - the graphics object to draw with
     * @param winner - the player who just scored
     */
    private void drawScoreScreen(Graphics2D g2d, String winner) {
        //fill black
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, w, h);
        //white text
        g2d.setColor(Color.WHITE);
        g2d.setFont(titleFont);
        g2d.drawString(winner + " SCORES!", w / 2 - 250, h / 2);
        g2d.setFont(menuFont);
        g2d.drawString("Press space to continue", w / 2 - 180, h / 2 + 100);
        //also show score
        drawScore(g2d);
    }

    /**
     * This will do all of the drawing needed for the "paused" screen
     *
     * @param g2d - the graphics object to draw with
     */
    private void drawPauseScreen(Graphics2D g2d) {
        //fill black
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, w, h);
        //also show score
        drawScore(g2d);
        //white text
        g2d.setColor(Color.WHITE);
        g2d.setFont(titleFont);
        g2d.drawString("GAME PAUSED", w / 2 - 320, h / 2);
        g2d.setFont(menuFont);
        g2d.drawString("Press space to continue", w / 2 - 180, h / 2 + 100);
        drawGrid(g2d);
    }
    
    /**
     * This will do all of the drawing needed for the loading scene
     *
     * @param - the graphics object to draw with
     */
    private void drawLoadScreen(Graphics2D g2d) {
        //fill black
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, w, h);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.ITALIC, 48));
        //using some magic numbers to centre on the screen
        if (loadTimer % 20 <= 7) {
            g2d.drawString("Loading.", w / 2 - 100, h / 2);
        } else if (loadTimer % 20 <= 14) {
            g2d.drawString("Loading..", w / 2 - 100, h / 2);
        } else {
            g2d.drawString("Loading...", w / 2 - 100, h / 2);
        }
    }

    /**
     * This will do all of the drawing needed for the main menu scene
     *
     * @param - the graphics object to draw with
     */
    private void drawMainMenu(Graphics2D g2d) {

        //fill black
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, w, h);

        //drawGrid(g2d); //to help with placement
        //draw menu text (https://zetcode.com/gfx/java2d/textfonts/)
        g2d.setColor(Color.WHITE);
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        g2d.setFont(titleFont);
        //using some magic numbers to centre on the screen
        g2d.drawString("PONG", w / 2 - 140, h / 2 - 150);
        g2d.setFont(menuFont);
        g2d.drawString("PLAYER VS PLAYER", w / 2 - 150, h / 2 - 50);
        g2d.drawString("PLAYER VS CPU", w / 2 - 125, h / 2);
        g2d.drawString("QUIT", w / 2 - 40, h / 2 + 50);

        //draw the menu cursors
        g2d.drawImage(menuLeftCursor.getImage(), menuLeftCursor.getCurrentX(), menuLeftCursor.getCurrentY(), null);
        g2d.drawImage(menuRightCursor.getImage(), menuRightCursor.getCurrentX(), menuRightCursor.getCurrentY(), null);
    }
    
    /**
     * Get the images and load em up
     */
    private void loadImages() {
        try {
            URL url;
            BufferedImage img;

            //first position is PLAY
            menuLeftCursor = new Cursor(w / 2 - 225, h / 2 - 100);
            menuRightCursor = new Cursor(w / 2 + 155, h / 2 - 100);
            //second is HIGH SCORE
            menuLeftCursor.addPosition(w / 2 - 200, h / 2 - 50);
            menuRightCursor.addPosition(w / 2 + 125, h / 2 - 50);
            //third is EXIT
            menuLeftCursor.addPosition(w / 2 - 115, h / 2);
            menuRightCursor.addPosition(w / 2 + 40, h / 2);

            //load the 7 images into the menu cursor animation
            for (int i = 1; i <= 7; i++) {
                url = DrawingSurface.class.getResource("star_0" + i + ".png");
                img = ImageIO.read(url);
                menuLeftCursor.addImage(img.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
                menuRightCursor.addImage(img.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
            }

            System.out.println("Images loaded");
        } catch (IOException e) {
            System.out.println("Unable to load images");
        }

    }

    /**
     * This will do all of the drawing during the main game play
     *
     * @param - the graphics object to draw with
     */
    private void drawGame(Graphics2D g2d) {

        //fill black
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, w, h);

        //drawGrid(g2d);
        //draw score
        drawScore(g2d);
        //draw paddles
        player1.draw(g2d);
        player2.draw(g2d);
        //draw ball
        b.draw(g2d);
    }

    private void drawScore(Graphics2D g2d) {
        //white text
        g2d.setColor(Color.WHITE);
        g2d.setFont(titleFont);
        //using some magic numbers to centre on the screen
        g2d.drawString(player1Score + ":" + player2Score, w / 2 - 55, 70);
    }

    /**
     * This will draw a grid on the screen to help with placement of things
     *
     * @param g2d - the graphics object to draw with
     * @param w - the width of the screen
     * @param h - the height of the scree
     */
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        //draw vertical lines
        for (int x = 0; x < w; x += 20) {
            g2d.drawLine(x, 0, x, h);
        }

        //draw horizontal lines
        for (int y = 0; y < h; y += 20) {
            g2d.drawLine(0, y, w, y);
        }
    }

    //overrides paintComponent in JPanel class
    //performs custom painting
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);//does the necessary work to prepare the panel for drawing
        doDrawing(g);
    }

    private void updateGamePlay() {
        //update the ball
        b.update();
        //update the paddles
        player1.update();
        player2.update();
        //check if the ball should bounce off the top or bottom        
        if (b.getY() + b.getRadius() >= h || b.getY() <= 0) {
            b.bounceY();
        }
        
        //check if the ball should bounce off of either paddle
        if(player1.isHitting(b) || player2.isHitting(b)){            
            b.bounceX();
        }

        //check if the ball has hit the left or right (scores!)
        if (b.getX() + b.getRadius() >= w) {
            //player 1 scores
            player1Score++;
            gameState = States.P1_SCORED;
        } else if (b.getX() <= 0) {
            //player 2 scores
            player2Score++;
            gameState = States.P2_SCORED;
        }

    }

    /**
     * This method instantiates the game objects
     */
    private void setupGame() {
        //create the ball and start it moving
        b = new Ball(w / 2 - 10, h / 2 - 10, 20);
        b.setSpeedX(5);
        b.setSpeedY(5);
        //create the left player paddle
        player1 = new Paddle(20, h / 2 - 50, 20, 100);
        //create the right player paddle
        player2 = new Paddle(w - 40, h / 2 - 50, 20, 100);

    }
    
    /**
     * This method resets the game objects after someone has scored
     * @param winner - the player who just scored
     */
    private void resetGameAfterScore(String winner){
        //reset each player
        player1.setX(20);
        player1.setY(h / 2 - 50);        
        player2.setX(w - 40);
        player2.setY(h / 2 - 50);
        //ball to center
        b.setX(w / 2 -10);
        b.setY(h / 2 -10);
        
        //start the ball towards the person who just scored
        if(winner.equals("P1")){
           b.setSpeedX(-5);
           b.setSpeedY(-5); 
        }else{
           b.setSpeedX(5);
           b.setSpeedY(5);
        }
        
    }

    /**
     * This method updates the menu animations
     */
    private void updateMenu() {

        menuLeftCursor.cycle(ticks);
        menuRightCursor.cycle(ticks);

    }

    //update the game depending on which state it's in
    public void updateGame() {
        ticks++;
        if (ticks > 60) {
            ticks = ticks % 60;
        }
        //System.out.println(ticks);
        if (gameState == States.MAIN_MENU) {
            updateMenu();
        } else if (gameState == States.PLAY_2P) {
            updateGamePlay();
        } else if (gameState == States.LOADING) {
            //this game state waits for the rest of the resources to load before drawing the menu
            loadTimer++;
            if (loadTimer > 100) {
                gameState = States.MAIN_MENU;
            }
        }
    }

    //this method is called after the JPanel is added to the JFrame
    //we can perform start up tasks here
    @Override
    public void addNotify() {
        super.addNotify();
        animator = new Thread(this);
        animator.start();
    }

    //this method is called only once, when the Thread starts
    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;
        //get the current time
        beforeTime = System.currentTimeMillis();

        while (true) { //this loop runs once ever 25 ms (the DELAY)

            //update the balls position   
            updateGame();
            //redraws the screen (calling the paint component method)
            repaint();
            //this fixes the issue with lag caused Linux graphics scheduling
            if (System.getProperty("os.name").equals("Linux")) {
                Toolkit.getDefaultToolkit().sync();
            }
            //calculate how much time has passed since the last call
            //this allows smooth updates and our ball will move at a constant speed (as opposed to being dependent on processor availability)
            timeDiff = System.currentTimeMillis() - beforeTime;

            //calculate how much time to wait before the next call
            sleep = DELAY - timeDiff;

            //always wait at least 2 ms
            if (sleep < 0) {
                sleep = 2;
            }

            //try to actually wait
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                //threads can be interrupted from other threads
                JOptionPane.showMessageDialog(this, "Thread interrupted: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            //get the new current time
            beforeTime = System.currentTimeMillis();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    //the methods below are required by the MouseListener interface, but we aren't adding any actions to them
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            //always quit if escape is pressed
            if (key == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
            if (gameState == States.PLAY_2P) {
                if (key == KeyEvent.VK_W) { //player 1 up
                    player1.setSpeedY(-5);
                } else if (key == KeyEvent.VK_S) { //player 1 down
                    player1.setSpeedY(5);
                }

                if (key == KeyEvent.VK_UP) { //player 2 up
                    player2.setSpeedY(-5);
                } else if (key == KeyEvent.VK_DOWN) { //player 2 down
                    player2.setSpeedY(5);
                }
                //space to pause at any time
                if (key == KeyEvent.VK_SPACE) {
                    gameState = States.PAUSE;
                }
            } else if (gameState == States.MAIN_MENU) {
                if (key == KeyEvent.VK_UP) {
                    menuLeftCursor.previous();
                    menuRightCursor.previous();
                } else if (key == KeyEvent.VK_DOWN) {
                    menuLeftCursor.next();
                    menuRightCursor.next();
                } else if (key == KeyEvent.VK_ENTER) {
                    //check which position the cursor is in and update state accordingly
                    if (menuLeftCursor.getPosition() == 0) {
                        setupGame();
                        gameState = States.PLAY_2P;
                    } else if (menuLeftCursor.getPosition() == 1) {
                        System.out.println("high scores");
                        gameState = States.PLAY_2P;
                    } else if (menuLeftCursor.getPosition() == 2) {
                        System.exit(0);
                    }

                }
            }else if (gameState == States.PAUSE) {
                //space to unpause
                if (key == KeyEvent.VK_SPACE) {
                    gameState = States.PLAY_2P;
                }
            }else if (gameState == States.P1_SCORED) {
                //space to unpause
                if (key == KeyEvent.VK_SPACE) {
                    resetGameAfterScore("P1");
                    gameState = States.PLAY_2P;
                }
            }else if (gameState == States.P2_SCORED) {
                //space to unpause
                if (key == KeyEvent.VK_SPACE) {
                    resetGameAfterScore("P2");
                    gameState = States.PLAY_2P;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            int key = e.getKeyCode();
            //always quit if escape is pressed
            if (key == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
            if (gameState == States.PLAY_2P) {
                if (key == KeyEvent.VK_W) { //player 1 up
                    player1.setSpeedY(0);
                } else if (key == KeyEvent.VK_S) { //player 1 down
                    player1.setSpeedY(0);
                }

                if (key == KeyEvent.VK_UP) { //player 2 up
                    player2.setSpeedY(0);
                } else if (key == KeyEvent.VK_DOWN) { //player 2 down
                    player2.setSpeedY(0);
                }

                if (key == KeyEvent.VK_PAUSE) {

                }
            }
        }
    }
}
