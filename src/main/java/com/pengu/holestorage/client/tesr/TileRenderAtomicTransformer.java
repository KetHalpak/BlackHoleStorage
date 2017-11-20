package com.pengu.holestorage.client.tesr;

import java.math.BigInteger;
import java.util.Random;
import java.util.function.Function;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.TexturePixelGetter;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.color.InterpolationUtil;
import com.pengu.hammercore.common.InterItemStack;
import com.pengu.hammercore.utils.ColorHelper;
import com.pengu.holestorage.tile.TileAtomicTransformer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class TileRenderAtomicTransformer extends TESR<TileAtomicTransformer>
{
	private static ItemStack renderStack1 = ItemStack.EMPTY;
	private static ItemStack renderStack2 = ItemStack.EMPTY;
	private static float progress;
	private static final Random rand = new Random();
	
	private static final Function<Integer, Integer> RAY_GET = i ->
	{
		int[] colors1 = TexturePixelGetter.getAllColors(renderStack1);
		int[] colors2 = TexturePixelGetter.getAllColors(renderStack2);
		
		int a = colors1[rand.nextInt(colors1.length)];
		int b = colors2[rand.nextInt(colors2.length)];
		
		a = 0x11 << 24 | ColorHelper.packRGB(ColorHelper.getRed(a), ColorHelper.getGreen(a), ColorHelper.getBlue(a));
		b = 0x11 << 24 | ColorHelper.packRGB(ColorHelper.getRed(b), ColorHelper.getGreen(b), ColorHelper.getBlue(b));
		
		return InterpolationUtil.interpolate(a, b, progress);
	};
	
	@Override
	public void renderTileEntityAt(TileAtomicTransformer te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(x + .5, y + .3, z + .5);
		Minecraft mc = Minecraft.getMinecraft();
		
		double progress = 0;
		
		renderStack1 = ItemStack.EMPTY;
		renderStack2 = ItemStack.EMPTY;
		
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
				
				TileRenderAtomicTransformer.progress = (float) (progress = stored / needed);
				
				renderStack1 = te.getStackInSlot(0);
				renderStack2 = te.recipe.getOutputItem();
				rand.setSeed(te.getPos().toLong());
				GL11.glPushMatrix();
				GL11.glTranslated(0, .2, 0);
				GL11.glScaled(8, 8, 8);
				RenderUtil.renderColorfulLightRayEffects(0, 0, 0, RAY_GET, te.getPos().toLong(), (te.ticksExisted + partialTicks) / 1000F, 1, 2F, 60, 30);
				GL11.glPopMatrix();
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