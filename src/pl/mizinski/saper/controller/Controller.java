package pl.mizinski.saper.controller;

//import pl.mizinski.saper.commons.*;
import pl.mizinski.saper.event.*;
import pl.mizinski.saper.model.*;
import pl.mizinski.saper.view.*;

import java.util.*;
import java.util.concurrent.*;
/**
 * realizacja klasy Kontroler z wzroca Model-Widok-Kontroler
 * <br> obs³uguje ¿¹dania u¿ytkownika. Wszelkie ¿¹dania deleguje do odpowiednich metod Modelu
 * @author Konrad Mizinski
 * @version 1.2
 */
public class Controller{
	
	private final View view;	
	private final Model model;
	/**Kolejka dla obiektow SaperEvent.*/
	private final BlockingQueue<SaperEvent> blockingQueue;
	/**odwzorowanie obiektow SaperEvent na obiekty SaperAction*/
	private final Map<Class<? extends SaperEvent>, SaperAction> eventActionMap;
	
	
	/**
	 * Tworzy obiekt typu Controller
	 * 
	 * @param view referencja na widok
	 * @param model referencja na Model
	 * @param blockingQueue kolejka do otrzymywania komunikatow z Widoku
	 */
	public Controller(final View view, final  Model model, final BlockingQueue<SaperEvent> blockingQueue) {
		this.view = view;
		this.model = model;
		this.blockingQueue = blockingQueue;
		eventActionMap = new HashMap<Class<? extends SaperEvent>, SaperAction>();
		fillEventActionMap();
	}
	
	/**
	 * zapelnia kontener eventActionMap
	 */
	private void fillEventActionMap(){
		eventActionMap.put(NewGameEvent.class, new SaperAction(){//nowa gra
			public void go(SaperEvent event){
				model.newGame();
				view.refresh(model.getDataPack());
			}
		});
		
		eventActionMap.put(SetSizeEvent.class, new SaperAction(){
			public void go(SaperEvent event){
				SetSizeEvent setSizeEvent = (SetSizeEvent) event;
				if(model.checkSize(setSizeEvent.getX(), setSizeEvent.getY(), setSizeEvent.getMines())){
					model.setSize(setSizeEvent.getX(), setSizeEvent.getY(), setSizeEvent.getMines());
					view.refresh(model.getDataPack());
				}else{
					view.cantMakeBoard();
				}
			}
		});
		
		eventActionMap.put(LeftButtonPressedEvent.class, new SaperAction(){
			public void go(SaperEvent event){
				LeftButtonPressedEvent leftButtonPressedEvent = (LeftButtonPressedEvent) event;
				if(model.uncoverField(leftButtonPressedEvent.getX(), leftButtonPressedEvent.getY()))
					view.refresh(model.getDataPack());
				if(model.isWin()){
					view.win();
					model.endGame();	
				}
				if(model.isLose()){
					view.lose();
					model.endGame();						
				}
			}
		});
		
		eventActionMap.put(RightButtonPressedEvent.class, new SaperAction(){
			public void go(SaperEvent event){
				RightButtonPressedEvent rightButtonPressedEvent = (RightButtonPressedEvent) event;
				if(model.markField(rightButtonPressedEvent.getX(), rightButtonPressedEvent.getY()))
					view.refresh(model.getDataPack());
				if(model.isWin())
				{
					view.win();	
					model.endGame();
				}
			}
		});
		
		eventActionMap.put(TimeEvent.class, new SaperAction(){
			public void go(SaperEvent event){
				if(model.incrementTime())
					view.refresh(model.getDataPack());
			}
		});
		
		eventActionMap.put(FinishGameEvant.class, new SaperAction(){
			public void go(SaperEvent e){
				System.exit(0);
			}
		});
	}

	
	/**
	 * funkcja obsligujca komunikaty z widoku w nieskonczonej petki
	 * <br> tzn pobieajaca obiekt z kolejki(blockingQueue) i na jego podstawie uruchamiajaca 
	 * odpowiednie dzialannie z mapy zadan(eventActionMap)
	 * <br>-normalne dzialanie kontrolera :)
	 */
	public void work(){	
		while(true){
			try{
				SaperEvent event = blockingQueue.take();
				SaperAction saperAction = eventActionMap.get(event.getClass());
				saperAction.go(event);
			}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
}
