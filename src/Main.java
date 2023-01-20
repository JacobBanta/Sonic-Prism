import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

class Main {

    private static void initWindow(int skip) {
      UnzipFiles.call("Sonic-Prism.jar", ".temp");
        // create a window frame and set the title in the toolbar
        JFrame window = new JFrame("Sonic Prism");
        try {
          window.setIconImage(ImageIO.read(new File(".temp/assets/Sonic_Prism_Logo.png")));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
        // when we close the window, stop the app
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // create the jpanel to draw on.
        // this also initializes the game loop
        Window board = new Window();
        if(skip == 1){
          board.opening = 149;
        }
        // add the jpanel to the window
        window.add(board);
        // pass keyboard inputs to the jpanel
        window.addKeyListener(board);

    // don't allow the user to resize the window
    window.setResizable(false);
    // fit the window size around the components (just our jpanel).
    // pack() should be called after setResizable() to avoid issues on some
    // platforms
    window.pack();
    // open window in the center of the screen
    window.setLocationRelativeTo(null);
    // display the window
    window.setVisible(true);
  }

  public static void main(String[] args) {
    /*
     * File directory = new File("./levels");
     * File[] csvFiles = directory.listFiles(new FileFilter() {
     * 
     * @Override
     * public boolean accept(File file) {
     * return file.getName().endsWith(".csv");
     * }
     * });
     * 
     * for (File csvFile : csvFiles) {
     * System.out.println(csvFile.getName());
     * }
     */
    // invokeLater() is used here to prevent our graphics processing from
    // blocking the GUI. https://stackoverflow.com/a/22534931/4655368
    // this is a lot of boilerplate code that you shouldn't be too concerned about.
    // just know that when main runs it will call initWindow() once.
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (args.length > 0) {
          if (Integer.parseInt(args[0]) == 1) {
            initWindow(1);
          } else {
            initWindow(0);
          }
        } else {
          initWindow(0);
        }
      }
    });
  }
}
