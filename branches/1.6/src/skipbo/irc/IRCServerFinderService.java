package skipbo.irc;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.schwering.irc.lib.IRCConnection;
import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCModeParser;
import org.schwering.irc.lib.IRCUser;

import skipbo.IServerFinderService;

public class IRCServerFinderService implements IServerFinderService, IRCEventListener
{
	/*
	 * Sammeladresse fuer mehrere Servers in Deutschland. Beim konnektieren wird
	 * zufaellig einer ausgewaehlt
	 */
	static final private String SERVER = "irc.de.euirc.net";

	private IRCConnection connection;

	@Override
	public List<InetAddress> findServer(int gamers) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerServer(String host, int gamers, int ttl) throws Exception
	{
		// TODO Auto-generated method stub

	}

	private IRCConnection getConnection() throws IOException
	{
		Random rnd = new Random();
		String nick = Integer.toHexString(rnd.nextInt(Integer.MAX_VALUE));
		System.out.println("Opening connection...");
		IRCConnection con = new IRCConnection(SERVER, 6667, 6669, null, nick, nick, nick);
		con.setDaemon(true);
		con.setColors(false);
		con.setPong(true);
		con.connect();
		System.out.println("Connected");
		String chName = generateChannelName();
		System.out.println("Join channel " + chName);
		con.doJoin(generateChannelName());
		System.out.println("Setting channel private channel mode");
		con.doMode(chName, "+s");
		con.doMode(chName, "+p");
		con.doMode(chName, "+k " + this.getClass().getName().hashCode());
		return con;
	}

	@SuppressWarnings("deprecation")
	private String generateChannelName()
	{
		Date date = new Date();
		int val = date.getDate();
		val = (val << 8) ^ date.getDay();
		val = (val << 8) ^ date.getMonth();
		val = val ^ this.getClass().getName().hashCode();
		return Integer.toHexString(date.getDate() + date.getDay() + date.getMonth());
	}

	@Override
	public void onDisconnected()
	{
		System.out.println("Disconnected");
		connection.close();
		connection = null;
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(String msg)
	{
		System.err.println("Error: " + msg);
	}

	@Override
	public void onError(int num, String msg)
	{
		System.err.println("Error: " + msg);
	}

	@Override
	public void onInvite(String chan, IRCUser user, String passiveNick)
	{
		System.out.println("IRCServerFinderService.onInvite()");
		// TODO Auto-generated method stub

	}

	@Override
	public void onJoin(String chan, IRCUser user)
	{
		System.out.println("IRCServerFinderService.onJoin()");
		// TODO Auto-generated method stub

	}

	@Override
	public void onKick(String chan, IRCUser user, String passiveNick, String msg)
	{
		System.out.println("IRCServerFinderService.onKick()");
		// TODO Auto-generated method stub

	}

	@Override
	public void onMode(String chan, IRCUser user, IRCModeParser modeParser)
	{
		System.out.println("IRCServerFinderService.onMode()");
		// TODO Auto-generated method stub

	}

	@Override
	public void onMode(IRCUser user, String passiveNick, String mode)
	{
		System.out.println("IRCServerFinderService.onMode()");
		// TODO Auto-generated method stub

	}

	@Override
	public void onNick(IRCUser user, String newNick)
	{
		System.out.println("IRCServerFinderService.onNick()");
		// TODO Auto-generated method stub

	}

	@Override
	public void onNotice(String target, IRCUser user, String msg)
	{
		System.out.println("IRCServerFinderService.onNotice()");
		// TODO Auto-generated method stub

	}

	@Override
	public void onPart(String chan, IRCUser user, String msg)
	{
		System.out.println("IRCServerFinderService.onPart()");
		// TODO Auto-generated method stub

	}

	@Override
	public void onPing(String ping)
	{
		System.out.println("IRCServerFinderService.onPing()");
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrivmsg(String target, IRCUser user, String msg)
	{
		System.out.println("IRCServerFinderService.onPrivmsg()");
		// TODO Auto-generated method stub

	}

	@Override
	public void onQuit(IRCUser user, String msg)
	{
		System.out.println("IRCServerFinderService.onQuit()");
		// TODO Auto-generated method stub

	}

	@Override
	public void onRegistered()
	{
		System.out.println("IRCServerFinderService.onRegistered()");
		// TODO Auto-generated method stub

	}

	@Override
	public void onReply(int num, String value, String msg)
	{
		System.out.println("IRCServerFinderService.onReply()");
		// TODO Auto-generated method stub

	}

	@Override
	public void onTopic(String chan, IRCUser user, String topic)
	{
		System.out.println("IRCServerFinderService.onTopic()");
	}

	@Override
	public void unknown(String prefix, String command, String middle, String trailing)
	{
		System.out.println("IRCServerFinderService.unknown()");
		// TODO Auto-generated method stub

	}

	@Override
	public void close()
	{
		if (connection != null)
		{
			connection.doQuit();
			connection.close();
			connection = null;
		}
	}

	@Override
	public void open() throws Exception
	{
		connection = getConnection();
	}

}