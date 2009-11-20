package skipbo;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import skipboserver.SkipBoServer;

/**
 * 
 * @author Paul Burlov
 * @version 1.0
 */
public class DialogSpielFinden extends Dialog implements ActionListener
{

	// private String name;
	// private String addresse;
	// private String port;
	private TextField tfName, tfAddresse, tfPort;
	// private SkipBoFenster al;
	// private boolean propertiesGesezt;
	private ClientProperties eingabe;
	private boolean cancel;

	public DialogSpielFinden(SkipBoFenster owner)
	{
		super(owner, true);
		// propertiesGesezt = false;
		cancel = false;
		setTitle(Resource.getString("title1"));
		setLayout(new GridLayout(4, 2));
		add(new Label(Resource.getString("name1"), Label.RIGHT));
		tfName = new TextField("", 20);
		// add(tfName);
		add(new Label(Resource.getString("adresse1"), Label.RIGHT));
		tfAddresse = new TextField("localhost", 20);
		add(tfAddresse);
		add(new Label(Resource.getString("port1"), Label.RIGHT));
		tfPort = new TextField(Integer.toString(SkipBoServer.DEFAULT_PORT), 5);
		add(tfPort);
		Button bt = new Button(Resource.getString("ok"));
		// bt.setEnabled(false);
		bt.addActionListener(this);
		add(bt);
		bt = new Button(Resource.getString("cancel"));
		bt.addActionListener(this);
		add(bt);
		setBackground(Color.lightGray);
		pack();
		setResizable(false);
		setLocation((owner.getLocation()).x + 300, (owner.getLocation().y + 300));
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		String cm;
		cm = e.getActionCommand();
		if (cm.equals(Resource.getString("ok")))
		{
			String name = tfName.getText();
			String addresse = tfAddresse.getText();
			String port = tfPort.getText();
			if (name.equals("") || addresse.equals("") || port.equals("")) return;
			int portInt;
			try
			{
				portInt = Integer.parseInt(port);
			} catch (NumberFormatException nfe)
			{
				return;
			}
			eingabe = new ClientProperties(name, addresse, portInt);
			// propertiesGesezt = true;
			dispose();
		} else if (cm.equals(Resource.getString("cancel")))
		{
			// propertiesGesezt=true;
			cancel = true;
			setVisible(false);
			dispose();
		}
	}

	public ClientProperties getEingabe()
	{
		return eingabe;
	}
}