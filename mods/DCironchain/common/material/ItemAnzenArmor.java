package mods.DCironchain.common.material;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.DCironchain.common.DCsIronChain;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemAnzenArmor extends ItemArmor{
	
	private static final String[] textype = new String[] {"anzen_met","sagyougi","sagyougi","anzen_boots"};
	
	public ItemAnzenArmor (int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4) {
		super(par1, par2EnumArmorMaterial, par3, par4);
	}
	
	@Override
	public void onCreated(ItemStack par1Itemstack, World par2World, EntityPlayer par3EntityPlayer)
	{
		super.onCreated(par1Itemstack, par2World, par3EntityPlayer);
		if(par1Itemstack.itemID == DCsIronChain.anzenMet.itemID)
		{
			par1Itemstack.addEnchantment(Enchantment.blastProtection, 1);
		}
		if(par1Itemstack.itemID == DCsIronChain.anzenBoots.itemID)
		{
			par1Itemstack.addEnchantment(Enchantment.featherFalling, 1);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.itemIcon = par1IconRegister.registerIcon("crowsdefeat:" + this.textype[this.armorType]);
	}
	
	
	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, int layer) {
		
		return "crowsdefeat:textures/armor/sagyougi.png";
	}

}
