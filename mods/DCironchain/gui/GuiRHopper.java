package mods.DCironchain.gui;

import mods.DCironchain.entity.ContainerRHopper;
import mods.DCironchain.entity.InventoryRHopper;
import mods.DCironchain.entity.TileEntityRHopper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;


public class GuiRHopper extends GuiContainer
{
    private static final ResourceLocation hopperGuiTextures = new ResourceLocation("textures/gui/container/hopper.png");
    private IInventory playerInv;
    private InventoryRHopper entityInv;

    public GuiRHopper(InventoryPlayer par1InventoryPlayer, TileEntityRHopper RHopper)
    {
        super(new ContainerRHopper(par1InventoryPlayer, RHopper));
        this.playerInv = par1InventoryPlayer;
        this.entityInv = RHopper.inventory;
        this.allowUserInput = false;
        this.ySize = 133;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    	String s = this.entityInv.isInvNameLocalized() ? this.entityInv.getInvName() : I18n.getString(this.entityInv.getInvName());
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(hopperGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
