package pl.mizinski.saper.view;
import java.awt.*;
import javax.swing.*;

import pl.mizinski.saper.commons.Constans;

/**
 * klasa reprezentuja ta czesc Widoku na ktorej toczy sie rozgrywka
 * przez kompozycje umieszczona w klasie SaperFrame
 * @author Konrad Mizinski
 * @version 1.1
 */
public class SaperPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	/**obrazki ktore moga zostac narysowane na panelu*/
	private final static Image images[]= new Image[15];	//obrazki ktore moga zostac narysowane na panelu
	/**do przechowywania referencji na tablice z danymi do odswiezenia
	 * <br>indeksy obrazkow wg znaczenia pol w strukturze DataPack
	 */
	private byte tab[][];
	
	{   
		images[0]=new ImageIcon(this.getClass().getResource("img/0.jpg")).getImage();
		images[1]=new ImageIcon(this.getClass().getResource("img/1.jpg")).getImage();
		images[2]=new ImageIcon(this.getClass().getResource("img/2.jpg")).getImage();
		images[3]=new ImageIcon(this.getClass().getResource("img/3.jpg")).getImage();
		images[4]=new ImageIcon(this.getClass().getResource("img/4.jpg")).getImage();
		images[5]=new ImageIcon(this.getClass().getResource("img/5.jpg")).getImage();
		images[6]=new ImageIcon(this.getClass().getResource("img/6.jpg")).getImage();
		images[7]=new ImageIcon(this.getClass().getResource("img/7.jpg")).getImage();
		images[8]=new ImageIcon(this.getClass().getResource("img/8.jpg")).getImage();
		images[9]=new ImageIcon(this.getClass().getResource("img/9.jpg")).getImage();
		images[10]=new ImageIcon(this.getClass().getResource("img/10.jpg")).getImage();
		images[11]=new ImageIcon(this.getClass().getResource("img/11.jpg")).getImage();
		images[12]=new ImageIcon(this.getClass().getResource("img/12.jpg")).getImage();
		images[13]=new ImageIcon(this.getClass().getResource("img/13.jpg")).getImage();
		images[14]=new ImageIcon(this.getClass().getResource("img/14.jpg")).getImage();

	}
	
	/**
	 * wywoluje setBounds poniewaz rodzic nie powinien stosowac menadzera rozkladu
	 */
	SaperPanel(){
		setBounds(0,0,1,1);
	}
	
	/**
	 * przesloniona funkcja do rysowania komponentu
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Container c = getParent(); 
		setBounds( (c.getWidth()-Constans.FIELD_SIZE*tab.length)/2, (c.getHeight()-Constans.FIELD_SIZE*tab[0].length)/2, Constans.FIELD_SIZE*tab.length, Constans.FIELD_SIZE*tab[0].length);
		for(int i=0;i<tab.length;++i)
			for(int j=0;j<tab[i].length;++j)
				g.drawImage(images[tab[i][j]],i*Constans.FIELD_SIZE,j*Constans.FIELD_SIZE,null);
		g.setColor(Color.BLACK);
		g.drawLine(0,0,0, Constans.FIELD_SIZE*tab[0].length);
		g.setColor(Color.BLACK);
		g.drawLine(0, Constans.FIELD_SIZE*tab[0].length-1, Constans.FIELD_SIZE*tab.length, Constans.FIELD_SIZE*tab[0].length-1);
	
	}
	
	/**
	 * funkcja rysujaca na panelu zadana plansze do gry
	 * 
	 * @param tab tablica z informacja co narysowac na danym polu
	 */
	public void drawPanel(byte tablica[][]){
		tab=tablica;
		repaint();
	}
	
}
