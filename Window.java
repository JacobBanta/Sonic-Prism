import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Date;

public class Window extends JPanel implements ActionListener, KeyListener, MouseListener {
    // suppress serialization warning
    private static final long serialVersionUID = 490905409104883233L;
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;
    public boolean space;
    public boolean alt;
    public boolean ctrl;
    public boolean shift;
    public Timer timer;
    public player Player;
    public int mousex;
    public int mousey;
    private int opening = 0;
    public boolean isInGame;
    public ground[][] obstacle = new ground[1][3];
    private BufferedImage inImage;
    private BufferedImage outImage;
    private float alpha = 0f;
    private long startTime = -1;
    public boolean fading;
    public  Timer timer2;
    public Window() {
        // set the game board size
        setPreferredSize(new Dimension(500, 500));
        // set the game board background color
        setBackground(new Color(173, 216, 230));
        Player = new player();
        Player.setsize(43,43);
        // this timer will call the actionPerformed() method every 25 ms
        timer = new Timer(25, this);
        timer.start();

        addMouseListener(this);

        obstacle[0][2] = new ground();
        obstacle[0][2].setCollision(100, 100, 996, 196);
        obstacle[0][1] = new ground();
        obstacle[0][1].setCollision(-988, 200, 100, 296);
        obstacle[0][0] = new ground();
        obstacle[0][0].setSlope(0, 200, 100, 100);
        try{
            inImage = ImageIO.read(new File("assets/Sonic_Prism_Logo.png"));
            outImage = ImageIO.read(new File("assets/main menu.png"));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }

        timer2 = new Timer(40, new ActionListener() {//crates new timer
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startTime < 0) {//fade start
                    startTime = System.currentTimeMillis();
                } else {//fade still going

                    long time = System.currentTimeMillis();//gets current time
                    long duration = time - startTime;//finds how long fade has been going
                    if (duration >= 750/*RUNNING_TIME*/) {
                        startTime = -1;
                        ((Timer) e.getSource()).stop();
                        alpha = 0f;
                    } else {
                        alpha = 1f - ((float)duration / (float)750/*(float)RUNNING_TIME*/);
                    }
                    repaint();
                }
            }
        });
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // this method is called by the timer every 25 ms.
        // use this space to update the state of your game or animation
        // before the graphics are redrawn.
        if(isInGame){
        Player.tick();
        Player.isOnGround = false;
        Player.isOnSlope = false;
        for(int x = 0; x < obstacle[0].length; x++){
          obstacle[0][x].checkCollision(Player);
        }
        Player.input(up, left, down, right, space, shift);
        }
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
        if(isInGame){
        // draw our graphics.
          drawBackground(g);
          Player.draw(g, this);
          for(int x = 0; x < obstacle[0].length; x++){
            obstacle[0][x].draw(g, this, (int)Player.posx);
          }
        }else if(opening > 150 && opening < 200){
          opening++;
          Graphics2D g2d = (Graphics2D) g.create();
          g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
          int x = (getWidth() - inImage.getWidth()) / 2;
          int y = (getHeight() - inImage.getHeight()) / 2;
          g2d.drawImage(inImage, x, y, this);

          g2d.setComposite(AlphaComposite.SrcOver.derive(1f - alpha));
          x = (getWidth() - outImage.getWidth()) / 2;
          y = (getHeight() - outImage.getHeight()) / 2;
          g2d.drawImage(outImage, x, y, this);
          g2d.dispose();
        }else{
          opening++;
          drawOpening(g, opening, this);
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
        if (e.getKeyCode() == 16) shift = false;
        if (e.getKeyCode() == 17) ctrl = false;
        if (e.getKeyCode() == 18) alt = false;
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
      if(x > 158 && x < 342 && y > 197 && y < 302){
        isInGame = true;
      }
    }
//name is a bit misleading. this method actually just draws the ground peices
    private void drawBackground(Graphics g) {
        g.setColor(new Color(255, 216, 230));
        for(int x = 0; x < obstacle[0].length; x++){
          if(obstacle[0][x].type == 0){
            g.fillRect(obstacle[0][x].left - (int)Player.posx + 250,obstacle[0][x].top,obstacle[0][x].right - obstacle[0][x].left,obstacle[0][x].bottom - obstacle[0][x].top);
          }
          else if(obstacle[0][x].type == 1){
            g.fillPolygon(new int[] {obstacle[0][x].startx - (int)Player.posx + 250, obstacle[0][x].endx - (int)Player.posx + 250, obstacle[0][x].highx - (int)Player.posx + 250}, new int[] {obstacle[0][x].starty, obstacle[0][x].endy, obstacle[0][x].highy}, 3);
          }
        }
    }
    private void drawOpening(Graphics g, int openin, ImageObserver observer){

      if(openin > 200){
        fading = false;
        drawMenu(g, observer);
      }
      else if(openin < 150){
        drawLogo(g, observer);
      }
      else{
        fading = true;
        alpha = 0f;
        timer2.start();
      }
    }
    private void drawMenu(Graphics g, ImageObserver observer){
      try {
          BufferedImage image = ImageIO.read(new File("assets/main menu.png"));
          g.drawImage(image, 168, 207, observer);
      } catch (IOException exc) {
          System.out.println("Error opening image file: " + exc.getMessage());
      }
    }
    private void drawLogo(Graphics g, ImageObserver observer){
      try {
          BufferedImage image = ImageIO.read(new File("assets/Sonic_Prism_Logo.png"));
          g.drawImage(image, 32, 20, observer);
      } catch (IOException exc) {
          System.out.println("Error opening image file: " + exc.getMessage());
      }
    }
}
