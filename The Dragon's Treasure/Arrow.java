import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.util.Random;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.IOException;

public class Arrow extends Projectile{
	private boolean outOfBounds = false;
	private static int damage = 1;
	
	public Arrow(int x, int y) {
		super("Arrow.png");
		setX(x + 30);
		setY(y - 30);
	}
	
	public static int getDamage() { return damage; }
	public void setDamage(int dmg) { damage = dmg; }
	
	public synchronized void updateState() {
		launch(2, 0, -1);
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
