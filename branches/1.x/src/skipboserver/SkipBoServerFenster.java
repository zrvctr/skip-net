package skipboserver;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.List;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * �berschrift: Beschreibung: Copyright: Copyright (c) 2001 Organisation:
 * 
 * @author Paul Burlov
 * @version 1.0
 */
public class SkipBoServerFenster extends Frame implements ActionListener
{

	private List messagelist;
	private TextField tx;
	private SkipBoServer owner;
	private CheckboxGroup ckb;
	private boolean reset_zustand;

	public SkipBoServerFenster(SkipBoServer owner)
	{
		super("Skip-Bo Server");
		setIconImage(getToolkit().createImage(getClass().getResource("/skipbo/images/skipbo.gif")));
		reset_zustand = true;
		this.owner = owner;
		setSize(400, 300);
		setLocation(100, 100);
		setBackground(Color.lightGray);
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		GridBagConstraints gbc = makegbc(0, 0, 1, 1);
		ckb = new CheckboxGroup();
		Checkbox cb = new Checkbox("Zwei Spieler", true, ckb);
		cb.setName("2");
		gbc.fill = GridBagConstraints.NONE;
		gbl.setConstraints(cb, gbc);
		add(cb);
		gbc = makegbc(1, 0, 1, 1);
		cb = new Checkbox("Drei Spieler", false, ckb);
		cb.setName("3");
		gbc.fill = GridBagConstraints.NONE;
		gbl.setConstraints(cb, gbc);
		add(cb);
		gbc = makegbc(2, 0, 1, 1);
		cb = new Checkbox("Vier Spieler", false, ckb);
		cb.setName("4");
		gbc.fill = GridBagConstraints.NONE;
		gbl.setConstraints(cb, gbc);
		add(cb);
		gbc = makegbc(0, 1, 1, 1);
		Label label = new Label("Port:");
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(label, gbc);
		add(label);
		gbc = makegbc(1, 1, 2, 1);
		tx = new TextField(Integer.toString(SkipBoServer.DEFAULT_PORT));
		tx.setEditable(true);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 100;
		gbl.setConstraints(tx, gbc);
		add(tx);
		Button bt = new Button("Start");
		bt.addActionListener(this);
		gbc = makegbc(1, 2, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 100;
		gbl.setConstraints(bt, gbc);
		add(bt);
		bt = new Button("Stop");
		bt.addActionListener(this);
		bt.setEnabled(false);
		gbc = makegbc(2, 2, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 100;
		gbl.setConstraints(bt, gbc);
		add(bt);
		messagelist = new List();
		gbc = makegbc(0, 3, 3, 3);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 100;
		gbc.weighty = 100;
		gbl.setConstraints(messagelist, gbc);
		add(messagelist);
		bt = new Button("Clear");
		bt.addActionListener(this);
		gbc = makegbc(0, 6, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 100;
		gbl.setConstraints(bt, gbc);
		add(bt);
		bt = new Button("Hide");
		bt.addActionListener(this);
		bt.setEnabled(false);
		gbc = makegbc(1, 6, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 100;
		gbl.setConstraints(bt, gbc);
		bt.setEnabled(false);
		add(bt);
		bt = new Button("Exit");
		bt.addActionListener(this);
		gbc = makegbc(2, 6, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 100;
		gbl.setConstraints(bt, gbc);
		add(bt);
	}

	private GridBagConstraints makegbc(int x, int y, int width, int height)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.insets = new Insets(1, 1, 1, 1);
		return gbc;
	}

	public void actionPerformed(ActionEvent e)
	{
		String cm;
		cm = e.getActionCommand();
		if (cm.equals("Clear"))
		{
			messagelist.removeAll();
		} else if (cm.equals("Hide"))
		{
			// setVisible(false);
		} else if (cm.equals("Exit"))
		{
			owner.stopServer("Exit Befehl des Benutzers");
			dispose();
		} else if (cm.equals("Start"))
		{
			int spieleranzahl = Integer.decode(ckb.getSelectedCheckbox().getName()).intValue();
			setStart();
			try
			{
				owner.startServer(spieleranzahl);
			} catch (IOException ex)
			{
				owner.stopServer("Serverstart fehlgeschlagen: " + ex.getLocalizedMessage());
			}
		} else if (cm.equals("Stop"))
		{
			owner.stopServer("Stop Befehl des Benutzers");
		}
	}

	public void setStart()
	{
		if (reset_zustand)
		{
			for (int i = 0; i <= 10; i++)
			{
				getComponent(i).setEnabled(!getComponent(i).isEnabled());
			}
		}
		reset_zustand = false;
		getComponent(7).setEnabled(true);
	}

	public void setStop()
	{
		if (!reset_zustand)
		{
			for (int i = 0; i <= 10; i++)
			{
				getComponent(i).setEnabled(!getComponent(i).isEnabled());
			}
		}
		reset_zustand = true;
		getComponent(7).setEnabled(true);
	}

	public synchronized void nachrichtAusgeben(String nachricht)
	{
		messagelist.add(nachricht);
	}
}