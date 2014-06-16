package mods.DCironchain.common.material;

import java.util.Random;

import mods.DCironchain.common.DCsIronChain;
import mods.DCironchain.entity.*;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRHopperGold extends BlockRHopper{
	
	private final Random rand = new Random();
	@SideOnly(Side.CLIENT)
	private Icon baseIcon;
	@SideOnly(Side.CLIENT)
	private Icon sideIcon;
	@SideOnly(Side.CLIENT)
	private Icon insideIcon;
	
	public boolean isReverse = false;

	public BlockRHopperGold(int blockid, boolean flag) {
		super(blockid);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		this.isReverse = flag;
	}
	
	@Override
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
        int j1 = Facing.oppositeSide[par5];

        if (j1 == 0 && this.isReverse)
        {
            j1 = 1;
        }
        
        if (j1 == 1 && !this.isReverse)
        {
        	j1 = 0;
        }

        return j1;
    }
	
	@Override
	public TileEntity createNewTileEntity(World par1World)
    {
        return this.isReverse ? new TileEntityRHopperGold() : new TileEntityHopperGold();
    }
	
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
    {
		return par1 == 0 ? this.insideIcon : (par1 == 1 ? this.baseIcon : this.sideIcon);
    }
	
	public int getRenderType()
    {
        return DCsIronChain.modelHopper2;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister par1IconRegister)
    {
        this.baseIcon = par1IconRegister.registerIcon("crowsdefeat:rhopper_gold_top");
        this.sideIcon = par1IconRegister.registerIcon("crowsdefeat:rhopper_gold_side");
        this.insideIcon = par1IconRegister.registerIcon("hopper_inside");
    }
}
