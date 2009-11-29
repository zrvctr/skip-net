package skipbo;

import java.awt.Color;
import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * SpielRegeln Beschreibung: Erzeugt ScrollFenster mit Spielbeschreibung
 * Copyright: Paul Burlov Copyright (c) 2001 Organisation:
 * 
 * @author Paul Burlov
 * @version 1.0
 */
public class SpielRegeln extends JDialog
{

	private String s;
	private TextArea ta;
	private BufferedReader fr;
	private Vector vec;

	public SpielRegeln(JFrame owner)
	{
		super(owner, Resource.getString("spielregeln"), false);// erzeugt Frame
		// mit Titel
		ta = new TextArea();
		vec = new Vector();// TextPuffer
		try
		{
			fr = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/skipbo/spielregeln.txt")));
			// liest zeilenweise File "spielregeln.txt" ins TextFenster
			while ((s = fr.readLine()) != null)
			{
				ta.append(s);
				ta.append(System.getProperty("line.separator"));
			}
			fr.close();
		} catch (IOException e)
		{
			System.out.println(e.toString());
			ta.setVisible(false);
			dispose();
		}
		ta.setBackground(Color.lightGray);
		ta.setEditable(false);
		this.getContentPane().add(ta);
		setSize(600, 600);
		setLocation(100, 100);
		setResizable(true);
		setVisible(true);
	}
}