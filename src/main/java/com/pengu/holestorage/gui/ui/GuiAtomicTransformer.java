package com.pengu.holestorage.gui.ui;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.common.InterItemStack;
import com.pengu.holestorage.Info;
import com.pengu.holestorage.api.atomictransformer.SimpleTransformerRecipe;
import com.pengu.holestorage.gui.inv.ContainerAtomicTransformer;
import com.pengu.holestorage.tile.TileAtomicTransformer;

public class GuiAtomicTransformer extends GuiContainer
{
	public final ResourceLocation texture = new ResourceLocation(Info.MOD_ID, "textures/gui/atomic_transformer.png");
	public final DecimalFormat format = new DecimalFormat("#0,00");
	public final TileAtomicTransformer tile;
	
	public GuiAtomicTransformer(TileAtomicTransformer tile, InventoryPlayer inv)
	{
		super(new ContainerAtomicTransformer(tile, inv));
		this.tile = tile;
		xSize = 176;
		ySize = 166;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GL11.glEnable(GL11.GL_BLEND);
		
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if(tile.recipe == null)
			drawTexturedModalRect(guiLeft + 17, guiTop + 30, xSize, 0, 16, 26);
		else
			try
			{
				SimpleTransformerRecipe r = tile.recipe;
				
				double progress = new BigDecimal(tile.stored).divide(new BigDecimal(r.getEnergyUsed(tile.inventory.getStackInSlot(0)))).doubleValue();
				
				RenderUtil.drawTexturedModalRect(guiLeft + 17, guiTop + 30, xSize + 16, 0, 16, progress * 26);
				RenderUtil.drawTexturedModalRect(guiLeft + 17, guiTop + 30 + progress * 26, xSize + 32, progress * 26, 16, 26 - progress * 26);
				
				GL11.glPushMatrix();
				GL11.glTranslated(guiLeft + 44, guiTop + 9, 0);
				GL11.glScaled(.5, .5, 1);
				fontRenderer.drawString(I18n.translateToLocal("gui." + Info.MOD_ID + ":rf.required") + ": " + String.format("%,d", r.getEnergyUsed(tile.inventory.getStackInSlot(0))) + " RF", 0, 0, 0, false);
				fontRenderer.drawString(I18n.translateToLocal("gui." + Info.MOD_ID + ":rf.stored") + ": " + String.format("%,d", tile.stored) + " RF", 0, 18, 0, false);
				fontRenderer.drawString(I18n.translateToLocal("gui." + Info.MOD_ID + ":input") + ": " + r.getInputItemCount() + "x " + tile.getStackInSlot(0).getDisplayName(), 0, 36, 0, false);
				fontRenderer.drawString(I18n.translateToLocal("gui." + Info.MOD_ID + ":output") + ": " + InterItemStack.getStackSize(tile.recipe.getOutputItem()) + "x " + tile.recipe.getOutputItem().getDisplayName(), 0, 54, 0, false);
				fontRenderer.drawString(I18n.translateToLocal("gui." + Info.MOD_ID + ":progress") + ": " + format.format(progress * 100D * 100D) + "%", 0, 72, 0, false);
				GL11.glPopMatrix();
			} catch(Throwable error)
			{
			}
		
		GL11.glDisable(GL11.GL_BLEND);
	}
}