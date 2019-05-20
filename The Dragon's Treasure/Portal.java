import java.awt.Graphics;

public class Portal extends Sprite{
	public Portal() {
		super("Portal.png");
		setX(500);
		setY(500);
	}

	@Override
	public void updateImage(Graphics g) {
		g.drawImage(getImage(), getX(), getY(), 150, 150, null);
	}
}
