package mods.DCironchain.Load;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import buildcraft.api.core.Position;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile;
import buildcraft.api.transport.IPipeTile.PipeType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BCLoadHandler{
	
	public static ForgeDirection[] getPipeConected (World world, int X, int Y, int Z, ForgeDirection from)
	{
		LinkedList possiblePipe = new LinkedList();
		
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
		      if ((from == ForgeDirection.UNKNOWN) || (from == dir.getOpposite()))
		      {
		    	  {
		    		  Position pos = new Position(X, Y, Z, dir);
		    		  pos.moveForwards(1.0D);
		    		  
		    		  TileEntity pipeTile = world.getBlockTileEntity((int) pos.x, (int) pos.y, (int) pos.z);
		    		  
		    		  if (pipeTile instanceof IPipeTile)
		    		  {
		    			  IPipeTile pipe = (IPipeTile)pipeTile;
		    			  if ((from != ForgeDirection.UNKNOWN) && ((pipeTile instanceof IPipeConnection))) 
		    			  {
		    		            if (((IPipeConnection)pipeTile).overridePipeConnection(IPipeTile.PipeType.ITEM, from) != IPipeConnection.ConnectOverride.DISCONNECT)
		    		              possiblePipe.add(dir);
		    		          }
		    			  else if ((pipe.getPipeType() == IPipeTile.PipeType.ITEM) && (pipe.isPipeConnected(dir.getOpposite())))
		    		            possiblePipe.add(dir);
		    		  }
		    	  }
		      }
		}
			
		return (ForgeDirection[])possiblePipe.toArray(new ForgeDirection[0]);
	}
	
	public static boolean dropIntoPipe (TileEntity tile, ArrayList pipes, ItemStack itemstack)
	{
		if (itemstack == null || itemstack.stackSize <= 0 || pipes.size() <= 0)
		{
			int choice = tile.worldObj.rand.nextInt(pipes.size());
			Position itemPos = new Position(tile.xCoord, tile.yCoord, tile.zCoord, (ForgeDirection)pipes.get(choice));

		    itemPos.x += 0.5D;
		    itemPos.y += 0.25D;
		    itemPos.z += 0.5D;
		    itemPos.moveForwards(0.5D);
		    
		    Position pipePos = new Position(tile.xCoord, tile.yCoord, tile.zCoord, (ForgeDirection)pipes.get(choice));
		    pipePos.moveForwards(1.0D);
		    
			IPipeTile pipe = (IPipeTile)tile.worldObj.getBlockTileEntity((int)pipePos.x, (int)pipePos.y, (int)pipePos.z);
			ItemStack drop = itemstack.splitStack(1);
			if (pipe.injectItem(drop, true, itemPos.orientation.getOpposite()) > 0) {
			      return true;
			    }
			    pipes.remove(choice);
			    
			return false;
		}
		else
		{
			return false;
		}
	}

}
