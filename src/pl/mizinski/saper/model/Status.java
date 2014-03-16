package pl.mizinski.saper.model;
/**
 * Oznaczenie stanu gry
 * <br>BEGUN - oczekiwania na wykonanie pierwszego ruchu
 * <br>PLYING - gra w toku
 * <br>WIN - gra wygrana
 * <br>LOSE - gra przegrana
 * <br>END - gra zakonczona stan nastepuje zaraz po WIN/LOSE
 */
public enum Status{
	BEGUN, PLAYING, WIN, LOSE, END
}