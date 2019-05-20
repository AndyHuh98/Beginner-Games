import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import java.util.Random;

public class Dragon extends Sprite {
	private int maxHealth = 100;
	private int currentHealth;
	private int moveDirection;
	private Projectile fireball;
	private static Timer timer = new Timer();
	private static boolean isNotDead = true;
	
	public Dragon() {
		super("Dragon.png");
		super.setX(350);
		super.setY(0);
		currentHealth = 100;
	}
	
	public Dragon(int level) {
		super("Dragon" + level + ".png");
		setMaxHealth(150 + 100 * level);
		setDragonHealth(getDragonMaxHealth());
	}
	
	public void alive() {
		isNotDead = true;
	}

	public void setMoveDirection(int x) {
		moveDirection = x;
	}
	public int getMoveDirection() { return moveDirection; }
	public int getDragonMaxHealth() { return maxHealth; }
	public void setMaxHealth(int max) { maxHealth = max; }
	public int getDragonCurrentHealth() { return currentHealth; }
	public void setDragonHealth(int health) {
		currentHealth = health; 
	}
	public boolean getDragonIsNotDead() { return isNotDead; }
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
	
	@Override
	public synchronized void updateImage(Graphics g) {
		g.drawImage(getImage(), getX(), getY(), 350, 350, null);
	}
}
