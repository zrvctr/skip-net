package skipbo;

/**
 * 
 * @author Paul Burlov
 * @version 1.0
 */
public class Karte
{

	public static final int JOKER = 13;
	public static final int NOPE = 0;
	public int kartenNummer;
	public boolean wasJoker = false;

	public Karte(int number)
	{
		kartenNummer = number;
		if (number == JOKER) wasJoker = true;
	}

	public int getKartennummer()
	{
		return kartenNummer;
	}
}