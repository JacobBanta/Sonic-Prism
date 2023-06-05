import java.awt.Graphics;
import java.util.ArrayList;

public class Level implements java.io.Serializable{
	private ArrayList<Ground> ground;
	private ArrayList<Enemy> enemies;

	private static final long serialVersionUID = 1L;

	public Level() {
		ground = new ArrayList<>();
	}

	public void addGround(Ground g) {
		ground.add(g);
	}

	public void remove() {
		ground.remove(ground.size() - 1);
	}

	public void remove(Ground g) {
		ground.remove(g);
	}

	public void draw(Graphics g, int x) {
		for (Ground gr : ground) {
			gr.draw(g, null, x);
		}
		for(Enemy e : enemies){
			e.draw(g, x);
		}
	}
	
	public void checkCollision(player p) {
		for (Ground g : ground) {
			g.checkCollision(p);
		}
	}
}
