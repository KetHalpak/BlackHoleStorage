package com.pengu.holestorage.client.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.common.InterItemStack;
import com.pengu.holestorage.tile.TileWormholeFormer;

public class TileRenderWormholeFormer extends TESR<TileWormholeFormer>
{
	@Override
	public void renderTileEntityAt(TileWormholeFormer te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		Minecraft mc = Minecraft.getMinecraft();
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + .5, y + .5, z + .5);
		
		double progress = te.storage.getEnergyStored();
		progress /= te.storage.getMaxEnergyStored();
		
		if(!InterItemStack.isStackNull(te.inventory.getStackInSlot(0)))
		{
			GL11.glPushMatrix();
			GL11.glScaled(.8, .8, .8);
			GL11.glTranslated(0, .225 * (1 - progress), -.125);
			GL11.glRotated(90, 1, 0, 0);
			mc.getRenderItem().renderItem(te.inventory.getStackInSlot(0).copy(), TransformType.GROUND);
			GL11.glPopMatrix();
		}
		
		if(!InterItemStack.isStackNull(te.inventory.getStackInSlot(1)))
		{
			GL11.glPushMatrix();
			GL11.glScaled(.8, .8, .8);
			GL11.glTranslated(0, -.425 * (1 - progress), -.125);
			GL11.glRotated(90, 1, 0, 0);
			mc.getRenderItem().renderItem(te.inventory.getStackInSlot(1).copy(), TransformType.GROUND);
			GL11.glPopMatrix();
		}
		
		GL11.glPopMatrix();
	}
}