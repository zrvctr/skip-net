/*
 * Copyright © 2001,2008 by Paul Burlov. All Rights Reserved.
 * Created Sep 12, 2008
 */
package skipbo;

import java.net.MalformedURLException;

import skipbo.xmlrpc.XmlRpcServerFinderServiceImpl;

public class ServerFinderService
{
	static private IServerFinderService instance;

	static public IServerFinderService getInstance()
	{
		if (instance == null)
		{
			try
			{
				instance = new XmlRpcServerFinderServiceImpl();
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
		}
		return instance;
	}

}
