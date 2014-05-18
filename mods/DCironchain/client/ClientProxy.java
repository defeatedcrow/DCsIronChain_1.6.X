package mods.DCironchain.client;

import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.DCironchain.common.CommonProxy;
import mods.DCironchain.entity.TileEntityFloodLight;
import mods.DCironchain.entity.TileEntityRHopper;
import mods.DCironchain.entity.TileEntityRHopperBlack;
import mods.DCironchain.entity.TileEntityRHopperGold;


@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public World getClientWorld()
	{
		return FMLClientHandler.instance().getClient().theWorld;
	}
	
	@Override
	public int addArmor(String armor)
	{
		return RenderingRegistry.addNewArmourRendererPrefix(armor);
	}
	
	public int getRenderID()
	{
		return RenderingRegistry.getNextAvailableRenderId();
	}
	
	@Override
	public void registerRenderers()
	{
		RenderingRegistry.registerBlockHandler(new RenderFloodLight());
		RenderingRegistry.registerBlockHandler(new RenderRHopper());
	}
	
	public void registerTile() {
		GameRegistry.registerTileEntity(TileEntityRHopper.class, "TileEntityRHopper");
		GameRegistry.registerTileEntity(TileEntityRHopperGold.class, "TileEntityRHopperGold");
		GameRegistry.registerTileEntity(TileEntityRHopperBlack.class, "TileEntityRHopperBlack");
		ClientRegistry.registerTileEntity(TileEntityFloodLight.class, "TileEntityFloodLight", new TileRenderFloodLight());
	}
}
