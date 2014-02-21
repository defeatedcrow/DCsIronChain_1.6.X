package mods.DCironchain.entity;

import buildcraft.api.inventory.ISpecialInventory;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeConnection.ConnectOverride;
import buildcraft.api.transport.IPipeTile.PipeType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.Hopper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class InventoryRHopper implements Hopper{
	
	public ItemStack[] hopperItemStacks = new ItemStack[5];
	
	private TileEntityRHopper RHopper;
	
	public InventoryRHopper(TileEntityRHopper RHopper2)
	{
		this.RHopper = RHopper2;
	}
	
	public TileEntityRHopper RHopper()
	{
		return this.RHopper;
	}

	@Override
	public int getSizeInventory() {
		
		return this.hopperItemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		
		return this.hopperItemStacks[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (this.hopperItemStacks[i] != null)
		{
			ItemStack itemstack;
 
			if (this.hopperItemStacks[i].stackSize <= j)
			{
				itemstack = this.hopperItemStacks[i];
				this.hopperItemStacks[i] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.hopperItemStacks[i].splitStack(j);
 
				if (this.hopperItemStacks[i].stackSize == 0)
				{
					this.hopperItemStacks[i] = null;
				}
 
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (this.hopperItemStacks[i] != null)
		{
			ItemStack itemstack = this.hopperItemStacks[i];
			this.hopperItemStacks[i] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.hopperItemStacks[i] = itemstack;
		 
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
		{
			itemstack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName() {
		
		return "Upward Hopper";
	}

	@Override
	public boolean isInvNameLocalized() {
		
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		
		return 64;
	}

	@Override
	public void onInventoryChanged() {
		this.RHopper.onInventoryChanged();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		
		return this.RHopper.worldObj.getBlockTileEntity(this.RHopper.xCoord, this.RHopper.yCoord, this.RHopper.zCoord) != this.RHopper ? false : entityplayer.getDistanceSq((double) this.RHopper.xCoord + 0.5D, (double) this.RHopper.yCoord + 0.5D, (double) this.RHopper.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openChest() {
		
	}

	@Override
	public void closeChest() {
		
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		
		return true;
	}

	/**
     * Gets the world X position for this hopper entity.
     */
    public double getXPos()
    {
        return (double)this.RHopper.xCoord;
    }

    /**
     * Gets the world Y position for this hopper entity.
     */
    public double getYPos()
    {
        return (double)this.RHopper.yCoord;
    }

    /**
     * Gets the world Z position for this hopper entity.
     */
    public double getZPos()
    {
        return (double)this.RHopper.zCoord;
    }

	@Override
	public World getWorldObj() {
		
		return this.RHopper.worldObj;
	}
	
	//バグるのでコメントアウト中
	/*
	public ItemStack[] extractItem(boolean doRemove, ForgeDirection from, int maxItemCount)
	  {
	    Hopper inv = this;
	    ItemStack extract = null;

	    for (int i = 0; i < 5; i++) {
	      if (inv.getStackInSlot(i) != null)
	      {
	        ItemStack stack = inv.getStackInSlot(i);

	        if (doRemove) {
	          extract = inv.decrStackSize(i, 1); break;
	        }
	        extract = stack.copy();
	        extract.stackSize = 1;

	        break;
	      }
	    }
	    if (extract != null) {
	      return new ItemStack[] { extract };
	    }
	    return null;
	  }

	@Override
	public int addItem(ItemStack stack, boolean doAdd, ForgeDirection from) {
		
		int l = this.getSizeInventory();
		int removeItemstack = 0;
		for (int i = 0; l < l && stack != null && stack.stackSize > 0; ++l)
        {
            ItemStack current = this.getStackInSlot(i);
            if (this.isItemValidForSlot(i, stack))
            {
            	boolean flag = false;

                if (current == null)
                {
                    this.setInventorySlotContents(i, stack);
                    stack = null;
                    flag = true;
                }
                else if (areItemStacksEqualItem(current, stack))
                {
                    int k = stack.getMaxStackSize() - current.stackSize;
                    int j = Math.min(stack.stackSize, k);
                    stack.stackSize -= j;
                    current.stackSize += j;
                    removeItemstack = j;
                    flag = j > 0;
                }
                
                if (flag)
                {
                    this.onInventoryChanged();
                }
            }
        }
		
		if (stack != null && stack.stackSize == 0)
        {
            stack = null;
            return 0;
        }
		else
		{
			return removeItemstack;
		}
	}
	
	private static boolean areItemStacksEqualItem(ItemStack par0ItemStack, ItemStack par1ItemStack)
    {
        return par0ItemStack.itemID != par1ItemStack.itemID ? false : (par0ItemStack.getItemDamage() != par1ItemStack.getItemDamage() ? false : (par0ItemStack.stackSize > par0ItemStack.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(par0ItemStack, par1ItemStack)));
    }
	
	@Override
	public ConnectOverride overridePipeConnection(PipeType type,
			ForgeDirection with) {
		if (type == PipeType.ITEM) return ConnectOverride.CONNECT;
		else return ConnectOverride.DEFAULT;
	}
	*/

}
