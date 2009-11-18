/*
 * Copyright © 2001,2009 by Paul Burlov. All Rights Reserved.
 * Created 16.11.2009
 */
package de.burlov.skipnet;

import java.util.List;

public interface IServerFinderService
{

	public abstract void registerServer(int gamers, int ttl) throws Exception;

	public abstract void unregisterServer() throws Exception;

	public abstract List<String> findServer(int gamers) throws Exception;
}