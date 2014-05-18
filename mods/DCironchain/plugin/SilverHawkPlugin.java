package mods.DCironchain.plugin;

import mods.defeatedcrow.common.SilverHawkCore;
import net.minecraft.block.Block;

public class SilverHawkPlugin {
	
	public static Block SHLantern;
	
	public void load()
	{
		this.SHLantern = SilverHawkCore.CDLantern;
	}

}
