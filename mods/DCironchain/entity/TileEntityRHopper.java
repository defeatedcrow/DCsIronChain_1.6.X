package mods.DCironchain.entity;

import java.util.ArrayList;
import java.util.List;

import mods.DCironchain.Load.BCLoadHandler;
import mods.DCironchain.common.BlockRHopper;
import mods.DCironchain.common.DCsIronChain;
import mods.DCironchain.common.DCsLog;
import mods.DCironchain.common.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.Hopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.inventory.ISpecialInventory;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeConnection.ConnectOverride;
import buildcraft.api.transport.IPipeTile.PipeType;

public class TileEntityRHopper extends TileEntity implements Hopper, IPipeConnection, ISpecialInventory
{

    private int transferCooldown = -1;
    
    public ItemStack[] hopperItemStacks = new ItemStack[5];

    //NBT
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
        this.hopperItemStacks = new ItemStack[this.getSizeInventory()];

        this.transferCooldown = par1NBTTagCompound.getInteger("TransferCooldown");

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.hopperItemStacks.length)
            {
                this.hopperItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.hopperItemStacks.length; ++i)
        {
            if (this.hopperItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.hopperItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);
        par1NBTTagCompound.setInteger("TransferCooldown", this.transferCooldown);
    }

    //packet
    @Override
    public Packet getDescriptionPacket()
    {
            return PacketHandler.getPacket(this);
    }

    //read
    public void setTransferCooldown(int par1)
    {
        this.transferCooldown = par1;
    }

    public boolean isCoolingDown()
    {
        return this.transferCooldown > 0;
    }
    
    public void setItems(ItemStack[] _items)
    {
            if (_items != null)
            {
                    for (int i = 0; i < this.getSizeInventory(); ++i)
                    {
                            if (_items[i] != null)
                            {
                                    this.hopperItemStacks[i] = _items[i].copy();
                            }
                            else
                            {
                                    this.hopperItemStacks[i] = null;
                            }
                    }
            }
    }

    //write
	public ItemStack[] getItems() {
		
		return this.hopperItemStacks;
	}

	public int getCoolTime() {
		
		return this.transferCooldown;
	}
    
    
    //count cooldown
    public void updateEntity()
    {
        if (this.worldObj != null && !this.worldObj.isRemote)
        {
            --this.transferCooldown;

            if (!this.isCoolingDown())
            {
                this.setTransferCooldown(0);
                this.updateHopper();
            }
        }
    }

    public boolean updateHopper()
    {
        if (this.worldObj != null && !this.worldObj.isRemote)
        {
            if (!this.isCoolingDown() && BlockRHopper.getIsBlockNotPoweredFromMetadata(this.getBlockMetadata()))
            {
                boolean flag = this.insertItemToInventory();
                flag = suckItemsIntoHopper(this) || flag;

                if (flag)
                {
                    this.setTransferCooldown(4);
                    this.onInventoryChanged();
                    return true;
                }
            }

            return false;
        }
        else
        {
            return false;
        }
    }

    //for hopper contents
    //RHopperから送り出す
    private boolean insertItemToInventory()
    {
        boolean tryInsert = false;
        
        //BCパイプへの挿入
    	if (DCsIronChain.getLoadBC)
        {
        	//接続しているパイプのリスト（方向）
            ForgeDirection from = ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite();//メタデータを得る
            if (from == ForgeDirection.DOWN) from = ForgeDirection.UP;//下向きに設置した場合は真上を向いている
        	ArrayList<ForgeDirection> dir = BCLoadHandler.getPipeConected(this.worldObj, this.xCoord, this.yCoord, this.zCoord, from);

        	DCsLog.debugTrace("CurrentMetadata: " + this.getBlockMetadata());
        	DCsLog.debugTrace("CurrentDirection: " + from.name());
        	DCsLog.debugTrace("Direction size: " + dir.size());
        	
        	//fromはメタデータから得た向き（一方向）なので、大抵の場合はサイズ1か0のはず
        	if (dir.size() > 0)
        	{
        		for (int i = 0; i < 5; ++i)//5スロット内部をサーチ
                {
                    if (this.getStackInSlot(i) != null)//中身があった
                    {
                        ItemStack current = this.getStackInSlot(i);
                        DCsLog.debugTrace("Current size: " + current.stackSize);
                    	ItemStack itemstack[] = this.extractItem(true, from, 1);
                    	
                        for (int l = 0; l < itemstack.length; l++)
                        {
                        	DCsLog.debugTrace("Extracted Item: " + itemstack[l].stackSize + " : " + itemstack[l].getDisplayName());
                        	if (BCLoadHandler.dropIntoPipe(this, dir, itemstack[l]))
                            {
                        		DCsLog.debugTrace("Current size after: " + current.stackSize);
                            	
                            	tryInsert = true;
                            	break;
                            }
                        	
                        }
                        this.onInventoryChanged();
                        return true;
                    }
                }
        	}
        }
        
    	//バニラホッパーの挙動
    	IInventory iinventory = this.getOutputInventory();

        if (iinventory == null || tryInsert)//パイプへの搬出がうまくいった場合は何もしない
        {
            return false;
        }
        else
        {
            for (int i = 0; i < this.getSizeInventory(); ++i)//バニラホッパーと同じ挙動
            {
                if (this.getStackInSlot(i) != null)
                {
                    ItemStack itemstack = this.getStackInSlot(i).copy();
                    ItemStack itemstack1 = insertStack(iinventory, this.decrStackSize(i, 1), Facing.oppositeSide[BlockRHopper.getDirectionFromMetadata(this.getBlockMetadata())]);

                    if (itemstack1 == null || itemstack1.stackSize == 0)
                    {
                        iinventory.onInventoryChanged();
                        return true;
                    }

                    this.setInventorySlotContents(i, itemstack);
                }
            }

            return false;
        }
    }

    //RHopperに吸い込む
    public static boolean suckItemsIntoHopper(Hopper par0Hopper)
    {
        IInventory iinventory = getInventoryAboveHopper(par0Hopper);

        if (iinventory != null)
        {
            byte b0 = 0;

            if (iinventory instanceof ISidedInventory && b0 > -1)
            {
                ISidedInventory isidedinventory = (ISidedInventory)iinventory;
                int[] aint = isidedinventory.getAccessibleSlotsFromSide(b0);

                for (int i = 0; i < aint.length; ++i)
                {
                    if (insertStackFromInventory(par0Hopper, iinventory, aint[i], b0))
                    {
                        return true;
                    }
                }
            }
            else
            {
                int j = iinventory.getSizeInventory();

                for (int k = 0; k < j; ++k)
                {
                    if (insertStackFromInventory(par0Hopper, iinventory, k, b0))
                    {
                        return true;
                    }
                }
            }
        }
        else
        {
            EntityItem entityitem = getEntityAbove(par0Hopper.getWorldObj(), par0Hopper.getXPos(), par0Hopper.getYPos() - 1.0D, par0Hopper.getZPos());

            if (entityitem != null)
            {
                return insertStackFromEntity(par0Hopper, entityitem);
            }
        }

        return false;
    }

    //インベントリから
    private static boolean insertStackFromInventory(Hopper par0Hopper, IInventory par1IInventory, int par2, int par3)
    {
        ItemStack itemstack = par1IInventory.getStackInSlot(par2);

        if (itemstack != null && canExtractItemFromInventory(par1IInventory, itemstack, par2, par3))
        {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = insertStack(par0Hopper, par1IInventory.decrStackSize(par2, 1), -1);

            if (itemstack2 == null || itemstack2.stackSize == 0)
            {
                par1IInventory.onInventoryChanged();
                return true;
            }

            par1IInventory.setInventorySlotContents(par2, itemstack1);
        }

        return false;
    }

    //落ちているアイテムを拾う
    public static boolean insertStackFromEntity(IInventory par0IInventory, EntityItem par1EntityItem)
    {
        boolean flag = false;

        if (par1EntityItem == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = par1EntityItem.getEntityItem().copy();
            ItemStack itemstack1 = insertStack(par0IInventory, itemstack, -1);

            if (itemstack1 != null && itemstack1.stackSize != 0)
            {
                par1EntityItem.setEntityItemStack(itemstack1);
            }
            else
            {
                flag = true;
                par1EntityItem.setDead();
            }

            return flag;
        }
    }

    /**
     * Inserts a stack into an inventory. Args: Inventory, stack, side. Returns leftover items.
     */
    public static ItemStack insertStack(IInventory par0IInventory, ItemStack par1ItemStack, int par2)
    {
        if (par0IInventory instanceof ISidedInventory && par2 > -1)
        {
            ISidedInventory isidedinventory = (ISidedInventory)par0IInventory;
            int[] aint = isidedinventory.getAccessibleSlotsFromSide(par2);

            for (int j = 0; j < aint.length && par1ItemStack != null && par1ItemStack.stackSize > 0; ++j)
            {
                par1ItemStack = insertItemstack(par0IInventory, par1ItemStack, aint[j], par2);
            }
        }
        else
        {
            int k = par0IInventory.getSizeInventory();

            for (int l = 0; l < k && par1ItemStack != null && par1ItemStack.stackSize > 0; ++l)
            {
                par1ItemStack = insertItemstack(par0IInventory, par1ItemStack, l, par2);
            }
        }

        if (par1ItemStack != null && par1ItemStack.stackSize == 0)
        {
            par1ItemStack = null;
        }

        return par1ItemStack;
    }

    /**
     * Args: inventory, item, slot, side
     */
    private static boolean canInsertItemToInventory(IInventory par0IInventory, ItemStack par1ItemStack, int par2, int par3)
    {
        return !par0IInventory.isItemValidForSlot(par2, par1ItemStack) ? false : !(par0IInventory instanceof ISidedInventory) || ((ISidedInventory)par0IInventory).canInsertItem(par2, par1ItemStack, par3);
    }

    //吸い込み対象のインベントリ。SidedInventoryの材料スロットやホッパー系からの搬入は禁止。
    private static boolean canExtractItemFromInventory(IInventory par0IInventory, ItemStack par1ItemStack, int par2, int par3)
    {
        if (par0IInventory instanceof ISidedInventory) {
        	return ((ISidedInventory)par0IInventory).canExtractItem(par2, par1ItemStack, par3);
        }
        else if (par0IInventory instanceof TileEntityRHopper) {
        	return false;
        }
        else if (par0IInventory instanceof TileEntityHopper) {
        	return false;
        }
        else {
        	return true;
        }
    }

    private static ItemStack insertItemstack(IInventory par0IInventory, ItemStack par1ItemStack, int par2, int par3)
    {
        ItemStack itemstack1 = par0IInventory.getStackInSlot(par2);

        if (canInsertItemToInventory(par0IInventory, par1ItemStack, par2, par3))
        {
            boolean flag = false;

            if (itemstack1 == null)
            {
                par0IInventory.setInventorySlotContents(par2, par1ItemStack);
                par1ItemStack = null;
                flag = true;
            }
            else if (areItemStacksEqualItem(itemstack1, par1ItemStack))
            {
                int k = par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
                int l = Math.min(par1ItemStack.stackSize, k);
                par1ItemStack.stackSize -= l;
                itemstack1.stackSize += l;
                flag = l > 0;
            }

            if (flag)
            {
                if (par0IInventory instanceof InventoryRHopper)
                {
                    ((InventoryRHopper) par0IInventory).RHopper().setTransferCooldown(4);
                    par0IInventory.onInventoryChanged();
                }

                par0IInventory.onInventoryChanged();
            }
        }

        return par1ItemStack;
    }

    //搬出先インベントリ
    private IInventory getOutputInventory()
    {
        int i = BlockRHopper.getDirectionFromMetadata(this.getBlockMetadata());
        IInventory iinventory = getInventoryAtLocation(this.getWorldObj(), (double)(this.xCoord + Facing.offsetsXForSide[i]), (double)(this.yCoord + Facing.offsetsYForSide[i]), (double)(this.zCoord + Facing.offsetsZForSide[i]));
        return iinventory;
    }

    //1ブロック下のインベントリ取得
    public static IInventory getInventoryAboveHopper(Hopper par0Hopper)
    {
        return getInventoryAtLocation(par0Hopper.getWorldObj(), par0Hopper.getXPos(), par0Hopper.getYPos() - 1.0D, par0Hopper.getZPos());
    }

    //EntityItemの吸引範囲
    public static EntityItem getEntityAbove(World par0World, double par1, double par3, double par5)
    {
        List list = par0World.selectEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getAABBPool().getAABB(par1 - 1.0D, par3, par5 - 1.0D, par1 + 2.0D, par3 + 2.0D, par5 + 2.0D), IEntitySelector.selectAnything);
        return list.size() > 0 ? (EntityItem)list.get(0) : null;
    }

    /**
     * Gets an inventory at the given location to extract items into or take items from. Can find either a tile entity
     * or regular entity implementing IInventory.
     */
    public static IInventory getInventoryAtLocation(World par0World, double par1, double par3, double par5)
    {
        IInventory iinventory = null;
        int i = MathHelper.floor_double(par1);
        int j = MathHelper.floor_double(par3);
        int k = MathHelper.floor_double(par5);
        TileEntity tileentity = par0World.getBlockTileEntity(i, j, k);

        if (tileentity != null && tileentity instanceof IInventory)
        {
            iinventory = (IInventory)tileentity;

            if (iinventory instanceof TileEntityChest)
            {
                int l = par0World.getBlockId(i, j, k);
                Block block = Block.blocksList[l];

                if (block instanceof BlockChest)
                {
                    iinventory = ((BlockChest)block).getInventory(par0World, i, j, k);
                }
            }
        }

        if (iinventory == null)
        {
            List list = par0World.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getAABBPool().getAABB(par1, par3, par5, par1 + 1.0D, par3 + 1.0D, par5 + 1.0D), IEntitySelector.selectInventories);

            if (list != null && list.size() > 0)
            {
                iinventory = (IInventory)list.get(par0World.rand.nextInt(list.size()));
            }
        }

        return iinventory;
    }

    private static boolean areItemStacksEqualItem(ItemStack par0ItemStack, ItemStack par1ItemStack)
    {
        return par0ItemStack.itemID != par1ItemStack.itemID ? false : (par0ItemStack.getItemDamage() != par1ItemStack.getItemDamage() ? false : (par0ItemStack.stackSize > par0ItemStack.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(par0ItemStack, par1ItemStack)));
    }
    
    //Inventory
    @Override
	public int getSizeInventory() {
		
		return this.hopperItemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if (i < this.getSizeInventory()) {
			return this.hopperItemStacks[i];
		}
		else 
			return (ItemStack)null;
		
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (this.hopperItemStacks[i] != null)
		{
			ItemStack itemstack;
 
			if (this.hopperItemStacks[i].stackSize <= j)
			{
				itemstack = this.hopperItemStacks[i].copy();
				this.hopperItemStacks[i].stackSize = 0;
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
		if (i < 5) {
			this.hopperItemStacks[i] = itemstack;
			
			if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
			{
				itemstack.stackSize = this.getInventoryStackLimit();
			}
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
		super.onInventoryChanged();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityplayer.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
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
        return (double)this.xCoord;
    }

    /**
     * Gets the world Y position for this hopper entity.
     */
    public double getYPos()
    {
        return (double)this.yCoord;
    }

    /**
     * Gets the world Z position for this hopper entity.
     */
    public double getZPos()
    {
        return (double)this.zCoord;
    }

	@Override
	public World getWorldObj() {
		
		return this.worldObj;
	}
	
	//以下はBCパイプへアイテムを送るためのメソッドのはずだけどちゃんと動かないよく分からない何か。
	@Override
	public ItemStack[] extractItem(boolean doRemove, ForgeDirection from, int maxItemCount)
	  {
	    TileEntityRHopper inv = this;
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

	//こちらはパイプから来たアイテムを受け入れるためのもの。
	@Override
	public int addItem(ItemStack stack, boolean doAdd, ForgeDirection from) {
		
		int l = this.getSizeInventory();
		int removeItemstack = 0;
		boolean sameDir = from== ForgeDirection.getOrientation(this.getBlockMetadata());
		
		if (!sameDir) {
			for (int i = 0; i < 5 && stack != null && stack.stackSize > 0; i++)
	        {
	            ItemStack current = this.getStackInSlot(i);
	            if (this.isItemValidForSlot(i, stack))
	            {
	            	boolean flag = false;

	                if (current == null || current.stackSize <= 0)
	                {
	                    this.setInventorySlotContents(i, stack.copy());
	                    removeItemstack = stack.stackSize;
	                    stack.stackSize = 0;
	                    stack = (ItemStack)null;
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
	
	@Override
	public ConnectOverride overridePipeConnection(PipeType type,
			ForgeDirection with) {
		if (type == PipeType.ITEM /*&& (with == ForgeDirection.DOWN || with == ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite())*/) {
			return ConnectOverride.CONNECT;
		}
		else return ConnectOverride.DISCONNECT;
	}
}
