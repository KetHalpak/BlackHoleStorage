package com.pengu.holestorage.client.tesr;

import java.math.BigInteger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.common.InterItemStack;
import com.pengu.holestorage.tile.TileAtomicTransformer;

public class TileRenderAtomicTransformer extends TESR<TileAtomicTransformer>
{
	@Override
	public void renderTileEntityAt(TileAtomicTransformer te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(x + .5, y + .3, z + .5);
		Minecraft mc = Minecraft.getMinecraft();
		
		double progress = 0;
		
		if(te.recipe != null)
			b:
			{
				BigInteger max = te.recipe.getEnergyUsed(te.getStackInSlot(0));
				BigInteger stor = te.stored;
				
				double stored = te.stored.doubleValue();
				double needed = te.recipe.getEnergyUsed(te.inventory.getStackInSlot(0)).doubleValue();
				
				if(!Double.isFinite(stored))
					stored = Double.MAX_VALUE;
				if(!Double.isFinite(needed))
					needed = Double.MAX_VALUE;
				if(needed == 0)
					break b;
				
				progress = stored / needed;
			}
		
		if(!InterItemStack.isStackNull(te.getStackInSlot(0)))
		{
			GL11.glPushMatrix();
			GL11.glScaled(1 - progress, 1 - progress, 1 - progress);
			mc.getRenderItem().renderItem(te.getStackInSlot(0).copy(), TransformType.GROUND);
			GL11.glPopMatrix();
		}
		
		if(te.recipe != null)
		{
			ItemStack out = te.recipe.getOutputItem();
			if(!InterItemStack.isStackNull(out))
			{
				GL11.glPushMatrix();
				GL11.glScaled(progress, progress, progress);
				mc.getRenderItem().renderItem(out, TransformType.GROUND);
				GL11.glPopMatrix();
			}
		}
		
		GL11.glPopMatrix();
	}
}