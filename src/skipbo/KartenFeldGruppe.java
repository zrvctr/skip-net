package skipbo;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JLabel;

/**
 * Copyright (c) 2001 Organisation:
 * 
 * @author Paul Burlov
 * @version 1.0
 */
public class KartenFeldGruppe implements MouseListener
{

	private ArrayList kartenfelder;
	private JLabel label;
	private String name;
	private SkipBoFenster owner;
	private int clickedFeld;
	private boolean zugewiesen;// ob schon zu einem Spieler zugewiesen
	private int reihenfolge = -1; // Absolute Zugreihenfolge

	public KartenFeldGruppe()
	{
		kartenfelder = new ArrayList(5);
		zugewiesen = false;
	}

	public KartenFeldGruppe(SkipBoFenster owner)
	{
		this.owner = owner;
		kartenfelder = new ArrayList(5);
		zugewiesen = false;
	}

	public void setReihenfolge(int reihenfolge)
	{
		this.reihenfolge = reihenfolge;
	}

	public int getReihenfolge()
	{
		return reihenfolge;
	}

	public boolean zugewiesen()
	{
		return zugewiesen;
	}

	public void zugewiesen(boolean zugewiesen)
	{
		this.zugewiesen = zugewiesen;
	}

	public void add(KartenFeld kr)
	{
		kartenfelder.add(kr.getNumber(), kr);
	}

	public void addLabel(JLabel lb)
	{
		label = lb;
	}

	public void setName(String name)
	{
		this.name = name;
		if (label != null) label.setText(name);
		// zugewiesen=true;
	}

	public String getName()
	{
		return name;
	}

	public int getClickedFeld()
	{
		return clickedFeld;
	}

	public int getKarte(int kartenFeld)
	{
		return ((KartenFeld) kartenfelder.get(kartenFeld)).getKarte();
	}

	public KartenFeld getKartenfeld(int feldnummer)
	{
		return (KartenFeld) kartenfelder.get(feldnummer);
	}

	public void addKarte(int kartenFeld, int karte)
	{
		((KartenFeld) kartenfelder.get(kartenFeld)).addKarte(karte);
	}

	public void removeKarte(int kartenFeld)
	{
		if (kartenfelder.size() <= kartenFeld) return;
		((KartenFeld) kartenfelder.get(kartenFeld)).removeKarte();
	}

	/*
	 * public int[] getKartenstapel(int kartenFeld){ return
	 * ((KartenFeld)kartenfelder.get(kartenFeld)).getKartenstapel();
	 */
	public void setKartenstapel(int kartenFeld, int[] kartenstapel)
	{
		((KartenFeld) kartenfelder.get(kartenFeld)).setKartenstapel(kartenstapel);
	}

	public void stapelAuswaehlen(int kartenFeld, boolean bl)
	{
		((KartenFeld) kartenfelder.get(kartenFeld)).setAusgewaehlt(bl);
	}

	public void mouseClicked(MouseEvent e)
	{
		clickedFeld = ((KartenFeld) e.getComponent()).getNumber();
		owner.kartenfeldClicked(this);
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
	}

	public void mouseReleased(MouseEvent e)
	{
	}
}