package skipbo;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SpieltischLayout implements LayoutManager2
{

	static public final String SPIELERNAME_LINKS = "SPIELERNAME_LINKS";
	static public final String SPIELERNAME_RECHTS = "SPIELERNAME_RECHTS";
	static public final String SPIELERNAME_OBEN = "SPIELERNAME_OBEN";
	static public final String KARTENGRUPPE_LINKS = "KARTENGRUPPE_LINKS";
	static public final String KARTENGRUPPE_OBEN = "KARTENGRUPPE_OBEN";
	static public final String KARTENGRUPPE_RECHTS = "KARTENGRUPPE_RECHTS";
	static public final String KARTENGRUPPE_UNTEN = "KARTENGRUPPE_UNTEN";
	static public final String KARTENGRUPPE_CENTER = "KARTENGRUPPE_CENTER";
	static private final float kartenfeld_breite_faktor = 0.1f;
	static private final float kartenfeld_hoehe_faktor = 0.18f;
	static private final float spielfeld_breite_zu_hoehe = 1.2f;
	static private final float positionX_kartenfeld_links = 0.05f;
	static private final float positionY_kartenfeld_links = 0.15f;
	static private final float positionX_kartenfeld_rechts = 0.8f;
	static private final float positionY_kartenfeld_rechts = 0.15f;
	static private final float positionX_kartenfeld_oben = 0.25f;
	static private final float positionY_kartenfeld_oben = 0.1f;
	static private final float positionX_kartenfeld_center = 0.30f;
	static private final float positionY_kartenfeld_center = 0.33f;
	static private final float positionX_kartenfeld_unten = 0.25f;
	static private final float positionY_kartenfeld_unten = 0.6f;
	private float breite;
	private float hoehe;
	private float kartenfeldbreite;
	private float kartenfeldhoehe;
	private Hashtable labels = new Hashtable();
	private KartenFeld[] kartengruppe_links = new KartenFeld[5];
	private KartenFeld[] kartengruppe_rechts = new KartenFeld[5];
	private KartenFeld[] kartengruppe_oben = new KartenFeld[5];
	private KartenFeld[] kartengruppe_center = new KartenFeld[4];
	private KartenFeld[] kartengruppe_unten = new KartenFeld[10];

	public SpieltischLayout()
	{
	}

	private void layoutKartenfelder()
	{
		int x, x2, y, y2, z;
		//+++++++ Linke Kartengruppe plazieren +++++++++++
		x = (int) (breite * positionX_kartenfeld_links);
		y = (int) (hoehe * positionY_kartenfeld_links);
		for(z = 0; z < kartengruppe_links.length; z++)
		{
			KartenFeld feld = kartengruppe_links[z];
			y2 = y + ((int) kartenfeldbreite) * z;
			feld.setSize((int) kartenfeldhoehe, (int) kartenfeldbreite);
			feld.setLocation(x, y2);
		}
		//+++++++ Rechte Kartengruppe plazieren +++++++++++
		x = (int) (breite * positionX_kartenfeld_rechts);
		y = (int) (hoehe * positionY_kartenfeld_rechts);
		int i = 0;
		for(z = kartengruppe_rechts.length - 1; z >= 0; z--)
		{
			KartenFeld feld = kartengruppe_rechts[z];
			y2 = y + ((int) kartenfeldbreite) * i;
			i++;
			feld.setSize((int) kartenfeldhoehe, (int) kartenfeldbreite);
			feld.setLocation(x, y2);
		}
		//+++++++ Obere Kartengruppe plazieren +++++++++++
		x = (int) (breite * positionX_kartenfeld_oben);
		y = (int) (hoehe * positionY_kartenfeld_oben);
		i = 0;
		for(z = kartengruppe_oben.length - 1; z >= 0; z--)
		{
			KartenFeld feld = kartengruppe_oben[z];
			x2 = x + ((int) kartenfeldbreite) * i;
			i++;
			feld.setSize((int) kartenfeldbreite, (int) kartenfeldhoehe);
			feld.setLocation(x2, y);
		}
		//+++++++ Zentrale Kartengruppe plazieren +++++++++++
		x = (int) (breite * positionX_kartenfeld_center);
		y = (int) (hoehe * positionY_kartenfeld_center);
		for(z = 0; z < kartengruppe_center.length; z++)
		{
			KartenFeld feld = kartengruppe_center[z];
			x2 = x + ((int) kartenfeldbreite) * z;
			feld.setSize((int) kartenfeldbreite, (int) kartenfeldhoehe);
			feld.setLocation(x2, y);
		}
		//+++++++ Untere Kartengruppe plazieren +++++++++++
		x = (int) (breite * positionX_kartenfeld_unten);
		y = (int) (hoehe * positionY_kartenfeld_unten);
		for(z = 0; z < kartengruppe_unten.length; z++)
		{
			KartenFeld feld = kartengruppe_unten[z];
			x2 = x + ((int) kartenfeldbreite) * (z % 5);
			y2 = y;
			if(z >= 5)
			{
				y2 = y + ((int) kartenfeldhoehe);
			}
			feld.setSize((int) kartenfeldbreite, (int) kartenfeldhoehe);
			feld.setLocation(x2, y2);
		}
	}

	public void layoutContainer(Container target)
	{
		// +++++++ Breite zu Hoehe von Parent-Component normalisieren ++++++
		Dimension currentSize = target.getSize();
		float width = (float) (currentSize.getHeight() * spielfeld_breite_zu_hoehe);
		float height = (float) (currentSize.getWidth() / spielfeld_breite_zu_hoehe);
		if(width > currentSize.width)
		{
			breite = currentSize.width;
			hoehe = height;
		} else
		{
			breite = width;
			hoehe = currentSize.height;
		}
		kartenfeldbreite = breite * kartenfeld_breite_faktor;
		kartenfeldhoehe = hoehe * kartenfeld_hoehe_faktor;
		//    ((JComponent)target).setPreferredSize(new
		// Dimension((int)breite,(int)hoehe));
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		layoutLabels(target);
		layoutKartenfelder();
	}

	private void layoutLabels(Component target)
	{
		JLabel label = (JLabel) labels.get(SPIELERNAME_LINKS);
		int labelbreite = (int) kartenfeldhoehe;
		label.setSize(labelbreite, 25);
		int x = (int) (breite * positionX_kartenfeld_links);
		int y = (int) (hoehe * positionY_kartenfeld_links - label.getSize().height);
		label.setLocation(x, y);
		label = (JLabel) labels.get(SPIELERNAME_RECHTS);
		label.setSize(labelbreite, 25);
		x = (int) (breite * positionX_kartenfeld_rechts);
		y = (int) (hoehe * positionY_kartenfeld_rechts - label.getSize().height);
		label.setLocation(x, y);
		label = (JLabel) labels.get(SPIELERNAME_OBEN);
		label.setSize(labelbreite, 25);
		x = (int) (breite * positionX_kartenfeld_oben + ((kartenfeldbreite * 5) - labelbreite) / 2);
		y = (int) (hoehe * positionY_kartenfeld_oben - label.getSize().height);
		label.setLocation(x, y);
	}

	public void addLayoutComponent(Component comp, Object constr)
	{
		if(constr == null) return;
		String position = constr.toString();
		if(position.startsWith("SPIELERNAME"))
		{
			labels.put(position, comp);
			return;
		}
		if(position == KARTENGRUPPE_LINKS)
			kartengruppe_links[((KartenFeld) comp).getNumber()] = (KartenFeld) comp;
		else if(position == KARTENGRUPPE_OBEN)
			kartengruppe_oben[((KartenFeld) comp).getNumber()] = (KartenFeld) comp;
		else if(position == KARTENGRUPPE_RECHTS)
			kartengruppe_rechts[((KartenFeld) comp).getNumber()] = (KartenFeld) comp;
		else if(position == KARTENGRUPPE_UNTEN)
			kartengruppe_unten[((KartenFeld) comp).getNumber()] = (KartenFeld) comp;
		else if(position == KARTENGRUPPE_CENTER)
			kartengruppe_center[((KartenFeld) comp).getNumber()] = (KartenFeld) comp;
		else
			throw new RuntimeException("Unbekannte Layout-Position.");
	}

	public void addLayoutComponent(String name, Component comp)
	{
		addLayoutComponent(comp, name);
	}

	public void removeLayoutComponent(Component comp)
	{
	}

	public void invalidateLayout(Container target)
	{
	}

	public float getLayoutAlignmentY(Container target)
	{
		return 0.5f;
	}

	public float getLayoutAlignmentX(Container target)
	{
		return 0.5f;
	}

	public Dimension maximumLayoutSize(Container target)
	{
		return new Dimension(2000, 2000);
	}

	public Dimension minimumLayoutSize(Container target)
	{
		return new Dimension(300, 300);
	}

	public Dimension preferredLayoutSize(Container target)
	{
		return new Dimension(670, 600);
	}
}