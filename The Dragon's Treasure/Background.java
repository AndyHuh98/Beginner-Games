import java.awt.Graphics;

/*
This class was created to in order to correctly place the "background" texture of the game. The background is added as a sprite to
the ArrayList of sprites that's iterated through.
*/

public class Background extends Sprite {
	public Background() {
		super("background.png");
		setX(0);
		setY(0);
	}
	@Override
	public void updateImage(Graphics g) {
		g.drawImage(getImage(), getX(), getY(), 1000, 1000, null);
	}

}
