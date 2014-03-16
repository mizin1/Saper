package pl.mizinski.saper.view;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.concurrent.*;
import javax.swing.*;
import javax.swing.border.*;
import pl.mizinski.saper.commons.*;
import pl.mizinski.saper.event.*;
import javax.swing.text.*;

/**
 * realizacja klasy Widok z wzroca Model-Widok-Kontroler
 * odpowiada za komunikacje programu z u¿ytkownikiem;
 * @author Konrad Mizinski
 * @version 1.2
 */
public class View{
	private final BlockingQueue<SaperEvent> blockingQueue;//do komunikacji z kontrolerem
	private SaperFrame frame;

	/**
	 * Tworzy nowy widok
	 * 
	 * @param b kolejka sluzaca do komunikacji z kontrolerem
	 * @param dp wzorzec wg ktorego ma zostac odrysowany widok
	 */
	public View(final BlockingQueue<SaperEvent> blockingQueue, final DataPack dp){
		this.blockingQueue=blockingQueue;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame = new SaperFrame();
				refresh(dp);
			}
		});
	}
	
	/**
	 * Odswiezenie widoku na podstawie danych otrzymanych z modelu
	 * 
	 * @param d obiekt DataPack z aktualnym stanem rozgrywki
	 */
	public void refresh(final DataPack d){
		SwingUtilities.invokeLater(new Runnable() {		
			@Override
			public void run() {
				frame.setSize(d.board.length*Constans.FIELD_SIZE + Constans.HORIZONTAL_MARGIN, d.board[0].length*Constans.FIELD_SIZE + Constans.VERTICAL_MARGIN);
				frame.setTime(d.time);
				frame.setRemainMines(d.mines);
				frame.drawPanel(d.board);
			}
		});
	}
	
	/**
	 * funkcja informujaca gracza o wygraniu gry
	 */
	public void win(){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JOptionPane.showMessageDialog(null, "Gratulacje wygrales","Gratulacje",JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
	
	/**
	 * funkcja informujaca gracza o przegraniu gry
	 */
	public void lose(){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JOptionPane.showMessageDialog(null, "Niestety przegrales",":(",JOptionPane.ERROR_MESSAGE);
			}
		});
	}
	
	/**
	 * funkcja informujaca gracza o nie powodzeniu u utworzenia zadanej przez niego plasnszy
	 * <br>(z powodu podania zlych parametrow)
	 */
	public void cantMakeBoard(){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JOptionPane.showMessageDialog(null, "Nie mozna utworzyc planszy a takich parametrach","Blad",JOptionPane.ERROR_MESSAGE);
			}
		});
	}
	
	
	/**
	 * klasa reprezentuja widoczna czesc aplikacji
	 * przez kompozycje umieszczona w klasie View
	 * @author Konrad Mizinski
	 * @version 1.1
	 */
	class SaperFrame extends JFrame {

		private static final long serialVersionUID = 1L;
		private final SaperPanel panel;	
		private final JLabel time;
		private final JLabel remainMines;
		
		SaperFrame(){
			super("Saper 2012 by Konrad Miziñski");
			try{
				 UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			setLayout(new BorderLayout());
			setJMenuBar(makeMenu());	
			JPanel bottom =new JPanel();
			add(BorderLayout.SOUTH, bottom);
			time = new JLabel("0");
			remainMines = new  JLabel("0");
			bottom.add(time);
			bottom.add(remainMines);
			panel = new SaperPanel();
			JPanel srodek = new JPanel();		
			srodek.add(panel);
			srodek.setBorder(new EtchedBorder());
			srodek.setLayout(null);
			add(BorderLayout.CENTER, srodek);
			panel.addMouseListener(new FieldClickedListener());
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setResizable(false);
			setVisible(true);
		}
		
		/**
		 * ustawia wyswietlany czas na zadana wartosc
		 * 
		 * @param t liczba jaka ma bys wyswietkana w polu czas
		 */
		public  void setTime(int t){
			time.setText(Integer.toString(t));
		}
		
		/**
		 * ustawia wyswietlana ilosc nieoznaczonych min na zadana wartosc
		 * 
		 * @param r ilosc nieoznaczonych min jaka ma zostac wyswietlona
		 */
		public void setRemainMines(final int r){
			remainMines.setText(Integer.toString(r));
		}
		
		/**
		 * odrysowuje plansze do gre wg zadanego wzorca
		 * 
		 * @param tab tablica sluzaca jako wzorzec do odrysowania planszy
		 */
		public void drawPanel(byte tab[][]){
			panel.drawPanel(tab);
		}
		
		/**
		 * tworzy pasek menu wyswietlany na gorze okna
		 * 
		 * @return referencja na sworzony pasek
		 */
		private JMenuBar makeMenu(){
			JMenuItem ng = new JMenuItem("Nowa Gra");
			ng.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try{
						blockingQueue.put(new NewGameEvent());
					}
					catch(Exception ex){
							ex.printStackTrace();
							throw new RuntimeException(ex);
					}			
				}
			});
			JMenuItem op = new JMenuItem("Opcje");
			op.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
						makeOptionsDialog();
					}
			});
			JMenuItem end = new JMenuItem("Zakoncz");
			end.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try{
						blockingQueue.put(new FinishGameEvant());
						}catch(Exception ex){
							ex.printStackTrace();
							throw new RuntimeException(ex);
						}	
					}
			});
			JMenu gra = new JMenu("Gra");
			gra.add(ng);
			gra.add(op);
			gra.add(end);
			JMenuBar menu = new JMenuBar();
			menu.add(gra);
			return menu;
		}
		
		/**
		 * tworzy okno ktore pozwala na zmiane rozmiaru planszy
		 * <br>proponuje 1 z 3 poziomow(latwy, sredni, trudny)
		 * <br>lub pozawala na reczne wprowadzenie danych
		 * 
		 * @return referencja na okno dialogowe
		 */
		private JDialog makeOptionsDialog(){
			final JDialog d = new JDialog(SaperFrame.this, "Opcje", true);
			d.setSize(Constans.OPTIONS_DIALOG_HORIZONTAL_SIZE, Constans.OPTIONS_DIALOG_VERTICAL_SIZE);
			final JRadioButton p = new JRadioButton("Poczatkujacy");
			final JRadioButton s = new JRadioButton("Sredni");
			final JRadioButton z = new JRadioButton("Zaawansowany");
			final JRadioButton n = new JRadioButton("Niestandardowy");
			final ButtonGroup bg = new ButtonGroup();
			bg.add(p);
			bg.add(s);
			bg.add(z);
			bg.add(n);
			d.setLayout(new GridLayout(5,1));
			JPanel pl = new JPanel();
			pl.setLayout(new GridLayout(1,3));
			pl.add(p);
			pl.add(s);
			pl.add(z);
			d.add(pl);
			d.add(n);
			p.setSelected(true);
			pl = new JPanel();
			pl.setLayout(new GridLayout(1,3));
			final JLabel lSzer=new JLabel("Szerokosc");
			final JLabel lWys=new JLabel("Wysokosc");
			final JLabel lMin = new JLabel("Liczba Min");
			pl.add(lSzer);
			pl.add(lWys);
			pl.add(lMin);
			d.add(pl);
			pl = new JPanel();
			pl.setLayout(new GridLayout(1,3));
			final JTextComponent wys = new JTextField();
			final JTextComponent szer = new JTextField();
			final JTextComponent min = new JTextField();
			JPanel pl2 = new JPanel();
			pl2.setLayout(new BorderLayout());
			pl2.setBorder(new EtchedBorder());;
			pl2.add(szer);
			pl.add(pl2);	
			pl2 = new JPanel();
			pl2.setLayout(new BorderLayout());
			pl2.setBorder(new EtchedBorder());;
			pl2.add(wys);
			pl.add(pl2);			
			pl2 = new JPanel();
			pl2.setLayout(new BorderLayout());
			pl2.setBorder(new EtchedBorder());;
			pl2.add(min);
			pl.add(pl2);
			d.add(pl);
			pl = new JPanel();
			pl.setLayout(new FlowLayout());
			final JButton ok = new JButton("OK");
			ActionListener al = new ActionListener(){
				public void actionPerformed(ActionEvent e){
					lSzer.setEnabled(false);
					lWys.setEnabled(false);
					lMin.setEnabled(false);
					szer.setEnabled(false);
					wys.setEnabled(false);
					min.setEnabled(false);
				}
			};
			al.actionPerformed(null);
			p.addActionListener(al);
			s.addActionListener(al);
			z.addActionListener(al);
			n.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					lSzer.setEnabled(true);
					lWys.setEnabled(true);
					lMin.setEnabled(true);
					szer.setEnabled(true);
					wys.setEnabled(true);
					min.setEnabled(true);
				}
			});
			ok.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Enumeration<AbstractButton> en = bg.getElements();
					if(en.hasMoreElements()){
						try{
							if (p.isSelected())
								blockingQueue.put(new SetSizeEvent(Constans.BEGINER_X, Constans.BEGINER_Y, Constans.BEGINER_MINES));
							else if (s.isSelected())
								blockingQueue.put(new SetSizeEvent(Constans.MEDIUM_X, Constans.MEDIUM_Y, Constans.MEDIUM_MINES));
							else if (z.isSelected())
								blockingQueue.put(new SetSizeEvent(Constans.EXPERT_X, Constans.EXPERT_Y, Constans.EXPERT_MINES));
							else if (n.isSelected()){
								blockingQueue.put(new SetSizeEvent(new Integer(szer.getText()),new Integer(wys.getText()),new Integer(min.getText())));
							}
						}catch(NumberFormatException ex){
							cantMakeBoard();
						}catch(Exception ex){
							ex.printStackTrace();
							throw new RuntimeException(ex);
						}
					}
					d.dispose();
				}
			});
			pl.add(ok);
			d.add(pl);
			d.setVisible(true);
			return d;
		}
		
		/**
		 * do obsulgi klikniec mysza na panelu do gry
		 */
		private class FieldClickedListener extends MouseAdapter{
			public void mousePressed(MouseEvent event){
				try{
					if(event.getButton() == MouseEvent.BUTTON1){
						blockingQueue.put(new LeftButtonPressedEvent(event.getX()/Constans.FIELD_SIZE, event.getY()/Constans.FIELD_SIZE));
					}else{
						blockingQueue.put(new RightButtonPressedEvent(event.getX()/Constans.FIELD_SIZE, event.getY()/Constans.FIELD_SIZE));
					}
				}catch(Exception e){
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}	
		}
		
	}
}