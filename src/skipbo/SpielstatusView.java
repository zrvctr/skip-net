package skipbo;

import java.util.*;

/**
 * Wrapper-Classe für den Spielstand, mit getter-Methoden für eigene und fremde
 * Kartenfelder
 */
public class SpielstatusView
{

	private KartenFeldGruppe[] feldgruppen;
	private int[] ablegekarten = new int[4];
	private int[] handkarten = new int[5];
	//private Stack[] hilfskarten;
	private int[] hilfskarten = new int[4];
	private int spielstapelkarte;
	private int[][] gegnerkarten;

	public SpielstatusView(SpielstatusView spielstatus)
	{
		feldgruppen = spielstatus.feldgruppen;
		System.arraycopy(spielstatus.ablegekarten, 0, ablegekarten, 0,
				spielstatus.ablegekarten.length);
		System.arraycopy(spielstatus.handkarten, 0, handkarten, 0, spielstatus.handkarten.length);
		System
				.arraycopy(spielstatus.hilfskarten, 0, hilfskarten, 0,
						spielstatus.hilfskarten.length);
		gegnerkarten = spielstatus.gegnerkarten;
		/*
		 * hilfskarten = new Stack[spielstatus.hilfskarten.length]; for(int i = 0;
		 * i < spielstatus.hilfskarten.length; i++) { hilfskarten[i] =
		 * (Stack)spielstatus.hilfskarten[i].clone(); }
		 */
		spielstapelkarte = spielstatus.spielstapelkarte;
	}

	public SpielstatusView(KartenFeldGruppe[] feldgruppen)
	{
		this.feldgruppen = feldgruppen;
		spielstapelkarte = feldgruppen[3].getKarte(4);
		for(int i = 0; i < 4; i++)
		{
			ablegekarten[i] = feldgruppen[4].getKarte(i);
		}
		for(int i = 0; i < 4; i++)
		{
			hilfskarten[i] = feldgruppen[3].getKarte(i);
		}
		/*
		 * hilfskarten = new Stack[4]; for(int i = 0; i < 4; i++) {
		 * hilfskarten[i] = new Stack(); int[] stapel =
		 * feldgruppen[3].getKartenfeld(i).getKartenstapel(); for(int j = 0; j <
		 * stapel.length; j++) { hilfskarten[i].push(new Integer(stapel[j])); } }
		 */
		for(int i = 5; i < 10; i++)
		{
			handkarten[i - 5] = feldgruppen[3].getKarte(i);
		}
		gegnerkarten = getAlleGegnerkarten();
	}

	/**
	 * Dursucht Hilfskarten auf den Parameterwert
	 * 
	 * @return feldnummer mit gefundene Karte, anderfalss -1
	 */
	public int habeHilfskarte(int wert)
	{
		for(int j = 0; j < hilfskarten.length; j++)
		{
			if(hilfskarten[j] == wert) return j;
			//      if(((Integer)hilfskarten[j].peek()).intValue() == wert) return
			// j;
		}
		return -1;
	}

	/**
	 * Dursucht Handkarte auf den Parameterwert
	 * 
	 * @return feldnummer mit gefundene Karte, anderfalss -1
	 */
	public int habeHandkarte(int wert)
	{
		for(int j = 0; j < handkarten.length; j++)
		{
			if(handkarten[j] == wert) return j;
		}
		return -1;
	}

	/**
	 * Liefert 4 Werte die für Ablegekarten stehen
	 */
	public int[] getAblegekarten()
	{
		return ablegekarten;
	}

	/**
	 * Liefert die obere Karte vom Spielstapel
	 */
	public int getSpielstapelkarte()
	{
		return spielstapelkarte;
	}

	/**
	 * Liefert Array mit 4 Werten, die Hilfsstapel repräsentieren 0 = leeres
	 * Feld
	 */
	public int[] getHilfskarten()
	{
		/*
		 * int[] ret = new int[4]; for(int i = 0; i < hilfskarten.length; i++) {
		 * ret[i] = ((Integer)hilfskarten[i].peek()).intValue(); } return ret;
		 */
		return hilfskarten;
	}

	/**
	 * Leifert Array mit 5 Werten, die Handkarten repräsentieren 0 = leeres
	 * Feld
	 */
	public int[] getHandkarten()
	{
		return handkarten;
	}

	/**
	 * Liefert zweidimensionale Array: äußere Array: repräsentiert alle Gegner
	 * innere Array: 5 sichtbaren Karten von Gegenspieler (Spielstapel wird
	 * durch 5-te Wert dargestellt)
	 */
	public int[][] getGegnerkarten()
	{
		return gegnerkarten;
	}

	private int[][] getAlleGegnerkarten()
	{
		int[][] ret = new int[3][5];
		for(int j = 0; j < 3; j++)
		{
			ret[j] = getGegnerkarten(j);
		}
		return ret;
	}

	private int[] getGegnerkarten(int kartenfeldgruppe)
	{
		int[] ret = new int[5];
		for(int i = 0; i < 5; i++)
		{
			ret[i] = feldgruppen[kartenfeldgruppe].getKarte(i);
		}
		return ret;
	}

	/**
	 * Gibt Name des Spielers zurück
	 */
	public String getEigenname()
	{
		return feldgruppen[3].getName();
	}

	public String getZentralFeldname()
	{
		return feldgruppen[4].getName();
	}

	public void removeHandkarte(int feldnummer)
	{
		handkarten[feldnummer] = Karte.NOPE;
	}

	public void removeHilfskarte(int feldnummer)
	{
		//     return ((Integer)hilfskarten[feldnummer].pop()).intValue();
		hilfskarten[feldnummer] = Karte.NOPE;
	}

	public int[] getKartenVonNaechstenSpieler()
	{
		int anfangsspieler = -1;
		int eigen_reihenfolge = feldgruppen[3].getReihenfolge();
		for(int i = 0; i < feldgruppen.length; i++)
		{
			if(feldgruppen[i].getReihenfolge() > eigen_reihenfolge) return getGegnerkarten(i);
			if(feldgruppen[i].getReihenfolge() == 0) anfangsspieler = i;
		}
		//Falls diese Spieler letze in der Reihe ist dann, den Karten von
		// allerersten Spieler zurückgeben
		return getGegnerkarten(anfangsspieler);
	}

	public int getIndexVonGroessteHandkarte()
	{
		return getBiggestNumber(handkarten);
	}

	public int getIndexVonGroessteHilfskarte()
	{
		return getBiggestNumber(hilfskarten);
	}

	public int getFreieAblagefeld()
	{
		for(int j = 0; j < ablegekarten.length; j++)
		{
			if(ablegekarten[j] == Karte.NOPE) return j;
		}
		return -1;
	}

	public int getFreieHilfsfeld()
	{
		for(int j = 0; j < hilfskarten.length; j++)
		{
			if(hilfskarten[j] == Karte.NOPE) return j;
		}
		return -1;
	}

	/**
	 * Gibt den Index der Handkarte die am meisten Plätze belegt
	 */
	public int getMeistVorhandeneHandkarte()
	{
		int index = -1;
		int counter = 1;
		for(int i = 0; i < handkarten.length; i++)
		{
			if(handkarten[i] == Karte.NOPE || handkarten[i] == Karte.JOKER) continue;
			int counter2 = 0;
			int index2 = -1;
			for(int j = 0; j < handkarten.length; j++)
			{
				if(handkarten[i] == handkarten[j])
				{
					counter2++;
					index2 = j;
				}
			}
			if(counter2 > counter)
			{
				index = index2;
				counter = counter2;
			}
		}
		return index;
	}

	/**
	 * Gibt den Index der Ablegekarte die am meisten Plätze belegt
	 */
	public int getMeistVorhandeneAblegekarte()
	{
		int index = -1;
		int counter = 1;
		for(int i = 0; i < ablegekarten.length; i++)
		{
			int counter2 = 0;
			int index2 = -1;
			for(int j = 0; j < ablegekarten.length; j++)
			{
				if(ablegekarten[i] == ablegekarten[j])
				{
					counter2++;
					index2 = j;
				}
			}
			if(counter2 > counter)
			{
				index = index2;
				counter = counter2;
			}
		}
		return index;
	}

	private int getBiggestNumber(int[] array)
	{
		int wert = 0;
		int index = -1;
		for(int j = 0; j < array.length; j++)
		{// Alles außer Joker zählen
			if(array[j] > wert && array[j] != Karte.JOKER)
			{
				wert = array[j];
				index = j;
			}
		}
		if(wert == 0)
		{// wenn nichts außer Joker da ist, dann Joker nehmen
			for(int j = 0; j < array.length; j++)
			{
				if(array[j] > wert)
				{
					wert = array[j];
					index = j;
				}
			}
		}
		return index;
	}

	/**
	 * Berechnet die absolute Differenz zwischen zwei Kartenwerten (karte_1 -
	 * karte_2) ohne Vorzeichen
	 */
	static public int differenz(int karte_1, int karte_2)
	{
		int diff = karte_1 - karte_2;
		if(diff >= 0) return diff;
		return diff + 12;
	}

	public boolean equals(Object spielstatus)
	{
		if(!(spielstatus instanceof SpielstatusView)) return false;
		SpielstatusView status = (SpielstatusView) spielstatus;
		if(!Arrays.equals(status.ablegekarten, ablegekarten)) return false;
		if(!Arrays.equals(status.handkarten, handkarten)) return false;
		if(!Arrays.equals(status.hilfskarten, hilfskarten)) return false;
		if(status.spielstapelkarte != spielstapelkarte) return false;
		for(int i = 0; i < gegnerkarten.length; i++)
		{
			if(!Arrays.equals(status.gegnerkarten[i], gegnerkarten[i])) return false;
		}
		return true;
	}

	/**
	 * Berechnent nächstniedrige Wert
	 */
	static public int decrementWert(int kartenwert)
	{
		if(kartenwert < 2) return 12;
		return --kartenwert;
	}

	/**
	 * Berechnent nächsthöhere Wert
	 */
	static public int incrementWert(int kartenwert)
	{
		if(kartenwert > 11) return 1;
		return ++kartenwert;
	}
}