/*
 * Copyright © 2001,2008 by Paul Burlov. All Rights Reserved.
 * Created Sep 12, 2008
 */
package skipbo;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import de.burlov.opendht.GetResult;
import de.burlov.opendht.OpenDHTClient;
import de.burlov.opendht.xmlrpc.ClientImpl;

public class OpenDhtCommunicator
{
	static final private String DHT_KEY = "SKIP-NET 2";
	static private OpenDhtCommunicator instance = new OpenDhtCommunicator();

	static public OpenDhtCommunicator getInstance()
	{
		return instance;
	}

	private OpenDHTClient client;

	protected OpenDhtCommunicator()
	{
		client = new ClientImpl();
	}

	public void registerServer(String host, int gamers, int ttl) throws Exception
	{
		System.out.println("register server. Gamers: " + gamers);
		InetAddress adr = InetAddress.getByName(host);
		/*
		 * Testen ob Adresse stimmit
		 */
		adr.getHostName();
		int res = client.put((DHT_KEY + Integer.toString(gamers)).getBytes(), host.getBytes(), ttl);
		if (res != 0)
		{
			System.out.println("Registering server failed");
		}
	}

	public List<InetAddress> findServer(int gamers) throws Exception
	{
		byte[] key = (DHT_KEY + Integer.toString(gamers)).getBytes();
		LinkedList<InetAddress> ret = new LinkedList<InetAddress>();
		GetResult result = null;
		do
		{
			result = client.get(key, 10, (result == null ? new byte[0] : result.getPlacemark()));
			for (byte[] val : result.getValues())
			{
				try
				{
					String host = new String(val);
					/*
					 * Host validieren
					 */
					InetAddress adr = InetAddress.getByName(host);
					ret.add(adr);
				} catch (Exception e)
				{
					// ignore
				}
			}
		} while (result.getPlacemark() != null && result.getPlacemark().length > 0);
		return ret;
	}
}
