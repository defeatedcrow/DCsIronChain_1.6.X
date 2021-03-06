package mods.DCironchain.common;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import mods.DCironchain.entity.ContainerRHopper;
import mods.DCironchain.entity.*;
import mods.DCironchain.gui.GuiRHopper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


public class CommonProxy implements IGuiHandler{
	
	public int addArmor(String armor)
	{
		return 0;
	}
	
	public int getRenderID()
	{
		return -1;
	}
	
	public void registerRenderers(){}
	
	public void registerTile() {
		GameRegistry.registerTileEntity(TileEntityRHopper.class, "TileEntityRHopper");
		GameRegistry.registerTileEntity(TileEntityRHopperGold.class, "TileEntityRHopperGold");
		GameRegistry.registerTileEntity(TileEntityRHopperBlack.class, "TileEntityRHopperBlack");
		GameRegistry.registerTileEntity(TileEntityHopperBlack.class, "TileEntityHopperBlack");
		GameRegistry.registerTileEntity(TileEntityHopperGold.class, "TileEntityHopperGold");
		GameRegistry.registerTileEntity(TileEntityFloodLight.class, "TileEntityFloodLight");
	}

	public World getClientWorld() {
		
		return null;
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		TileEntity tileentity = world.getBlockTileEntity(x, y, z);
		InventoryPlayer inventoryPlayer = player.inventory;
		if (tileentity != null && tileentity instanceof TileEntityRHopper && ID == DCsIronChain.guiIdRHopper) {
			return new ContainerRHopper(inventoryPlayer, (TileEntityRHopper) tileentity);
		}
		else
		{
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		TileEntity tileentity = world.getBlockTileEntity(x, y, z);
		InventoryPlayer inventoryPlayer = player.inventory;
		if (tileentity != null && tileentity instanceof TileEntityRHopper && ID == DCsIronChain.guiIdRHopper) {
			return new GuiRHopper(inventoryPlayer, (TileEntityRHopper) tileentity);
		}
		else
		{
			return null;
		}
	}

}
