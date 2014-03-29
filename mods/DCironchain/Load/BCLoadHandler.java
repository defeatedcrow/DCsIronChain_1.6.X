package mods.DCironchain.Load;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import buildcraft.api.core.Position;
import buildcraft.api.transport.IPipe;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile;
import buildcraft.api.transport.IPipeTile.PipeType;
import mods.DCironchain.common.DCsLog;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public class BCLoadHandler{
	
	public static ArrayList<ForgeDirection> getPipeConected (World world, int X, int Y, int Z, ForgeDirection from)
	{
		ArrayList<ForgeDirection> possiblePipe = new ArrayList<ForgeDirection>();
		
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
		      if ((from == ForgeDirection.UNKNOWN) || (from == dir.getOpposite()))
		      {
		    	  {
		    		  Position pos = new Position(X, Y, Z, dir);
		    		  pos.moveForwards(1.0D);
		    		  
		    		  TileEntity pipeTile = world.getBlockTileEntity((int) pos.x, (int) pos.y, (int) pos.z);
		    		  
		    		  DCsLog.debugTrace("Now Checking conection");
		    		  DCsLog.debugTrace("Conected direction dir: " + dir.name());
		    		  DCsLog.debugTrace("Conected direction from: " + from.name());
		    		  
		    		  if (pipeTile instanceof IPipeTile)
		    		  {
		    			  IPipeTile pipe = (IPipeTile)pipeTile;
		    			  DCsLog.debugTrace("Pypetype name: " + pipe.getPipeType().name());
		    			  if ((from != ForgeDirection.UNKNOWN) && ((pipeTile instanceof IPipeConnection))) 
		    			  {
		    		            if (((IPipeConnection)pipeTile).overridePipeConnection(IPipeTile.PipeType.ITEM, dir) != IPipeConnection.ConnectOverride.DISCONNECT)
		    		            possiblePipe.add(dir);
		    		      }
		    			  else if ((pipe.getPipeType() == IPipeTile.PipeType.ITEM) && (pipe.isPipeConnected(dir.getOpposite())))
		    		            possiblePipe.add(dir);
		    		  }
		    	  }
		      }
		}
			
		return possiblePipe;
	}
	
	public static boolean dropIntoPipe (TileEntity tile, ArrayList<ForgeDirection> pipes, ItemStack itemstack)
	{
		if (itemstack != null && itemstack.stackSize > 0 && pipes.size() > 0)
		{
			int choice = tile.worldObj.rand.nextInt(pipes.size());
			Position itemPos = new Position(tile.xCoord, tile.yCoord, tile.zCoord, pipes.get(choice));

		    itemPos.x += 0.5D;
		    itemPos.y += 0.25D;
		    itemPos.z += 0.5D;
		    itemPos.moveForwards(0.5D);
		    
		    Position pipePos = new Position(tile.xCoord, tile.yCoord, tile.zCoord, pipes.get(choice));
		    pipePos.moveForwards(1.0D);
		    
			IPipeTile pipe = (IPipeTile)tile.worldObj.getBlockTileEntity((int)pipePos.x, (int)pipePos.y, (int)pipePos.z);
			ItemStack drop = itemstack.copy();
			
			DCsLog.debugTrace("Export direction: " + pipes.get(choice).name());
			DCsLog.debugTrace("Drop Item: " + drop.stackSize + " : " + drop.getDisplayName());
			
			if (pipe.injectItem(drop, true, pipePos.orientation.getOpposite()) > 0) {
				  DCsLog.debugTrace("Succeeded to extract");
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
