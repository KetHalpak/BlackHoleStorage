package com.pengu.holestorage.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.particle.api.ParticleList;
import com.pengu.holestorage.init.ItemsBHS;
import com.pengu.holestorage.proxy.ClientProxy;
import com.pengu.holestorage.vortex.Vortex;

@SideOnly(Side.CLIENT)
public class Render3D
{
	@SubscribeEvent
	public void drawWorld(RenderWorldLastEvent evt)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		if(player.getHeldItem(EnumHand.MAIN_HAND).getItem() == ItemsBHS.WORMHOLE_PEARL)
		{
			ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
			NBTTagCompound nbt = stack.getTagCompound();
			if(ItemsBHS.WORMHOLE_PEARL.hasEffect(stack))
			{
				BlockPos bound = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
				GlStateManager.disableDepth();
				renderBlockOverlay(Block.FULL_BLOCK_AABB, bound, 1, evt.getPartialTicks());
				GlStateManager.enableDepth();
			}
		}
	}
	
	@SubscribeEvent
	public void tickParticle(ClientTickEvent evt)
	{
		for(Vortex vortex : ClientProxy.particleVortex)
			vortex.update();
	}
	
	public static void renderBlockOverlay(AxisAlignedBB aabb, BlockPos pos, float lineWidthMultiplier, float partialTicks)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		GL11.glPushMatrix();
		GL11.glTranslated(pos.getX(), pos.getY(), pos.getZ());
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.glLineWidth(lineWidthMultiplier);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		
		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
		RenderGlobal.drawSelectionBoundingBox(aabb.offset(-d0, -d1, -d2), 1.0F, 1.0F, 1.0F, 0.4F);
		
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GL11.glPopMatrix();
	}
}