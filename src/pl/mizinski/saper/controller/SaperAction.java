package pl.mizinski.saper.controller;
import pl.mizinski.saper.event.*;
/**
 * interfejs definiujacy odpowiedz kontorlera na zapytanie plynace z modelu 
 * aby z niego skorzystac nalezy zdefiniowac funkcje go
 * @version 1.0
 */
public interface SaperAction {
	abstract public void go(SaperEvent e);
}