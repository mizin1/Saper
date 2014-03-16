package pl.mizinski.saper;
import pl.mizinski.saper.model.*;
import pl.mizinski.saper.view.*;
import pl.mizinski.saper.controller.*;
import pl.mizinski.saper.event.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.*;

/**
 * glowna klasa programu inicjalizuje obiekty Model, View, Controller
 * <br>tworzy BlockingQueue do komunikacji Widok=>Kontroler
 * <br>tworzy obiekt Swing.Timer generujacy co sekunde obiekt TimeEvent
 * <br>wykonuje metode run() kontorlera w celu rozpoczecia dzialania programu
 * @author Konrad Mizinski
 * @version 1.1
 */
public class Saper {
	public static void main(String[] args){
		try{
			final Model model = new Model();
			final BlockingQueue<SaperEvent> blockingQueue  = new LinkedBlockingQueue<SaperEvent>();
			final View view = new View(blockingQueue, model.getDataPack());
			final Controller controller = new Controller(view,model,blockingQueue);
			/**Timer z pakietu Swing*/
			final Timer timer = new Timer(1000, new ActionListener(){
				public void actionPerformed(ActionEvent event){
					try{
						blockingQueue.put(new TimeEvent());
					}catch(Exception e){
						e.printStackTrace();
						throw new RuntimeException(e);
					}	
				}
			});
			timer.start();
			controller.work();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
}
