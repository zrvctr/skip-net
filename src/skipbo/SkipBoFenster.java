package skipbo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

/**
 * �berschrift: Beschreibung: Copyright: Paul Burlov Copyright (c) 2001
 * Organisation:
 * 
 * @author Paul Burlov
 * @version 1.0
 */
public class SkipBoFenster extends JFrame
{

	private SkipBo owner;
	private JMenuBar mb = new JMenuBar();;
	private JLabel label;
	private JLabel nameLinks, nameRechts, nameOben;
	private KartenFeldGruppe[] feldgruppe;// feldLinks, feldOben, feldRechts,
	// feldMitte, feldUnten;
	private KartenFeld[] kf;
	private KartenFeldGruppe sf1, sf2;
	private int click1, click2;
	private boolean mouse;
	private String spielername;// Eigene Name
	private JPanel panelRoot = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel panelMessages = new JPanel();
	private JScrollPane jScrollPane1 = new JScrollPane();
	private BorderLayout borderLayout2 = new BorderLayout();
	private JTextArea taMessages = new JTextArea();
	private JPanel panelSpielfeld = new JPanel();
	private JTextField tfEingabe = new JTextField();
	private JMenu menuSpiel = new JMenu();
	private JMenuItem itemInternetSpielFinden = new JMenuItem();
	private JMenuItem itemMitServerVerbinden = new JMenuItem();
	private JMenuItem itemSpielBeenden = new JMenuItem();
	private JMenuItem itemInternetSpielStarten = new JMenuItem();
	private JMenuItem itemServerStarten = new JMenuItem();
	private JMenu menuHilfe = new JMenu();
	private JMenuItem itemSpielregel = new JMenuItem();
	private JMenuItem itemInfo = new JMenuItem();
	private JMenuItem jItemSpielstarten = new JMenuItem();

	protected SkipBoFenster()
	{
		super("Skip-Net");
		try
		{
			jbInit();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		setSize(600, 700);
	}

	public SkipBoFenster(final SkipBo owner)
	{
		this();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setFocusable(true);
		setIconImage(getToolkit().createImage(getClass().getResource("/skipbo/images/skipbo.gif")));
		this.owner = owner;
		// panelSpielfeld.setPreferredSize(new Dimension(670,600));
		setLocation(50, 50);
		panelSpielfeld.setBackground(new Color(0, 160, 0));
		feldgruppe = new KartenFeldGruppe[5];
		kf = new KartenFeld[29];
		// Initialisiert linke Kartenreihe
		kf[0] = (new KartenFeld(0, KartenFeld.LINKS, null));
		kf[1] = (new KartenFeld(1, KartenFeld.LINKS, null));
		kf[2] = (new KartenFeld(2, KartenFeld.LINKS, null));
		kf[3] = (new KartenFeld(3, KartenFeld.LINKS, null));
		kf[4] = (new KartenFeld(4, KartenFeld.LINKS, null));
		nameLinks = new JLabel("Spieler links", JLabel.CENTER);
		nameLinks.setFont(new Font("Serif", Font.PLAIN, 14));
		nameLinks.setForeground(Color.blue);
		feldgruppe[0] = new KartenFeldGruppe();
		for (int i = 0; i <= 4; i++)
		{
			feldgruppe[0].add(kf[i]);
			panelSpielfeld.add(kf[i], SpieltischLayout.KARTENGRUPPE_LINKS);
		}
		panelSpielfeld.add(nameLinks, SpieltischLayout.SPIELERNAME_LINKS);
		feldgruppe[0].addLabel(nameLinks);
		// Initialisiert obere Kartenreihe
		kf[9] = (new KartenFeld(4, KartenFeld.OBEN, null));
		kf[8] = (new KartenFeld(3, KartenFeld.OBEN, null));
		kf[7] = (new KartenFeld(2, KartenFeld.OBEN, null));
		kf[6] = (new KartenFeld(1, KartenFeld.OBEN, null));
		kf[5] = (new KartenFeld(0, KartenFeld.OBEN, null));
		nameOben = new JLabel("Spieler oben", JLabel.CENTER);
		nameOben.setFont(new Font("Serif", Font.PLAIN, 14));
		nameOben.setForeground(Color.blue);
		feldgruppe[1] = new KartenFeldGruppe();
		for (int i = 5; i <= 9; i++)
		{
			feldgruppe[1].add(kf[i]);
			panelSpielfeld.add(kf[i], SpieltischLayout.KARTENGRUPPE_OBEN);
		}
		panelSpielfeld.add(nameOben, SpieltischLayout.SPIELERNAME_OBEN);
		feldgruppe[1].addLabel(nameOben);
		// Initialisiert rechte Kartenreihe
		kf[14] = (new KartenFeld(4, KartenFeld.RECHTS, null));
		kf[13] = (new KartenFeld(3, KartenFeld.RECHTS, null));
		kf[12] = (new KartenFeld(2, KartenFeld.RECHTS, null));
		kf[11] = (new KartenFeld(1, KartenFeld.RECHTS, null));
		kf[10] = (new KartenFeld(0, KartenFeld.RECHTS, null));
		nameRechts = new JLabel("Spieler rechts", JLabel.CENTER);
		nameRechts.setFont(new Font("Serif", Font.PLAIN, 14));
		nameRechts.setForeground(Color.blue);
		// nameRechts.setVisible(true);
		feldgruppe[2] = new KartenFeldGruppe();
		for (int i = 10; i <= 14; i++)
		{
			feldgruppe[2].add(kf[i]);
			panelSpielfeld.add(kf[i], SpieltischLayout.KARTENGRUPPE_RECHTS);
		}
		panelSpielfeld.add(nameRechts, SpieltischLayout.SPIELERNAME_RECHTS);
		feldgruppe[2].addLabel(nameRechts);
		// Initialisiert zwei unteren Kartenreihen
		feldgruppe[3] = new KartenFeldGruppe(this);
		kf[15] = (new KartenFeld(0, KartenFeld.UNTEN, feldgruppe[3]));
		kf[16] = (new KartenFeld(1, KartenFeld.UNTEN, feldgruppe[3]));
		kf[17] = (new KartenFeld(2, KartenFeld.UNTEN, feldgruppe[3]));
		kf[18] = (new KartenFeld(3, KartenFeld.UNTEN, feldgruppe[3]));
		kf[19] = (new KartenFeld(4, KartenFeld.UNTEN, feldgruppe[3]));
		kf[20] = (new KartenFeld(5, KartenFeld.UNTEN, feldgruppe[3]));
		kf[21] = (new KartenFeld(6, KartenFeld.UNTEN, feldgruppe[3]));
		kf[22] = (new KartenFeld(7, KartenFeld.UNTEN, feldgruppe[3]));
		kf[23] = (new KartenFeld(8, KartenFeld.UNTEN, feldgruppe[3]));
		kf[24] = (new KartenFeld(9, KartenFeld.UNTEN, feldgruppe[3]));
		for (int i = 15; i <= 24; i++)
		{
			feldgruppe[3].add(kf[i]);
			panelSpielfeld.add(kf[i], SpieltischLayout.KARTENGRUPPE_UNTEN);
		}
		feldgruppe[3].setName("Eigen");
		// Initialisiert mittlere Kartenreihe
		feldgruppe[4] = new KartenFeldGruppe(this);
		kf[25] = (new KartenFeld(0, KartenFeld.UNTEN, feldgruppe[4]));
		kf[26] = (new KartenFeld(1, KartenFeld.UNTEN, feldgruppe[4]));
		kf[27] = (new KartenFeld(2, KartenFeld.UNTEN, feldgruppe[4]));
		kf[28] = (new KartenFeld(3, KartenFeld.UNTEN, feldgruppe[4]));
		for (int i = 25; i <= 28; i++)
		{
			feldgruppe[4].add(kf[i]);
			panelSpielfeld.add(kf[i], SpieltischLayout.KARTENGRUPPE_CENTER);
		}
		feldgruppe[4].setName("Center");
		// for(int i=0;i<29;i++){panelSpielfeld.add(kf[i]); }
		// pack();
		setResizable(true);
		// setVisible(true);
	}

	public void nachrichtAusgeben(String nachricht)
	{
		label.setText(nachricht);
		label.repaint();
	}

	public void anmeldenSpieler(String spielername, int reihenfolge)
	{
		if (!feldgruppe[3].zugewiesen())
		{
			feldgruppe[3].setName(spielername);
			feldgruppe[3].zugewiesen(true);
			feldgruppe[3].setReihenfolge(reihenfolge);
			this.spielername = spielername;
		} else if (!feldgruppe[1].zugewiesen())
		{
			feldgruppe[1].setName(spielername);
			feldgruppe[1].zugewiesen(true);
			feldgruppe[1].setReihenfolge(reihenfolge);
		} else if (!feldgruppe[0].zugewiesen())
		{
			feldgruppe[0].setName(spielername);
			feldgruppe[0].zugewiesen(true);
			feldgruppe[0].setReihenfolge(reihenfolge);
		} else if (!feldgruppe[2].zugewiesen())
		{
			feldgruppe[2].setName(spielername);
			feldgruppe[2].zugewiesen(true);
			feldgruppe[2].setReihenfolge(reihenfolge);
		}
		nachrichtAusgeben(spielername + Resource.getString("msg30"));
	}

	public int getFreiesHandkartenfeld()
	{
		for (int i = 5; i <= 9; i++)
		{
			if (feldgruppe[3].getKarte(i) == 0) return i;
		}
		return 10;
	}

	public int handkartenZaehlen()
	{
		int zahl = 0;
		for (int i = 5; i <= 9; i++)
		{
			if (feldgruppe[3].getKarte(i) != 0) zahl++;
		}
		return zahl;
	}

	/*
	 * public void setHandkarte(int karte){ for(int i=5;i <=9;i++){
	 * if(feldgruppe[3].getKarte(i) == 0){ feldgruppe[3].addKarte(i,karte);
	 * return; } }
	 */
	public void addKarte(String name, int feldnummer, int karte)
	{
		for (int i = 0; i <= 4; i++)
		{
			if (feldgruppe[i].getName().equals(name))
			{
				feldgruppe[i].addKarte(feldnummer, karte);
			}
		}
	}

	public void removeKarte(String name, int feldnummer)
	{
		for (int i = 0; i <= 4; i++)
		{
			if (feldgruppe[i].getName().equals(name))
			{
				feldgruppe[i].removeKarte(feldnummer);
			}
		}
	}

	public int getKarte(String name, int feldnummer)
	{
		for (int i = 0; i <= 4; i++)
		{
			if (feldgruppe[i].getName().equals(name))
			{
				return feldgruppe[i].getKarte(feldnummer);
			}
		}
		return 0;
	}

	public void setKartenstapel(String name, int feldnummer, int[] kartenstapel)
	{
		for (int i = 0; i <= 4; i++)
		{
			if (feldgruppe[i].getName().equals(name))
			{
				feldgruppe[i].setKartenstapel(feldnummer, kartenstapel);
				return;
			}
		}
	}

	public void reset()
	{
		for (int i = 0; i < 29; i++)
		{
			int[] s = new int[31];
			s[0] = 0;
			kf[i].setKartenstapel(s);
		}
		for (int i = 0; i < 4; i++)
		{
			feldgruppe[i].setName("");
			feldgruppe[i].zugewiesen(false);
		}
		taMessages.setText("");
	}

	// Action bei den Klicks auf die Kartenfelder
	public void kartenfeldClicked(KartenFeldGruppe sf)
	{
		if (owner.istBlockiert()) return;
		if (!mouse)// falls erste Mausklick
		{
			if (!sf.getName().equals(spielername)) return;
			sf1 = sf;
			click1 = sf1.getClickedFeld();
			if (sf.getKarte(click1) == 0) return;
			sf1.stapelAuswaehlen(click1, true);
			mouse = true;
		} else
		// falls zweite Mausklick
		{
			sf2 = sf;
			click2 = sf.getClickedFeld();
			if (((click1 <= 4) && (sf1.getName().equals(sf2.getName()))) || (click2 == 4) || ((click1 >= 5) && (click2 >= 5))
					|| (((sf1.getKarte(click1) != sf2.getKarte(click2) + 1) && (sf1.getKarte(click1) != Karte.JOKER) && (sf2.getKarte(click2) != Karte.JOKER) && (sf2.getName().equals("Center")))))
			{
				sf1.stapelAuswaehlen(click1, false);
				mouse = false;
				return;
			}
			// sf1.stapelAuswaehlen(click1,false);
			mouse = false;
			owner.zugSenden(sf1.getName(), click1, sf2.getName(), click2/* ,sf1.getKarte(click1) */);
			// sf2.addKarte(click2,sf1.removeKarte(click1));
		}
	}

	private void jbInit() throws Exception
	{
		this.addWindowListener(new WindowAdapter()
		{

			@Override
			public void windowClosing(WindowEvent e)
			{
				this_windowClosing(e);
			}
		});
		panelRoot.setLayout(borderLayout1);
		panelMessages.setLayout(borderLayout2);
		panelMessages.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		taMessages.setRows(5);
		taMessages.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		taMessages.setBackground(Color.black);
		taMessages.setForeground(Color.yellow);
		taMessages.setFont(new Font("Arial", 0, 11));
		taMessages.setEditable(false);
		panelSpielfeld.setLayout(new SpieltischLayout());
		panelSpielfeld.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		tfEingabe.setForeground(Color.yellow);
		tfEingabe.setBackground(Color.black);
		tfEingabe.setCaretColor(Color.white);
		tfEingabe.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				tfEingabe_actionPerformed(e);
			}
		});
		
		menuSpiel.setText(Resource.getString("spiel"));
		
		itemInternetSpielFinden.setText(Resource.getString("item30"));
		itemInternetSpielFinden.setToolTipText(Resource.getString("item31"));
		itemInternetSpielFinden.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				itemInternetSpielFinden_actionPerformed(e);
			}
		});
		itemMitServerVerbinden.setText(Resource.getString("item32"));
		itemMitServerVerbinden.setToolTipText(Resource.getString("tooltip32"));
		itemMitServerVerbinden.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				itemMitServerVerbinden_actionPerformed(e);
			}
		});
		itemSpielBeenden.setText(Resource.getString("item33"));
		itemSpielBeenden.setToolTipText(Resource.getString("tooltip33"));
		itemSpielBeenden.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				itemSpielBeenden_actionPerformed(e);
			}
		});
		itemInternetSpielStarten.setText(Resource.getString("item34"));
		itemInternetSpielStarten.setToolTipText(Resource.getString("tooltip34"));
		itemInternetSpielStarten.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				itemInternetSpielStarten_actionPerformed(e);
			}
		});
		itemServerStarten.setText(Resource.getString("item35"));
		itemServerStarten.setToolTipText(Resource.getString("tooltip35"));
		itemServerStarten.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				itemServerStarten_actionPerformed(e);
			}
		});
		menuHilfe.setText(Resource.getString("hilfe"));
		itemSpielregel.setText(Resource.getString("Spielregeln"));
		itemSpielregel.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				itemSpielregel_actionPerformed(e);
			}
		});
		itemInfo.setText(Resource.getString("Info"));
		itemInfo.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				itemInfo_actionPerformed(e);
			}
		});
		jScrollPane1.getViewport().add(taMessages, null);
		label = new JLabel(Resource.getString("Nachrichtenausgabe"), JLabel.CENTER);
		jItemSpielstarten.setText(Resource.getString("item36"));
		jItemSpielstarten.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				jItemSpielstarten_actionPerformed(e);
			}
		});
		label.setFont(new Font("Serif", Font.PLAIN, 14));
		panelMessages.add(label, BorderLayout.NORTH);
		panelMessages.add(jScrollPane1, BorderLayout.CENTER);
		panelMessages.add(tfEingabe, BorderLayout.SOUTH);
		panelRoot.add(panelMessages, BorderLayout.SOUTH);
		panelRoot.add(panelSpielfeld, BorderLayout.CENTER);
		this.getContentPane().add(panelRoot, BorderLayout.CENTER);
		menuSpiel.add(jItemSpielstarten);
		menuSpiel.addSeparator();
		menuSpiel.add(itemInternetSpielFinden);
		menuSpiel.add(itemInternetSpielStarten);
		menuSpiel.addSeparator();
		menuSpiel.add(itemMitServerVerbinden);
		menuSpiel.add(itemServerStarten);
		menuSpiel.addSeparator();
		menuSpiel.add(itemSpielBeenden);
		mb.add(menuSpiel);
		menuHilfe.add(itemSpielregel);
		menuHilfe.add(itemInfo);
		mb.add(menuHilfe);
		JLabel lab = new JLabel("<html><a href=\"http://skipoid-nexus.appspot.com\">"+ Resource.getString("playOnline") + "</a></html>");//Resource.getString("onlineGame"));
		lab.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(Desktop.isDesktopSupported()){
					try {
						Desktop.getDesktop().browse(new URI("http://skipoid-nexus.appspot.com"));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		lab.setToolTipText("http://skipoid-nexus.appspot.com");
//		JButton btOnlineGame = new JButton("onlineGame");
//		btOnlineGame.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				Desktop.isDesktopSupported()
//			}
//		});
		mb.add(Box.createHorizontalGlue());
		mb.add(lab);
		setJMenuBar(mb);
	}

	void tfEingabe_actionPerformed(ActionEvent e)
	{
		owner.sendChatMessage(tfEingabe.getText());
		tfEingabe.setText("");
	}

	public void printChatMessage(String message)
	{
		taMessages.setText(message + "\n" + taMessages.getText());
		// taMessages.append("\n"+message);
	}

	private void itemInternetSpielFinden_actionPerformed(ActionEvent e)
	{
		owner.internetSpielFinden();
	}

	private void itemInternetSpielStarten_actionPerformed(ActionEvent e)
	{
		owner.internetServerStarten();
	}

	private void itemMitServerVerbinden_actionPerformed(ActionEvent e)
	{
		owner.spielManuellFinden();
	}

	private void itemServerStarten_actionPerformed(ActionEvent e)
	{
		owner.lokaleServerStarten(true);
	}

	private void itemSpielBeenden_actionPerformed(ActionEvent e)
	{
		owner.aktuellesSpielBeenden();
	}

	private void itemSpielregel_actionPerformed(ActionEvent e)
	{
		new SpielRegeln(this).setVisible(true);
	}

	private void itemInfo_actionPerformed(ActionEvent e)
	{
		JOptionPane.showMessageDialog(this, Resource.getString("info_text"), Resource.getString("Info"), JOptionPane.INFORMATION_MESSAGE);
	}

	private void this_windowClosing(WindowEvent e)
	{
		owner.shutDown();
	}

	public void activate()
	{
		setState(JFrame.NORMAL);
		tfEingabe.requestFocus();
	}

	private void jItemSpielstarten_actionPerformed(ActionEvent e)
	{
		owner.computerspielStarten();
	}

	public SpielstatusView getSpielstatus()
	{
		return new SpielstatusView(feldgruppe);
	}
}