/*
 * B Cutten
 * May 26, 2022
 * A Java version of Pong
 */
package pong;

import java.awt.EventQueue;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author cutten
 */
public class Pong extends JFrame{

    //constructor
    public Pong() {
        //create the User interface
        initUI();
    }

    //create the custom JFrame
    private void initUI() {        
        //set title of the JFrame
        setTitle("Pong");
        int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        
        //add a custom JPanel to draw on
        add(new DrawingSurface());
        //set the size of the window to full screen
        setSize(width, height);
        //tell the JFrame what to do when closed
        //this is important if our application has multiple windows
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        //makes sure that GUI updates nicely with the rest of the OS
        EventQueue.invokeLater(() -> {
            JFrame ex = new Pong();
            ex.setVisible(true);
        });
    }
    
}
