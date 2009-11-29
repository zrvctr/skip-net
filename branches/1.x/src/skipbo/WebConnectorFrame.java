package skipbo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import com.burlov.swing.SwingHelper;

public class WebConnectorFrame extends JFrame
{

	private JPanel jPanel1 = new JPanel();
	private JPanel jPanel2 = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel jPanel3 = new JPanel();
	private JScrollPane jScrollPane1 = new JScrollPane();
	private BorderLayout borderLayout2 = new BorderLayout();
	private JList listServers = new JList();
	private BorderLayout borderLayout3 = new BorderLayout();
	private SkipBo parent;
	private JPanel jPanel4 = new JPanel();
	private JButton btConnect = new JButton();
	private JButton btRefresh = new JButton();
	private BorderLayout borderLayout4 = new BorderLayout();
	private JPanel jPanel5 = new JPanel();
	private JRadioButton radio2 = new JRadioButton();
	private JRadioButton radio3 = new JRadioButton();
	private JRadioButton radio4 = new JRadioButton();
	private JLabel jLabel1 = new JLabel();
	private ButtonGroup bg = new ButtonGroup();

	public WebConnectorFrame(SkipBo parent)
	{
		try
		{
			jbInit();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		this.parent = parent;
		bg.add(radio2);
		bg.add(radio3);
		bg.add(radio4);
		setIconImage(getToolkit().createImage(getClass().getResource("/skipbo/images/skipbo.gif")));
		SwingHelper.centreWindow(null, this);
		pack();
		setVisible(true);
		this.btRefresh_actionPerformed(null);
	}

	private void jbInit() throws Exception
	{
		this.getContentPane().setLayout(borderLayout3);
		this.setSize(new Dimension(400, 300));
		setTitle(Resource.getString("title70"));
		jPanel1.setLayout(borderLayout1);
		jPanel2.setLayout(borderLayout2);
		jPanel2.setBorder(BorderFactory.createTitledBorder(Resource.getString("title71")));
		jPanel3.setLayout(borderLayout4);
		btConnect.setText(Resource.getString("verbinden"));
		btConnect.setToolTipText(Resource.getString("tooltip70"));
		btConnect.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				btConnect_actionPerformed(e);
			}
		});
		btRefresh.setText(Resource.getString("aktualisieren"));
		btRefresh.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				btRefresh_actionPerformed(e);
			}
		});
		radio2.setText("2 " + Resource.getString("players"));
		radio2.setSelected(true);
		radio3.setText("3 " + Resource.getString("players"));
		radio4.setText("4 " + Resource.getString("players"));
		jLabel1.setText(Resource.getString("text70"));
		jScrollPane1.getViewport().add(listServers, null);
		jPanel2.add(jScrollPane1, BorderLayout.CENTER);
		jPanel1.add(jPanel2, BorderLayout.CENTER);
		jPanel4.add(btConnect, null);
		jPanel4.add(btRefresh, null);
		jPanel3.add(jPanel4, BorderLayout.CENTER);
		jPanel5.add(jLabel1, null);
		jPanel5.add(radio2, null);
		jPanel5.add(radio3, null);
		jPanel5.add(radio4, null);
		jPanel3.add(jPanel5, BorderLayout.NORTH);
		jPanel1.add(jPanel3, BorderLayout.SOUTH);
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
	}

	private void btConnect_actionPerformed(ActionEvent ae)
	{
		InetAddress adr = (InetAddress) listServers.getSelectedValue();
		if (adr == null) return;
		try
		{
			parent.connectToServer(adr.getHostName());
		} catch (Throwable e)
		{
			return;
		}
	}

	private void btRefresh_actionPerformed(ActionEvent ae)
	{
		try
		{
			listServers.setListData(findServers(getSelectedSpieleranzahl()).toArray());
		} catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, Resource.getString("fehler70") + e.getLocalizedMessage(), Resource.getString("fehler"), JOptionPane.ERROR_MESSAGE);
		}
	}

	private List<InetAddress> findServers(int gamers) throws Exception
	{
		List<InetAddress> ret = new ArrayList<InetAddress>();
		for (String host : ServerFinderService.getInstance().findServer(gamers))
		{
			ret.add(InetAddress.getByName(host));
		}
		return ret;
	}

	private int getSelectedSpieleranzahl()
	{
		if (radio2.isSelected()) return 2;
		if (radio3.isSelected()) return 3;
		if (radio4.isSelected()) return 4;
		return 2;
	}

}