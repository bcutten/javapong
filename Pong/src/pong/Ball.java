/*
 * B Cutten
 * May 31, 2022
 * Represents a ball, which is one of the game objects
 */
package pong;

import java.awt.Color;
import java.awt.Graphics2D;

public class Ball extends GameObject{
    //attributes of a Ball
    private int radius;
    private Color c;

    /**
     * Create a new ball
     * @param x - the x location of the ball
     * @param y - the y location of the ball
     * @param radius - the size of the ball
     */
    public Ball(int x, int y, int radius) {
        super(x, y);
        this.radius = radius;
        //set defaults
        c = Color.WHITE;
    }
    
    /**
     * Flips the x speed and adds some acceleration
     */
    public void bounceX(){
        speedX = -speedX;
        //accX += 0.1;
    }
    
    /**
     * Flips the y speed and adds some acceleration
     */
    public void bounceY(){
        speedY = -speedY;
        //accY += 0.1;
    }
    
    /**
     * Draws the ball based on the state of the attributes
     * @param g2d - the Graphics obj which does the drawing
     */
    public void draw(Graphics2D g2d){
        g2d.setColor(c);
        g2d.fillOval(x,y,radius,radius);
    }
    
    
    /**
     * Getter for the radius
     * @return the radius of the ball
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Setter for the radius attribute
     * @param radius - the new radius
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }
  
    /**
     * Getter for the color
     * @return the color of the ball
     */
    public Color getColor() {
        return c;
    }

    /**
     * Setter for the color attribute
     * @param c - the new Color
     */
    public void setColor(Color c) {
        this.c = c;
    }

    /**
     * Get a String representation of the ball
     * @return All of the attributes of the Ball in a String
     */
    public String toString() {
        return super.toString() + "Ball {radius=" + radius + ", c=" + c + '}';
    }

    /**
     * Checks if another ball is the same as this one
     * @param other the other ball to compare to this one
     * @return true if they are the same, false otherwise
     */
    public boolean equals(Ball other) {
        //same objects?
        if (this == other) {
            return true;
        }
        //ensure the other ball has been created
        if (other == null) {
            return false;
        }        
        //are the radius' different?
        if (this.radius != other.radius) {
            return false;
        }
        //different colors?
        if (!this.c.equals(other.c)) {
            return false;
        }
        //if all the above checks pass, then the balls are the same
        return true;
    }
    
    
    
    
    
}
