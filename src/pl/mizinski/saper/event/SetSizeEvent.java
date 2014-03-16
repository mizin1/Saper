package pl.mizinski.saper.event;
/**
 * klasa reprezentuja chec rozpoczecia nowej gry
 * na plaszy o zadanych wymiarach
 * i okreslonej ilosci bomb
 * @author Konrad Mizinski
 * @version 1.1
 */
public class SetSizeEvent extends SaperEvent {
	int x,y; //wymiary planszcy
	int mines; //ilosc bomb
	public SetSizeEvent(int x, int y, int mines) {
		super();
		this.x = x;
		this.y = y;
		this.mines = mines;
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
	public int getMines() {
		return mines;
	}
	public void setMines(final int mines) {
		this.mines = mines;
	}
}