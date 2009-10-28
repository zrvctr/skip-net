/*
 * Copyright © 2001,2008 by Paul Burlov. All Rights Reserved.
 * Created Sep 11, 2008
 */
package de.burlov.opendht.xmlrpc;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import de.burlov.opendht.GetResult;
import de.burlov.opendht.OpenDHTClient;

public class ClientImpl implements OpenDHTClient
{
	static final private int PORT = 5851;
	/*
	 * OASIS Adresse die automatisch an den naechst gelegenen Host vermittelt
	 */
	static final private String HOST = "opendht.nyuld.net";
	private XmlRpcClient client;
	private InetAddress gateway;

	public ClientImpl()
	{
		// ClientFactory factory = new ClientFactory(client);
		// dht = (IOpenDHT) factory.newInstance(getClass().getClassLoader(),
		// IOpenDHT.class, "");
	}

	private void init() throws Exception
	{
		if (client == null)
		{
			gateway = InetAddress.getByName(HOST);
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			try
			{
				config.setServerURL(new URL("http://" + gateway.getHostAddress() + ":" + PORT + "/RPC2"));
			} catch (MalformedURLException e)
			{
				throw new RuntimeException(e);
			}
			client = new XmlRpcClient();
			client.setConfig(config);
		}
	}

	@Override
	public GetResult get(byte[] key, int maxResults, byte[] placemark) throws Exception
	{
		init();
		Object[] params = new Object[4];
		params[0] = key;
		params[1] = Integer.valueOf(maxResults);
		params[2] = placemark;
		params[3] = getClass().getName();
		Object[] res = (Object[]) client.execute("get_details", params);
		// Object[] res = dht.get(key, maxResults, placemark,
		// getClass().getName());
		LinkedList<byte[]> values = new LinkedList<byte[]>();
		for (Object obj : (Object[]) res[0])
		{
			Object[] valRow = (Object[]) obj;
			Integer ttl = (Integer) valRow[1];
			if (ttl.intValue() < 0)
			{
				/*
				 * TTL ist abgelaufen
				 */
				continue;
			}
			values.add((byte[]) valRow[0]);
		}
		return new GetResult(values, (byte[]) res[1]);
	}

	@Override
	public int put(byte[] key, byte[] value, int ttlSecond) throws Exception
	{
		init();
		Object[] params = new Object[4];
		params[0] = key;
		params[1] = value;
		params[2] = Integer.valueOf(ttlSecond);
		params[3] = getClass().getName();
		Object ret = client.execute("put", params);
		return (Integer) ret;
		// return dht.put(key, value, ttlSecond, getClass().getName());
	}

}
