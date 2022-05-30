/*
 * B Cutten
 * May 30, 2022
 * A cursor class, so that the user can select a menu item
 */
package pong;

import java.util.ArrayList;

public class Cursor extends Animation{
    
    private int index;
    private ArrayList<Integer> xPositions;
    private ArrayList<Integer> yPositions;

    /**
     * Create a new cursor at the given coordinate
     * @param x - the x position
     * @param y - the y position
     */
    public Cursor(int x, int y) {
        index = 0;
        xPositions = new ArrayList();
        xPositions.add(x);
        yPositions = new ArrayList();
        yPositions.add(y);
    }
    
    /**
     * Move cursor to the next index in the list
     */
    public void next(){
        index++;
        //wrap around to the start
        if (index > xPositions.size() - 1){
            index = 0;
        }
    }
    
    /**
     * Move cursor to the previous position
     */
    public void previous(){
        index--;
        //wrap around to the start
        if (index < 0){
            index = xPositions.size() - 1;
        }
    }

    /**
     * The current x position to draw the cursor at
     * @return the x coordinate
     */
    public int getCurrentX(){
        return xPositions.get(index);
    }
    
    
    /**
     * The current y position to draw the cursor at
     * @return the y coordinate
     */
    public int getCurrentY(){
        return yPositions.get(index);
    }
    
    /**
     * Get the current index of the cursor
     * @return - the index of he cursor
     */
    
    public int getPosition() {
        return index;
    }

    /**
     * Change the current index of the cursor
     * @param position - the new position
     */
    public void setPosition(int position) {
        this.index = position;
    }

    /**
     * Add a new position for the cursor
     * @param x - the x coordinate
     * @param y - the y coordinate
     */
    public void addPosition(int x, int y){
        xPositions.add(x);
        yPositions.add(y);
        
    }

    @Override
    public String toString() {
        return super.toString() + "Cursor{" + "position=" + index + ", xPositions=" + xPositions + ", yPositions=" + yPositions + '}';
    }
    
    
    
    
    
    
}
