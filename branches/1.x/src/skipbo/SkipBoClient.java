package skipbo;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import skipboserver.SkipBoServer;

/**
 * �berschrift: Beschreibung: Copyright: Copyright (c) 2001 Organisation:
 * 
 * @author Paul Burlov
 * @version 1.0
 */
public class SkipBoClient extends Thread
{

	private Socket sock;
	private InputStream sockIn;
	private OutputStream sockOut;
	private ObjectOutputStream objOut;
	private ObjectOutputStream objOut_file;
	private ObjectInputStream objIn_file;
	private ObjectInputStream objIn;
	private SkipBo owner;

	public SkipBoClient(SkipBo owner)
	{
		this.owner = owner;
	}

	public void stoppen()
	{
		try
		{
			if (objIn != null) objIn.close();
		} catch (IOException ioe)
		{
		}
		try
		{
			if (objOut != null) objOut.close();
		} catch (IOException ioe)
		{
		}
	}

	public void connectToServer(String host) throws IOException
	{
		sock = new Socket(host, SkipBoServer.DEFAULT_PORT);
		sockIn = sock.getInputStream();
		sockOut = sock.getOutputStream();
		objOut = new ObjectOutputStream(sockOut);
		objIn = new ObjectInputStream(sockIn);
		start();
	}

	public void sendNachricht(NetzNachricht nn) throws IOException
	{
		objOut.writeObject(nn);
		objOut.flush();
		objOut.reset();
	}

	public void sendChatMessage(String message) throws IOException
	{
		objOut.writeObject(message);
		objOut.flush();
		objOut.reset();
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				Object obj = objIn.readObject();
				if (obj instanceof String)
				{
					owner.chatMessageReceived((String) obj);
					continue;
				} else if (obj instanceof NetzNachricht)
				{
					owner.nachrichtBearbeiten((NetzNachricht) obj);
				} else
				{
					throw new ClassCastException(Resource.getString("inkompartible_version"));
				}
			}
		} catch (Throwable ioe)
		{
			ioe.printStackTrace();
			// if(ioe instanceof EOFException) owner.clientTerminated(new
			// ClassCastException("Inkompartible Version."));
			owner.clientTerminated(ioe);
		}
	}
}