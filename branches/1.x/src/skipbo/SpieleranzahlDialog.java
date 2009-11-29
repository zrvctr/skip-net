package skipbo;

import java.awt.Frame;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.burlov.swing.SwingHelper;

public class SpieleranzahlDialog extends JDialog
{

	private JPanel jPanel1 = new JPanel();
	private JPanel jPanel2 = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JButton btOk = new JButton();
	private JRadioButton rb2 = new JRadioButton();
	private JRadioButton rb3 = new JRadioButton();
	private JRadioButton rb4 = new JRadioButton();
	JButton btCancel = new JButton();
	private boolean cancel = true;

	public SpieleranzahlDialog(Frame parent)
	{
		super(parent, Resource.getString("title50"), true);
		try
		{
			jbInit();
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		ButtonGroup bg = new ButtonGroup();
		bg.add(rb2);
		bg.add(rb3);
		bg.add(rb4);
		pack();
		setResizable(false);
		SwingHelper.centreWindow(parent, this);
	}

	public int showDialog()
	{
		setVisible(true);
		if(cancel) return -1;
		if(rb2.isSelected()) return 2;
		if(rb3.isSelected()) return 3;
		if(rb4.isSelected()) return 4;
		return -1;
	}

	private void jbInit() throws Exception
	{
		this.setSize(new Dimension(400, 300));
		this.getContentPane().setLayout(borderLayout1);
		jPanel1.setBorder(BorderFactory.createTitledBorder(Resource.getString("text50")));
		btOk.setText(Resource.getString("ok"));
		btOk.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				btOk_actionPerformed(e);
			}
		});
		rb2.setText("2 " + Resource.getString("players"));
		rb2.setSelected(true);
		rb3.setText("3 " + Resource.getString("players"));
		rb4.setText("4 " + Resource.getString("players"));
		btCancel.setText(Resource.getString("cancel"));
		btCancel.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				btCancel_actionPerformed(e);
			}
		});
		jPanel1.add(rb2, null);
		jPanel1.add(rb3, null);
		jPanel1.add(rb4, null);
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
		jPanel2.add(btOk, null);
		jPanel2.add(btCancel, null);
		this.getContentPane().add(jPanel2, BorderLayout.SOUTH);
	}

	void btOk_actionPerformed(ActionEvent e)
	{
		cancel = false;
		dispose();
	}

	void btCancel_actionPerformed(ActionEvent e)
	{
		cancel = true;
		dispose();
	}
}