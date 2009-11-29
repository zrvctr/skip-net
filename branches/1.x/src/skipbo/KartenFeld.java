package skipbo;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

/**
 * KartenFeld Beschreibung: Erzeugt ein Kartenfeld wo die Mausklicks empfangen
 * werden und Kartensymbole ausgegeben. Grosse des Feldes ist 110x70 oder
 * 70x110, haengt von den Konstruktorparameter "vertikal" ab. Copyright: Paul
 * Burlov Copyright (c) 2001 Organisation:
 * 
 * @author Paul Burlov
 * @version 1.0
 */
public class KartenFeld extends JComponent
{

	static final public int LINKS = 1;
	static final public int OBEN = 2;
	static final public int RECHTS = 3;
	static final public int UNTEN = 4;
	static private Image[] oben, unten, rechts, links;
	static private String sep;
	static private boolean imageflag;
	private Image[] images;
	private int number;
	private int vordergrundimage;
	private int hintergrundimage;
	private int ausrichtung;
	private int[] kartenstapel;
	private int stapelcounter;
	static
	{
		imageflag = false;
		sep = System.getProperty("file.separator");
	}

	/**
	 * Konstruktor
	 * 
	 * @param String
	 *            name - Bezeichnung des Kartenfeldes zur spaeteren
	 * @param Identifizirung.
	 * @param int
	 *            x und int y Koordinaten des Kartenfeldes auf der Spielflaeche.
	 * @param boolean
	 *            vertikal bei "true" wird ein senkrechtes Kartenfeld erzeugt
	 * @param bei
	 *            "false" ein waagerechtes.
	 */
	public KartenFeld(int number, int ausrichtung, MouseListener ml)
	{
		if (!imageflag) initImageCache();
		this.number = number;
		addMouseListener(ml);
		this.ausrichtung = ausrichtung;
		if ((ausrichtung == KartenFeld.OBEN) || (ausrichtung == KartenFeld.UNTEN))
		{
			setSize(70, 110);
		} else setSize(110, 70);
		if (ausrichtung == KartenFeld.OBEN)
		{
			images = oben;
		} else if (ausrichtung == KartenFeld.UNTEN)
		{
			setCursor(new Cursor(Cursor.HAND_CURSOR));
			images = unten;
			if (number >= 25) setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		} else if (ausrichtung == KartenFeld.RECHTS)
		{
			images = rechts;
		} else if (ausrichtung == KartenFeld.LINKS)
		{
			images = links;
		}
		kartenstapel = new int[50];
		for (int i = 0; i < 50; i++)
			kartenstapel[i] = 0;
		stapelcounter = 0;
		hintergrundimage = 0;// kartenstapel[stapelcounter];
	}

	private void initImageCache()
	{
		links = new Image[15];
		rechts = new Image[15];
		oben = new Image[15];
		unten = new Image[15];
		// holt die Bilder von der Platte ins interne Imagesbuffer
		MediaTracker tracker = new MediaTracker(this);
		for (int i = 1; i <= 14; i++)
		{
			try
			{
				String path;
				path = "/skipbo/images/" + i + ".GIF";
				unten[i] = getToolkit().getImage(getClass().getResource(path));
				tracker.addImage(unten[i], i * 10 + 1);
				path = "/skipbo/images/" + i + "L.GIF";
				links[i] = getToolkit().getImage(getClass().getResource(path));
				tracker.addImage(links[i], i * 10 + 2);
				path = "/skipbo/images/" + i + "R.GIF";
				rechts[i] = getToolkit().getImage(getClass().getResource(path));
				tracker.addImage(rechts[i], i * 10 + 3);
				path = "/skipbo/images/" + i + "O.GIF";
				oben[i] = getToolkit().getImage(getClass().getResource(path));
				tracker.addImage(oben[i], i * 10 + 4);
				tracker.waitForAll();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		imageflag = true;
	}

	@Override
	public void paint(Graphics g)
	{
		// g.clearRect(0,0,getSize().width,getSize().height);
		g.setColor(new Color(0, 160, 0));
		g.fillRoundRect(0, 0, getSize().width, getSize().height, 0, 0);
		g.setColor(new Color(0, 140, 0));
		g.fillRoundRect(0, 9, getSize().width - 10, getSize().height - 10, 8, 8);
		g.setColor(Color.black);
		g.drawRoundRect(0, 9, getSize().width - 10, getSize().height - 10, 8, 8);
		int z = stapelcounter / 3;//
		int x = 0;
		int y = 10;
		if (stapelcounter != 0)
		{
			for (int i = 0; i < z; i++)
			{
				g.setColor(new Color(190, 190, 190));
				g.drawLine(x, y, x, y + (getSize().height - 10));
				x++;
				y--;
			}
			x = 0;
			y = (getSize().height);
			for (int i = 0; i < z; i++)
			{
				g.setColor(new Color(150, 150, 150));
				g.drawLine(x, y, x + getSize().width - 10, y);
				x++;
				y--;
			}
			// y+=1;
		}
		x = z;
		y = 10 - z;
		if (hintergrundimage != 0)
		{
			g.drawImage(images[hintergrundimage], x, y, getSize().width - 10, getSize().height - 10, this);
		}
		if (vordergrundimage != 0)
		{
			g.drawImage(images[vordergrundimage], 0, 0, getSize().width - 10, getSize().height - 10, this);
		}
	}

	public void setAusgewaehlt(boolean ausgewaehlt)
	{
		if (ausgewaehlt)
		{
			hintergrundimage = 0;
			if (stapelcounter > 0)
			{
				if (number == 4) hintergrundimage = 14;
				else
				{
					hintergrundimage = kartenstapel[stapelcounter - 1];
				}
			}
			vordergrundimage = kartenstapel[stapelcounter];
		} else
		{
			hintergrundimage = kartenstapel[stapelcounter];
			vordergrundimage = 0;
		}
		repaint();
	}

	public void addKarte(int karte)
	{
		if ((stapelcounter > 0) || (this.kartenstapel[0] != 0)) ++stapelcounter;
		kartenstapel[stapelcounter] = karte;
		hintergrundimage = karte;
		setAusgewaehlt(true);
		// try{Thread.sleep(200);}catch(InterruptedException ie){}
		setAusgewaehlt(false);
		// repaint();
	}

	public int removeKarte()
	{
		setAusgewaehlt(true);
		int karte = kartenstapel[stapelcounter];
		kartenstapel[stapelcounter] = 0;
		if (stapelcounter > 0) stapelcounter--;
		// try{Thread.sleep(200);}catch(InterruptedException ie){}
		setAusgewaehlt(false);
		// hintergrundimage=kartenstapel[stapelcounter];
		// repaint();
		return karte;
	}

	public int getKarte()
	{
		return kartenstapel[stapelcounter];
	}

	public int[] getKartenstapel()
	{
		return kartenstapel;
	}

	/*
	 * public int[] getKartenstapel(){ return kartenstapel;
	 */
	public void setKartenstapel(int[] kartenstapel)
	{
		this.kartenstapel = kartenstapel;
		stapelcounter = 0;
		while (this.kartenstapel[stapelcounter] != 0)
		{
			stapelcounter++;
			if (stapelcounter > 29) break;
		}
		if (stapelcounter > 0) stapelcounter--;
		hintergrundimage = kartenstapel[stapelcounter];
		setAusgewaehlt(false);
		// repaint();
		return;
	}

	public int getNumber()
	{
		return (number);
	}
}