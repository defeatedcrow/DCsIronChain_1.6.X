package mods.DCironchain.common.material;

import java.util.Random;

import mods.DCironchain.entity.TileEntityRHopper;
import mods.DCironchain.entity.TileEntityRHopperGold;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRHopperGold extends BlockRHopper{
	
	private final Random rand = new Random();
	@SideOnly(Side.CLIENT)
	private Icon baseIcon;
	@SideOnly(Side.CLIENT)
	private Icon insideIcon;

	public BlockRHopperGold(int blockid) {
		super(blockid);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityRHopperGold();
    }
	
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
    {
        return par1 == 0 ? this.baseIcon : this.insideIcon;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister par1IconRegister)
    {
        this.baseIcon = par1IconRegister.registerIcon("gold_block");
        this.insideIcon = par1IconRegister.registerIcon("hopper_inside");
    }
}
