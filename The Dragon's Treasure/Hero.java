import java.awt.Graphics;

public class Hero extends Sprite{
	private int maxHealth;
	private int currentHealth;
	private boolean isNotDead = true;
	
	public Hero() {
		super("Hero.png");
		maxHealth = 60;
		currentHealth = 60;
		super.setX(900);
		super.setY(785);
	}
	
	public int getHeroMaxHealth() { return maxHealth; }
	public int getHeroCurrentHealth() { return currentHealth; }
	public void setHeroHealth(int health) { currentHealth = health; }
	
	public synchronized void fireballHit() {
		if (currentHealth - 3 >= 0) {
			currentHealth -= 3;
		}
		if (currentHealth - 3 == -3) {
			setImage("tombstone.png");
			isNotDead = false;
		}
	}
	
	public boolean isNotDead() {
		return isNotDead;
	}
	public void alive() {
		isNotDead = true;
		setImage("Hero.png");
	}
	
	@Override
	public synchronized void updateImage(Graphics g) {
		g.drawImage(getImage(), getX(), getY(), 100, 135, null);
	}
	
	public void moveLeft(int constraint1, int constraint2) {
		if (getX() - constraint1 > constraint2) {
			setX(getX() - constraint1);
		}
	}
	
	public void moveRight(int constraint1, int constraint2) {
		if (getX() + constraint1 < constraint2) {
			setX(getX() + constraint1);
		}
	}
	
	public void moveUp(int constraint1, int constraint2) {
		if (getY() - constraint1 > constraint2) {
			setY(getY() - constraint1);
		}
	}
	
	public void moveDown(int constraint1, int constraint2) {
		if (getY() + constraint1 < constraint2) {
			setY(getY() + constraint1);
		}
	}
}
