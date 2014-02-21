package mods.DCironchain.entity;

import java.util.ArrayList;
import java.util.List;

import mods.DCironchain.Load.BCLoadHandler;
import mods.DCironchain.common.BlockRHopper;
import mods.DCironchain.common.DCsIronChain;
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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityRHopper extends TileEntity 
{

    private int transferCooldown = -1;
    
    public InventoryRHopper inventory;
    
    public TileEntityRHopper(){
    	this.inventory = new InventoryRHopper(this);
    }

    //NBT
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
        this.inventory.hopperItemStacks = new ItemStack[this.inventory.getSizeInventory()];

        this.transferCooldown = par1NBTTagCompound.getInteger("TransferCooldown");

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.inventory.hopperItemStacks.length)
            {
                this.inventory.hopperItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.hopperItemStacks.length; ++i)
        {
            if (this.inventory.hopperItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventory.hopperItemStacks[i].writeToNBT(nbttagcompound1);
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
                    for (int i = 0; i < this.inventory.getSizeInventory(); ++i)
                    {
                            if (_items[i] != null)
                            {
                                    this.inventory.hopperItemStacks[i] = _items[i].copy();
                            }
                            else
                            {
                                    this.inventory.hopperItemStacks[i] = null;
                            }
                    }
            }
    }

    //write
	public ItemStack[] getItems() {
		
		return this.inventory.hopperItemStacks;
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
                flag = suckItemsIntoHopper(this.inventory) || flag;

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
        /*
    	if (DCsIronChain.getLoadBC)
        {
        	ArrayList pipes = new ArrayList();
            ForgeDirection from = ForgeDirection.getOrientation(this.getBlockMetadata());
            if (from == ForgeDirection.DOWN) from = ForgeDirection.UP;
        	ForgeDirection[] dir = BCLoadHandler.getPipeConected(this.worldObj, this.xCoord, this.yCoord, this.zCoord, from);
        	for (int i = 0; i < dir.length; i++)
        	{
        		pipes.add(dir[i]);
        	}

        	if (pipes.size() > 0)
        	{
        		for (int i = 0; i < this.inventory.getSizeInventory(); ++i)
                {
                    if (this.inventory.getStackInSlot(i) != null)
                    {
                        ItemStack itemstack[] = this.inventory.extractItem(true, from, 1);
                        for (int l = 0; l < itemstack.length; l++)
                        {
                        	if (BCLoadHandler.dropIntoPipe(this, pipes, itemstack[l]))
                            {
                            	itemstack[l].stackSize -=1;
                            }
                        	if (itemstack == null || itemstack[l].stackSize == 0)
                            {
                                this.inventory.onInventoryChanged();
                                tryInsert = true;
                                return true;
                            }
                        	this.inventory.setInventorySlotContents(i, itemstack[l]);
                        }
                    }
                }
        	}
        }
        */
    	
    	IInventory iinventory = this.getOutputInventory();

        if (iinventory == null || tryInsert)
        {
            return false;
        }
        else
        {
            for (int i = 0; i < this.inventory.getSizeInventory(); ++i)
            {
                if (this.inventory.getStackInSlot(i) != null)
                {
                    ItemStack itemstack = this.inventory.getStackInSlot(i).copy();
                    ItemStack itemstack1 = insertStack(iinventory, this.inventory.decrStackSize(i, 1), Facing.oppositeSide[BlockRHopper.getDirectionFromMetadata(this.getBlockMetadata())]);

                    if (itemstack1 == null || itemstack1.stackSize == 0)
                    {
                        iinventory.onInventoryChanged();
                        return true;
                    }

                    this.inventory.setInventorySlotContents(i, itemstack);
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

    //バニラホッパーへの搬出を禁止
    private static boolean canExtractItemFromInventory(IInventory par0IInventory, ItemStack par1ItemStack, int par2, int par3)
    {
        return (!(par0IInventory instanceof ISidedInventory) || ((ISidedInventory)par0IInventory).canExtractItem(par2, par1ItemStack, par3)) || (par0IInventory instanceof InventoryRHopper);
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

}
