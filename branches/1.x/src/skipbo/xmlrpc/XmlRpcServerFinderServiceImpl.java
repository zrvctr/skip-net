/*
 * Copyright © 2001,2009 by Paul Burlov. All Rights Reserved.
 * Created 18.11.2009
 */
package skipbo.xmlrpc;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.util.ClientFactory;

import skipbo.IServerFinderService;

public class XmlRpcServerFinderServiceImpl implements IServerFinderService
{
	static final private String SERVER_HOST = "skipbohive.appspot.com";
	IServerFinderService finder;

	public XmlRpcServerFinderServiceImpl() throws MalformedURLException
	{
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		// config.setServerURL(new URL("http://127.0.0.1:8080/xmlrpc"));
		config.setServerURL(new URL("http://" + SERVER_HOST + "/xmlrpc"));
		config.setEnabledForExceptions(true);
		config.setEnabledForExtensions(true);
		XmlRpcClient client = new XmlRpcClient();
		client.setConfig(config);
		ClientFactory factory = new ClientFactory(client);
		finder = (IServerFinderService) factory.newInstance(IServerFinderService.class);
	}

	@Override
	public List<String> findServer(int gamers) throws Exception
	{
		return finder.findServer(gamers);
	}

	@Override
	public void registerServer(int gamers, int ttl) throws Exception
	{
		finder.registerServer(gamers, ttl);
	}

	@Override
	public void unregisterServer() throws Exception
	{
		finder.unregisterServer();
	}

}
