/*
 * Copyright © 2001,2008 by Paul Burlov. All Rights Reserved.
 * Created Sep 12, 2008
 */
package skipbo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MyIpResolver
{
	static public String resolveMyIp()
	{
		try
		{
			URL url = new URL("http://www.whatismyip.org/");
			InputStream in = url.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String ret = reader.readLine();
			reader.close();
			if (ret != null)
			{
				return ret.trim();
			}
			return ret;
		} catch (MalformedURLException e)
		{
			throw new RuntimeException(e);
		} catch (IOException e)
		{
		}
		return null;
	}
}
