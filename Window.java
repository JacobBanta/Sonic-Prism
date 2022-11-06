import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
public class Window extends JPanel implements ActionListener, KeyListener {
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
    public ground[] obstacle = new ground[3];
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

        obstacle[2] = new ground();
        obstacle[2].setCollision(100, 100, 996, 196);
        obstacle[1] = new ground();
        obstacle[1].setCollision(-988, 200, 100, 296);
        obstacle[0] = new ground();
        obstacle[0].setSlope(0, 200, 100, 100);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // this method is called by the timer every 25 ms.
        // use this space to update the state of your game or animation
        // before the graphics are redrawn.

        Player.tick();
        Player.isOnGround = false;
        Player.isOnSlope = false;
        for(int x = 0; x < obstacle.length; x++){
          obstacle[x].checkCollision(Player);
        }
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
        Player.draw(g, this);
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
}
