package skipboserver;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import skipbo.IServerFinderService;
import skipbo.Karte;
import skipbo.MyIpResolver;
import skipbo.NetzNachricht;
import skipbo.ServerFinderService;
import skipbo.Resource;

/**
 * �berschrift: Beschreibung: Hauptklasse des Skip-Bo Servers Copyright:
 * Copyright (c) 2001 Organisation:
 * 
 * @author Paul Burlov
 * @version 1.0
 */
public class SkipBoServer
{

	static public final int DEFAULT_PORT = 11615;
	public SkipBoServerFenster serverfenster;
	private Connector connector;
	private Vector clients = new Vector(); // enth�lt Spielernamen in
	// Anmeldereihenfolge
	private Hashtable communicators = new Hashtable(); // Keys - Spielernamen,
	// Values - dazugeh�rige
	// ServerCommunicatoren
	private int spieleranzahl;
	// private int connected_spieler;
	private int active_spieler;
	private boolean connected;
	private Vector[] central_field;
	private Vector kartenpool;
	private Vector used_karten;
	private Random rnd;
	// SkipBoUrlCommunicator();
	private boolean registered = false;
	private Timer timer = new Timer();
	private String myHost;

	/**
	 */
	public SkipBoServer(boolean showFrame)
	{
		serverfenster = new SkipBoServerFenster(this);
		if (showFrame) serverfenster.setVisible(true);
		spieleranzahl = 0;
		connected = false;
	}

	public synchronized void nachrichtenBearbeiten(ServerCommunicator source, NetzNachricht nn)
	{
		String name_src = new String(nn.spielername_source);
		String name_dest = new String(nn.spielername_dest);
		Karte karte = null;
		switch (nn.command)
		{
		case NetzNachricht.ZUG_ABGEBEN:
			active_spieler++;
			active_spieler = active_spieler % spieleranzahl;
			nn = new NetzNachricht(NetzNachricht.ZUG_ERLAUBT);
			source = (ServerCommunicator) communicators.get(clients.get(active_spieler)); // aktiven
			// Kommunikator
			// rausfinden
			sendTo(source, nn);
			nn.command = NetzNachricht.NACHRICHT;
			nn.spielername_source = clients.get(active_spieler) + Resource.getString("msg100");
			sendToAll(nn, (String) clients.get(active_spieler));
			break;
		case NetzNachricht.SPIEL_BEENDEN:
			stopServer(name_src);
			break;
		case NetzNachricht.GET_KARTE:
			nn.command = NetzNachricht.ADD_KARTE;
			if (kartenpool.size() == 0)
			{
				neuMischen();
			}
			if (kartenpool.size() > 0)
			{
				karte = (Karte) kartenpool.remove(kartenpool.size() - 1);
				nn.karte = karte.getKartennummer();
			} else nn.karte = Karte.NOPE; // "leere" Karte abschicken, weil
			// keine freie Karten mehr vorhanden
			sendTo(source, nn);
			nn.command = NetzNachricht.ZUG_ERLAUBT;
			sendTo(source, nn);
			break;
		case NetzNachricht.MOVE_KARTE:
			// server.sendNachricht(nn);
			// if(nn.feldnummer_source > 4)
			// nn.command=NetzNachricht.ADD_KARTE;
			if (name_dest.equals("Center"))
			{
				if (nn.karte == Karte.JOKER)
				{
					if (central_field[nn.feldnummer_dest].size() <= 0)
					{
						nn.karte = 1; // wenn noch keine Karten im Feld
						// liegen dann wird Joker zu "1"
					} else nn.karte = ((Karte) central_field[nn.feldnummer_dest].lastElement()).getKartennummer() + 1; // Joker
					// wird
					// in
					// n�chst h�here
					// Karte
					// umgewandelt
					karte = new Karte(nn.karte); // Joker in eine
					// Spielkarte umwandeln
					karte.wasJoker = true;
					central_field[nn.feldnummer_dest].add(karte);
				} else central_field[nn.feldnummer_dest].add(new Karte(nn.karte));
			}
			sendToAll(nn, "");
			if ((nn.karte == 12 && name_dest.equals("Center"))) // Pr�ft ob
			// es die
			// obere
			// Karte mit
			// Wert 12
			// ist
			{
				Enumeration e = central_field[nn.feldnummer_dest].elements();
				while (e.hasMoreElements())
				{
					karte = (Karte) e.nextElement();
					if (karte.wasJoker) karte.kartenNummer = Karte.JOKER; // Benutzte
					// Joker
					// zur�ck
					// in
					// Joker
					// umwandeln
					used_karten.add(karte);
				}
				central_field[nn.feldnummer_dest].removeAllElements();
				nn.command = NetzNachricht.SET_KARTENSTAPEL;
				nn.kartenstapel = new int[50];
				nn.kartenstapel[0] = 0;
				sendToAll(nn, "");
				nn.kartenstapel = null;
			}
			if (name_src.equals(name_dest) && nn.feldnummer_dest < 4)
			{
				active_spieler++;
				active_spieler = active_spieler % spieleranzahl;
			}
			nn = new NetzNachricht(NetzNachricht.ZUG_ERLAUBT);
			source = (ServerCommunicator) communicators.get(clients.get(active_spieler)); // aktiven
			// Kommunikator
			// rausfinden
			sendTo(source, nn);
			nn.command = NetzNachricht.NACHRICHT;
			nn.spielername_source = clients.get(active_spieler) + Resource.getString("msg100");
			sendToAll(nn, (String) clients.get(active_spieler));
			break;
		case NetzNachricht.ANMELDEN:
			/*
			 * if(nn.karte != SkipBo.VERSION) { NetzNachricht reply = new
			 * NetzNachricht(NetzNachricht.ABGELEHNT); reply.params = new
			 * String[]{"Inkompatible Spielversionen."};
			 * source.unregisterNachritenListener(); sendTo(source,reply);
			 * return; }
			 */
			if (clients.size() >= spieleranzahl)
			{
				NetzNachricht abbruch = new NetzNachricht(NetzNachricht.ABGELEHNT);
				abbruch.params = new String[] { Resource.getString("msg100_1") };
				source.unregisterNachritenListener();
				sendTo(source, abbruch);
				return;
			}
			if (communicators.containsKey(name_src)) // Es exist. bereits
			// ein Spieler mit
			// diesen Namen
			{
				NetzNachricht abbruch = new NetzNachricht(NetzNachricht.ABGELEHNT);
				abbruch.spielername_source = name_src;
				abbruch.params = new String[] { Resource.getString("msg100_2") + name_src + Resource.getString("msg100_3") };
				source.unregisterNachritenListener();
				sendTo(source, abbruch);
				return;
			}
			nn.karte = clients.size(); // Absolute Zugreihenfolge
			sendTo(source, nn);
			for (int w = 0; w < clients.size(); w++)
			{
				nn.command = NetzNachricht.ANMELDEN;
				nn.spielername_source = ((String) clients.get(w));
				nn.karte = w; // Absolute Zugreihenfolge
				sendTo(source, nn);
			}
			serverfenster.nachrichtAusgeben("Spieler " + name_src + " connected.");
			clients.add(name_src);
			communicators.put(name_src, source);
			source.setCommunicatorName(name_src);
			nn = new NetzNachricht(NetzNachricht.ANMELDEN);
			nn.spielername_source = name_src;
			sendToAll(nn, name_src);
			if (clients.size() != spieleranzahl) return;
			spielBeginnen();
		}
	}

	private void spielBeginnen()
	{
		unregisterServer("Spiel beginnt.");
		// Kartenstapeln initialisieren
		NetzNachricht nn = new NetzNachricht(NetzNachricht.SET_KARTENSTAPEL);
		for (int i = 0; i < clients.size(); i++)
		{
			int[] temp = new int[30];
			for (int j = 0; j < 30; j++)
			{
				temp[j] = ((Karte) kartenpool.lastElement()).getKartennummer();
				kartenpool.setSize(kartenpool.size() - 1);
			}
			nn.command = NetzNachricht.SET_KARTENSTAPEL;
			nn.kartenstapel = temp;
			nn.feldnummer_dest = 4;
			nn.spielername_dest = ((String) clients.get(i));
			sendToAll(nn, "");
		}
		active_spieler = 0;
		nn.command = NetzNachricht.ZUG_ERLAUBT;
		nn.kartenstapel = null;
		ServerCommunicator source = (ServerCommunicator) communicators.get(clients.get(active_spieler));
		sendTo(source, nn);
		nn.command = NetzNachricht.NACHRICHT;
		nn.spielername_source = clients.get(active_spieler) + Resource.getString("msg100");
		sendToAll(nn, (String) clients.get(active_spieler));
	}

	public int startServer(int spieleranzahl) throws IOException
	{
		initDaten();
		clients = new Vector();
		communicators = new Hashtable();
		this.spieleranzahl = spieleranzahl;
		int versuch = 0;
		boolean success = false;
		/*
		 * while(!success) { try { connector = new Connector(this, port);
		 * success = true; }catch(IOException e) { versuch++; port++; if(versuch ==
		 * 10) throw e; //10 Versuche } }
		 */
		connector = new Connector(this, DEFAULT_PORT);
		connected = true;
		String adr = InetAddress.getLocalHost().getHostAddress();
		GregorianCalendar cal = new GregorianCalendar();
		serverfenster.nachrichtAusgeben("Serveradresse: " + adr + " auf Port: " + DEFAULT_PORT);
		serverfenster.nachrichtAusgeben("Server f�r " + spieleranzahl + " Spieler gestartet um " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
		serverfenster.nachrichtAusgeben("Warten auf Spieler");
		return DEFAULT_PORT;
	}

	public boolean istConnected()
	{
		return connected;
	}

	public synchronized void sendToAll(NetzNachricht nn, String ausnahmeName)
	{
		Enumeration namen = clients.elements();
		while (namen.hasMoreElements())
		{
			String name = (String) namen.nextElement();
			if (name.compareTo(ausnahmeName) != 0)
			{
				ServerCommunicator comm = ((ServerCommunicator) communicators.get(name));
				try
				{
					if (comm.isConnected()) comm.sendNachricht(nn);
				} catch (IOException e)
				{
					communicatorTerminated(comm);
				}
			}
		}
	}

	public synchronized void sendTo(ServerCommunicator comm, NetzNachricht nn)
	{
		try
		{
			comm.sendNachricht(nn);
		} catch (IOException e)
		{
			communicatorTerminated(comm);
		}
	}

	private synchronized void unregisterServer(String message)
	{
		if (registered)
		{
			registered = false;
		}
	}

	public synchronized void stopServer(String ursache)
	{
		timer.cancel();
		unregisterServer("Server gestoppt");
		if (connected)
		{
			connected = false;
			NetzNachricht nn = new NetzNachricht(NetzNachricht.SPIEL_BEENDEN);
			nn.params = new String[] { ursache };
			sendToAll(nn, "");
			// disconnectCommunicators();
			serverfenster.setStop();
			clients = new Vector();
			spieleranzahl = 0;
			communicators = new Hashtable();
			if (!kartenpool.isEmpty()) kartenpool.removeAllElements();
			if (!used_karten.isEmpty()) used_karten.removeAllElements();
			serverfenster.nachrichtAusgeben("Server gestoppt. " + ursache);
			if (connector != null) connector.stopConnector();
		}
	}

	synchronized public void communicatorTerminated(ServerCommunicator source)
	{
		if (!connected) return;
		if (communicators.containsValue(source))
		{
			stopServer(Resource.getString("msg100_4") + source.getCommunicatorName() + Resource.getString("msg100_5"));
		}
	}

	/*
	 * private void disconnectCommunicators() { Enumeration comms =
	 * communicators.keys(); while(comms.hasMoreElements()) {
	 * ((ServerCommunicator)communicators.get(comms.nextElement())).stopCommunicator(); } }
	 */
	// Mischen und bewegen Karten aus used_karten in kartenpool
	private void neuMischen()
	{
		kartenpool.addAll(used_karten);
		used_karten.removeAllElements();
		Collections.shuffle(kartenpool);
		/*
		 * while(!used_karten.isEmpty()) { for(int i=0;i
		 * <162;i++)kartenpool.add(used_karten.remove(rnd.nextInt(used_karten.size()))); }
		 */
	}

	/** Initialisierung und Mischen von Karten */
	private void initDaten()
	{
		central_field = new Vector[4];
		used_karten = new Vector(170);
		kartenpool = new Vector(170);
		// rnd=new Random();
		// erzeugung von 144 karten von 1 bis 12
		for (int i = 0; i < 12; i++)
		{
			for (int j = 1; j <= 12; j++)
				used_karten.add(new Karte(j));
		}
		// zuf�gung von 18 joker karten
		for (int i = 0; i < 18; i++)
		{
			used_karten.add(new Karte(13));
		}
		for (int i = 0; i < 4; i++)
			central_field[i] = new Vector(13);
		neuMischen();
	}

	public void chatMessageReceived(ServerCommunicator source, String message)
	{
		String ausnahmeName = "";// source.getName();
		Enumeration namen = clients.elements();
		while (namen.hasMoreElements())
		{
			String name = (String) namen.nextElement();
			if (name.compareTo(ausnahmeName) != 0)
			{
				ServerCommunicator comm = ((ServerCommunicator) communicators.get(name));
				try
				{
					if (comm.isConnected()) comm.sendChatMessage(message);
				} catch (IOException e)
				{
					communicatorTerminated(comm);
				}
			}
		}
	}

	public void registerServer(int spieleranzahl) throws IOException
	{
		registered = true;
		if (myHost == null || myHost.length() < 8)
		{
			myHost = MyIpResolver.resolveMyIp();
		}
		timer.schedule(new RegisterRefresher(spieleranzahl), 0, 10000);
	}

	class RegisterRefresher extends TimerTask
	{
		private IServerFinderService comm = ServerFinderService.getInstance();
		private int spieleranzahl;

		public RegisterRefresher(int spieleranzahl)
		{
			this.spieleranzahl = spieleranzahl;
		}

		@Override
		public void run()
		{
			if (myHost == null || myHost.length() < 8)
			{
				System.out.println("invalid host name: " + myHost);
				cancel();
				return;
			}
			try
			{
				if (!registered)
				{
					cancel();
					return;
				}
				comm.registerServer(myHost, spieleranzahl, 10);
			} catch (Exception e)
			{
				stopServer(e.getLocalizedMessage());
			}
		}
	}
}