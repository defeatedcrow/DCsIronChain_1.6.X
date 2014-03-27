package mods.DCironchain.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import mods.DCironchain.entity.TileEntityFloodLight;
import mods.DCironchain.entity.TileEntityRHopper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{
        @Override
        public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
        {
            if (packet.channel.equals("TileRHopper")) {
    			ByteArrayDataInput bis = ByteStreams.newDataInput(packet.data);
                ItemStack[] items = null;
    			
                try
                {
                        int x = bis.readInt();
                        int y = bis.readInt();
                        int z = bis.readInt();
                        int TransferCooldown = bis.readInt();
                        boolean hasStacks = bis.readByte() != 0;
                        if (hasStacks)
                        {
                                items = new ItemStack[5];
                                for (int i = 0; i < items.length; ++i)
                                {
                                        items[i] = Packet.readItemStack(bis);
                                }
                        }
                }
                catch (IOException e)
                {
                }

                
            }
            else if (packet.channel.equals("TileFloodLight")) {
    			ByteArrayDataInput bis = ByteStreams.newDataInput(packet.data);
    			
//                try
//                {
                	int x = bis.readInt();
    				int y = bis.readInt();
    				int z = bis.readInt();
    				int direction = bis.readByte();
    				
    				World world = DCsIronChain.proxy.getClientWorld();
    				if (world == null) {
    					world = ((EntityPlayer) player).worldObj;
    				}
    				
    				TileEntity tile = world.getBlockTileEntity(x, y, z);
    				
    				if (tile instanceof TileEntityFloodLight) {
    					TileEntityFloodLight tileLight = (TileEntityFloodLight)tile;
    					tileLight.setDirection();
    				}
//                }
//                catch (IOException e)
//                {
//                	
//                }

                
            } 
        }

        public static Packet getPacket(TileEntityRHopper tileEntityRHopper)
        {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);

                int x = tileEntityRHopper.xCoord;
                int y = tileEntityRHopper.yCoord;
                int z = tileEntityRHopper.zCoord;
                int TransferCooldown = tileEntityRHopper.getCoolTime();
                ItemStack[] items = tileEntityRHopper.getItems();
                boolean hasStacks = (items != null);

                try
                {
                        dos.writeInt(x);
                        dos.writeInt(y);
                        dos.writeInt(z);
                        dos.writeInt(TransferCooldown);
                        dos.writeByte(hasStacks? 1 : 0);
                        if (hasStacks)
                        {
                                for (int i = 0; i < items.length; i++)
                                {
                                        Packet.writeItemStack(items[i], dos);
                                }
                        }
                }
                catch(Exception e)
                {
                        e.printStackTrace();
                }

                Packet250CustomPayload packet = new Packet250CustomPayload();
                packet.channel = "TileRHopper";
                packet.data = bos.toByteArray();
                packet.length = bos.size();
                packet.isChunkDataPacket = true;

                return packet;
        }
        
        public static Packet getLightPacket(TileEntityFloodLight tileLight)
        {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);

                int x = tileLight.xCoord;
                int y = tileLight.yCoord;
                int z = tileLight.zCoord;
                byte direction = tileLight.getDirectionByte();

                try
                {
                        dos.writeInt(x);
                        dos.writeInt(y);
                        dos.writeInt(z);
                        dos.writeByte(direction);
                }
                catch(Exception e)
                {
                        e.printStackTrace();
                }

                Packet250CustomPayload packet = new Packet250CustomPayload();
                packet.channel = "TileFloodLight";
                packet.data = bos.toByteArray();
                packet.length = bos.size();
                packet.isChunkDataPacket = true;

                return packet;
        }

}
