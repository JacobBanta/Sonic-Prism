import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ground {
  public int top, bottom, left, right, type, startx, starty, endx, endy, highx, highy, lowx, lowy;
  public BufferedImage concatImage2;
  private BufferedImage image;

  public ground() {

  }

  public ground(int type, int[] points) {
    if (type == 0 && points.length == 4) {
      setCollision(points[0], points[1], points[2], points[3]);
    }
    if (type == 1 && points.length == 4) {
      setSlope(points[0], points[1], points[2], points[3]);
    }
  }

  public static BufferedImage convertToARGB(BufferedImage image) {
    BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = newImage.createGraphics();
    g.drawImage(image, 0, 0, null);
    g.dispose();
    return newImage;
  }

  public void setCollision(int x1, int y1, int x2, int y2) {
    type = 0;
    if (y1 > y2) {
      top = y2;
      bottom = y1;
    } else {
      top = y1;
      bottom = y2;
    }
    if (x1 > x2) {
      left = x2;
      right = x1;
    } else {
      left = x1;
      right = x2;
    }
    try {
      image = ImageIO.read(new File(System.getenv("temp") + "/SonicPrism/assets/groundTile.png"));
    } catch (IOException exc) {
      System.out.println("Error opening image file: " + exc.getMessage());
    }
    int tileHeight = (int) ((bottom - top) / 32);
    int tileWidth = (int) ((right - left) / 32);
    BufferedImage images[] = new BufferedImage[tileHeight];
    for (int j = 0; j < images.length; j++) {
      images[j] = image;
    }
    int heightTotal = 0;
    for (int j = 0; j < images.length; j++) {
      heightTotal += images[j].getHeight();
    }

    int heightCurr = 0;
    BufferedImage concatImage = new BufferedImage(32, heightTotal, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = concatImage.createGraphics();
    for (int j = 0; j < images.length; j++) {
      g2d.drawImage(images[j], 0, heightCurr, null);
      heightCurr += images[j].getHeight();
    }
    g2d.dispose();
    BufferedImage images2[] = new BufferedImage[tileWidth];
    for (int j = 0; j < images2.length; j++) {
      images2[j] = concatImage;
    }
    int heightTotal2 = 0;
    for (int j = 0; j < images2.length; j++) {
      heightTotal2 += images2[j].getWidth();
    }

    int heightCurr2 = 0;
    concatImage2 = new BufferedImage(heightTotal2, heightTotal, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d2 = concatImage2.createGraphics();
    for (int j = 0; j < images2.length; j++) {
      g2d2.drawImage(images2[j], heightCurr2, 0, null);
      heightCurr2 += images2[j].getWidth();
    }
    g2d2.dispose();
  }

  public void setSlope(int x1, int y1, int x2, int y2) {
    if (y1 > y2) {
      highy = y1;
      lowy = y2;
    } else {
      highy = y2;
      lowy = y1;
    }
    if (x1 > x2) {
      highx = x1;
      lowx = x2;
    } else {
      highx = x2;
      lowx = x1;
    }
    type = 1;
    startx = x1;
    starty = y1;
    endx = x2;
    endy = y2;
    try {
      image = ImageIO.read(new File(System.getenv("temp") + "/SonicPrism/assets/groundTile.png"));
    } catch (IOException exc) {
      System.out.println("Error opening image file: " + exc.getMessage());
    }
    int tileHeight = (int) ((highy - lowy) / 32);
    int tileWidth = (int) ((highx - lowx) / 32);
    BufferedImage images[] = new BufferedImage[tileHeight];
    for (int j = 0; j < images.length; j++) {
      images[j] = image;
    }
    int heightTotal = 0;
    for (int j = 0; j < images.length; j++) {
      heightTotal += images[j].getHeight();
    }

    int heightCurr = 0;
    BufferedImage concatImage = new BufferedImage(32, heightTotal, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = concatImage.createGraphics();
    for (int j = 0; j < images.length; j++) {
      g2d.drawImage(images[j], 0, heightCurr, null);
      heightCurr += images[j].getHeight();
    }
    g2d.dispose();
    BufferedImage images2[] = new BufferedImage[tileWidth];
    for (int j = 0; j < images2.length; j++) {
      images2[j] = concatImage;
    }
    int heightTotal2 = 0;
    for (int j = 0; j < images2.length; j++) {
      heightTotal2 += images2[j].getWidth();
    }

    int heightCurr2 = 0;
    concatImage2 = new BufferedImage(heightTotal2, heightTotal, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d2 = concatImage2.createGraphics();
    for (int j = 0; j < images2.length; j++) {
      g2d2.drawImage(images2[j], heightCurr2, 0, null);
      heightCurr2 += images2[j].getWidth();
    }
    g2d2.dispose();
    concatImage2 = convertToARGB(concatImage2);
    for (int y = 0; y < concatImage2.getHeight(); y++) {
      for (int x = 0; x < concatImage2.getWidth(); x++) {
        if (y + lowy < findy(x + lowx)) {
          int mc = (0 << 24) | 0x00ffffff;
          int pixel = concatImage2.getRGB(x, y);
          int newcolor = pixel & mc;
          concatImage2.setRGB(x, y, newcolor);
        }
      }
    }
  }

  public int findy(int x) {
    if (type == 0) {
      return top;
    }
    if (type == 1) {
      double slope = (((double) starty - (double) endy) / ((double) startx - (double) endx));
      return (int) (slope * x + (starty - (startx * slope)));
    }
    return 0;
  }

  public void draw(Graphics g, ImageObserver observer, int x) {
    if (type == 0) {
      g.drawImage(concatImage2, (int) (left - x + 250), (int) (top), observer);
    }
    if (type == 1) {
      g.drawImage(concatImage2, (int) (lowx - x + 250), (int) (lowy), observer);
    }
  }

  public void checkCollision(player p) {
    if (type == 0) {
      if (left < p.right && right > p.left && top < p.bottom && bottom > p.top) {
        // System.out.println("colliding with box");
        if (p.right > left && p.left < left && p.bottom > top && p.top < top) {
          if (p.right - left > p.bottom - top) {
            p.setpos(p.posx, top - p.height / 2);
            p.setvel(p.velx, 0);
            p.isOnGround = true;
          }
          if (p.right - left < p.bottom - top) {
            p.setpos(left - p.width / 2, p.posy);
            // p.setvel(0 ,p.vely);
          }
        } else if (p.right > left && p.left < left && bottom > p.top && p.bottom > bottom) {
          if (p.right - left > bottom - p.top) {
            p.setpos(p.posx, bottom + p.height / 2);
            p.setvel(p.velx, 0);
          }
          if (p.right - left < bottom - p.top) {
            p.setpos(left - p.width / 2, p.posy);
            p.setvel(0, p.vely);
          }
        } else if (right > p.left && p.right > right && p.bottom > top && p.top < top) {
          if (right - p.left > p.bottom - top) {
            p.setpos(p.posx, top - p.height / 2);
            p.setvel(p.velx, 0);
            p.isOnGround = true;
          }
          if (right - p.left < p.bottom - top) {
            p.setpos(right + p.width / 2, p.posy);
            // p.setvel(0 ,p.vely);
          }
        } else if (right > p.left && p.right > right && bottom > p.top && p.bottom > bottom) {
          if (right - p.left > bottom - p.top) {
            p.setpos(p.posx, bottom + p.height / 2);
            p.setvel(p.velx, 0);
          }
          if (right - p.left < bottom - p.top) {
            p.setpos(right + p.width / 2, p.posy);
            p.setvel(0, p.vely);
          }
        } else if (p.right > left && p.left < left) {
          p.setpos(left - p.width / 2, p.posy);
          p.setvel(0, p.vely);
        } else if (right > p.left && p.right > right) {
          p.setpos(right + p.width / 2, p.posy);
          p.setvel(0, p.vely);
        } else if (p.bottom > top && p.top < top) {
          p.setpos(p.posx, top - p.height / 2);
          p.setvel(p.velx, 0);
          p.isOnGround = true;
        } else if (bottom > p.top && p.bottom > bottom) {
          p.setpos(p.posx, bottom + p.height / 2);
          p.setvel(p.velx, 0);
        }
      }
    } else if (type == 1) {
      if (p.left + 1 > startx && p.left < endx) {
        if (p.bottom > findy((int) p.left)) {
          p.posy = findy((int) p.left) - (p.height / 2);
          p.setvel(p.velx, 0);
          p.isOnGround = true;
          p.isOnSlope = true;
        }
      }
      if (p.right > startx && p.right - 1 < endx) {
        if (p.bottom > findy((int) p.right)) {
          p.posy = findy((int) p.right) - (p.height / 2);
          p.setvel(p.velx, 0);
          p.isOnGround = true;
          p.isOnSlope = true;
        }
      }
    }
  }
}
