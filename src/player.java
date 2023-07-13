import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class player {
	public boolean lookingUp, lookingDown, updir, downdir, leftdir, rightdir, facing_left;
	public boolean isOnGround, wasOnGround, isOnSlope, jumped = false;
	public int stillTimer = 0;
	public int animateIdle, animateRun, animatefast, animateTurn, animateRoll = 0;
	public float termVelx = 15;
	public float termVely = 10;
	public float width, height = 1;
	public float posx, posy = 0;
	public float velx, vely, top, bottom, left, right;
	private BufferedImage image;

	public player() {
		// load the assets
		loadImage();
	}

	private void loadImage() {
		setimage(9, 26, 43, 43);
	} 

	private void animate_run() {
		if (isOnSlope) {
			setimage(9 + 63 * animateRun, 194, 43, 43);
		} else {
			setimage(9 + 63 * animateRun, 110, 43, 43);
		}
		animateRun++;
		if (animateRun > 7) {
			animateRun = 0;
		}
	}

	private void animate_run_fast() {
		if (isOnSlope) {
			setimage(529 + 63 * animatefast, 194, 43, 43);
		} else {
			setimage(529 + 63 * animatefast, 110, 43, 43);
		}
		animatefast++;
		if (animatefast > 3) {
			animatefast = 0;
		}
	}

	private void animate_roll() {
		if (animateRoll <= 4) {
			setimage(797 + 63 * animateRoll, 194, 43, 43);
		} else {
			setimage(9 + 63 * (animateRoll - 5), 290, 43, 43);
		}
		animateRoll++;
		if (animateRoll > 10) {
			animateRoll = 5;
		}
	}

	private void animate_looking_up() {
		if ((velx > -.2 && velx < .2) && (vely < .5 && vely > -.5)) {
			if (!lookingUp) {
				setimage(688, 26, 43, 43);
				if (facing_left) {
					mirror_image();
				}
				lookingUp = true;
			} else if (lookingUp) {
				setimage(688 + 63, 26, 43, 43);
				if (facing_left) {
					mirror_image();
				}
			}
		} else {
			lookingUp = false;
		}
	}

	private void animate_looking_down() {
		if ((velx > -.2 && velx < .2) && (vely < .5 && vely > -.5)) {
			if (!lookingDown) {
				setimage(829, 26, 43, 43);
				if (facing_left) {
					mirror_image();
				}
				lookingDown = true;
			} else if (lookingDown) {
				setimage(829 + 63, 26, 43, 43);
				if (facing_left) {
					mirror_image();
				}
			}
		}
	}

	private void animate_still() {
		setimage(9, 26, 43, 43);
		if (facing_left) {
			mirror_image();
		}
	}

	private void animate_idle() {
		setimage(88 + 63 * (int) (animateIdle / 4), 26, 43, 43);
		if (facing_left) {
			mirror_image();
		}
		animateIdle++;
		if (animateIdle > 19) {
			animateIdle = 0;
		}
	}

	private void animate_turn_left() {
		setimage(797 + 63 * animateTurn, 110, 43, 43);
		animateTurn++;
		if (animateTurn > 3) {
			animateTurn = 0;
			setvel(0, vely);
			facing_left = true;
		}
	}

	private void animate_turn_right() {
		setimage(797 + 63 * animateTurn, 110, 43, 43);
		mirror_image();
		animateTurn++;
		if (animateTurn > 3) {
			animateTurn = 0;
			setvel(0, vely);
			facing_left = false;
		}
	}

	private void setimage(int x, int y, int w, int h) {
		try {
			image = ImageIO
					.read(new File(
							System.getenv("temp") + "/SonicPrism/assets/Sonic_Prime_Test_Sprites_-_Sprite_Sheet.png"))
					.getSubimage(x, y, w, h);
		} catch (IOException exc) {
			System.out.println("Error opening image file: " + exc.getMessage());
		}
	}

	private void mirror_image() {
		AffineTransform at = new AffineTransform();
		at.concatenate(AffineTransform.getScaleInstance(1, -1));
		at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
		image = createTransformed(image, at);
		at = AffineTransform.getRotateInstance(
				Math.PI, image.getWidth() / 2, image.getHeight() / 2.0);
		image = createTransformed(image, at);
	}

	private static BufferedImage createTransformed(BufferedImage image, AffineTransform at) {
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
		g.drawImage(image, (int) (250 - width / 2), (int) (posy - height / 2), observer);
	}

	public void setsize(float x, float y) {
		width = x;
		height = y;
	}

	public void setpos(float x, float y) {
		posx = x;
		posy = y;
		top = posy - height / 2;
		bottom = posy + height / 2;
		left = posx - width / 2;
		right = posx + width / 2;
	}

	public void setvel(float x, float y) {
		velx = x;
		vely = y;
		if (velx > termVelx) {
			velx = termVelx;
		}
		if (velx < -1 * termVelx) {
			velx = -1 * termVelx;
		}
		if (vely > termVely) {
			vely = termVely;
		}
		if (vely < -1 * termVely) {
			vely = -1 * termVely;
		}

		if (posy < 0) {
			posy = 0;
			vely = 0;
		}
	}

	public void setTerminal(float x, float y) {
		termVelx = x;
		termVely = y;
	}

	public void tick() {
		setpos(posx + velx, posy + vely);
		if (jumped && !isOnGround) {
			setimage(971, 26, 43, 43);
			if (facing_left) {
				mirror_image();
			}
		} else if (jumped && isOnGround && vely > -1) {
			jumped = false;
		} else if (velx > .5 && !leftdir) {// moving right
			if (lookingDown) {
				animate_roll();
			} else {
				animateRoll = 0;
				if (velx > 9) {
					animate_run_fast();
				} else {
					animate_run();
				}
			}
		} else if (velx < -.5 && !rightdir) {// moving left
			facing_left = true;
			if (lookingDown) {
				animate_roll();
			} else {
				animateRoll = 0;
				if (velx < -9) {
					animate_run_fast();
				} else {
					animate_run();
				}
			}
			mirror_image();
		} else {// still
			animateRoll = 0;
			if (vely < .5) {
				stillTimer++;
			}
			animate_still();
		}
		if (stillTimer > 120) {// trigger for idle animation
			animate_idle();
		}
		if (updir) {// trigger for looking up
			animate_looking_up();
		} else {
			lookingUp = false;
		}
		if (downdir) {// trigger for crouching
			animate_looking_down();
		} else if (velx < 2 && velx > -2) {// disables crouch
			lookingDown = false;
			animateRoll = 0;
		}
		if (velx > 0) {
			facing_left = false;
		} else if (velx < 0) {
			facing_left = true;
		}
		if (velx < 2 && velx > -2 && !leftdir && !rightdir) {
			setvel(velx * (float) .5, vely + (float) .4);
		} else {
			setvel(velx * (float) .95, vely + (float) .4);
		}
		if (posy > 500) {
			setpos(posx, 0);
		}
		if (velx < -4 && velx > -8 && rightdir && (isOnGround || wasOnGround)) {
			animate_turn_right();
		} else if (velx > 4 && velx < 8 && leftdir && (isOnGround || wasOnGround)) {
			animate_turn_left();
		} else {
			animateTurn = 0;
		}
	}

	public void input(boolean up, boolean left, boolean down, boolean right, boolean space, boolean shift) {
		updir = up;
		downdir = down;
		if (left && right) {
			leftdir = false;
			rightdir = false;
		} else {
			leftdir = left;
			rightdir = right;
		}
		if (up) {
			stillTimer = 0;
			// setvel(velx, vely - 10);
		}
		if (down) {
			stillTimer = 0;
		}
		if (left && !right) {
			stillTimer = 0;
			setvel(velx - (float) .7, vely);
		}
		if (right && !left) {
			stillTimer = 0;
			setvel(velx + (float) .7, vely);
		}
		if (shift) {
			stillTimer = 0;
			setvel(0, 0);
		}
		if (space) {
			stillTimer = 0;
			if (isOnGround) {
				if (lookingUp) {
					setvel(velx, vely - 15);
				} else {
					setvel(velx, vely - (float) 7.5);
				}
				jumped = true;
			}
		}
	}
}
