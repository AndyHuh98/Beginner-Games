import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import java.util.Random;

/* The dragon class is used for the antagonist of the game; the dragons. It extends the Sprite class,
which allows it to take on an image and moves based on looped calls to it's image being updated constantly,
along with its updated X and Y coordinate values. */

public class Dragon extends Sprite {
	private int maxHealth = 100; //default max Health
	private int currentHealth; //current health (decreases each time dragon is struck by arrow)
	private int moveDirection; 
	private Projectile fireball; //No longer in use, fireball objects are just created in Model instead.
	private static Timer timer = new Timer(); //No longer in use, previously was used to move the dragon.
	private static boolean isNotDead = true; //Used in other methods to check if the dragon has 0 health or not.
	
	//Default constructor
	public Dragon() {
		super("Dragon.png");
		super.setX(350);
		super.setY(0);
		currentHealth = 100;
	}
	
	//Constructor, with a level parameter.
	public Dragon(int level) {
		super("Dragon" + level + ".png");
		//Necessary because I only included 4 unique dragon sprites. If more are added, can increase the 3.
		if (level > 3) {
			setImage("Dragon3.png");
		}
		setMaxHealth(150 + 100 * level);
		setDragonHealth(getDragonMaxHealth());
	}
	
	//Checks if dragon is alive.
	public void alive() {
		isNotDead = true;
	}
	
	//Used to set the move direction in Model
	public void setMoveDirection(int x) {
		moveDirection = x;
	}
	
	//A blob of getters/setters.
	public int getMoveDirection() { return moveDirection; }
	public int getDragonMaxHealth() { return maxHealth; }
	public void setMaxHealth(int max) { maxHealth = max; }
	public int getDragonCurrentHealth() { return currentHealth; }
	public void setDragonHealth(int health) {
		currentHealth = health; 
	}
	public boolean getDragonIsNotDead() { return isNotDead; }
	
	//Called in Model whenever the arrow hits the dragon. Gets the damage of the arrow and subtracts it
	//from current health
	public void arrowHit() {
		if (isNotDead) {
			if (getDragonCurrentHealth() - Arrow.getDamage() >= 0) {
				setDragonHealth(getDragonCurrentHealth() - Arrow.getDamage());
			} 
			if (getDragonCurrentHealth() - Arrow.getDamage() < 0) {
				setDragonHealth(0);
				setImage("DeadDragon.png");
				isNotDead = false;
				//JOptionPane.showMessageDialog(null, "You killed the dragon. Walk through the portal to reach the treasure room. To dig for treasure, press [SPACE].");
			}
		}
	}
	
	public synchronized void moveLeft() {
		if (getX() + 250 < 700) {
			setX(getX() + 300);
		}
	}
	
	public synchronized void moveRight() {
		if (getX() - 230 > 0) {
			setX(getX() - 300);
		}
	}
	
	//The updatedImage method inherited from Sprite -- draws the image at the new X and Y location.
	//Called in Model.
	@Override
	public synchronized void updateImage(Graphics g) {
		g.drawImage(getImage(), getX(), getY(), 350, 350, null);
	}
}

