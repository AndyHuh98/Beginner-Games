import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;

import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.IOException;

public abstract class Sprite {
	private String jpgName;
	private int locationX;
	private int locationY;
	private int spriteWidth;
	private int spriteHeight;
	private Image image;
	
	public Sprite(String jpgName) {
		setImage(jpgName);
	}
	
	public synchronized int getX() {	return locationX; }
	public synchronized int getY() {	return locationY; }
	public int getWidth() { return spriteWidth; }
	public int getHeight() { return spriteHeight; }
	public synchronized void setX(int x) { locationX = x; }
	public synchronized void setY(int y) { locationY = y; }
	
	public void setImage(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException ioe) {
            System.out.println("Unable to load image file.");
        }
	}
	public synchronized Image getImage() { return image; }	
	
	public abstract void updateImage(Graphics g);
	
	
	public synchronized boolean overlaps(Hero hero, int width, int height) {
		Rectangle rect2 = new Rectangle(getX() - 75, getY() - 100, width, height);
		
		if (rect2.intersects(hero.getX(), hero.getY(), hero.getWidth(), hero.getHeight())) {
			return true;
		} else {
			return false;
		}
	}
	
	public synchronized boolean overlaps(Dragon dragon, int width, int height) {
		Rectangle rect2 = new Rectangle(getX() - 350, getY() - 250, width, height);
		
		if (rect2.intersects(dragon.getX(), dragon.getY(), dragon.getWidth(), dragon.getHeight())) {
			return true;
		} else {
			return false;
		}
	}
	
	public synchronized boolean overlaps(Portal portal, int width, int height) {
		Rectangle rect2 = new Rectangle(getX() - 100, getY() - 100, width, height);
		
		if (rect2.intersects(portal.getX(), portal.getY(), portal.getWidth(), portal.getHeight())) {
			return true;
		} else {
			return false;
		}
	}
}
