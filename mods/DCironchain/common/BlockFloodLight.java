package mods.DCironchain.common;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.src.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import mods.DCironchain.client.RenderFloodLight;
import mods.DCironchain.common.*;
import mods.DCironchain.entity.TileEntityFloodLight;

public class BlockFloodLight extends BlockContainer{
	
	@SideOnly(Side.CLIENT)
	private Icon baseIcon;
	@SideOnly(Side.CLIENT)
	private Icon glowIcon;
	
	private final int[] sideX = {-3, 0, 3, 0};//west,north,east,south
	private final int[] sideZ = {0, -3, 0, 3};
	
	public BlockFloodLight (int blockid)
	{
		super(blockid, Material.glass);
		this.setHardness(0.5F);
		this.setResistance(2.0F);
		this.setStepSound(Block.soundStoneFootstep);
		this.setLightValue(1.0F);
		this.setTickRandomly(true);
	}
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
		this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }
	
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
		this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
    }
	
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
	
	@Override
	public int getRenderType()
	{
		return DCsIronChain.modelFLight;
	}
	
	@Override
	public boolean isOpaqueCube()
    {
        return false;
    }
	
	@Override
	public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return false;
    }
	
	@Override
	public int idDropped(int metadata, Random rand, int fortune)
	{
		return this.blockID;
	}
	

	public int tickRate(World par1World)
    {
        return 5;
    }
	
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
		int h = this.setLight(par1World, par2, par3, par4, 0);
		if (h > 0 && h < 20)
    	{
    		par1World.setBlock((par2 + this.sideX[0]), (par3 - h), (par4 + sideZ[0]), DCsIronChain.DCLightPart.blockID, 0, 2);
    	}
		return 0;
    }
	
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        ItemStack itemstack = par5EntityPlayer.inventory.getCurrentItem();
        int meta = par1World.getBlockMetadata(par2, par3, par4);

        if (itemstack == null)
        {
        	this.eraseLight(par1World, par2, par3, par4, meta);
        	par1World.playSoundAtEntity(par5EntityPlayer, "random.pop", 0.4F, 1.8F); //for debug
        	if (meta != 3)
        	{
        		par1World.setBlockMetadataWithNotify(par2, par3, par4, meta + 1, 3);
        		int h = this.setLight(par1World, par2, par3, par4, (meta + 1));
        		if (h > 0 && h < 20)
            	{
            		par1World.setBlock((par2 + this.sideX[meta + 1]), (par3 - h), (par4 + sideZ[meta + 1]), DCsIronChain.DCLightPart.blockID, meta + 1, 2);
            	}
        		
        	}
        	else
        	{
        		par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 3);
        		int h = this.setLight(par1World, par2, par3, par4, 0);
        		if (h > 0 && h < 20)
            	{
            		par1World.setBlock((par2 + this.sideX[0]), (par3 - h), (par4 + sideZ[0]), DCsIronChain.DCLightPart.blockID, 0, 2);
            	}
        	}
        	return true;
        }
        else
        {
        	return false;
        }
    }
	
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote)
        {
        	int meta = par1World.getBlockMetadata(par2, par3, par4);
        	int h = this.setLight(par1World, par2, par3, par4, meta);
        	if (h > 0 && h < 20)
        	{
        		par1World.setBlock((par2 + this.sideX[meta]), (par3 - h), (par4 + sideZ[meta]), DCsIronChain.DCLightPart.blockID, meta, 2);
        		//par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.5D, (double)par4 + 0.5D, "random.click", 0.3F, 0.5F); //for debug
        	}
        }   
    }
	
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
		int meta = par1World.getBlockMetadata(par2, par3, par4);
		int h = this.setLight(par1World, par2, par3, par4, meta);
		if (h > 0 && h < 20)
		{
			par1World.setBlock((par2 + this.sideX[meta]), (par3 - h), (par4 + sideZ[meta]), DCsIronChain.DCLightPart.blockID, meta, 2);
		}
    }
	
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
		this.eraseLight(par1World, par2, par3, par4, par6);
    }
	
	private int setLight (World world, int X, int Y, int Z, int meta)
	{
		int height = -1;
		boolean end = false;
		boolean success = false;
		
		for(int i = 0; i < 21; i++)
		{
			if (world.isAirBlock((X + this.sideX[meta]), (Y - i), (Z + sideZ[meta])) && (Y - i) > 0 && !end)
			{
				height++;
			}
			else
			{
				end = true;
			}
		}
		
		if (end && (height > 0) && (height < 20))
		{
			int ID = world.getBlockId((X + this.sideX[meta]), (Y- height - 1), (Z + sideZ[meta]));
			if (ID == DCsIronChain.DCLightPart.blockID || DCsIronChain.notUseLight)
			{
				height = -1;
			}
		}
		
		return height;
	}
	
	private void eraseLight (World world, int X, int Y, int Z, int meta)
	{
		for(int i = 0; i < 21; i++)
		{
			int ID = world.getBlockId((X + this.sideX[meta]), (Y - i), (Z + sideZ[meta]));
			if (ID == DCsIronChain.DCLightPart.blockID)
			{
				world.setBlockToAir((X + this.sideX[meta]), (Y - i), (Z + sideZ[meta]));
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public Icon getIcon (int par1, int par2)
	{
		return par1 == 0 ? this.baseIcon : this.glowIcon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.glowIcon = par1IconRegister.registerIcon("crowsdefeat:floodlight_glow");
		this.baseIcon = par1IconRegister.registerIcon("crowsdefeat:anchorbolt");
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityFloodLight();
	}

}
