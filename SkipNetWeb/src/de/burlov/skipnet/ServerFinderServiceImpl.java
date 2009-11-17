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
import javax.jdo.Transaction;

public class ServerFinderServiceImpl implements IServerFinderService
{
	PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public ServerFinderServiceImpl()
	{
		super();

	}

	public List<String> findServer(int gamers) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction trans = pm.currentTransaction();
		List<String> ret = new ArrayList<String>();
		try
		{
			trans.begin();
			Collection<ServerInfo> res = (Collection<ServerInfo>) pm.newQuery(ServerInfo.class, "gamers==" + gamers).execute();
			for (ServerInfo server : res)
			{
				if (System.currentTimeMillis() > server.validUntil)
				{
					pm.deletePersistent(server);
					continue;
				}
				ret.add(server.host);
			}
			trans.commit();
		} finally
		{
			if (trans.isActive())
			{
				trans.rollback();
			}
			pm.close();
		}
		return ret;
	}

	public void registerServer(String host, int gamers, long ttl) throws Exception
	{
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
}
