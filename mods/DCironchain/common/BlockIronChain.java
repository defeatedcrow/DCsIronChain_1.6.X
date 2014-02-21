package mods.DCironchain.common;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import net.minecraft.block.Block;
import ic2.api.tile.IWrenchable;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import mods.DCironchain.common.*;

public class BlockIronChain extends Block{
	
	public BlockIronChain(int blockid)
	{
		super(blockid, Material.iron);
		this.setHardness(0.5F);
		this.setResistance(2.0F);
		this.setStepSound(Block.soundMetalFootstep);
		this.setLightValue(0.0F);
		float f = 0.375F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
	}
	
	public void setBlockBoundsForItemRender()
    {
		float f = 0.375F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
    }
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        float f = 0.375F;
        return AxisAlignedBB.getAABBPool().getAABB((double)((float)par2 + f), (double)par3, (double)((float)par4 + f), (double)(((float)(par2 + 1) - f)), (double)((float)par3 + 1), (double)((float)(par4 + 1) - f));
    }
	
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
		float f = 0.375F;
        return AxisAlignedBB.getAABBPool().getAABB((double)((float)par2 + f), (double)par3, (double)((float)par4 + f), (double)(((float)(par2 + 1) - f)), (double)((float)par3 + 1), (double)((float)(par4 + 1) - f));
    }
	
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
		float f = 0.375F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
    }
	
	@Override
	public int idDropped(int metadata, Random rand, int fortune)
	{
		return this.blockID;
	}
	
	@SideOnly(Side.CLIENT)
	public int getRenderType()
    {
        return 1;
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
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon("crowsdefeat:blockchain");
		
	}
	
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        ItemStack itemstack = par5EntityPlayer.inventory.getCurrentItem();
        
        int l = 0;
        l = this.thisChainLength(par1World, par2, par3, par4);

        if (itemstack == null)
        {
        	if (l < 1)
        	{
        		return false;
        	}
        	else if (l < 65)
        	{
        		par5EntityPlayer.inventory.addItemStackToInventory(new ItemStack(DCsIronChain.ironChain, l));
        		par1World.playSoundAtEntity(par5EntityPlayer, "crowsdefeat:chain", 1.0F, 0.7F);
        		for(int i = 1; i < (l + 1); i++)
        		{
        			par1World.setBlockToAir(par2, (par3 - l + i), par4);
        		}
        		return true;
        	}
        	else
        	{
        		return false;
        	}
        }
        else if (this.canPlace(itemstack))
        {
            int placeID = itemstack.itemID;
            
            if (l < 0)
            {
            	return false;
            }
            else if ((l < 65) && par1World.isAirBlock(par2, (par3 - l), par4))
            {
            	par1World.setBlock(par2, (par3 - l), par4, placeID);
            	par1World.playSoundAtEntity(par5EntityPlayer, "crowsdefeat:chain", 1.0F, 0.7F);
            	if (!par5EntityPlayer.capabilities.isCreativeMode && --itemstack.stackSize <= 0)
                {
                    par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, (ItemStack)null);
                }
            	return true;
            }
            else
            {
            	return true;
            }
            
            }
        else
        {
        	
        	if (l < 0)
            {
            	return false;
            }
            else if ((l < 65) && par1World.isAirBlock(par2, (par3 - l), par4))
            {
            	if (itemstack.itemID < 4095)
            	{
            		Block block = Block.blocksList[itemstack.itemID];
                	if(block != null && block instanceof Block && Block.opaqueCubeLookup[itemstack.itemID])
                	{
                		par1World.setBlock(par2, (par3 - l), par4, block.blockID, itemstack.getItemDamage(), 2);
                    	par1World.playSoundAtEntity(par5EntityPlayer, "crowsdefeat:chain", 1.0F, 0.7F);
                    	if (!par5EntityPlayer.capabilities.isCreativeMode && --itemstack.stackSize <= 0)
                        {
                            par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, (ItemStack)null);
                        }
                    	
                    	if (DCsIronChain.getLoadIC2 && !par1World.isAirBlock(par2, (par3 - l), par4) && block instanceof IWrenchable)
                    	{
                    		par1World.setBlockMetadataWithNotify(par2, (par3 - l), par4, 2, 2);
                    	}
                    	return true;
                	}
                	else
                	{
                		return true;
                	}
            	}
            	else
            	{
            		return false;
            	}
            }
            else
            {
            	return false;
            }
        	
        	
        }
    }
	
	private boolean canPlace(ItemStack itemstack) {
		
		if (itemstack.itemID == DCsIronChain.ironChain.blockID) {
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private int thisChainLength (World world, int x, int y, int z)
	{
		int l = 0;
		boolean end = false;
		
		for(int i = 0; i < 65; i++)
		{
			if ((world.getBlockId(x, (y - i), z) == DCsIronChain.ironChain.blockID) && ((y - i) > 0) && !end)
			{
				++l;
			}
			else
			{
				end = true;
			}
		}
		
		
		return l;
	}

	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
		ForgeDirection dir = DOWN;
		if ((par1World.getBlockId(par2, par3 + 1, par4) != DCsIronChain.ironChain.blockID) && (par1World.getBlockId(par2, par3 + 1, par4) != DCsIronChain.anchorBolt.blockID ) && !par1World.isBlockSolidOnSide(par2, par3 + 1, par4, dir))
        {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
        }
    }
	
	@Override
    public boolean isLadder(World world, int x, int y, int z, EntityLivingBase entity)
    {
        return true;
    }

}