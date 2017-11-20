package com.pengu.holestorage.client.render.item;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.item.iItemRender;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.holestorage.client.particle.ParticleExplosion;
import com.pengu.holestorage.init.ItemsBHS;
import com.pengu.holestorage.items.ItemUnobtainable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * This class renders the animation for AntiMatter
 * 
 * @author APengu
 */
public class ItemRenderAntiMatter implements iItemRender
{
	private static final ItemStack CORE = new ItemStack(ItemsBHS.ANTI_MATTER_CORE), ELECTRON1 = new ItemStack(ItemsBHS.ANTI_MATTER_POSITRON_1), ELECTRON2 = new ItemStack(ItemsBHS.ANTI_MATTER_POSITRON_2), ELECTRON3 = new ItemStack(ItemsBHS.ANTI_MATTER_POSITRON_3);
	
	@Override
	public void renderItem(ItemStack item)
	{
		RenderItem r = Minecraft.getMinecraft().getRenderItem();
		
		GL11.glPushMatrix();
		
		double rotation = (System.currentTimeMillis() - item.hashCode() * 8L) % (36000L * 2L) / 10D;
		
		GL11.glPushMatrix();
		GL11.glTranslated(.5, .5, .5);
		
		double scale = Math.sin(rotation / 30D) * 3;
		GL11.glPushMatrix();
		GL11.glScaled(8 + scale, 8 + scale, 8 + scale);
		RenderUtil.renderColorfulLightRayEffects(0, 0, 0, ParticleExplosion.COLORS, 0L, (float) rotation / 3600F, 1, 50, 15);
		GL11.glPopMatrix();
		
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
	}
}