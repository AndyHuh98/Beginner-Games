import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.util.Random;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.IOException;

public class Fireball extends Projectile {
	private static int fireballCount = 0;
	private double launchAngleX;
	private double launchAngleY;
	private boolean outOfBounds = false;
	private int damage = 6;
	
	public Fireball(int x, int y) {
		super("Fireball.png");
		setX(x + 150);
		setY(y + 320);
		
		launchAngleX = new Random().nextInt();
		launchAngleY = new Random().nextInt();
		if (launchAngleY < 0) {
			launchAngleY = -launchAngleY;
		}
	}
	
	public int getFBDamage() { return damage; }
	public void setFBDamage(int dmg ) { damage = dmg; }
	
	public int getFireballCount() {
		return fireballCount;
	}
	
	public synchronized void updateState() {
		launch(3, launchAngleX, launchAngleY);
	}
	
	public boolean outOfBounds(int width, int height) {
		if (getX() < 0 || getX() > width) {
			outOfBounds = true;
		} else if (getY() < 0 || getY() > height) {
			outOfBounds = true;
		} else {
			outOfBounds = false;
		}
		return outOfBounds;
	}
	
	public boolean getOutOfBounds() {
		return outOfBounds;
	}
}
