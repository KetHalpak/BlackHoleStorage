package com.pengu.holestorage.client.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.holestorage.init.BlocksBHS;
import com.pengu.holestorage.tile.TileBlackHoleFormer;

public class TileRenderBlackHoleFormer extends TESR<TileBlackHoleFormer>
{
	@Override
	public void renderTileEntityAt(TileBlackHoleFormer te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		double progress = te.EnergyStored.divide(te.ABSORBED).doubleValue();
		double const1 = .5174425935;
		double lift = progress >= const1 ? ((progress - const1) / (1 - const1)) * 1.199 : 0;
		double max = 1.7;
		progress *= max;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + .5, y + .65 - progress / max / 1.5 + lift, z + .5);
		GL11.glScaled(progress, progress, progress);
		Minecraft mc = Minecraft.getMinecraft();
		
		mc.getRenderItem().renderItem(new ItemStack(BlocksBHS.BLACK_HOLE), TransformType.GROUND);
		
		GL11.glPopMatrix();
	}
}