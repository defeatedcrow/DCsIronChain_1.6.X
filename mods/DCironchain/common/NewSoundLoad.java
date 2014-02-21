package mods.DCironchain.common;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.sound.PlayStreamingEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class NewSoundLoad{
	
	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void SoundLoad(SoundLoadEvent event)
	{
		
		String dir1 = new String("crowsdefeat:chain.ogg");
		
		try
		{
			event.manager.soundPoolSounds.addSound(dir1);
		}
		catch (Exception e)
		{
			System.err.println("Failed to register one or more sounds.");
		}
		
	}
	

}
