/*
 * Copyright © 2001,2008 by Paul Burlov. All Rights Reserved.
 * Created Sep 12, 2008
 */
package skipbo;

import skipbo.irc.IRCServerFinderService;

public class ServerFinderService
{
	static private IServerFinderService instance = new IRCServerFinderService();

	static public IServerFinderService getInstance()
	{
		return instance;
	}

}
