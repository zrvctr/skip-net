/*
 * Copyright © 2001,2009 by Paul Burlov. All Rights Reserved.
 * Created 17.11.2009
 */
package de.burlov.skipnet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class ServerFinderServiceImpl implements IServerFinderService
{
	static final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public ServerFinderServiceImpl()
	{
		super();

	}

	public List<String> findServer(int gamers) throws Exception
	{
		System.out.println("ServerFinderServiceImpl.findServer(): " + gamers);
		PersistenceManager pm = pmf.getPersistenceManager();
		List<String> ret = new ArrayList<String>();
		try
		{
			Collection<ServerInfo> res = (Collection<ServerInfo>) pm.newQuery(ServerInfo.class, "gamers==" + gamers).execute();
			for (ServerInfo server : res)
			{
				if (System.currentTimeMillis() > server.validUntil)
				{
					/*
					 * TTL ist abgelaufen
					 */
					pm.deletePersistent(server);
					continue;
				}
				ret.add(server.host);
			}
		} finally
		{
			pm.close();
		}
		return ret;
	}

	public void registerServer(int gamers, int ttl) throws Exception
	{
		String host = CustomXmlRpcServlet.getClientIpAddress();
		System.out.println("ServerFinderServiceImpl.registerServer(): " + host + " " + gamers);
		ServerInfo info = new ServerInfo(host, gamers, System.currentTimeMillis() + ttl);
		PersistenceManager pm = pmf.getPersistenceManager();
		try
		{
			pm.makePersistent(info);
		} finally
		{
			pm.close();
		}
	}

	public void unregisterServer() throws Exception
	{
		String host = CustomXmlRpcServlet.getClientIpAddress();
		System.out.println("ServerFinderServiceImpl.unregisterServer(): " + host);
		PersistenceManager pm = pmf.getPersistenceManager();
		List<String> ret = new ArrayList<String>();
		try
		{
			Collection<ServerInfo> res = (Collection<ServerInfo>) pm.newQuery(ServerInfo.class, "host=='" + host + "'").execute();
			for (ServerInfo server : res)
			{
				pm.deletePersistent(server);
			}
		} finally
		{
			pm.close();
		}
	}
}
