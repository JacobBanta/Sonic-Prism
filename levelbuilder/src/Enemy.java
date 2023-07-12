import java.awt.Graphics;

public abstract class Enemy {
	public abstract void draw(Graphics g, int x);

	public abstract void checkCollision(player p);
}
