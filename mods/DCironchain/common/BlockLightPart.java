package mods.DCironchain.common;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.src.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import mods.DCironchain.common.*;

public class BlockLightPart extends Block{
	
	@SideOnly(Side.CLIENT)
	private Icon baseIcon;
	@SideOnly(Side.CLIENT)
	private Icon glowIcon;
	
	private final int[] sideX = {3, 0, -3, 0};//east,south,west,north
	private final int[] sideZ = {0, 3, 0, -3};//it's point symmetric to the floodlight.
	
	public BlockLightPart (int blockid)
	{
		super(blockid, Material.circuits);
		this.setHardness(0.1F);
		this.setResistance(0.0F);
		this.setStepSound(Block.soundStoneFootstep);
		this.setLightValue(1.0F);
		this.setTickRandomly(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
	}
	
	@Override
	public int idDropped(int metadata, Random rand, int fortune)
	{
		return this.blockID;
	}
	
	public int quantityDropped(Random par1Random)
    {
        return 0;
    }
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }
	
	public boolean isOpaqueCube()
    {
        return false;
    }
	
	public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return false;
    }

	public int tickRate(World par1World)
    {
        return 10;
    }
	
	public boolean isBlockReplaceable(World world, int x, int y, int z)
    {
        return true;
    }
	
	public boolean isAirBlock(World world, int x, int y, int z)
    {
        return true;
    }
	
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
		if (!par1World.isRemote)
        {
			if (!this.canBlockStay(par1World, par2, par3, par4))
	        {
	        	par1World.setBlockToAir(par2, par3, par4);
	        }
        }
		
    }
	
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
		if (!this.canBlockStay(par1World, par2, par3, par4))
		{
			par1World.setBlockToAir(par2, par3, par4);
		}
		else if ((par3 - 2 > 0) && par1World.isAirBlock(par2, par3 - 1, par4) && par1World.getBlockId(par2, par3 - 2, par4) != this.blockID)
		{
			int l = par1World.getBlockMetadata(par2, par3, par4);
			par1World.setBlock(par2, par3 - 1, par4, this.blockID, l, 2);
			par1World.setBlockToAir(par2, par3, par4);
		}
    }
	
	public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
		int l = par1World.getBlockMetadata(par2, par3, par4);
		return this.checkFloodLight(par1World, par2, par3, par4, l);
    }
	
	private boolean checkFloodLight (World world, int X, int Y, int Z, int meta)
	{
		boolean end = false;
		
		for(int i = 0; i < 21; i++)
		{
			int ID = world.getBlockId((X + this.sideX[meta]), (Y + i), (Z + sideZ[meta]));
			
			if (ID == DCsIronChain.floodLight.blockID && ((Y + i) < 255) && !end)
			{
				int l = world.getBlockMetadata((X + this.sideX[meta]), (Y + i), (Z + sideZ[meta]));
				if (l == meta) end = true;
			}
		}
		
		return end;
	}
	
	@SideOnly(Side.CLIENT)
	public Icon getIcon (int par1, int par2)
	{
		return DCsIronChain.visibleLight ? this.baseIcon : this.glowIcon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.baseIcon = par1IconRegister.registerIcon("crowsdefeat:floodlight_light");
		this.glowIcon = par1IconRegister.registerIcon("crowsdefeat:floodlight_air");
		
	}

}
