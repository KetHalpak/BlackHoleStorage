package com.pengu.holestorage.client.tesr;

import static com.pengu.holestorage.client.shaders.BHSShaders.blackHole;
import static com.pengu.holestorage.client.shaders.BHSShaders.useShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.shader.ShaderProgram;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.holestorage.init.BlocksBHS;
import com.pengu.holestorage.init.ItemsBHS;
import com.pengu.holestorage.items.ItemUnobtainable;
import com.pengu.holestorage.tile.TileBlackHole;

public class TileRenderBlackHole extends TESR<TileBlackHole>
{
	private ItemStack renderStack = ItemStack.EMPTY, shieldStack = ItemStack.EMPTY;
	
	private void makeStack()
	{
		renderStack = new ItemStack(BlocksBHS.BLACK_HOLE);
		shieldStack = new ItemStack(ItemsBHS.ITEM_UNOBTAINABLE, 1, ItemUnobtainable.black_hole_shield);
	}
	
	@Override
	public void renderTileEntityAt(TileBlackHole te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		GL11.glPushMatrix();
		
		if(renderStack == null || renderStack.isEmpty() || renderStack.getItem() != Item.getItemFromBlock(BlocksBHS.BLACK_HOLE))
			makeStack();
		if(shieldStack == null || shieldStack.isEmpty() || shieldStack.getItem() != ItemsBHS.ITEM_UNOBTAINABLE)
			makeStack();
		
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		
		RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
		
		double scale = 5.7 + Math.sqrt(te.additionalMass);
		
		double blackHoleRotationSpeed = (1 - te.currentShieldLevel);
		
		GL11.glTranslated(.5, .55 + -0.2 * scale, .5);
		GL11.glRotated((System.currentTimeMillis() / 500D % 10000D) * -360 * (1 - te.currentShieldLevel + .005F), 0, 1, 0);
		GL11.glScaled(scale, scale, scale);
		
		itemRender.renderItem(renderStack, TransformType.GROUND);
		
		GL11.glPopMatrix();
		
		if(!useShaders())
			return; // if we dont use shaders, dont render anything
			
		scale = scale * 1.06 * 1.15 + Math.sin(System.currentTimeMillis() / 25000D) * .1D;
		
		GL11.glTranslated(x + .5, y + .47, z + .5);
		
		double offsetConst = .125;
		GL11.glPushMatrix();
		
		// GL11.glRotated((System.currentTimeMillis() / 25000D % 10000D) * 90,
		// 0, 1, 0);
		
		GL11.glScaled(scale, scale, scale);
		GL11.glTranslated(offsetConst, offsetConst / -2D, offsetConst);
		
		blackHole.freeBindShader();
		int loc = blackHole.getUniformLoc("shield");
		ARBShaderObjects.glUniform1fARB(loc, te.currentShieldLevel);
		itemRender.renderItem(shieldStack, TransformType.GROUND);
		ShaderProgram.unbindShader();
		
		GL11.glPopMatrix();
		
		GL11.glPopMatrix();
	}
}