import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Thread;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Window extends JPanel implements ActionListener, KeyListener, MouseListener {
	// suppress serialization warning
	private static final long serialVersionUID = 490905409104883233L;
	public boolean up, down, left, right, space, alt, ctrl, shift, fading, isInGame;
	public int mousex, mousey;
	public int opening = 0;
	private float alpha = 0f;
	private long startTime = -1;
	public player Player;
	private Level level;
	public Timer timer, timer2;
	private BufferedImage inImage, outImage;
	public Scanner sc;
	private String[] levelNames;
	private boolean levelSelect, titleCard;
	private ArrayList<BufferedImage> backgroundImages;
	private String levelLocation = ".temp/level.ser";
	private ArrayList<BufferedImage> titleCards;
	private long titleCardStart;
	private int titleCardPhase;

	public Window() {
		level = new Level();
		levelLocation = ".temp/levels/"
				+ new File(".temp/levels").listFiles(new FileFilter() {
					@Override
					public boolean accept(File file) {
						return file.getName().endsWith(".ser");
					}
				})[0].getName();
		setPreferredSize(new Dimension(500, 500));
		setBackground(new Color(173, 216, 230));
		Player = new player();
		Player.setsize(43, 43);
		timer = new Timer(25, this);
		timer.start();
		addMouseListener(this);
		try {
			inImage = ImageIO.read(new File(".temp/assets/Sonic_Prism_Logo.png"));
			outImage = ImageIO.read(new File(".temp/assets/main menu.png"));
		} catch (IOException exc) {
			System.out.println("Error opening logo image file: " + exc.getMessage());
		}
		timer2 = new Timer(0, new ActionListener() {// crates new timer
			@Override
			public void actionPerformed(ActionEvent e) {
				if (startTime < 0) {// fade start
					startTime = System.currentTimeMillis();
					BufferedImage tmp = inImage;
					inImage = outImage;
					outImage = tmp;
				} else {// fade still going
					long time = System.currentTimeMillis();// gets current time
					long duration = time - startTime;// finds how long fade has been going
					if (duration >= 750) {
						startTime = -1;
						((Timer) e.getSource()).stop();
						alpha = 0f;
					} else {
						alpha = 1f - ((float) duration / (float) 750);
					}
					repaint();
				}
			}
		});
		
		backgroundImages = new ArrayList<>();
		//TODO: add images
		// backgroundImages.add(inImage);
		// backgroundImages.add(outImage);
	}

	public void loadLevelNames() {
		File levelsFolder = new File("levels");
		File[] levelFiles = levelsFolder.listFiles();
		levelNames = new String[levelFiles.length];
		for (int i = 0; i < levelFiles.length; i++) {
			String fileName = levelFiles[i].getName();
			int dotIndex = fileName.lastIndexOf('.');
			levelNames[i] = fileName.substring(0, dotIndex);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// this method is called by the timer every 25 ms.
		// use this space to update the state of your game or animation
		// before the graphics are redrawn.
		if (isInGame) {
			Player.tick();
			Player.wasOnGround = Player.isOnGround;
			Player.isOnGround = false;
			Player.isOnSlope = false;
			level.checkCollision(Player);
			Player.input(up, left, down, right, space, shift);
		}
		// calling repaint() will trigger paintComponent() to run again,
		// which will refresh/redraw the graphics.
		if (!(opening >= 150 && opening < 200)) {
			repaint();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// when calling g.drawImage() we can use "this" for the ImageObserver
		// because Component implements the ImageObserver interface, and JPanel
		// extends from Component. So "this" Board instance, as a Component, can
		// react to imageUpdate() events triggered by g.drawImage()
		if(titleCard){
			if(titleCards == null){
				titleCards = new ArrayList<>();
				try{
					for(int x = 1; x <= 3; x++){
						titleCards.add(ImageIO.read(new File(".temp/assets/title-cards/"+x+".png")));
					}
					for(int x = 1; x <= 16; x++){
						titleCards.add(ImageIO.read(new File(".temp/assets/title-cards/1_"+x+".png")));
					}
				} catch (IOException exc) {
					System.out.println("Error opening title cards: " + exc.getMessage());
				}
			}
			// Create a BufferedImage with the desired dimensions and color model
			BufferedImage tempImage = new BufferedImage(320, 224, BufferedImage.TYPE_INT_ARGB);
			
			// Obtain a Graphics2D object from the BufferedImage
			Graphics2D g2d = tempImage.createGraphics();
			
			g2d.create(0, 0, 320, 224);
			g2d.drawImage(titleCards.get(0), 0 - titleCardPhase % 32 + 32, 0 + titleCardPhase % 32 - 32, null);
			g2d.drawImage(titleCards.get(1), 0, 0, null);
			g2d.drawImage(titleCards.get(2), 0, titleCardPhase % 32, null);
			g2d.drawImage(titleCards.get(2), 0, titleCardPhase % 32 - 224, null);
			g2d.drawImage(titleCards.get(3 + titleCardPhase++ % 16), 0, 0, null);
			g2d.drawString(levelLocation.substring(levelLocation.lastIndexOf("/") + 1, levelLocation.lastIndexOf(".")), 200, 150);
			g2d.dispose();
			g.drawImage(tempImage, 0, 0, 500, 500, null);
			//if(titleCardPhase == 16)
				//titleCardPhase = 0;
			if(System.currentTimeMillis() > titleCardStart + 5000){
				titleCard = false;
			}
		}else if (isInGame) {
			// draw our graphics.
			drawBackground(g);
			Player.draw(g, this);
			level.draw(g, (int) Player.posx);
		} else if (levelSelect) {

			File directory = new File(".temp/levels");
			File[] csvFiles = directory.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.getName().endsWith(".ser");
				}
			});
			if(csvFiles != null){
				for (int x = 0; x < csvFiles.length; x++) {
					drawtext(g, 50, x * 50, csvFiles[x].getName());
				}
			}else{
				drawtext(g, 50, 50, "no levels :(");
			}
		} else {
			opening++;
			if (opening == 150) {
				alpha = 0f;
				timer2.start();
			}
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setComposite(AlphaComposite.SrcOver.derive(1f - alpha));
			int x = (getWidth() - inImage.getWidth()) / 2;
			int y = (getHeight() - inImage.getHeight()) / 2;
			g2d.drawImage(inImage, x, y, this);

			g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
			x = (getWidth() - outImage.getWidth()) / 2;
			y = (getHeight() - outImage.getHeight()) / 2;
			g2d.drawImage(outImage, x, y, this);
			g2d.dispose();
		}
		// this smooths out animations on some systems
		Toolkit.getDefaultToolkit().sync();
	}

	private void drawtext(Graphics g, int x, int y, String text) {
		// we need to cast the Graphics to Graphics2D to draw nicer text
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(
				RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(
				RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		// set the text color and font
		g2d.setColor(new Color(10, 150, 125));
		g2d.setFont(new Font("Lato", Font.BOLD, 25));
		// draw the score in the bottom center of the screen
		// https://stackoverflow.com/a/27740330/4655368
		FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
		// the text will be contained within this rectangle.
		// here I've sized it to be the entire bottom row of board tiles
		Rectangle rect;
		rect = new Rectangle(x, y, 500 - x * 2, 50);
		// determine the x coordinate for the text
		int x2 = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		// determine the y coordinate for the text
		// (note we add the ascent, as in java 2d 0 is top of the screen)
		int y2 = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		// draw the string
		g2d.drawString(text, x2, y2);
	}

	public void loadLevel() {
		playTitleCard();
		Level obj = null;
		try {
			FileInputStream fileIn = new FileInputStream(levelLocation);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			obj = (Level) objectIn.readObject();
			objectIn.close();
			fileIn.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		if (obj != null) {
			level = obj;
		}
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
		if (e.getKeyCode() == 16)
			shift = false;
		if (e.getKeyCode() == 17)
			ctrl = false;
		if (e.getKeyCode() == 18) {
			alt = false;
			for (int x = 0; x < levelNames.length; x++) {
				System.out.println(levelNames[x]);
			}
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
		int x = e.getX();
		int y = e.getY();
		if (levelSelect) {
			File[] csvFiles = new File(".temp/levels").listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.getName().endsWith(".ser");
				}
			});
			for (int i = 0; i < csvFiles.length; i++) {
				if (y > i * 50 && y < i * 50 + 50) {
					levelLocation = ".temp/levels/" + csvFiles[i].getName();
					levelSelect = false;
				}
			}
		} else {
			if (x > 158 && x < 342 && y > 197 && y < 302 && opening > 200) {
				loadLevel();
				isInGame = true;
			}
			if (x > 164 && x < 341 && y > 324 && y < 343 && opening > 200) {
				levelSelect = true;
			}
		}
	}
	private void drawBackground(Graphics g) {
		for (int i = 0; i < backgroundImages.size(); i++) {
			g.drawImage(backgroundImages.get(i), 0 - (int) Player.posx / (i + 2), 0, null);
		}
	}
	public void playTitleCard(){
		titleCard = true; 
		titleCardStart = System.currentTimeMillis();
	}
}
