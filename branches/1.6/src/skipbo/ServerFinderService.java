/*
 * Copyright © 2001,2008 by Paul Burlov. All Rights Reserved.
 * Created Sep 12, 2008
 */
package skipbo;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

public class ServerFinderService
{
	/*
	 * Sammeladresse fuer mehrere Servers in Deutschland. Beim Konnektieren wird
	 * zufaellig einer ausgewaehlt
	 */
	static final private String SERVER = "irc.de.euirc.net";

	static private ServerFinderService instance = new ServerFinderService();

	static public ServerFinderService getInstance()
	{
		return instance;
	}

	protected ServerFinderService()
	{
	}

	public void registerServer(String host, int gamers, int ttl) throws Exception
	{
		System.out.println("register server. Gamers: " + gamers);
		// TODO
	}

	public List<InetAddress> findServer(int gamers) throws Exception
	{
		LinkedList<InetAddress> ret = new LinkedList<InetAddress>();
		// TODO
		return ret;
	}
}
