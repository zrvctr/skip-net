package skipbo;

import java.io.IOException;
import java.util.PropertyResourceBundle;

/**
 * Klasse zur Sprachenunabhaengiger gestaltung von SkipBo
 */
public class Resource
{

	private PropertyResourceBundle bundle;
	static private Resource instance;

	protected Resource() throws IOException
	{
		bundle = new PropertyResourceBundle(getClass().getResourceAsStream("skipbo.res"));
	}

	static public String getString(String key)
	{
		try
		{
			if (instance == null) instance = new Resource();
			return instance.bundle.getString(key);
		} catch (IOException e)
		{
			throw new RuntimeException("Kann die Resource Klasse nicht instanziieren", e);
		}
	}
}