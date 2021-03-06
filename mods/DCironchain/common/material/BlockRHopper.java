package mods.DCironchain.common.material;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import static net.minecraftforge.common.ForgeDirection.UP;
import static net.minecraftforge.common.ForgeDirection.DOWN;
import mods.DCironchain.client.RenderRHopper;
import mods.DCironchain.common.*;
import mods.DCironchain.entity.TileEntityRHopper;

public class BlockRHopper extends BlockContainer{
	
	
	private final Random rand = new Random();
	@SideOnly(Side.CLIENT)
	private Icon baseIcon;
	@SideOnly(Side.CLIENT)
	private Icon insideIcon;
	
	//DOWN, UP, NORTH, SOUTH, WEST, EAST
	private final int[] sideX = {0, 0, 0, 0, -1, 1};
	private final int[] sideZ = {0, 0, -1, 1, 0, 0};
	
	public BlockRHopper (int blockid)
	{
		super(blockid, Material.iron);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	//set side
	
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
        int j1 = Facing.oppositeSide[par5];

        if (j1 == 0)
        {
            j1 = 1;
        }

        return j1;
    }
	
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
        this.updateMetadata(par1World, par2, par3, par4);
    }
	
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        this.updateMetadata(par1World, par2, par3, par4);
    }
	
	private void updateMetadata(World par1World, int par2, int par3, int par4)
    {
        int l = par1World.getBlockMetadata(par2, par3, par4);
        int i1 = getDirectionFromMetadata(l);
        boolean flag = !par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
        boolean flag1 = getIsBlockNotPoweredFromMetadata(l);

        if (flag != flag1)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, i1 | (flag ? 0 : 8), 4);
        }
    }
	
	public static int getDirectionFromMetadata(int par0)
    {
        return par0 & 7;
    }
	
	public static boolean getIsBlockNotPoweredFromMetadata(int par0)
    {
        return (par0 & 8) != 8;
    }
	
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
    {
        int meta = world.getBlockMetadata(x, y, z);
        return (side == UP) ? true : false;
    }
	
	//tileentity
	public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityRHopper();
    }
	
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
        super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLivingBase, par6ItemStack);

        if (par6ItemStack.hasDisplayName())
        {
            TileEntityRHopper tileentityRhopper = getHopperTile(par1World, par2, par3, par4);
        }
    }
	
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityRHopper tileentityRhopper = getHopperTile(par1World, par2, par3, par4);

            if (tileentityRhopper != null)
            {
                par5EntityPlayer.openGui(DCsIronChain.instance, DCsIronChain.instance.guiIdRHopper, par1World, par2, par3, par4);
            }

            return true;
        }
    }
	
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntityRHopper tileentityRhopper = (TileEntityRHopper)par1World.getBlockTileEntity(par2, par3, par4);

        if (tileentityRhopper != null)
        {
            for (int j1 = 0; j1 < tileentityRhopper.getSizeInventory(); ++j1)
            {
                ItemStack itemstack = tileentityRhopper.getStackInSlot(j1);

                if (itemstack != null)
                {
                    float f = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0)
                    {
                        int k1 = this.rand.nextInt(21) + 10;

                        if (k1 > itemstack.stackSize)
                        {
                            k1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= k1;
                        EntityItem entityitem = new EntityItem(par1World, (double)((float)par2 + f), (double)((float)par3 + f1), (double)((float)par4 + f2), new ItemStack(itemstack.itemID, k1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
                        par1World.spawnEntityInWorld(entityitem);
                    }
                }
            }

            par1World.func_96440_m(par2, par3, par4, par5);
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
	
	//RS
	
	public boolean hasComparatorInputOverride()
    {
        return true;
    }
	
	public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
        return Container.calcRedstoneFromInventory(getHopperTile(par1World, par2, par3, par4));
    }
	
	public static TileEntityRHopper getHopperTile(IBlockAccess par0IBlockAccess, int par1, int par2, int par3)
    {
        return (TileEntityRHopper)par0IBlockAccess.getBlockTileEntity(par1, par2, par3);
    }
	
	//render & icon
	
	public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }
	
	public int getRenderType()
    {
        return DCsIronChain.modelRHopper;
    }
	
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return true;
    }
	
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
    {
        return par1 == 0 ? BlockHopper.getHopperIcon("hopper_outside") : BlockHopper.getHopperIcon("hopper_inside");
    }
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
    {
        this.baseIcon = par1IconRegister.registerIcon("hopper_outside");
        this.insideIcon = par1IconRegister.registerIcon("hopper_top");
    }
	
}
