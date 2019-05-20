import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;

import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.IOException;

public class Projectile extends Sprite{
	public Projectile(String pngName) {
		super(pngName);
	}

	@Override
	public synchronized void updateImage(Graphics g) {
		g.drawImage(getImage(), getX(), getY(), 50, 50, null);
	}
	
	public synchronized double launch(int distance, double launchX, double launchY) {
		double direction = Math.sqrt((launchX * launchX) + (launchY * launchY));
		
		double xDistance = (launchX * distance) / direction;
		double yDistance = (launchY * distance) / direction;
		
		int x = (int) (getX() + (int) xDistance);
		int y = getY() + (int) yDistance;
		setX(x);
		setY(y);
		
		return distance;
	}
}
