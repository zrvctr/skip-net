/*
 * Copyright © 2001,2009 by Paul Burlov. All Rights Reserved.
 * Created 17.11.2009
 */
package de.burlov.skipnet;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ServerInfo
{
	@PrimaryKey
	@Persistent
	String host;
	@Persistent
	int gamers;
	@Persistent
	long validUntil;

	public ServerInfo(String host, int gamers, long validUntil)
	{
		super();
		this.host = host;
		this.gamers = gamers;
		this.validUntil = validUntil;
	}
}
