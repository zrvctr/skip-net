/*
 * Copyright © 2001,2009 by Paul Burlov. All Rights Reserved.
 * Created 18.11.2009
 */
package de.burlov.skipnet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlrpc.webserver.XmlRpcServlet;

public class CustomXmlRpcServlet extends XmlRpcServlet
{
	private static ThreadLocal clientIpAddress = new ThreadLocal();

	public static String getClientIpAddress()
	{
		return (String) clientIpAddress.get();
	}

	@Override
	public void doPost(HttpServletRequest pRequest, HttpServletResponse pResponse) throws IOException, ServletException
	{
		clientIpAddress.set(pRequest.getRemoteAddr());
		super.doPost(pRequest, pResponse);
	}

}
