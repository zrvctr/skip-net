package com.burlov.swing;

import java.awt.*;

public class SwingHelper
{

	public SwingHelper()
	{
	}

	/**
	 * Zentriert ein Window relativ zu dem Parent-Window
	 * 
	 * @param parent
	 *            Parent-Window, wenn null, dann wird relativ zu dem Bildschirm
	 *            zentriert
	 * @param child
	 *            Window das zentrirt werden soll.
	 */
	static public void centreWindow(Window parent, Window child)
	{
		if(child == null) return;
		Point parentLocation = null;
		Dimension parentSize = null;
		if(parent == null)
		{
			parentLocation = new Point(0, 0);
			parentSize = child.getToolkit().getScreenSize();
		} else
		{
			parentLocation = parent.getLocationOnScreen();
			parentSize = parent.getSize();
		}
		Dimension childSize = child.getSize();
		child
				.setLocation((int) (parentLocation.getX() + parentSize.getWidth() / 2 - childSize
						.getWidth() / 2),
						(int) (parentLocation.getY() + parentSize.getHeight() / 2 - childSize
								.getHeight() / 2));
	}
}