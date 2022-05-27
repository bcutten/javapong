/*
 * B Cutten
 * May 26, 2022
 * A nifty animation class, essentially just a list of images that cycles
 */
package pong;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author cutten
 */
public class Animation extends GameObject {

    private final ArrayList<Image> images;
    private int currentImage;
    private int speed;

    public Animation() {
        images = new ArrayList();
        currentImage = 0;
        speed = 5;
    }

    /**
     * Add a new image to the end of the animation
     *
     * @param image - the image to add
     */
    public void addImage(Image image) {
        images.add(image);
    }

    /**
     * Restarts the animation
     */
    public void reset() {
        currentImage = 0;
    }

    /**
     * Moves to the next image in the animation
     *
     * @return the current image
     */
    public Image cycle(int ticks) {
        if (ticks % speed == 0) {
            currentImage++; //move to the next image

            //loop back to the start
            if (currentImage > images.size() - 1) {
                currentImage = 0;
            }
        }
        return images.get(currentImage);
    }

    /**
     * This returns the current image
     *
     * @return the current image
     */
    public Image getImage() {
        return images.get(currentImage);
    }

    public void draw(Graphics2D g2d) {

        System.out.println(x + " " + y);
        g2d.drawImage(images.get(currentImage), x, y, null);
    }

    /**
     * *
     * Compares two Animations to see if they are the same
     *
     * @param obj - the object to compare to this animation
     * @return true if they are the same
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) { //same reference?
            return true;
        }
        if (obj == null) { //no object exists?
            return false;
        }
        if (getClass() != obj.getClass()) { //different types?
            return false;
        }
        final Animation other = (Animation) obj; //cast down

        //compare references 
        if (!Objects.equals(this.images, other.images)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Animation{" + "images=" + images + ", currentImage=" + currentImage + '}';
    }

}
