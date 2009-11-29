package skipbo;

import java.util.ArrayList;
import java.util.Random;

/**
 * Copyright (c) 2001 Organisation:
 * 
 * @author Paul Burlov
 * @version 1.0
 */
public class Kartenstand
{

	private ArrayList[] kartenstand;

	public Kartenstand()
	{
		kartenstand = new ArrayList[14];
		Random r = new Random();
		for (int i = 0; i < 13; i++)
		{
			kartenstand[i] = new ArrayList(16);
			for (int j = r.nextInt(16) + 1; j > 0; j--)
			{
				kartenstand[i].add(new Karte(r.nextInt(13) + 1));
			}
		}
		/*
		 * kartenstand[15].add(0,new Karte(10)); kartenstand[25].add(0,new
		 * Karte(9)); kartenstand[26].add(0,new Karte(13));
		 */
	}

	public ArrayList getKartenstapel(int kartenplatz)
	{
		return (kartenstand[kartenplatz]);
	}

	public void setKartenstapel(int kartenplatz, ArrayList ks)
	{
		kartenstand[kartenplatz] = ks;
	}
}