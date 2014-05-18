package mods.DCironchain.common.material;

import java.util.Random;

import mods.DCironchain.common.DCsIronChain;
import mods.DCironchain.entity.TileEntityRHopper;
import mods.DCironchain.entity.TileEntityRHopperBlack;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRHopperBlack extends BlockRHopper{
	
	private final Random rand = new Random();
	@SideOnly(Side.CLIENT)
	private Icon baseIcon;
	@SideOnly(Side.CLIENT)
	private Icon insideIcon;

	public BlockRHopperBlack(int blockid) {
		super(blockid);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
		boolean flag = false;
		TileEntityRHopperBlack tile = (TileEntityRHopperBlack)par1World.getBlockTileEntity(par2, par3, par4);
		if (tile != null && !par1World.isRemote)
		{
			if (par5EntityPlayer.isSneaking())
			{
				short mode = tile.getMode();
				short next = 1;
				switch (mode){
				case 1:
					next = 2;
					break;
				case 2:
					next = 3;
					break;
				case 3:
					next = 4;
					break;
				case 4:
					next = 5;
					break;
				case 5:
					next = 1;
					break;
				default:
					next = 1;
					break;
				}
				tile.setMode(next);
				par5EntityPlayer.addChatMessage("Absorption area : " + (next*2 + 1) + "x" + (next*2 + 1));
				flag = true;
			}
			else
			{
				par5EntityPlayer.openGui(DCsIronChain.instance, DCsIronChain.instance.guiIdRHopper, par1World, par2, par3, par4);
				flag = true;
			}
			
		}
		else
		{
			flag = true;
		}
		return flag;
    }
	
	@Override
	public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityRHopperBlack();
    }
	
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
    {
        return par1 == 0 ? this.baseIcon : this.insideIcon;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister par1IconRegister)
    {
        this.baseIcon = par1IconRegister.registerIcon("obsidian");
        this.insideIcon = par1IconRegister.registerIcon("hopper_top");
    }
}
