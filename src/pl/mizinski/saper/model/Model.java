package pl.mizinski.saper.model; 
import pl.mizinski.saper.commons.*;
import pl.mizinski.saper.model.board.*;

/**
 * realizacja klasy Model z wzroca Model-Widok-Kontroler
 * odpowiada za przechowywanie i zmiany stanu gry
 * @version 1.1
 * @author Konrad Mizinski
 */
public class Model{
	private Board board; 	//plansz na ktorej toczy sie gra
	private Time time;		//licznik czasu
	private Status status;
	
	/**
	 * konstruktor domyslny tworzy model standardowej gry w sapera na planszy 10x10 z 10 minami
	 */
	public Model(){
		time = new Time();
		setSize(Constans.BEGINER_X, Constans.BEGINER_Y, Constans.BEGINER_MINES);
	}
	
	/**
	 * inkrementuje licznik czasu
	 * 
	 * @return true jesli jakies zmiany zostaly wprowadzone
	 */
	public boolean incrementTime(){
		if(status == Status.PLAYING){
			time.increment();
			return true;
		}
		return false;
	}
	
	/**
	 * sprawadza czy zadana plansza moze zosta utworzona:<br>
	 * -sprawdza czy wymiary mieszcza sie w zakresie <10:100><br>
	 * -czy bomb jest wiecej niz 10 i nie wiecej niz 85% wszystkich pol
	 * 
	 * @param x,y wymiary planszy
	 * @param fieldsWithMines ilosc bomb na planszy
	 * @return true jesli taka plansza moze zostac utworzona
	 * @see  Model#setSize(int, int, int)
	 */
	public boolean checkSize(final int x, final int y, final int fieldsWithMines){
		//test rozmiaru
		if( x > 100 || y > 100 || x < 10 || y < 10 )
			return false;
		if( fieldsWithMines < 10 )
			return false;
		if (fieldsWithMines > 0.85 * x * y)
			return false;
		return true;

	}
	
	/**
	 * inicjuje plansze o zadanych parametrach
	 * 
	 * @param x,y wymiary planszy
	 * @param fieldsWithMines ilosc bomb na planszy
	 * @return true jesli jakies zmiany zostaly wprowadzone
	 * @see  Model#checkSize(int, int, int)
	 */ 
	public boolean setSize(final int x, final int y, final int fieldsWithMines){
		this.board = new Board(x, y, fieldsWithMines);
		this.status = Status.BEGUN;
		this.time.reset();
		return true;
	}
	
	/**
	 * rozpoczecie nowej gry(NIE postawienie piewszego ruchu)
	 * 
	 * @return true jesli jakies zmiany zostaly wprowadzone
	 */
	public boolean newGame(){
		setSize(board.getX(), board.getY(), board.getFieldsWithMines());
		return true;
	}
	
	/**
	 * proba odkrycia pola<br>
	 * gdy jest to pierwszy ruch w grze dodatkowo inicjalizuje plansze
	 * sprawdza czy nie po wykonaniu ruchu gracz nie przegra lub nie wygra
	 * wykonuje Board.uncover
	 * 
	 * @param x,y wspolrzedne danego pola na planszy
	 * @return true jesli jakies zmiany zostaly wprowadzone
	 */
	public boolean uncoverField(final int x, final int y){
		if(status == Status.BEGUN){
			board.setMines(x, y);
			board.uncover(x, y);
			status = Status.PLAYING;
			return true;
		}
		if(status == Status.PLAYING){
			if(board.isFieldMined(x, y) && !board.isFieldYesMarked(y, x)){
				status = Status.LOSE;
				board.getField(x, y).setUncovered(true); 
				return true;
			}
			boolean temp = board.uncover(x, y);
			if (board.isClear())
				status = Status.WIN;
			return temp;
		}
		return false;
	}
	
	/**
	 * proba oznaczenia pola<br>
	 * wykonuje Board.mark
	 * 
	 * @param x,y wspolrzedne danego pola na planszy
	 * @return true jesli jakies zmiany zostaly wprowadzone
	 */
	public boolean markField(final int x, final int y){
		if(status == Status.PLAYING){
			boolean temp = board.mark(x, y);
			if (board.isClear())
				status = Status.WIN;
			return temp;
		}
		return false;
	}
	
	/**
	 * @return true jesli gracz wygral rozgrywke
	 */
	public boolean isWin(){
		return status == Status.WIN;
	}
	
	/**
	 * @return true jesli gracz przegral rozgrywke;
	 */
	public boolean isLose(){
		return status == Status.LOSE;
	}
	
	/**
	 * zakonczenie rozgrywki funkcja powinna zostac wykonana po poinformowaniu gracza o zwyciestwie lub przegranej
	 */
	public void endGame(){
		status = Status.END;
	}
	
	/**
	 * prosba o wygenerowanie danych dla odswierzenia widoku
	 * 
	 * @return obiekt DataPack z aktualnym stanem rozgrywki
	 */
	public DataPack getDataPack(){
		switch(status){
			case BEGUN: return getBegunDataPack();
			case LOSE:  return getLoseDataPack();
			default:	return getPlayingDataPack();
			
		}
	}
	private DataPack getBegunDataPack(){
		DataPack dp = new DataPack();
		dp.time = time.getGameTime();
		dp.mines = board.getFieldsRemained();
		dp.board = new byte[board.getX()][board.getY()];
		return dp;
	}
	
	private DataPack getPlayingDataPack(){
		DataPack dp = new DataPack();
		dp.time = time.getGameTime();
		dp.mines = board.getFieldsRemained();
		dp.board = new byte[board.getX()][board.getY()];
		for(int x = 0; x < board.getX(); ++x)
			for(int y = 0; y < board.getY(); ++y){
				if(board.isFieldYesMarked(x, y))
					dp.board[x][y] = 9;
				else if (board.isFieldQuestionMarked(x, y))
					dp.board[x][y] = 11;
				else if (board.isFieldUncovered(x, y)){
					int temp = 0;
					for(int i = x - 1; i <= x + 1; ++i)
						for(int j = y - 1; j <= y + 1; ++j ){
							if(i >= 0 && j >= 0 && i < board.getX() && j < board.getY())
								if(board.isFieldMined(i, j)){
									++temp;
								}
						}
					dp.board[x][y]= temp== 0 ? 10 : (byte)temp;
				}
			}
		
		return dp;
	}
	
	private DataPack getLoseDataPack(){
		DataPack dp = new DataPack();
		dp.time = time.getGameTime();
		dp.mines = board.getFieldsRemained();
		dp.board = new byte[board.getX()][board.getY()];
		for(int x = 0; x < board.getX(); ++x)
			for(int y = 0; y < board.getY(); ++y){
				if(board.isFieldUncovered(x, y)){
					if(board.isFieldMined(x, y)){
						dp.board[x][y]=14;
					}else{
						int temp = 0;
						for(int i = x - 1; i <= x + 1; ++i)
							for(int j = y - 1; j <= y + 1; ++j ){
								if(i >= 0 && j >= 0 && i < board.getX() && j < board.getY())
									if(board.isFieldMined(i, j)){
										++temp;
									}
							}
						dp.board[x][y]= temp== 0 ? 10 : (byte)temp;
					}
				}else if (board.isFieldYesMarked(x,y))
					dp.board[x][y] = (byte) (board.isFieldMined(x, y) ? 9 : 13);
				else if (board.isFieldNoMarked(x ,y))
					dp.board[x][y] = (byte) (board.isFieldMined(x, y) ? 12 : 0);
				else if (board.isFieldQuestionMarked(x ,y))
					dp.board[x][y] = (byte) (board.isFieldMined(x, y) ? 12 : 11);
			}	
		return dp;
	}
}
