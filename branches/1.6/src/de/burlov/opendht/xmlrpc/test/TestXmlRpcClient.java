/*
 * Copyright © 2001,2008 by Paul Burlov. All Rights Reserved.
 * Created Sep 11, 2008
 */
package de.burlov.opendht.xmlrpc.test;

import java.util.Arrays;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import de.burlov.opendht.GetResult;
import de.burlov.opendht.xmlrpc.ClientImpl;

public class TestXmlRpcClient
{
	@Test
	public void test() throws Exception
	{
		/*
		 * Testdaten generieren
		 */
		Random rnd = new Random();
		int averageLengt = 300;
		byte[][] values = new byte[1][];
		for (int index = 0; index < values.length; index++)
		{
			byte[] val = new byte[rnd.nextInt(averageLengt) + 1];
			rnd.nextBytes(val);
			values[index] = val;
		}
		ClientImpl cl = new ClientImpl();
		byte[] key = new byte[20];
		rnd.nextBytes(key);
		for (byte[] val : values)
		{
			cl.put(key, val, 100);
		}

		GetResult res = cl.get(key, 100, new byte[0]);
		Assert.assertTrue(res.getValues().size() == values.length);
		int index = 0;
		for (byte[] val : res.getValues())
		{
			Assert.assertTrue(Arrays.equals(val, values[index++]));
		}
	}
}
