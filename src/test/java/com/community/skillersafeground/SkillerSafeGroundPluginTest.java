package com.community.skillersafeground;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class SkillerSafeGroundPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(SkillerSafeGroundPlugin.class);
		RuneLite.main(args);
	}
}
