package skipbo;

import java.io.Serializable;

/**
 * NachrichtenObject fuer Client-Server Kommunikation Copyright: Copyright (c)
 * 2001 Organisation:
 * 
 * @author Paul Burlov
 * @version 1.0
 */
public class NetzNachricht implements Serializable
{

	// ANMELDEN braucht ein String in spielername;
	static final public int ANMELDEN = 0;
	// ZUG_ERLAUBT braucht keine zusaezlichen Parametern
	static final public int ZUG_ERLAUBT = 1;
	// ZUC_ABGEBEN wird von client zum server geschikt um zu signalizieren,
	// dass
	// client diese Runde beendet hat. Keine zusaetzlichen Parametern
	// static final public int ZUG_ABGEBEN=2;
	// SET_KARTENSTAPEL braucht spielername, Kartenfeldnummer(0..4), und ein
	// Array
	// von int Elementen "kartenstapel" die Kartennummern repraesentieren
	static final public int SET_KARTENSTAPEL = 3;
	// braucht im Feld "karte" ein int als Kartennummer
	static final public int GET_KARTE = 4;
	// braucht ein String im Feld spielername
	static final public int NACHRICHT = 5;
	static final public int SPIEL_BEENDEN = 6;
	static final public int ADD_KARTE = 7;
	static final public int MOVE_KARTE = 8;
	static final public int ABGELEHNT = 12;
	// static final public int EXISTIERENDE_NAME=9;
	// static final public int SPIELER_ANZAHL_UEBERSCHRITTEN=10;
	static final public int VERSION = 11;
	static final public int ZUG_ABGEBEN = 13;
	static final long serialVersionUID = 131L;
	public int command;// Kommando
	public int karte;// Kartennummer
	public int feldnummer_source;
	public int feldnummer_dest;
	public int[] kartenstapel;// Array von karten
	public String spielername_source = "";
	public String spielername_dest = "";// Nachricht
	public String[] params = new String[0];

	public NetzNachricht(int command)
	{
		this.command = command;
	}
}