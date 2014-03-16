package pl.mizinski.saper.model;

/**
 * klsasa reprezentujaca czas gry
 * @author Konrad Mizinski
 * @version 1.0
 */
public class Time {
	private int gameTime;
	
	/**
	 * @return czas w sekundach od rozpoczecia aktualnej gry
	 */
	public int getGameTime() {
		return gameTime;
	}
	
	/**
	 * rozpoczyna ponowne odliczanie czasu od 0;
	 */
	public void reset(){
		gameTime = 0;
	}
	
	/**
	 * inkrementuje licznik czasu
	 */
	public void increment(){
		gameTime++;
	}
}
