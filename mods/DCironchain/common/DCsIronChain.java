package mods.DCironchain.common;

import static cpw.mods.fml.relauncher.Side.CLIENT;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import mods.DCironchain.common.*;
import mods.DCironchain.entity.TileEntityRHopper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.src.*;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(
		modid = "DCsIronChain",
		name = "DCsIronChainMod",
		version = "1.6.2_1.1c",
		dependencies = "required-after:Forge@[9.10,);required-after:FML@[6.2.0,);after:BuildCraft|Core"
		)
@NetworkMod(
		clientSideRequired = true,
		serverSideRequired = false,
		channels = {"TileRHopper", "TileFloodLight"}, packetHandler = PacketHandler.class
		)

public class DCsIronChain{
	
	@SidedProxy(clientSide = "mods.DCironchain.client.ClientProxy", serverSide = "mods.DCironchain.common.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance("DCsIronChain")
    public static DCsIronChain instance;
	
	public static Block  ironChain;
	public static Block  anchorBolt;
	public static Block  kyatatu;
	public static Block  floodLight;
	public static Block  DCLightPart;
	public static Block  RHopper;
	
	public static Item anzenMet, sagyougi, sagyougiB, anzenBoots;
	
	public int blockIdDCChain = 627;
	public int blockIdDCAnchor = 628;
	public int blockIdKyatatu = 629;
	public int blockIdFloodLight = 630;
	public int blockIdDCPart = 631;
	public int blockIdRHopper = 632;
	
	public int itemIdAnzenMet = 6035;
	public int itemIdSagyougi = 6036;
	public int itemIdAnzenBoots = 6038;
	public int itemIdSagyougiB = 6037;
	
	public static int guiIdRHopper = 1;
	public static int modelRHopper;
	public static int modelFLight;
	
	public static boolean getLoadIC2 = false;
	public static boolean getLoadBC = false;
	public static boolean notUseLight = false;
	public static boolean visibleLight = false;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		try
		{
			cfg.load();
			Property blockChain = cfg.getBlock("Iron Chain", blockIdDCChain);
			Property blockAnchor = cfg.getBlock("Anchor Bolt", blockIdDCAnchor);
			Property blockKyatatu = cfg.getBlock("Kyauatu", blockIdKyatatu);
			Property blockDCflood = cfg.getBlock("FloodLight", blockIdFloodLight);
			Property blockDCpart = cfg.getBlock("LightBlock", blockIdDCPart);
			Property blockRHopper = cfg.getBlock("UpwardHopper", blockIdRHopper);
			
			Property itemAnzenMet = cfg.getItem("AnzenHelmet", itemIdAnzenMet);
			Property itemSagyougi = cfg.getItem("WorkerWear", itemIdSagyougi);
			Property itemSagyouB = cfg.getItem("WorkerWearBottom", itemIdSagyougiB);
			Property itemAnzenBoots = cfg.getItem("AnzenBoots", itemIdAnzenBoots);
			
			Property notUseLightBlock = cfg.get("Setting", "NotUseLightBlock", notUseLight, "Floodlight block does not put a invisible light block.");
			Property visibleLightBlock = cfg.get("Setting", "VisibleLightBlock", visibleLight, "Allow invisible light block be visible.");
			
			blockIdDCChain = blockChain.getInt();
			blockIdDCAnchor = blockAnchor.getInt();
			blockIdKyatatu = blockKyatatu.getInt();
			blockIdFloodLight = blockDCflood.getInt();
			blockIdDCPart = blockDCpart.getInt();
			blockIdRHopper = blockRHopper.getInt();
			
			itemIdAnzenMet = itemAnzenMet.getInt();
			itemIdSagyougi = itemSagyougi.getInt();
			itemIdSagyougiB = itemSagyouB.getInt();
			itemIdAnzenBoots = itemAnzenBoots.getInt();
			
			notUseLight = notUseLightBlock.getBoolean(notUseLight);
			visibleLight = visibleLightBlock.getBoolean(visibleLight);

		}
		catch (Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Error Message");

		}
		finally
		{
			cfg.save();
		}
		
		//sound load event
				MinecraftForge.EVENT_BUS.register(new NewSoundLoad());
		
		ironChain = (new BlockIronChain(blockIdDCChain)).
				setUnlocalizedName("defeatedcrow.DCironChain").
				setCreativeTab(CreativeTabs.tabDecorations);
		
		anchorBolt = (new BlockAnchorBolt(blockIdDCAnchor)).
				setUnlocalizedName("defeatedcrow.DCanchorBolt").
				setCreativeTab(CreativeTabs.tabDecorations);
		
		kyatatu = (new BlockKyatatu(blockIdKyatatu)).
				setUnlocalizedName("defeatedcrow.kyatatu").
				setCreativeTab(CreativeTabs.tabDecorations);
		
		floodLight = (new BlockFloodLight(blockIdFloodLight)).
				setUnlocalizedName("defeatedcrow.floodlight").
				setCreativeTab(CreativeTabs.tabDecorations);
		
		DCLightPart = (new BlockLightPart(blockIdDCPart)).
				setUnlocalizedName("defeatedcrow.lightBlock");
		
		RHopper = (new BlockRHopper(blockIdRHopper)).
				setUnlocalizedName("defeatedcrow.upwardHopper").
				setCreativeTab(CreativeTabs.tabRedstone);
		
		//int index1 = ModLoader.addArmor("anzenarmor");
		
		anzenMet = (new ItemAnzenArmor(itemIdAnzenMet - 256, EnumArmorMaterial.CHAIN, DCsIronChain.proxy.addArmor("anzenarmor"), 0)).
				setUnlocalizedName("defeatedcrow.anzenMet").
				setCreativeTab(CreativeTabs.tabCombat).setTextureName("crowsdefeat:anzen_met");
		
		sagyougi = (new ItemAnzenArmor(itemIdSagyougi - 256, EnumArmorMaterial.CHAIN, DCsIronChain.proxy.addArmor("anzenarmor"), 1)).
				setUnlocalizedName("defeatedcrow.sagyougi").
				setCreativeTab(CreativeTabs.tabCombat).setTextureName("crowsdefeat:sagyougi");
		
		anzenBoots = (new ItemAnzenArmor(itemIdAnzenBoots - 256, EnumArmorMaterial.CHAIN, DCsIronChain.proxy.addArmor("anzenarmor"), 3)).
				setUnlocalizedName("defeatedcrow.anzenBoots").
				setCreativeTab(CreativeTabs.tabCombat).setTextureName("crowsdefeat:anzen_boots");
		
		GameRegistry.registerBlock(ironChain, "ironChain");
		GameRegistry.registerBlock(anchorBolt, "anchorBolt");
		GameRegistry.registerBlock(kyatatu, ItemKyatatu.class, "kyatatu");
		GameRegistry.registerBlock(floodLight, "floodLight");
		GameRegistry.registerBlock(DCLightPart, "DCLightPart");
		GameRegistry.registerBlock(RHopper, "UpwardHopper");
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		//Registering new render
		this.modelRHopper = proxy.getRenderID();
		this.modelFLight = proxy.getRenderID();
		proxy.registerRenderers();
		
		//Registering new recipe
	      (new RecipeRegister()).addRecipe();
	      
	    //Registering language
	      (new LangRegister()).registerLang();
	      
	    //registering new gui, entity
	    proxy.registerTile();
	      
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
	      
	      if (Loader.isModLoaded("IC2"))
		    {
		        try
		        {
		          this.getLoadIC2 = true;
		        }
		        catch (Exception e) {
		          System.out.println("failed to check IC2");
		          e.printStackTrace(System.err);
		        }
		    }
	      
	      if (Loader.isModLoaded("BuildCraft|Core"))
		    {
		        try
		        {
		          this.getLoadBC = true;
		        }
		        catch (Exception e) {
		          System.out.println("failed to check BC");
		          e.printStackTrace(System.err);
		        }
		    }
	}
	
}
