/*
 * Copyright © 2001,2008 by Paul Burlov. All Rights Reserved.
 * Created Sep 11, 2008
 */
package de.burlov.opendht;

import java.util.List;

public class GetResult
{
	final private List<byte[]> values;
	final private byte[] placemark;

	public GetResult(List<byte[]> values, byte[] placemark)
	{
		super();
		this.values = values;
		this.placemark = placemark;
	}

	public List<byte[]> getValues()
	{
		return values;
	}

	public byte[] getPlacemark()
	{
		return placemark;
	}

}
