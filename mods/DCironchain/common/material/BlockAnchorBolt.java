package mods.DCironchain.common.material;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.DCironchain.common.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAnchorBolt extends Block{
	
	public BlockAnchorBolt (int blockid)
	{
		super(blockid, Material.ground);
		this.setHardness(0.5F);
		this.setResistance(2.0F);
		this.setStepSound(Block.soundMetalFootstep);
		this.setLightValue(0.0F);
	}
	
	public void setBlockBoundsForItemRender()
    {
        float f = 0.075F;
        float f2 = 0.625F;
        this.setBlockBounds(0.5F - f, 0.4F, 1.0F - f2, 0.5F + f, 0.4F + f, 1.0F);
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
        this.updateAnchorBounds(par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }
	
	public void updateAnchorBounds(int par1)
    {
        float f = 0.075F;
        float f2 = 0.625F;

        if (par1 == 2)
        {
            this.setBlockBounds(0.5F - f, 0.0F, 1.0F - f2, 0.5F + f, 0.125F, 1.0F);
        }

        if (par1 == 3)
        {
            this.setBlockBounds(0.5F - f, 0.0F, 0.0F, 0.5F + f, 0.125F, f2);
        }

        if (par1 == 4)
        {
            this.setBlockBounds(1.0F - f2, 0.0F, 0.5F - f, 1.0F, 0.125F, 0.5F + f);
        }

        if (par1 == 5)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.5F - f, f2, 0.125F, 0.5F + f);
        }
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
	
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST ) ||
               par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST ) ||
               par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH) ||
               par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH);
    }
	
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
        int j1 = par9;

        if ((j1 == 0 || par5 == 2) && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH))
        {
            j1 = 2;
        }

        if ((j1 == 0 || par5 == 3) && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH))
        {
            j1 = 3;
        }

        if ((j1 == 0 || par5 == 4) && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST))
        {
            j1 = 4;
        }

        if ((j1 == 0 || par5 == 5) && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST))
        {
            j1 = 5;
        }

        return j1;
    }
	
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        int i1 = par1World.getBlockMetadata(par2, par3, par4);
        boolean flag = false;

        if (i1 == 2 && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH))
        {
            flag = true;
        }

        if (i1 == 3 && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH))
        {
            flag = true;
        }

        if (i1 == 4 && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST))
        {
            flag = true;
        }

        if (i1 == 5 && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST))
        {
            flag = true;
        }

        if (!flag)
        {
            this.dropBlockAsItem(par1World, par2, par3, par4, i1, 0);
            par1World.setBlockToAir(par2, par3, par4);
        }

        super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
    }
	
	@Override
    public boolean isLadder(World world, int x, int y, int z, EntityLivingBase entity)
    {
        return true;
    }
	
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
		ItemStack itemstack = par5EntityPlayer.inventory.getCurrentItem();
        int l = 0;
        l = this.thisChainLength(par1World, par2, par3, par4);

        if (itemstack == null)
        {
        	if (l < 0)
        	{
        		return false;
        	}
        	else if (l < 65)
        	{
        		par5EntityPlayer.inventory.addItemStackToInventory(new ItemStack(DCsIronChain.ironChain, l));
        		par5EntityPlayer.inventory.addItemStackToInventory(new ItemStack(DCsIronChain.anchorBolt, 1));
        		if (l > 0)
        		{
        			par1World.playSoundAtEntity(par5EntityPlayer, "crowsdefeat:chain", 1.0F, 0.7F);
        		}
        		else
        		{
        			par1World.playSoundAtEntity(par5EntityPlayer, "random.pop", 0.4F, 1.9F);
        		}
        		
        		for(int i = 0; i < (l + 1); i++)
        		{
        			par1World.setBlockToAir(par2, (par3 - l + i), par4);
        		}
        	}
        	return false;
        }
        else if (this.canPlace(itemstack))
        {
            int placeID = itemstack.itemID;
            int placeMeta = itemstack.getItemDamage(); 
            
            if ((l < 65) && par1World.isAirBlock(par2, (par3 - l - 1), par4))
            {
            	par1World.setBlock(par2, (par3 - l - 1), par4, placeID, placeMeta, 3);
            	par1World.playSoundAtEntity(par5EntityPlayer, "crowsdefeat:chain", 1.0F, 0.7F);
            	if (!par5EntityPlayer.capabilities.isCreativeMode && --itemstack.stackSize <= 0)
                {
                    par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, (ItemStack)null);
                }
            	return true;
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
		
		for(int i = 1; i < 66; i++)
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
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon("crowsdefeat:anchorbolt");
		
	}
}
