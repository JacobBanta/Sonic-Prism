import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;
import java.util.Date;

class Main {

    private static void initWindow() {
        // create a window frame and set the title in the toolbar
        JFrame window = new JFrame("Sonic Prism");
        try {
          window.setIconImage(ImageIO.read(new File("assets/Sonic_Prism_Logo.png")));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
        // when we close the window, stop the app
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // create the jpanel to draw on.
        // this also initializes the game loop
        Window board = new Window();
        // add the jpanel to the window
        window.add(board);
        // pass keyboard inputs to the jpanel
        window.addKeyListener(board);

        // don't allow the user to resize the window
        window.setResizable(false);
        // fit the window size around the components (just our jpanel).
        // pack() should be called after setResizable() to avoid issues on some platforms
        window.pack();
        // open window in the center of the screen
        window.setLocationRelativeTo(null);


        // display the window
        window.setVisible(true);
    }

    public static void main(String[] args) {
        // invokeLater() is used here to prevent our graphics processing from
        // blocking the GUI. https://stackoverflow.com/a/22534931/4655368
        // this is a lot of boilerplate code that you shouldn't be too concerned about.
        // just know that when main runs it will call initWindow() once.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initWindow();
            }
        });
    }
}
