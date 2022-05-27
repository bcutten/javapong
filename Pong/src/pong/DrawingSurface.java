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
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
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

public class DrawingSurface extends JPanel implements MouseListener, Runnable {

    private Thread animator;
    private final int DELAY = 25;
    private States gameState;
    private Font titleFont;
    private Font menuFont;
    private Animation menuCursor;
    private int ticks = 0;

    //these are the different phases the game can be on
    enum States {
        MAIN_MENU,
        PLAY,
        PAUSE,
        GAME_OVER
    }

    public DrawingSurface() { //constructor for the panel

        gameState = States.MAIN_MENU;//start with the menu screen

        //attach the mouse listener to the panel and give it "focus"
        this.addMouseListener(this);
        this.setFocusable(true);
        this.requestFocus();
        loadFont();
        loadImages();
    }

    /**
     * Get the images from the spritesheet and load em up
     */
    private void loadImages() {
        try {
            URL url;
            BufferedImage img;
            int w = getSize().width;
            int h = getSize().height;
            menuCursor = new Animation();

            for (int i = 1; i <= 7; i++) {
                url = DrawingSurface.class.getResource("star_0" + i + ".png");
                System.out.println(url);
                img = ImageIO.read(url);
                menuCursor.addImage(img.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
            }
            System.out.println(menuCursor);
        } catch (IOException e) {
            System.out.println("Unable to load images");
        }

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
        } catch (IOException | FontFormatException e) {
            System.out.println("Unable to load fonts");
            titleFont = new Font("Franklin Gothic Medium", Font.PLAIN, 96);
            menuFont = new Font("Franklin Gothic Medium", Font.PLAIN, 32);
        }

    }

    //does the actual drawing, depending on which state the game is in
    private void doDrawing(Graphics g) {
        //the Graphics2D class is the class that handles all the drawing
        //must be casted from older Graphics class in order to have access to some newer methods
        Graphics2D g2d = (Graphics2D) g;
        if (gameState == States.MAIN_MENU) {
            drawMainMenu(g2d);
        } else if (gameState == States.PLAY) {
            drawGame(g2d);
        }

    }

    /**
     * This will do all of the drawing needed for the main menu scene
     *
     * @param - the graphics object to draw with
     */
    private void drawMainMenu(Graphics2D g2d) {

        int w = getSize().width;
        int h = getSize().height;

        //fill black
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, w, h);

        //drawGrid(g2d, w, h); //to help with placement
        //draw menu text (https://zetcode.com/gfx/java2d/textfonts/)
        g2d.setColor(Color.WHITE);
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        g2d.setFont(titleFont);
        //using some magic numbers to centre on the screen
        g2d.drawString("PONG", w / 2 - 135, h / 2 - 150);
        g2d.setFont(menuFont);
        g2d.drawString("PLAY", w / 2 - 40, h / 2 - 50);
        g2d.drawString("HIGH SCORES", w / 2 - 105, h / 2);
        g2d.drawString("QUIT", w / 2 - 40, h / 2 + 50);

        //draw the menu cursors
        g2d.drawImage(menuCursor.getImage(), w / 2 - 160, h / 2 - 50, null);
    }

    /**
     * This will do all of the drawing during the main game play
     *
     * @param - the graphics object to draw with
     */
    private void drawGame(Graphics2D g2d) {
        int w = getSize().width;
        int h = getSize().height;
        //fill black
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, w, h);
        //draw score

        //draw paddles
        //draw ball
    }

    /**
     * This will draw a grid on the screen to help with placement of things
     *
     * @param g2d - the graphics object to draw with
     * @param w - the width of the screen
     * @param h - the height of the scree
     */
    private void drawGrid(Graphics2D g2d, int w, int h) {
        g2d.setColor(Color.WHITE);
        //draw vertical lines
        for (int x = 0; x < w; x += 50) {
            g2d.drawLine(x, 0, x, h);
        }

        //draw horizontal lines
        for (int y = 0; y < h; y += 50) {
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

    }

    private void updateMenu() {
        menuCursor.cycle(ticks);
    }

    //update the game depending on which state it's in
    public void updateGame() {
        ticks++;
        if (ticks > 60) {
            ticks = ticks % 60;
        }
        System.out.println(ticks);
        if (gameState == States.MAIN_MENU) {
            updateMenu();
        } else if (gameState == States.PLAY) {
            updateGamePlay();
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
}
