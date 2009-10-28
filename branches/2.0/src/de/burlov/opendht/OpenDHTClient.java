/*
 * Copyright © 2001,2008 by Paul Burlov. All Rights Reserved.
 * Created Sep 11, 2008
 */
package de.burlov.opendht;

import java.io.IOException;

public interface OpenDHTClient
{
	/**
	 * Puts a value into DHT
	 * 
	 * @param key
	 * @param value
	 * @param ttlSeconds
	 *            values 'time to live'
	 * @return 0: success. 1: over capacity. 2: try again
	 * 
	 * @throws IOException
	 */
	public int put(byte[] key, byte[] value, int ttlSecond) throws Exception;

	/**
	 * Returns values for given key
	 * 
	 * @param key
	 * @param maxResults
	 * @param placemark
	 *            may be null
	 * @return
	 * @throws IOException
	 */
	public GetResult get(byte[] key, int maxResults, byte[] placemark) throws Exception;

}
