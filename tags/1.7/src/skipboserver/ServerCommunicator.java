package skipboserver;

import java.net.*;
import java.io.*;
import skipbo.*;

public class ServerCommunicator extends Thread
{

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private SkipBoServer listener;
	private boolean connected = false;
	private String name = "";

	public ServerCommunicator(SkipBoServer listener, Socket socket)
	{
		this.socket = socket;
		this.listener = listener;
		start();
	}

	public void unregisterNachritenListener()
	{
		listener = null;
	}

	public void setCommunicatorName(String name)
	{
		this.name = name;
	}

	public String getCommunicatorName()
	{
		return name;
	}

	public void stopCommunicator()
	{
		try
		{
			connected = false;
			socket.close();
		} catch(IOException e)
		{
		}
	}

	public void sendNachricht(NetzNachricht msg) throws IOException
	{
		out.writeObject(msg);
		out.flush();
		out.reset();
	}

	public void sendChatMessage(String message) throws IOException
	{
		out.writeObject(message);
		out.flush();
		out.reset();
	}

	public void run()
	{
		try
		{
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			connected = true;
			while(true)
			{
				Object obj = in.readObject();
				if(obj instanceof String)
				{
					listener.chatMessageReceived(this, (String) obj);
					continue;
				} else if(obj instanceof NetzNachricht)
				{
					listener.nachrichtenBearbeiten(this, (NetzNachricht) obj);
				} else
				{
					throw new ClassCastException(Resource.getString("err90"));
				}
			}
		} catch(Throwable e)
		{
			e.printStackTrace();
			if(connected && listener != null) listener.communicatorTerminated(this);
			stopCommunicator();
		}
	}

	public boolean isConnected()
	{
		return connected;
	}
}