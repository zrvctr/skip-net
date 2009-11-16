/*
 * Copyright © 2001,2009 by Paul Burlov. All Rights Reserved.
 * Created 16.11.2009
 */
package skipbo;

import java.net.InetAddress;
import java.util.List;

public interface IServerFinderService
{

	public abstract void registerServer(String host, int gamers, int ttl) throws Exception;

	public abstract List<InetAddress> findServer(int gamers) throws Exception;

	public void open() throws Exception;

	public void close();

}