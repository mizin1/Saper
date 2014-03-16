package pl.mizinski.saper.commons;
/**
 * klasa przenoszacza dane miedzy modelem a widokiem
 * zawiera tablice wartosci typu byte reprezentujaca poszczgolene pola planszy
 * <br>1-8: na polu nalezy wyswietlic dana cyfre
 * <br>0: pole w stanie poczatkowym
 * <br>9: pole oznaczone jako zaminowane
 * <br>10: pole odkryte ale bez naznaczonej cyfry
 * <br>11: pole oznaczone znakiem zapytnia
 * <br>12: bomba - po skonczeniu gry
 * <br>13: pole oznaczone na ktorym nie stoi bomba - po skonczeniu gry
 * <br>14: pole ktore spowodowalo przegrana - po skonczeniu gry
 * <br>dodatkowo informacje o czasie gry, i ilosci bomb ktore nalezy jeszcze zaznaczyc
 * @author Konrad Mizinski
 * @version 1.0
 */
public class DataPack{
	public byte[][] board;
	public int time;
	public int mines;
}