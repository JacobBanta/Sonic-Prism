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
  public int animate = 0;
  public float termVelx = 10;
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
  public player() {
      // load the assets
      loadImage();
  }
  private void loadImage() {
      try {
          // you can use just the filename if the image file is in your
          // project folder, otherwise you need to provide the file path.
          image = ImageIO.read(new File("Sonic_Prime_Test_Sprites_-_Sprite_Sheet.png")).getSubimage(9,26,43,43);
      } catch (IOException exc) {
          System.out.println("Error opening image file: " + exc.getMessage());
      }
  }
  private void animate_run_right(){
    facing_left = false;
    try {
      image = ImageIO.read(new File("Sonic_Prime_Test_Sprites_-_Sprite_Sheet.png")).getSubimage(9 + 63 * animate,110,43,43);
    } catch (IOException exc) {
        System.out.println("Error opening image file: " + exc.getMessage());
    }
    animate++;
    if(animate > 7){
      animate = 0;
    }
  }
  private void animate_run_left(){
    facing_left = true;
    try {
      image = ImageIO.read(new File("Sonic_Prime_Test_Sprites_-_Sprite_Sheet.png")).getSubimage(9 + 63 * animate,110,43,43);
      AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
      image = createTransformed(image, at);
      at = AffineTransform.getRotateInstance(
          Math.PI, image.getWidth()/2, image.getHeight()/2.0);
      image = createTransformed(image, at);
    } catch (IOException exc) {
        System.out.println("Error opening image file: " + exc.getMessage());
    }
    animate++;
    if(animate > 7){
      animate = 0;
    }
  }
  private void animate_still(){
    try {
        image = ImageIO.read(new File("Sonic_Prime_Test_Sprites_-_Sprite_Sheet.png")).getSubimage(9,26,43,43);
    } catch (IOException exc) {
        System.out.println("Error opening image file: " + exc.getMessage());
    }
    if(facing_left){
        AffineTransform at = new AffineTransform();
          at.concatenate(AffineTransform.getScaleInstance(1, -1));
          at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
        image = createTransformed(image, at);
        at = AffineTransform.getRotateInstance(
            Math.PI, image.getWidth()/2, image.getHeight()/2.0);
        image = createTransformed(image, at);
    }
  }
  private static BufferedImage createTransformed(
      BufferedImage image, AffineTransform at)
  {
      BufferedImage newImage = new BufferedImage(
          image.getWidth(), image.getHeight(),
          BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = newImage.createGraphics();
      g.transform(at);
      g.drawImage(image, 0, 0, null);
      g.dispose();
      return newImage;
  }
  public void draw(Graphics g, ImageObserver observer) {
      // with the Point class, note that pos.getX() returns a double, but
      // pos.x reliably returns an int. https://stackoverflow.com/a/30220114/4655368
      // this is also where we translate board grid position into a canvas pixel
      // position by multiplying by the tile size.
      g.drawImage(image, (int)(250 - width / 2), (int)(posy - height / 2), observer);
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
    setpos(posx + velx, posy + vely);
    if(vely < -1){
      try {
          image = ImageIO.read(new File("Sonic_Prime_Test_Sprites_-_Sprite_Sheet.png")).getSubimage(971,26,43,43);
      } catch (IOException exc) {
          System.out.println("Error opening image file: " + exc.getMessage());
      }
    }
    else if(velx > .5){
      animate_run_right();
    }
    else if(velx < -.5){
      animate_run_left();
    }
    else{
      animate_still();
    }
    setvel(velx * (float).95, vely + (float).4);
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
  }
  public void input(boolean up, boolean left, boolean down, boolean right, boolean space, boolean shift){
    if(up){
      //setvel(velx, vely - 10);
    }
    if(down){
      setvel(velx, vely + (float).5);
    }
    if(left && !right){
      setvel(velx - (float).5,vely);
    }
    if(right && !left){
      setvel(velx + (float).5,vely);
    }
    if(shift){
      setvel(0,0);
    }
    if(space){
      if(isOnGround){
        setvel(velx, vely - 10);
      }
    }
  }
    //w = 87 a = 65 s = 83 d = 68 space = 32 shift = 16 ctrl = 17 alt = 18
}
