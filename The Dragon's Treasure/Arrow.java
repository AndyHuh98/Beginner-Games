import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.util.Random;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.IOException;

/*
The Arrow class inherits the Projectile class, which is a class that most importantly contains a "launch" method which utilizes multithreading to 
"launch" projectiles. Arrow is one of the projectile subclasses, along with Fireballs.
*/

public class Arrow extends Projectile{
	private boolean outOfBounds = false; //Boolean for whether the arrow has traveled out of the JFrame
	private static int damage = 1; //How much damage the arrow does to the dragon
	
	//Constructor - Sets location at the tip of the hero (to make it look like arrows are exiting the bow)
	public Arrow(int x, int y) { //x and y are the hero location (called in Model)
		super("Arrow.png");
		setX(x + 30); //+30 to adjust to the center of the hero
		setY(y - 30); //-30 to adjust to the top of the hero
	}
	
	public static int getDamage() { return damage; } 
	public void setDamage(int dmg) { damage = dmg; }
	
	//continuously called (moving the projectile)
	public synchronized void updateState() {
		launch(2, 0, -1);
	}
	
	//Tests whether the Arrow is out of bounds or not
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
