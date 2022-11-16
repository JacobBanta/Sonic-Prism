import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.Scanner;
import javax.imageio.ImageIO;
import java.util.Date;
import java.lang.Math;

public class Window extends JPanel implements ActionListener, KeyListener, MouseListener {
    // suppress serialization warning
    private static final long serialVersionUID = 490905409104883233L;
    public boolean up, down, left, right, space, alt, ctrl, shift, fading, isInGame;
    public int mousex, mousey, phase = 1, currentType = 0;
    public int opening = 0;
    public int[] currentGround = new int[5];
    private float alpha = 0f;
    private long startTime = -1;
    public player Player;
    public ground[] obstacle;
    public Timer timer, timer2;
    private BufferedImage inImage, outImage;
    public Scanner sc;
    public Window() {
      loadLevel();
      setPreferredSize(new Dimension(500, 500));
      setBackground(new Color(173, 216, 230));
      Player = new player();
      timer = new Timer(25, this);
      timer.start();
      addMouseListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // this method is called by the timer every 25 ms.
        // use this space to update the state of your game or animation
        // before the graphics are redrawn.
      Player.tick();
      Player.input(up, left, down, right, space, shift);
        // calling repaint() will trigger paintComponent() to run again,
        // which will refresh/redraw the graphics.
        repaint();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // when calling g.drawImage() we can use "this" for the ImageObserver
        // because Component implements the ImageObserver interface, and JPanel
        // extends from Component. So "this" Board instance, as a Component, can
        // react to imageUpdate() events triggered by g.drawImage()
        // draw our graphics.
          drawBackground(g);
          for(int x = 0; x < obstacle.length; x++){
            obstacle[x].draw(g, this, (int)Player.posx);
          }
        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
      }
    @Override
    public void keyTyped(KeyEvent e) {}//necesary for the key presses to work for some reason
    @Override
    public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == 65) left = false;
      if (e.getKeyCode() == 68) right = false;
      if (e.getKeyCode() == 87) up = false;
      if (e.getKeyCode() == 83) down = false;
      if (e.getKeyCode() == 32) space = false;
      if (e.getKeyCode() == 16){
        shift = false;
        obstacle = new ground[0];
      }
      if (e.getKeyCode() == 17){
        ctrl = false;
        loadLevel();
      }
      if (e.getKeyCode() == 18){
        alt = false;
        try {
          FileWriter myWriter = new FileWriter("level.csv");
          String str = String.valueOf(obstacle.length);
          for(int x = 0; x < obstacle.length; x++){
            if(obstacle[x].type == 0){
              str = str + "," + obstacle[x].type + "," + obstacle[x].left + "," + obstacle[x].top + "," + obstacle[x].right + "," + obstacle[x].bottom;
            }else if(obstacle[x].type == 1){
              str = str + "," + obstacle[x].type + "," + obstacle[x].startx + "," + obstacle[x].starty + "," + obstacle[x].endx + "," + obstacle[x].endy;
            }
          }
          myWriter.write(str);
          myWriter.close();
          System.out.println("saved level");
        } catch (IOException eee) {
          System.out.println("An error occurred.");
          eee.printStackTrace();
        }
      }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println(e.getKeyCode());
        if (e.getKeyCode() == 65) left = true;
        if (e.getKeyCode() == 68) right = true;
        if (e.getKeyCode() == 87) up = true;
        if (e.getKeyCode() == 83) down = true;
        if (e.getKeyCode() == 32) space = true;
        if (e.getKeyCode() == 16) shift = true;
        if (e.getKeyCode() == 17) ctrl = true;
        if (e.getKeyCode() == 18) alt = true;
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {
      int x=e.getX();
      int y=e.getY();
      x = x + (int)Player.posx - 250;
      x = Math.round(((float)x / (float)32)) * 32;
      y = Math.round(((float)y / (float)32)) * 32;
      if(phase == 1){
        currentGround[0] = currentType;
        currentGround[1] = x;
        currentGround[2] = y;
        phase = 2;
      }
      else if(phase == 2){
        currentGround[3] = x;
        currentGround[4] = y;
        addObstacle(currentGround);
        phase = 1;
      }
    }
    public void addObstacle(int[] append){
      ground[] newobstacle = new ground[obstacle.length + 1];
      for(int x = 0; x < obstacle.length; x++){
        newobstacle[x] = obstacle[x];
      }
      newobstacle[obstacle.length] = new ground(append[0], new int[] {append[1], append[2], append[3], append[4]});
      obstacle = newobstacle;
    }
//name is a bit misleading. this method actually just draws the ground peices
    private void drawBackground(Graphics g) {
        g.setColor(new Color(255, 216, 230));
        for(int x = 0; x < obstacle.length; x++){
          if(obstacle[x].type == 0){
            g.fillRect(obstacle[x].left - (int)Player.posx + 250,obstacle[x].top,obstacle[x].right - obstacle[x].left,obstacle[x].bottom - obstacle[x].top);
          }
          else if(obstacle[x].type == 1){
            g.fillPolygon(new int[] {obstacle[x].startx - (int)Player.posx + 250, obstacle[x].endx - (int)Player.posx + 250, obstacle[x].highx - (int)Player.posx + 250}, new int[] {obstacle[x].starty, obstacle[x].endy, obstacle[x].highy}, 3);
          }
        }
    }
    public void loadLevel(){
      //parsing a CSV file into Scanner class constructor
      try{
        sc = new Scanner(new File("level.csv"));
      }catch(FileNotFoundException exc){
        System.out.println("something bad happend: ");
      }
      sc.useDelimiter(",");   //sets the delimiter pattern
      int counter = 0;
      int counter2 = 0;
      int[] set = new int[5];
      while (sc.hasNext()){  //returns a boolean value
        counter++;
        if((counter - 2) % 5 == 0 && counter != 2){
          obstacle[counter2] = new ground(set[0], new int[] {set[1], set[2], set[3], set[4]});
          counter2++;
        }
        if (counter == 1){
          obstacle = new ground[Integer.parseInt(sc.next())];
        }
        else{
          set[(counter - 2) % 5] = Integer.parseInt(sc.next());
        }
      }
      obstacle[counter2] = new ground(set[0], new int[] {set[1], set[2], set[3], set[4]});
      sc.close();  //closes the scanner
    }
}
