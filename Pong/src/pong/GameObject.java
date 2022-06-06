/*
 * B Cutten
 * May 27, 2022
 * A generic object in the game
 */
package pong;

public class GameObject {
    protected int x, y;
    protected double speedX, speedY;
    protected double accX, accY;

    /**
     * Primary constructor
     * @param x - the x position of the object
     * @param y - the y position of the object
     */
    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
        speedX = 0;
        speedY = 0;
        accX = 0;
        accY = 0;
    }
    
    /**
     * Secondary constructor, everything's zero
     */
    public GameObject(){
        this(0,0);
    }

    /**
     * Updates the speed based on the acceleration and the location of the object based on the speed
     */
    public void update(){
        speedX += accX;
        speedY += accY;
        x+=speedX;
        y+=speedY;
    }
   
    /**
     * Getter for the x coordinate
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Setter for the x coordinate
     * @param x - the new x value
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Getter for the y coordinate
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Setter for the y coordinate
     * @param y - the new y value
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Getter for the x speed
     * @return the x speed
     */
    public double getSpeedX() {
        return speedX;
    }

    /**
     * Setter for the x speed
     * @param speedX - the new x speed
     */
    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    /**
     * Getter for the y speed
     * @return the y speed
     */
    public double getSpeedY() {
        return speedY;
    }

    /**
     * Setter for the y speed
     * @param speedY - the new y speed
     */
    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    /**
     * Getter for the x acceleration
     * @return the x acceleration
     */
    public double getAccX() {
        return accX;
    }

    
    /**
     * Setter for the x acceleration
     * @param accX - the new x acceleration
     */
    public void setAccX(double accX) {
        this.accX = accX;
    }

    /**
     * Getter for the y acceleration
     * @return the y acceleration
     */
    public double getAccY() {
        return accY;
    }

    /**
     * Setter for the y acceleration
     * @param accY - the new y acceleration
     */
    public void setAccY(double accY) {
        this.accY = accY;
    }

     /***
     * Compares two GameObjects to see if they are the same
     * @param obj - the object to compare to this GameObject
     * @return true if they are the same
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) { //same object?
            return true;
        }
        if (obj == null) {//other one is not existing?
            return false;
        }
        if (getClass() != obj.getClass()) { //different types?
            return false;
        }
        final GameObject other = (GameObject) obj; //cast down
        
        //now compare all of the attributes to see if they are the same
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (Double.doubleToLongBits(this.speedX) != Double.doubleToLongBits(other.speedX)) {
            return false;
        }
        if (Double.doubleToLongBits(this.speedY) != Double.doubleToLongBits(other.speedY)) {
            return false;
        }
        if (Double.doubleToLongBits(this.accX) != Double.doubleToLongBits(other.accX)) {
            return false;
        }
        if (Double.doubleToLongBits(this.accY) != Double.doubleToLongBits(other.accY)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GameObject{" + "x=" + x + ", y=" + y + ", speedX=" + speedX + ", speedY=" + speedY + ", accX=" + accX + ", accY=" + accY + '}';
    }
    
    
    
    
}
