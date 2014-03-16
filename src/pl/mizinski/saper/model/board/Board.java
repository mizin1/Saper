package pl.mizinski.saper.model.board;
import java.util.*;
import pl.mizinski.saper.commons.*;

/**
 * klasa reprezrntujaca plansze do gry w sapera
 * zawiera obiekty typu Field
 * 
 * @author Konrad Mizinski
 * @version 1.1
 */
public class Board{
	/**ablica z polami*/
	private final Field[][] fields;
	/**liczba pol z minami*/
	private final int fieldsWithMines;	
	/**liczba oznaczonych pol (niekoniecznie z minami)*/
	private int fieldsMarked = 0;	
	/**liczba odkrytych pol*/
	private int fieldsUncovered = 0;
	private final Random random = new Random();	

	/**
	 * konstruktor tworzacy plansze o zadanych wymiarach
	 * nie ustawia zadnych min na planszy
	 * 
	 * @param x,y wymiary planszy
	 * @param fieldsWithMines liczb min na planszy
	 */
	public Board(final int x, final int y, final int fieldsWithMines){
		this.fieldsWithMines = fieldsWithMines;
		fields = new Field[x][y];
		for (int i = 0; i < x; ++i)
			for(int j = 0; j < y; ++j)
				fields[i][j]= new Field();
	}
	
	/**
	 * funkcja ustawiajaca miny na plaszy, po odkryciu 1szego pola
	 * 
	 * @param x,y pozycje pola ktore zostalo klikniete jako pierwsze
	 */
	public void setMines(final int x, final int y){
		Set<Pair<Integer>> set = new HashSet<Pair<Integer> >();//wroc do HashSet<Pair<Integer> >
		Pair<Integer> fp = (new Pair<Integer>(x, y));
		set.add(fp);
		while(set.size()<fieldsWithMines+1){
			set.add( new Pair<Integer>(random.nextInt(fields.length), random.nextInt(fields[0].length)) );
		}
		set.remove(fp); 
		for(Pair<Integer> p : set){
			fields[p.x][p.y].setMined(true);
		}
	}
	
	/**
	 * funkcja odkrywajaca zadane pole na planszy o ile nie jest juz odkryte lub oznaczone(Mark.YES)
	 * pole jest odkrywane gdy jest oznaczone jako Mark.QUESTION
	 * wywolujacy funkcje musi zapewnic ze jesli pole jest nieoznaczone to nie stoi na nim bomba
	 * 
	 * @param x,y wspolrzedne danego pola na planszy
	 * @return true jesli jakies zmiany zostaly wprowadzone
	 */
	public boolean uncover(final int x, final int y){
		if(fields[x][y].isMined() && fields[x][y].getMark() != Mark.YES)
			throw(new RuntimeException("Wykryto probe odkrycia pola na ktorym stoi bomba w momencie gdy powinno to juz byc obsluzone")); // for debuging
		if(fields[x][y].isUncovered() || fields[x][y].getMark() == Mark.YES)
			return false;
		fields[x][y].setMark(Mark.NO);
		fields[x][y].setUncovered(true);
		boolean nb = false; //czy sa bomby w sasiedztwie
		label:
		for(int i = x - 1; i <= x + 1; ++i)
			for(int j = y - 1; j <= y + 1; ++j ){
				if(i >= 0 && j >= 0 && i < fields.length && j < fields[0].length)
					if(fields[i][j].isMined()){
						nb=true;
						break label;
					}
			}
		if(!nb){
			for(int i = x - 1; i <= x + 1; ++i)
				for(int j = y - 1; j <= y + 1; ++j ){
					if(i >= 0 && j >= 0 && i < fields.length && j < fields[0].length)
						uncover(i, j);
				}
		}
		fieldsUncovered++;
		return true;
	}
	
	/**
	 * funkcja oznaczajaca zadane pole jesli nie jest odkryte:
	 * oznaczenia sa zmieniane cyklicznie NO->YES->QUESTION->NO...
	 * @param x,y wspolrzedne danego pola na planszy
	 * @return true jesli jakies zmiany zostaly wprowadzone
	 */
	public boolean mark(final int x, final int y){
		if(fields[x][y].isUncovered())
			return false;
		switch(fields[x][y].getMark()){
			case NO:fields[x][y].setMark(Mark.YES); fieldsMarked++; break;
			case YES:fields[x][y].setMark(Mark.QUESTION); fieldsMarked--; break;
			case QUESTION:fields[x][y].setMark(Mark.NO); break;
		}
		return true;
	}
	

	/**
	 * @param x,y pozycja pola na planszy
	 * @return referencja na zadane pole na plasnzy
	 */
	public Field getField(final int x, final int y){
		return fields[x][y];
	}

	/**
	 * @return liczba odkrytych pol
	 */
	public int getFieldsUncovered() {
		return fieldsUncovered;
	}
	
	/**
	 * @return liczba min ktore pozostaly do oznaczenia
	 */
	public int getFieldsRemained() {
		return fieldsWithMines - fieldsMarked;
	}
	
	/**	 * @return serokosc planszy
	 */
	public int getX(){
		return fields.length;
	}
	
	/**
	 * @return wysokosc planszy
	 */
	public int getY(){
		return fields[0].length;
	}

	/**
	 * @return liczba pol z minami
	 */
	public int getFieldsWithMines() {
		return fieldsWithMines;
	}
	
	/**
	 * @return liczba oznaczonych pol
	 */
	public int getFieldsMarked() {
		return fieldsMarked;
	}
	
	/**
	 * @return true jesli gracz poprawnie rozbroil cala plansze gry
	 */
	public boolean isClear(){
		return (fieldsUncovered+fieldsMarked == getX() * getY() && fieldsMarked == fieldsWithMines);
	}
	
	/**
	 * @param x, y pozycja pola na planszy
	 * @return true jesli zadane pole jest oznaczone jako YES
	 */
	public boolean isFieldYesMarked(final int x, final int y){
		return fields[x][y].getMark() == Mark.YES;
	}

	/**
	 * @param x, y pozycja pola na planszy
	 * @return true jesli zadane pole jest oznaczone jako QUESTION
	 */
	public boolean isFieldQuestionMarked(final int x, final int y){
		return fields[x][y].getMark() == Mark.QUESTION;
	}
	
	/**
	 * @param x, y pozycja pola na planszy
	 * @return true jesli zadane pole jest nieoznaczone(oznaczone jako NO)
	 */
	public boolean isFieldNoMarked(final int x, final int y){
		return fields[x][y].getMark() == Mark.NO;
	}
	
	/**
	 * @param x, y pozycja pola na planszy
	 * @return true jesli zadane pole jest zaminowane
	 */
	public boolean isFieldMined(final int x, final int y){
		return fields[x][y].isMined();
	}

	/**
	 * @param x, y pozycja pola na planszy
	 * @return true jesli zadane pole jest odkryte
	 */
	public boolean isFieldUncovered(final int x, final int y){
		return fields[x][y].isUncovered();
	}	
}
