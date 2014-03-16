package pl.mizinski.saper.event;

/**
 * klasa reprezentuja wciscniecie lewego przycisku myszy na planszy do gry
 * @author Konrad Mizinski
 * @version 1.1
 */
public class LeftButtonPressedEvent extends SaperEvent{
	private int x,y;

	public LeftButtonPressedEvent(final int x, final int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(final int y) {
		this.y = y;
	}
	
}