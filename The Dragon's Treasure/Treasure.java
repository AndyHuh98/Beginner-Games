import java.awt.Graphics;

public class Treasure extends Sprite {
	private int xCoordinate;
	private int yCoordinate;
	
	public Treasure() {
		super("Treasure1.png");
	}

	@Override
	public void updateImage(Graphics g) {
		g.drawImage(getImage(), getX(), getY(), 100, 100, null);
	}
}
