package com.pengu.holestorage.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.item.IItemRender;
import com.pengu.holestorage.init.ItemsBHS;
import com.pengu.holestorage.items.ItemUnobtainable;

/**
 * This class renders the animation for AntiMatter
 * 
 * @author APengu
 */
public class ItemRenderAntiMatter implements IItemRender
{
	private static final ItemStack CORE = new ItemStack(ItemsBHS.ITEM_UNOBTAINABLE, 1, ItemUnobtainable.anti_matter_core), ELECTRON1 = new ItemStack(ItemsBHS.ITEM_UNOBTAINABLE, 1, ItemUnobtainable.anti_matter_electron1), ELECTRON2 = new ItemStack(ItemsBHS.ITEM_UNOBTAINABLE, 1, ItemUnobtainable.anti_matter_electron2), ELECTRON3 = new ItemStack(ItemsBHS.ITEM_UNOBTAINABLE, 1, ItemUnobtainable.anti_matter_electron3);
	
	@Override
	public void renderItem(ItemStack item)
	{
		RenderItem r = Minecraft.getMinecraft().getRenderItem();
		
		GlStateManager.disableLighting();
		
		GL11.glPushMatrix();
		
		double rotation = (System.currentTimeMillis() - item.hashCode() * 8L) % (36000L * 2L) / 10D;
		
		// TransformType.GUI;
		
		GL11.glPushMatrix();
		GL11.glTranslated(.5, .5, .5);
		r.renderItem(CORE, TransformType.NONE);
		
		GL11.glTranslated(.5, 0, 0);
		GL11.glRotated(rotation, 1, 0, 0);
		r.renderItem(ELECTRON1, TransformType.NONE);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslated(.5, .5, .5);
		GL11.glRotated(rotation, 1, 1, 0);
		r.renderItem(ELECTRON2, TransformType.NONE);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslated(.5, .5, .5);
		GL11.glRotated(rotation, 0, 1, 1);
		GL11.glTranslated(0, .5, 0);
		r.renderItem(ELECTRON3, TransformType.NONE);
		GL11.glPopMatrix();
		
		GL11.glPopMatrix();
		
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
		GlStateManager.enableBlend();
	}
}