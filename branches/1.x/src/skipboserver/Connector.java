package skipboserver;

import skipbo.*;
import java.net.*;
import java.io.*;

public class Connector extends Thread
{

	private SkipBoServer owner;
	private ServerSocket serverSocket;

	public Connector(SkipBoServer owner, int port) throws IOException
	{
		this.owner = owner;
		serverSocket = new ServerSocket(port);
		start();
	}

	/**
	 * @ Hostadresse
	 */
	public String getHostAdresse()
	{
		return serverSocket.getInetAddress().getHostAddress();
	}

	public void stopConnector()
	{
		try
		{
			serverSocket.close();
		} catch(Exception e)
		{
		}
		;
	}

	public void run()
	{
		try
		{
			while(true)
			{
				Socket sock = serverSocket.accept();
				new ServerCommunicator(owner, sock);
			}
		} catch(IOException e)
		{
			e.printStackTrace();
			owner.stopServer(Resource.getString("err80"));
		}
	}
}