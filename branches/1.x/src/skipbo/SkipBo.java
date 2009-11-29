package skipbo;

import java.applet.Applet;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;

import javax.swing.JOptionPane;

import skipboserver.SkipBoServer;

/**
 * �berschrift: Beschreibung: Copyright: Copyright (c) 2001 Organisation:
 * 
 * @author Paul Burlov
 * @version 1.0
 */
public class SkipBo
{
	public static final long ZUG_PAUSE = 500;
	public static final int VERSION = 1510; //
	// public static AudioClip AUDIO_ANMELDUNG;
	// public static AudioClip AUDIO_ZUG;
	// public static AudioClip AUDIO_SPIELSTART;
	// public static AudioClip AUDIO_GEWONNEN;
	// public static AudioClip AUDIO_VERLOREN;
	// public static AudioClip AUDIO_SPIEL_ABGEBROCHEN;
	// public static final int COMMUNICATION_VERSION = 1400; // Version des
	// Communikationsprotokolls
	// public static final String SPRACHE = "DE";
	private long zugPause = ZUG_PAUSE;
	public boolean automatic_mode = false;
	private boolean testMode = false;
	private Computerspieler computerspieler;
	private boolean blockiert;
	private boolean zugabgeben;
	private boolean handkartenNachfuellen;
	private boolean keineKartenMehr = false;
	private boolean connected;
	private String spielername = "Spieler" + Integer.toString(new Random().nextInt(100));
	private SkipBoFenster skipboFenster;
	private SkipBoClient client;
	private SkipBoServer server;

	public SkipBo(boolean mode)
	{
		initSkipBo();
		automatic_mode = mode;
		skipboFenster = new SkipBoFenster(this);
		skipboFenster.setFocusable(true);
		skipboFenster.setFocusableWindowState(true);
		client = new SkipBoClient(this);
		if (mode) computerspieler = new Computerspieler(this);
		else skipboFenster.setVisible(true);
	}

	private void initSkipBo()
	{
		connected = false;
		blockiert = true;
		zugabgeben = true;
		handkartenNachfuellen = true;
	}

	public static void main(String[] args)
	{
		if (args.length > 0 && "-test".equalsIgnoreCase(args[0]))
		{// in Automatischen Testpodus starten
			SkipBo mainClient = new SkipBo(true);
			mainClient.testMode = true;
			mainClient.zugPause = 1;
			mainClient.skipboFenster.setVisible(true);
			mainClient.computerspielStarten();
		} else new SkipBo(false);
	}

	/*
	 * public void zugAbgeben(){ zugabgeben=true;
	 */
	public void sendChatMessage(String message)
	{
		if (!connected || client == null) return;
		message = "<" + spielername + "> " + message;
		try
		{
			client.sendChatMessage(message);
		} catch (IOException e)
		{
			clientTerminated(e);
		}
	}

	public boolean istBlockiert()
	{
		return blockiert;
	}

	public void nachrichtBearbeiten(NetzNachricht nn)
	{
		int cm = nn.command;
		String name_src = new String(nn.spielername_source);
		String name_dest = new String(nn.spielername_dest);
		switch (cm)
		{
		case NetzNachricht.ABGELEHNT:
			clientStoppen();
			skipboFenster.nachrichtAusgeben(nn.params[0]);// Ablehnungsgrund
			// ausgeben
			if (automatic_mode) computerspielerBeenden();
			break;
		case NetzNachricht.NACHRICHT:
			skipboFenster.nachrichtAusgeben(name_src);
			break;
		case NetzNachricht.SET_KARTENSTAPEL:
			skipboFenster.setKartenstapel(name_dest, nn.feldnummer_dest, nn.kartenstapel);
			break;
		case NetzNachricht.ANMELDEN:
			skipboFenster.anmeldenSpieler(name_src, nn.karte);
			Applet.newAudioClip(getClass().getResource("sound/dingdong.wav")).play();
			activateFrame();
			break;
		case NetzNachricht.MOVE_KARTE:
			String ns,
			nd;
			ns = new String(nn.spielername_source);
			nd = new String(nn.spielername_dest);
			skipboFenster.removeKarte(ns, nn.feldnummer_source);
			skipboFenster.addKarte(nd, nn.feldnummer_dest, nn.karte);
			break;
		case NetzNachricht.ADD_KARTE:
			if (nn.karte == Karte.NOPE) keineKartenMehr = true;
			skipboFenster.addKarte(name_dest, nn.feldnummer_dest, nn.karte);
			break;
		case NetzNachricht.ZUG_ERLAUBT:
			skipboFenster.nachrichtAusgeben(Resource.getString("ihr_zug"));
			// Applet.newAudioClip(getClass().getResource("sound/blip.wav")).play();
			// activateFrame();
			if (skipboFenster.getKarte(spielername, 4) == 0)
			{
				nn.command = NetzNachricht.SPIEL_BEENDEN;
				nn.spielername_source = spielername + " " + Resource.getString("hat_gewonnen");
				sendNachricht(nn);
				// Applet.newAudioClip(getClass().getResource("/skipbo/sound/trumpet.wav")).play();
			}
			int kartenanzahl = skipboFenster.handkartenZaehlen();
			if ((kartenanzahl == 0) && !zugabgeben) handkartenNachfuellen = true;
			if ((kartenanzahl != 5) && (zugabgeben || handkartenNachfuellen) && !keineKartenMehr)
			{
				nn = new NetzNachricht(NetzNachricht.GET_KARTE);
				nn.spielername_dest = spielername;
				nn.spielername_source = spielername;
				nn.feldnummer_dest = skipboFenster.getFreiesHandkartenfeld();
				sendNachricht(nn);
				break;
			}
			if ((kartenanzahl == 0) && keineKartenMehr && !automatic_mode)
			{// Wenn keine Karten mehr in untere Reihe, dann weiter Zug
				// geben
				zugAbgeben();
				break;
			}
			keineKartenMehr = false;
			handkartenNachfuellen = false;
			zugabgeben = false;
			blockiert = false;
			if (automatic_mode)
			{
				try
				{
					Thread.sleep(zugPause);
				} catch (Exception e)
				{
				}
				computerspieler.macheZug(skipboFenster.getSpielstatus());
			}
			break;
		case NetzNachricht.SPIEL_BEENDEN:
			clientStoppen();
			skipboFenster.nachrichtAusgeben(nn.params[0]);
			// Applet.newAudioClip(getClass().getResource("/skipbo/sound/trumpet.wav")).play();
			if (automatic_mode && !testMode) computerspielerBeenden();
			break;
		}
	}

	public void zugAbgeben()
	{
		sendNachricht(new NetzNachricht(NetzNachricht.ZUG_ABGEBEN));
	}

	protected void computerspielerBeenden()
	{
		skipboFenster.dispose();
	}

	private void activateFrame()
	{
		skipboFenster.activate();
	}

	private void sendNachricht(NetzNachricht nn)
	{
		if (!connected) return;
		try
		{
			client.sendNachricht(nn);
		} catch (IOException e)
		{
			clientTerminated(e);
		}
	}

	private void clientStoppen()
	{
		connected = false;
		if (client != null) client.stoppen();
		client = null;
		blockiert = true;
	}

	public void clientTerminated(Throwable e)
	{
		if (connected) skipboFenster.nachrichtAusgeben(Resource.getString("verbindung_getrennt") + " " + e.getLocalizedMessage());
		clientStoppen();
	}

	public void zugSenden(String src_name, int src_feld, String dest_name, int dest_feld)
	{
		System.out.println(src_name + ": " + src_feld + "(" + skipboFenster.getKarte(src_name, src_feld) + ")" + " auf " + dest_name + ": " + dest_feld + "("
				+ skipboFenster.getKarte(dest_name, dest_feld) + ")");
		if ((dest_name == "Center" && skipboFenster.getKarte(src_name, src_feld) != Karte.JOKER && (skipboFenster.getKarte(src_name, src_feld) - 1) != skipboFenster.getKarte(dest_name, dest_feld))
				|| (skipboFenster.getKarte(src_name, src_feld) == Karte.NOPE)) throw new RuntimeException("Nicht erlaubte Zug: Karte " + skipboFenster.getKarte(src_name, src_feld) + " wird auf Karte "
				+ skipboFenster.getKarte(dest_name, dest_feld) + " gelegt.");
		NetzNachricht nn = new NetzNachricht(NetzNachricht.MOVE_KARTE);
		nn.spielername_source = src_name;
		nn.spielername_dest = dest_name;
		nn.feldnummer_source = src_feld;
		nn.feldnummer_dest = dest_feld;
		nn.karte = skipboFenster.getKarte(src_name, src_feld);
		if ((dest_feld <= 4) && dest_name.equals(spielername))
		{
			zugabgeben = true;
			skipboFenster.nachrichtAusgeben(Resource.getString("zug_beendet"));
		}
		keineKartenMehr = false;
		blockiert = true;
		sendNachricht(nn);
	}

	public void chatMessageReceived(String message)
	{
		skipboFenster.printChatMessage(message);
	}

	public void shutDown()
	{
		if (connected)
		{
			int opt = JOptionPane.showOptionDialog(skipboFenster, Resource.getString("frage2"), Resource.getString("warnung"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null,
					null);
			if (opt == JOptionPane.OK_OPTION)
			{
				if (server != null) server.stopServer(Resource.getString("msg2") + " " + spielername + " " + Resource.getString("beendet")); // falls
				// server
				// lokal l�uft
				else aktuellesSpielBeenden(); // falls zu einem entfernten
				// Server connected
			} else return;
		}
		System.exit(0);
	}

	public void aktuellesSpielBeenden()
	{
		aktuellesSpielBeenden(Resource.getString("msg2") + " " + spielername + " " + Resource.getString("beendet"));
	}

	public void aktuellesSpielBeenden(String message)
	{
		NetzNachricht nn = new NetzNachricht(NetzNachricht.SPIEL_BEENDEN);
		nn.spielername_source = message;
		sendNachricht(nn);
		// clientStoppen();
	}

	public void lokaleServerStarten(boolean showHostAdresse)
	{
		lokaleServerStarten(spieleranzahlAbfragen(), showHostAdresse);
	}

	protected boolean lokaleServerStarten(int spieler, boolean showHostAdresse)
	{
		String addr;
		if (spieler < 2) return false;
		try
		{
			addr = InetAddress.getLocalHost().getHostAddress();
			SkipBoServer server = new SkipBoServer(false);
			server.startServer(spieler);
		} catch (IOException e)
		{
			JOptionPane.showMessageDialog(skipboFenster, Resource.getString("serverstart_fehlgeschlagen") + "\n" + e.getLocalizedMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (showHostAdresse) JOptionPane.showMessageDialog(skipboFenster, Resource.getString("serverstart_erfolgreich") + "\n" + Resource.getString("adresse1") + addr, "Server gestartet",
				JOptionPane.INFORMATION_MESSAGE);
		return connectToServer("localhost");
	}

	private boolean checkJavaVersion()
	{
		try
		{
			Class.forName("java.util.regex.Matcher");// Pr�ft das Vorhanden
			// von
			// der Klasse
			return true;
		} catch (Throwable e)
		{
			JOptionPane.showMessageDialog(skipboFenster, Resource.getString("msg21") + System.getProperty("java.version") + Resource.getString("msg22"), Resource.getString("warnung"),
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}

	/**
	 * Fragt nach ob evt laufendes Spiel beendet werden soll.
	 * 
	 * @return true, wenn alle ok, oder vom User best�tigt
	 * @return false falls User den Vorgang abgebrochen hat
	 */
	protected boolean checkConnected()
	{
		if (connected)
		{
			int opt = JOptionPane
					.showOptionDialog(skipboFenster, Resource.getString("msg23"), Resource.getString("warnung"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
			if (opt == JOptionPane.OK_OPTION)
			{
				aktuellesSpielBeenden();
			} else return false;
		}
		return true;
	}

	public void internetServerStarten()
	{
		if (!checkJavaVersion()) return;
		if (!checkConnected()) return;
		if (server != null) server.stopServer(Resource.getString("msg24") + spielername + " " + Resource.getString("beendet"));
		int spieler = spieleranzahlAbfragen();
		if (spieler < 2) return;
		try
		{
			server = new SkipBoServer(false);
			int port = server.startServer(spieler);
			server.registerServer(spieler);
			connectToServer("localhost");
		} catch (IOException e)
		{
			if (server != null) server.stopServer(Resource.getString("msg25"));
			server = null;
			JOptionPane.showMessageDialog(skipboFenster, Resource.getString("serverstart_fehlgeschlagen") + "\n" + e.getLocalizedMessage(), Resource.getString("fehler"), JOptionPane.ERROR_MESSAGE);
		}
	}

	public void internetSpielFinden()
	{
		if (!checkJavaVersion()) return;
		WebConnectorFrame frame = new WebConnectorFrame(this);
		// frame.setVisible(true);
	}

	public void spielManuellFinden()
	{
		String adr = (String) JOptionPane.showInputDialog(skipboFenster, Resource.getString("msg26") + ":", Resource.getString("msg26"), JOptionPane.QUESTION_MESSAGE, null, null, "localhost");
		if (adr == null) return;
		connectToServer(adr);
	}

	private int spieleranzahlAbfragen()
	{
		SpieleranzahlDialog dlg = new SpieleranzahlDialog(skipboFenster);
		return dlg.showDialog();
	}

	public boolean connectToServer(String host)
	{
		String name = (String) JOptionPane.showInputDialog(skipboFenster, Resource.getString("msg27"), "", JOptionPane.QUESTION_MESSAGE, null, null, spielername);
		if (name == null) return false;
		return connectToServer(host, name);
	}

	protected boolean connectToServer(String host, String name)
	{
		// if(connected)return;
		if (client != null && client.isAlive())
		{
			JOptionPane.showMessageDialog(skipboFenster, Resource.getString("msg28"), "", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		initSkipBo();// Interne Status-Variablen zur�cksetzen
		client = new SkipBoClient(this);
		skipboFenster.reset();
		skipboFenster.nachrichtAusgeben(Resource.getString("msg29"));
		try
		{
			client.connectToServer(host);
		} catch (Throwable t)
		{
			client = null;
			skipboFenster.nachrichtAusgeben(Resource.getString("msg210") + t.getLocalizedMessage());
			return false;
		}
		if (!client.isAlive()) return false;
		if (name == null || name.trim().length() == 0)
		{
			client.stoppen();
			client = null;
			return false;
		}
		NetzNachricht nn = new NetzNachricht(NetzNachricht.ANMELDEN);
		nn.spielername_source = name;
		nn.karte = 1400; // Clientversion dem Server mittteilen (deprecated,
		// ab
		// Ver 1.5 wird nicht ausgewertet)
		blockiert = true;
		connected = true;
		sendNachricht(nn);
		spielername = name;
		return true;
	}

	/**
	 * Startet ein Spiel mit Computergegnern
	 */
	public void computerspielStarten()
	{
		int anzahl = spieleranzahlAbfragen();
		if (anzahl < 2) return;
		if (!lokaleServerStarten(anzahl, false)) return; // Server lokal
		// starten
		// und den Client f�r
		// menschlichen Spieler
		// connecten.
		for (int i = 1; i < anzahl; i++)
		{
			SkipBo gegner = new SkipBo(true);
			gegner.zugPause = zugPause;
			if (!gegner.connectToServer("localhost", "Computer " + i))
			{
				JOptionPane.showMessageDialog(skipboFenster, Resource.getString("msg211"));
				if (server != null) server.stopServer(Resource.getString("24") + spielername + " " + Resource.getString("beendet"));
				return;
			}
		}
	}
}