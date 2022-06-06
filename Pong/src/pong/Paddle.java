/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pong;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author cutten
 */
public class Paddle extends GameObject{
    protected int width;
    protected int height;
    private Color c;

    /**
     * Create a new paddle
     * @param width - the width of the paddle
     * @param height - the height of the paddle
     * @param x - the x location of the top left corner of the paddle
     * @param y - the y location of the top left corner of the paddle
     */
    public Paddle(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
        c = Color.WHITE;
    }

    /**
     * Getter for the width of the paddle
     * @return the paddle's width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Setter for the width of the paddle
     * @param width - the new width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Getter for the height of the paddle
     * @return the paddle's height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Setter for the height of the paddle
     * @param height - the new height
     */
    public void setHeight(int height) {
        this.height = height;
    }
    
    /**
     * Getter for the color
     * @return the color of the paddle
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
     * Check if this paddle is hitting the given ball
     * @param b - the ball to check for collision
     * @return true if they're hitting, false otherwise
     */
    public boolean isHitting(Ball b){
        //check for collision on all 4 sides of the paddle
        if(b.getY() + b.getRadius() >= y && b.getY() - b.getRadius() <= y + height
                && b.getX() + b.getRadius() >= x && b.getX() <= x + width){
            //add something here to make the ball bounce differentle based on how close to the center of the paddle it is
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Draws the ball based on the state of the attributes
     * @param g2d - the Graphics obj which does the drawing
     */
    public void draw(Graphics2D g2d){
        g2d.setColor(c);
        g2d.fillRect(x,y,width,height);
    }

    @Override
    /**
     * Your standard to String method
     */
    public String toString() {
        return super.toString() + "Paddle{" + "width=" + width + ", height=" + height + '}';
    }
    
    
    
    
}
