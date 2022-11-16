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
public class player{
  public boolean lookingUp, lookingDown, updir, downdir, leftdir, rightdir, facing_left;
  public boolean isOnGround, wasOnGround, isOnSlope, jumped;
  public int stillTimer = 0;
  public int animateIdle, animateRun, animatefast, animateTurn, animateRoll;
  public float termVelx = 15;
  public float termVely = 10;
  public float width = 1, height = 1;
  public float posx = 0, posy = 0;
  public float velx, vely, top, bottom, left, right;
  private BufferedImage image;
  public player(){
    animateIdle = animateRun = animatefast = animateTurn = animateRoll = 0;
    isOnGround = wasOnGround = isOnSlope = jumped = false;
  }
  public void setsize(float x, float y){
    width = x;
    height = y;
  }
  public void setpos(float x, float y){
    posx = x;
    posy = y;
    top = posy - height / 2;
    bottom = posy + height / 2;
    left = posx - width / 2;
    right = posx + width / 2;
  }
  public void setvel(float x, float y){
    velx = x;
    vely = y;
    if(velx > termVelx){
      velx = termVelx;
    }
    if(velx < -1 * termVelx){
      velx = -1 * termVelx;
    }
    if(vely > termVely){
      vely = termVely;
    }
    if(vely < -1 * termVely){
      vely = -1 * termVely;
    }
  }
  public void setTerminal(float x, float y){
    termVelx = x;
    termVely = y;
  }
  public void tick(){
    if(leftdir){
      setpos(posx - 4,posy);
    }
    if(rightdir){
      setpos(posx + 4,posy);
    }
  }
  public void input(boolean up, boolean left, boolean down, boolean right, boolean space, boolean shift){
    if(left && right){
      leftdir = false;
      rightdir = false;
    }else{
      leftdir = left;
      rightdir = right;
    }
  }
}
