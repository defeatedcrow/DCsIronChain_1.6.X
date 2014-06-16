package mods.DCironchain.common.material;

import java.util.Random;

import mods.DCironchain.common.DCsIronChain;
import mods.DCironchain.entity.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRHopperBlack extends BlockRHopper{
	
	private final Random rand = new Random();
	@SideOnly(Side.CLIENT)
	private Icon baseIcon;
	@SideOnly(Side.CLIENT)
	private Icon sideIcon;
	@SideOnly(Side.CLIENT)
	private Icon insideIcon;
	
	public boolean isReverse = false;

	public BlockRHopperBlack(int blockid, boolean flag) {
		super(blockid);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		this.isReverse = flag;
	}
	
	@Override
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
        int j1 = Facing.oppositeSide[par5];

        if (j1 == 0 && this.isReverse)
        {
            j1 = 1;
        }
        
        if (j1 == 1 && !this.isReverse)
        {
        	j1 = 0;
        }

        return j1;
    }
	
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
		boolean flag = false;
		if (this.isReverse)
		{
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
		else
		{
			TileEntityHopperBlack tile = (TileEntityHopperBlack)par1World.getBlockTileEntity(par2, par3, par4);
			
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
			
		
    }
	
	@Override
	public TileEntity createNewTileEntity(World par1World)
    {
        return this.isReverse ? new TileEntityRHopperBlack() : new TileEntityHopperBlack();
    }
	
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
    {
		return par1 == 0 ? this.insideIcon : (par1 == 1 ? this.baseIcon : this.sideIcon);
    }
	
	public int getRenderType()
    {
        return DCsIronChain.modelHopper2;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister par1IconRegister)
    {
		this.baseIcon = par1IconRegister.registerIcon("crowsdefeat:rhopper_black_top");
        this.sideIcon = par1IconRegister.registerIcon("crowsdefeat:rhopper_black_side");
        this.insideIcon = par1IconRegister.registerIcon("hopper_inside");
    }
}
