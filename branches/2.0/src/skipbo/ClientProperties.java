package skipbo;

/**
 * 
 * @author Paul Burlov
 * @version 1.0
 */
public class ClientProperties
{

	private String addresse;
	private String name;
	private int port;

	public ClientProperties(String name, String addresse, int port)
	{
		this.addresse = addresse;
		this.name = name;
		this.port = port;
	}

	/*
	 * public ClientProperties(){ this(" "," ",0);
	 */
	public String getAddresse()
	{
		return addresse;
	}

	public String getName()
	{
		return name;
	}

	public int getPort()
	{
		return port;
	}
}
