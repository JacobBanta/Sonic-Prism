import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Window extends JPanel implements ActionListener, KeyListener, MouseListener {
	// suppress serialization warning
	private static final long serialVersionUID = 490905409104883233L;
	public boolean up, down, left, right, space, alt, ctrl, shift, fading, isInGame;
	public int mousex, mousey, newblock, phase = 1, currentType = 0, maxType = 1;
	public int opening = 0;
	public int[] currentGround = new int[5];
	public player Player;
	public Timer timer, timer2;
	public Scanner sc;
	public Level level;

	public Window() {
		loadLevel();
		setPreferredSize(new Dimension(500, 500));
		setBackground(new Color(173, 216, 230));
		Player = new player();
		timer = new Timer(25, this);
		timer.start();
		addMouseListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// this method is called by the timer every 25 ms.
		// use this space to update the state of your game or animation
		// before the graphics are redrawn.
		Player.tick();
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
		level.draw(g, (int)Player.posx);
		// this smooths out animations on some systems
		Toolkit.getDefaultToolkit().sync();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}// necesary for the key presses to work for some reason

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 65)
			left = false;
		if (e.getKeyCode() == 68)
			right = false;
		if (e.getKeyCode() == 87)
			up = false;
		if (e.getKeyCode() == 83)
			down = false;
		if (e.getKeyCode() == 32)
			space = false;
		if (e.getKeyCode() == 16) {
			shift = false;
			level = new Level();
			System.out.println("cleared");
		}
		if (e.getKeyCode() == 17) {
			ctrl = false;
			loadLevel();
		}
		if (e.getKeyCode() == 18) {
			alt = false;
			saveLevel();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// System.out.println(e.getKeyCode());
		if (e.getKeyCode() == 65)
			left = true;
		if (e.getKeyCode() == 68)
			right = true;
		if (e.getKeyCode() == 87)
			up = true;
		if (e.getKeyCode() == 83)
			down = true;
		if (e.getKeyCode() == 32)
			space = true;
		if (e.getKeyCode() == 16)
			shift = true;
		if (e.getKeyCode() == 17)
			ctrl = true;
		if (e.getKeyCode() == 18)
			alt = true;
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			int x = e.getX();
			int y = e.getY();
			x = x + (int) Player.posx - 250;
			x = Math.round(((float) x / (float) 32)) * 32;
			y = Math.round(((float) y / (float) 32)) * 32;
			if (phase == 1) {
				currentGround[0] = currentType;
				currentGround[1] = x;
				currentGround[2] = y;
				phase = 2;
				newblock = 1;
			} else if (phase == 2) {
				currentGround[3] = x;
				currentGround[4] = y;
				if(newblock == 1){
					addObstacle(currentGround);
					phase = 1;
				}else{
					currentGround[0] = currentType;
					currentGround[1] = x;
					currentGround[2] = y;
					phase = 2;
					newblock = 1;
				}
			}
		}
		if (e.getButton() == MouseEvent.BUTTON3) {
			int x = e.getX();
			int y = e.getY();
			x = x + (int) Player.posx - 250;
			x = Math.round(((float) x / (float) 32)) * 32;
			y = Math.round(((float) y / (float) 32)) * 32;
			if(phase == 2){
				currentGround[3] = x;
				currentGround[4] = y;
				if(newblock == 1){
					addObstacle(currentGround);
					newblock = 0;
				}
				else{
					replaceObstacle(currentGround);
				}
			}
		}
		if (e.getButton() == MouseEvent.BUTTON2) {
			if(currentType < maxType){
				currentType++;
			}else{
				currentType = 0;
			}
			System.out.println(String.format("current type is: %d", currentType));
		}
	}

	public void addObstacle(int[] append) {
		level.addGround(new Ground(append[0], new int[] { append[1], append[2], append[3], append[4] }));
	}

	public void replaceObstacle(int[] append) {
		level.remove();
		level.addGround(new Ground(append[0], new int[] { append[1], append[2], append[3], append[4] }));
	}

	// name is a bit misleading. this method actually just draws the ground peices
	private void drawBackground(Graphics g) {
		g.setColor(new Color(255, 216, 230));
		for (int x = 0; x < 532; x++) {
			if (x % 32 == 27) {
				g.drawLine(x - (int) Player.posx % 32, 0, x - (int) Player.posx % 32, 500);
			}
		}
		for (int y = 0; y < 532; y++) {
			if (y % 32 == 0) {
				g.drawLine(0, y, 500, y);
			}
		}
	}

	
	public void loadLevel() {
		Level obj = null;

		try {
			FileInputStream fileIn;
			if((new File("./level.ser")).exists()){
				fileIn = new FileInputStream("./level.ser");
			}else{
				fileIn = new FileInputStream(".temp/level.ser");
			}
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			obj = (Level) objectIn.readObject();
			objectIn.close();
			fileIn.close();
			System.out.println("loaded");
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		if (obj != null) {
			level = obj;
		}
	}
	public void saveLevel(){
		try {
			FileOutputStream fileOut = new FileOutputStream("./level.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(level);
			out.close();
			fileOut.close();
			System.out.println("saved");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
