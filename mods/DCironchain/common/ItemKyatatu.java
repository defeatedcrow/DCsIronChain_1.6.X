package mods.DCironchain.common;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemKyatatu extends ItemBlock{
	
	
	public ItemKyatatu(int itemId)
	{
		super(itemId);
		this.setMaxStackSize(1);
		
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister){
	this.itemIcon = par1IconRegister.registerIcon("crowsdefeat:itemKyatatu");
	}

}
