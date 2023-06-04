import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Masher_Gabuccho extends Enemy {
	private static BufferedImage image1, image2;
	private int x, y1, y2;

	public Masher_Gabuccho(int x1, int y1, int y2) {
		this.x = x1;
		this.y1 = y1;
		this.y2 = y2;
		if (image1 == null) {
			try {
				BufferedImage image1 = ImageIO
						.read(new File(System.getenv("temp") + "/SonicPrism/assets/Buzzsher.png"))
						.getSubimage(194, 27, 24, 32);
				Masher_Gabuccho.image1 = new BufferedImage(24, 32, BufferedImage.TYPE_INT_ARGB);
				for (int x = 0; x < image1.getWidth(); x++) {
					for (int y = 0; y < image1.getHeight(); y++) {
						if (image1.getRGB(x, y) == new Color(16, 112, 132).getRGB()) {
							Masher_Gabuccho.image1.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
						} else {
							Masher_Gabuccho.image1.setRGB(x, y, image1.getRGB(x, y));
						}
					}
				}
				BufferedImage image2 = ImageIO
						.read(new File(System.getenv("temp") + "/SonicPrism/assets/Buzzsher.png"))
						.getSubimage(226, 27, 32, 32);
				Masher_Gabuccho.image2 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
				for (int x = 0; x < image2.getWidth(); x++) {
					for (int y = 0; y < image2.getHeight(); y++) {
						if (image2.getRGB(x, y) == new Color(16, 112, 132).getRGB()) {
							Masher_Gabuccho.image2.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
						} else {
							Masher_Gabuccho.image2.setRGB(x, y, image2.getRGB(x, y));
						}
					}
				}
			} catch (IOException exc) {
				System.out.println("Error opening image file: " + exc.getMessage());
			}
		}
	}

	@Override
	public void draw(Graphics g, int x) {
		if (System.currentTimeMillis() / 500 % 2 == 0) {
			g.drawImage(image1, -x + this.x, getY(), null);
		} else {
			g.drawImage(image2, -x + this.x, getY(), null);
		}
	}

	private int getY() {
		double percentage = System.currentTimeMillis() % 2500 / (double) 1250;
		if (percentage < 1) {
			return (int) (percentage * (y2 - y1) + y1);
		} else {
			return (int) ((percentage - 2) * (y1 - y2) + y1);
		}
	}

	@Override
	public void checkCollision(player p) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'checkCollision'");
	}

}
