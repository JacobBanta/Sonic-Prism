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
  public int stillTimer = 0;
  public boolean lookingUp;
  public boolean updir;
  public boolean downdir;
  public boolean leftdir;
  public boolean rightdir;
  public int animateIdle = 0;
  public int animateRun = 0;
  public int animatefast = 0;
  public int animateTurn = 0;
  public float termVelx = 15;
  public float termVely = 10;
  public float width = 1;
  public float height = 1;
  public float posx = 0;
  public float posy = 0;
  public float velx;
  public float vely;
  public float top;
  public float bottom;
  public float left;
  public float right;
  public boolean isOnGround = false;
  private BufferedImage image;
  public boolean facing_left;
  //class construtor(called when player is created)
  public player() {
      // load the assets
      loadImage();
  }
  //loads the base image
  private void loadImage() {
      setimage(9,26,43,43);
  }
  //animates the basic movement for the right direction
  private void animate_run_right(){
    facing_left = false;
    setimage(9 + 63 * animateRun,110,43,43);
    animateRun++;
    if(animateRun > 7){
      animateRun = 0;
    }
  }
  //animates the basic movement for the left direction
  private void animate_run_left(){
    facing_left = true;
    setimage(9 + 63 * animateRun,110,43,43);
    mirror_image();
    animateRun++;
    if(animateRun > 7){
      animateRun = 0;
    }
  }
  //animates the full speed movement for the right direction
  private void animate_run_fast_right(){
    facing_left = false;
    setimage(529 + 63 * animatefast,110,43,43);
    animatefast++;
    if(animatefast > 3){
      animatefast = 0;
    }
  }
  //animates the full speed movement for the left direction
  private void animate_run_fast_left(){
    facing_left = true;
    setimage(529 + 63 * animatefast,110,43,43);
    mirror_image();
    animatefast++;
    if(animatefast > 3){
      animatefast = 0;
    }
  }
  //animates the looking up motion
  private void animate_looking_up(){
    if((velx > -.2 && velx < .2) && (vely < .5 && vely > -.5)){
      if(!lookingUp){
        setimage(688,26,43,43);
        if(facing_left){
          mirror_image();
        }
        lookingUp = true;
      }
      else if(lookingUp){
        setimage(688 + 63,26,43,43);
        if(facing_left){
          mirror_image();
        }
      }
    }
    else{
      lookingUp = false;
    }
  }
  //sets the image to be standing still
  private void animate_still(){
    setimage(9,26,43,43);
    if(facing_left){
        mirror_image();
    }
  }
  //animates the idle motion
  private void animate_idle(){
    setimage(88 + 63 * (int)(animateIdle / 4),26,43,43);
    animateIdle++;
    if(animateIdle > 19){
      animateIdle = 0;
    }
  }
  private void animate_turn_left(){
    setimage(797 + 63 * animateTurn,110,43,43);
    animateTurn++;
    if(animateTurn > 3){
      animateTurn = 0;
      setvel(0, vely);
      facing_left = true;
    }
  }
  private void animate_turn_right(){
    setimage(797 + 63 * animateTurn,110,43,43);
    mirror_image();
    animateTurn++;
    if(animateTurn > 3){
      animateTurn = 0;
      setvel(0, vely);
      facing_left = false;
    }
  }
  //invoked for setting the image
  private void setimage(int x, int y, int w, int h){
    try {
        image = ImageIO.read(new File("Sonic_Prime_Test_Sprites_-_Sprite_Sheet.png")).getSubimage(x,y,w,h);
    } catch (IOException exc) {
        System.out.println("Error opening image file: " + exc.getMessage());
    }
  }
  //invoked for mirroring the image(turning right to left)
  private void mirror_image(){
    AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
      image = createTransformed(image, at);
      at = AffineTransform.getRotateInstance(
          Math.PI, image.getWidth()/2, image.getHeight()/2.0);
      image = createTransformed(image, at);
  }
  //invoked by mirror_image
  private static BufferedImage createTransformed(BufferedImage image, AffineTransform at){
      BufferedImage newImage = new BufferedImage(
          image.getWidth(), image.getHeight(),
          BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = newImage.createGraphics();
      g.transform(at);
      g.drawImage(image, 0, 0, null);
      g.dispose();
      return newImage;
  }
  //draws player to screen
  public void draw(Graphics g, ImageObserver observer) {
      // with the Point class, note that pos.getX() returns a double, but
      // pos.x reliably returns an int. https://stackoverflow.com/a/30220114/4655368
      // this is also where we translate board grid position into a canvas pixel
      // position by multiplying by the tile size.
      g.drawImage(image, (int)(250 - width / 2), (int)(posy - height / 2), observer);
  }
  //sets size of player
  public void setsize(float x, float y){
    width = x;
    height = y;
  }
  //sets the position of player
  public void setpos(float x, float y){
    posx = x;
    posy = y;
    top = posy - height / 2;
    bottom = posy + height / 2;
    left = posx - width / 2;
    right = posx + width / 2;
  }
  //sets the velocity of the player
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
  //sets the maximum velocity that the player can reach
  public void setTerminal(float x, float y){
    termVelx = x;
    termVely = y;
  }
  //updates things
  public void tick(){
    setpos(posx + velx, posy + vely);
    if(vely < -.1){
      setimage(971,26,43,43);
      if(facing_left){
        mirror_image();
      }
    }
    else if(velx > .5 && ((!leftdir && rightdir) || (!rightdir && !leftdir))){
      if(velx > 9){
        animate_run_fast_right();
      }
      else{
        animate_run_right();
      }
    }
    else if(velx < -.5 && ((leftdir && !rightdir) || (!rightdir && !leftdir))){
      if(velx < -9){
        animate_run_fast_left();
      }
      else{
        animate_run_left();
      }
    }
    else{
      if(vely < .5){
        stillTimer++;
      }
      animate_still();
    }
    if(stillTimer > 120){
      animate_idle();
    }
    if(updir){
      animate_looking_up();
    }
    else{
      lookingUp = false;
    }
    if(velx > 0){
      facing_left = false;
    }
    else if (velx < 0){
      facing_left = true;
    }
    if(velx < 2 && velx > -2 && (!leftdir && !rightdir) && !(leftdir && rightdir)){
      setvel(velx * (float).5, vely + (float).4);
    }
    else{
      setvel(velx * (float).95, vely + (float).4);
    }
    /*if(posx < 0){
      setpos(500, posy);
    }*/
    if(posy < 0){
      setpos(posx, 500);
    }
    /*if(posx > 500){
      setpos(0, posy);
    }*/
    if(posy > 500){
      setpos(posx, 0);
    }
    if(velx < -4 && velx > -8 && rightdir && !leftdir){
      animate_turn_right();
    }
    else if(velx > 4 && velx < 8 && !rightdir && leftdir){
      animate_turn_left();
    }
    else{
      animateTurn = 0;
    }
  }
  //reacts to the keys being pressed
  public void input(boolean up, boolean left, boolean down, boolean right, boolean space, boolean shift){
    updir = up;
    downdir = down;
    leftdir = left;
    rightdir = right;
    if(up){
      stillTimer = 0;
      //setvel(velx, vely - 10);
    }
    if(down){
      stillTimer = 0;
      setvel(velx, vely + (float).5);
    }
    if(left && !right){
      stillTimer = 0;
      setvel(velx - (float).7,vely);
    }
    if(right && !left){
      stillTimer = 0;
      setvel(velx + (float).7,vely);
    }
    if(shift){
      stillTimer = 0;
      setvel(0,0);
    }
    if(space){
      stillTimer = 0;
      if(isOnGround){
        if(lookingUp){
          setvel(velx, vely - 15);
        }
        else{
          setvel(velx, vely - (float)7.5);
        }
      }
    }
  }
    //w = 87 a = 65 s = 83 d = 68 space = 32 shift = 16 ctrl = 17 alt = 18
}
