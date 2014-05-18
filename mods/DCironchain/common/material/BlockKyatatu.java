package mods.DCironchain.common.material;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import static net.minecraftforge.common.ForgeDirection.UP;
import net.minecraft.block.Block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import mods.DCironchain.common.*;

public class BlockKyatatu extends Block{
	
	@SideOnly(Side.CLIENT)
	private Icon baseIcon;
	@SideOnly(Side.CLIENT)
	private Icon sideIcon;
	@SideOnly(Side.CLIENT)
	private Icon sideIcon2;
	@SideOnly(Side.CLIENT)
	private Icon sideIcon3;
	@SideOnly(Side.CLIENT)
	private Icon topIcon;
	
	public BlockKyatatu(int blockid)
	{
		super(blockid, Material.circuits);
		this.setHardness(0.5F);
		this.setResistance(2.0F);
		this.setStepSound(Block.soundMetalFootstep);
		this.setLightValue(0.0F);
	}
	
	public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
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
		int meta = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
		float f = 0.1875F;
		if (meta < 2)
		{
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
		else if (meta == 2) this.setBlockBounds(0.0F, 0.0F, 0.0F + f, 1.0F, 1.0F, 1.0F - f);
		else if (meta == 3) this.setBlockBounds(0.0F + f, 0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F);
		else if (meta == 4) this.setBlockBounds(0.0F, 0.0F, 0.0F + (f*2), 1.0F, 1.0F, 1.0F - (f*2));
		else if (meta == 5) this.setBlockBounds(0.0F + (f*2), 0.0F, 0.0F, 1.0F - (f*2), 1.0F, 1.0F);
		else this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
	
	
	
	@Override
	public int idDropped(int metadata, Random rand, int fortune)
	{
		int m = metadata;
		if (m < 2) return this.blockID;
		else return 0;
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
	
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
		par1World.setBlock(par2, par3 + 1, par4, this.blockID, 2, 2);
		par1World.setBlock(par2, par3 + 2, par4, this.blockID, 4, 2);
		return 0;
    }
	
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        ItemStack itemstack = par5EntityPlayer.inventory.getCurrentItem();

        if (itemstack == null)
        {
        	if (par1World.getBlockMetadata(par2, par3, par4) == 0)
        	{
        		par1World.setBlockMetadataWithNotify(par2, par3, par4, 1, 2);
        		par1World.setBlockMetadataWithNotify(par2, par3 + 1, par4, 3, 2);
        		par1World.setBlockMetadataWithNotify(par2, par3 + 2, par4, 5, 2);
        	}
        	else if (par1World.getBlockMetadata(par2, par3, par4) == 1)
        	{
        		par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 2);
        		par1World.setBlockMetadataWithNotify(par2, par3 + 1, par4, 2, 2);
        		par1World.setBlockMetadataWithNotify(par2, par3 + 2, par4, 4, 2);
        	}
        	par1World.playSoundAtEntity(par5EntityPlayer, "random.pop", 0.4F, 1.8F);
        	return true;
        }
        else
        {
        	return false;
        }
    }
	
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return par3 >= 254 ? false : par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && par1World.isAirBlock(par2, par3 + 1, par4) && par1World.isAirBlock(par2, par3 + 2, par4);
    }

	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
		ForgeDirection dir = UP;
		if (!par1World.isBlockSolidOnSide(par2, par3 - 1, par4, dir) && par1World.getBlockId(par2, par3 - 1, par4) != this.blockID)
        {
            if (par1World.getBlockMetadata(par2, par3, par4) < 1) this.dropBlockAsItem(par1World, par2, par3, par4, 0, 0);
            par1World.setBlockToAir(par2, par3, par4);
        }
    }
	
	@SideOnly(Side.CLIENT)
	public Icon getIcon (int par1, int par2)
	{
		if ((par2 & 1) == 1)
		{
			return par1 == 1 ? this.topIcon : (par1 > 3 ? this.baseIcon : (par2 < 2 ? this.sideIcon : (par2 < 4 ? this.sideIcon2 : this.sideIcon3)));
		}
		else
		{
			return par1 == 1 ? this.topIcon : (par1 > 3 ? (par2 < 2 ? this.sideIcon : (par2 < 4 ? this.sideIcon2 : this.sideIcon3)) : this.baseIcon);
		}
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.baseIcon = par1IconRegister.registerIcon("crowsdefeat:kyatatu_step");
		this.sideIcon = par1IconRegister.registerIcon("crowsdefeat:kyatatu_side");
		this.sideIcon2 = par1IconRegister.registerIcon("crowsdefeat:kyatatu_side2");
		this.sideIcon3 = par1IconRegister.registerIcon("crowsdefeat:kyatatu_side3");
		this.topIcon = par1IconRegister.registerIcon("crowsdefeat:kyatatu_top");
	}

}
