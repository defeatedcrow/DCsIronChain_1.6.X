package mods.DCironchain.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeRegister {

	public void addRecipe() {
		
		GameRegistry.addRecipe(
	    		  new ItemStack(DCsIronChain.ironChain, 16),
	    		  new Object[]{" Z "," Z "," Z ",
	    			  Character.valueOf('Z'), Item.ingotIron
	    			  });
		 
		 GameRegistry.addRecipe(
	    		  new ItemStack(DCsIronChain.anchorBolt, 1),
	    		  new Object[]{"ZX",
	    			  Character.valueOf('Z'), Item.ingotIron,
	    			  Character.valueOf('X'), DCsIronChain.ironChain
	    			  });
      
      GameRegistry.addRecipe(
				 new ShapedOreRecipe(
	    		  new ItemStack(DCsIronChain.kyatatu, 1, 0),
	    		  new Object[]{"XYX","XYX","XYX",
	    			  Character.valueOf('X'), Item.ingotIron,
	    			  Character.valueOf('Y'), Block.ladder}));
      
      GameRegistry.addRecipe(
				 new ShapedOreRecipe(
	    		  new ItemStack(DCsIronChain.floodLight, 1),
	    		  new Object[]{"XXX","ZYZ"," X ",
	    			  Character.valueOf('X'), Item.ingotIron,
	    			  Character.valueOf('Y'), Block.glowStone,
	    			  Character.valueOf('Z'), Block.fenceIron}));
      
      GameRegistry.addRecipe(
				 new ShapedOreRecipe(
	    		  new ItemStack(DCsIronChain.anzenMet, 1),
	    		  new Object[]{"XYX","ZWZ",
	    			  Character.valueOf('X'), "dyeYellow",
	    			  Character.valueOf('Y'), Item.helmetLeather,
	    			  Character.valueOf('Z'), Item.silk,
	    			  Character.valueOf('W'), Block.cloth}));
      
      GameRegistry.addRecipe(
				 new ShapedOreRecipe(
	    		  new ItemStack(DCsIronChain.sagyougi, 1),
	    		  new Object[]{"X X","XXX","YYY",
	    			  Character.valueOf('X'), new ItemStack(Block.cloth, 1, 9),
	    			  Character.valueOf('Y'), Item.leather}));
      
      GameRegistry.addRecipe(
				 new ShapedOreRecipe(
	    		  new ItemStack(DCsIronChain.anzenBoots, 1),
	    		  new Object[]{"XZX","Y Y",
	    			  Character.valueOf('X'), Item.leather,
	    			  Character.valueOf('Y'), Item.ingotIron,
	    			  Character.valueOf('Z'), "dyeBlack"}));
      
      GameRegistry.addRecipe(
    		  new ItemStack(DCsIronChain.RHopper, 1),
    		  new Object[]{" X ","XZX","XYX",
    			  Character.valueOf('X'), Item.ingotIron,
    			  Character.valueOf('Y'), Block.torchRedstoneActive,
    			  Character.valueOf('Z'), Block.dropper
    			  });
      
      //use bronze recipe
      
      GameRegistry.addRecipe(
				 new ShapedOreRecipe(
	    		  new ItemStack(DCsIronChain.ironChain, 16),
	    		  new Object[]{" X "," X "," X ",
	    			  Character.valueOf('X'), "ingotBronze"}));
      
      GameRegistry.addRecipe(
				 new ShapedOreRecipe(
	    		  new ItemStack(DCsIronChain.anchorBolt, 1),
	    		  new Object[]{"XY",
	    			  Character.valueOf('X'), "ingotBronze",
	    			  Character.valueOf('Y'), DCsIronChain.ironChain}));
      
      GameRegistry.addRecipe(
				 new ShapedOreRecipe(
	    		  new ItemStack(DCsIronChain.kyatatu, 1, 0),
	    		  new Object[]{"XYX","XYX","XYX",
	    			  Character.valueOf('X'), "ingotBronze",
	    			  Character.valueOf('Y'), Block.ladder}));
      
      GameRegistry.addRecipe(
				 new ShapedOreRecipe(
	    		  new ItemStack(DCsIronChain.floodLight, 1),
	    		  new Object[]{"XXX","ZYZ"," X ",
	    			  Character.valueOf('X'), "ingotBronze",
	    			  Character.valueOf('Y'), Block.glowStone,
	    			  Character.valueOf('Z'), Block.fenceIron}));
		
	}

}
