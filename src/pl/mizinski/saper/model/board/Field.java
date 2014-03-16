package pl.mizinski.saper.model.board; 

/**
 * klasa opisujaca pojedyncze pole na planszy
 * @author Konrad Mizinski
 * @version 1.0
 */
public class Field{
	/**czy na polu stoi mina*/
	private boolean mined;
	/**czy pole zostalo odkryte*/
	private boolean uncovered;
	/**czy pole zostalo oznaczone*/
	private Mark mark;
	/**
	 * konstruktor ustawiajacy wszystkie atrybuty na false(NO dla mined)
	 */
	Field(){
		mined=false;
		uncovered=false;
		mark=Mark.NO;
	}
	
	public boolean isMined() {
		return mined;
	}
	public void setMined(final boolean mined) {
		this.mined = mined;
	}
	public boolean isUncovered() {
		return uncovered;
	}

	public void setUncovered(final boolean uncovered) {
		this.uncovered = uncovered;
	}

	public Mark getMark() {
		return mark;
	}
	public void setMark(final Mark mark) {
		this.mark = mark;
	}	
}