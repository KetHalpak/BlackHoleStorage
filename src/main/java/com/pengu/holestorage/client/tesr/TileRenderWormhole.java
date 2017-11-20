package com.pengu.holestorage.client.tesr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.holestorage.InfoBHS;
import com.pengu.holestorage.client.entity.EntityWormhole;
import com.pengu.holestorage.configs.BHSConfigs;
import com.pengu.holestorage.init.BlocksBHS;
import com.pengu.holestorage.proxy.ClientProxy;
import com.pengu.holestorage.tile.TileWormhole;

public class TileRenderWormhole extends TESR<TileWormhole>
{
	private static final ResourceLocation wormhole = new ResourceLocation(InfoBHS.MOD_ID, "textures/wormhole.png");
	private final EnumFacing[] side = new EnumFacing[1];
	
	private static Minecraft mc = Minecraft.getMinecraft();
	public static RenderGlobal wormholeGlobalRenderer = new RenderGlobal(mc);
	private int quality = BHSConfigs.client_wormholeQuality;
	private long renderEndNanoTime;
	
	private static Map<EntityWormhole, Integer> registerWormholes = new ConcurrentHashMap<EntityWormhole, Integer>();
	private static List<Integer> pendingRemoval = Collections.synchronizedList(new ArrayList<Integer>());
	
	@Override
	public void renderTileEntityAt(TileWormhole te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		IBlockState state = te.getWorld().getBlockState(te.getPos());
		if(state.getBlock() == BlocksBHS.WORMHOLE)
		{
			side[0] = state.getValue(InfoBHS.FACING_UDEWSN);
			GL11.glPushMatrix();
			GL11.glTranslated(x, y, z);
			
			if(side[0] == EnumFacing.UP)
				GL11.glTranslated(.15, -.6, .15);
			if(side[0] == EnumFacing.DOWN)
				GL11.glTranslated(.15, .9, .15);
			if(side[0] == EnumFacing.NORTH)
				GL11.glTranslated(.15, .15, .9);
			if(side[0] == EnumFacing.SOUTH)
				GL11.glTranslated(.15, .15, -.6);
			if(side[0] == EnumFacing.WEST)
				GL11.glTranslated(.9, .15, .15);
			if(side[0] == EnumFacing.EAST)
				GL11.glTranslated(-.6, .15, .15);
			
			GL11.glScaled(.707, .707, .707);
			HammerCore.renderProxy.getRenderHelper().renderEndPortalEffect(0, 0, 0, wormhole, side);
			GL11.glPopMatrix();
		}
		
		// disable see-through via see-through
		if(TileEntityRendererDispatcher.instance.entity instanceof EntityWormhole)
			return;
		
		if(!registerWormholes.containsKey(te.getWormholeEntity()))
		{
			int newTextureId = GL11.glGenTextures();
			GlStateManager.bindTexture(newTextureId);
			int quality = BHSConfigs.client_wormholeQuality;
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, quality, quality, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, BufferUtils.createByteBuffer(3 * quality * quality));
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			registerWormholes.put(te.getWormholeEntity(), newTextureId);
			return;
		}
		
		EntityWormhole wormhole = te.getWormholeEntityRemote();
		
		if(wormhole == null)
			return;
		
		wormhole.rendering = true;
		
		EnumFacing facing = te.getWorld().getBlockState(te.getPos()).getValue(InfoBHS.FACING_UDEWSN).getOpposite();
		GlStateManager.pushMatrix();
		if(registerWormholes.get(wormhole) != null)
		{
			GlStateManager.disableLighting();
			GlStateManager.bindTexture(registerWormholes.get(wormhole).intValue());
			GlStateManager.translate(x, y, z);
			GlStateManager.rotate(-90F * facing.getHorizontalIndex() + 180F, 0, 1, 0);
			
			if(side[0] == EnumFacing.DOWN)
			{
				GlStateManager.translate(.505, .5, -1);
				GlStateManager.rotate(90, 1, 0, 0);
			}
			
			if(side[0] == EnumFacing.UP)
			{
				GlStateManager.translate(.5, .5, 0);
				GlStateManager.rotate(-90, 1, 0, 0);
			}
			
			if(side[0] == EnumFacing.NORTH)
				GL11.glTranslated(-.5, 0, -.5);
			if(side[0] == EnumFacing.SOUTH)
				GL11.glTranslated(.5, 0, .5);
			if(side[0] == EnumFacing.WEST)
				GL11.glTranslated(.5, 0, -.5);
			if(side[0] == EnumFacing.EAST)
				GL11.glTranslated(-.5, 0, .5);
			
			GlStateManager.translate(-0.41F, .09F, -0.391F);
			GlStateManager.scale(.819, .819, 1);
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glTexCoord2d(1, 0);
				GL11.glVertex3d(0.0625, 0.0625, 0);
				GL11.glTexCoord2d(0, 0);
				GL11.glVertex3d(0.9375, 0.0625, 0);
				GL11.glTexCoord2d(0, 1);
				GL11.glVertex3d(0.9375, 0.9375, 0);
				GL11.glTexCoord2d(1, 1);
				GL11.glVertex3d(0.0625, 0.9375, 0);
			}
			GL11.glEnd();
			GlStateManager.enableLighting();
		}
		GlStateManager.popMatrix();
		GlStateManager.enableLighting();
	}
	
	public static void removeRegisteredWormhole(Entity entity)
	{
		pendingRemoval.add(registerWormholes.get(entity));
		registerWormholes.remove(entity);
	}
	
	public static void clearRegisteredWormholes()
	{
		registerWormholes.clear();
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.RenderTickEvent event)
	{
		if(event.phase.equals(TickEvent.Phase.END))
			return;
		
		if(BHSConfigs.client_seeThroughtWormholes == 0)
			return;
		
		if(!pendingRemoval.isEmpty())
		{
			for(Integer integer : pendingRemoval)
				GlStateManager.deleteTexture(integer.intValue());
			pendingRemoval.clear();
		}
		
		if(mc.inGameHasFocus)
		{
			for(EntityWormhole entity : registerWormholes.keySet())
			{
				if(entity == null)
				{
					registerWormholes.remove(entity);
					continue;
				}
				
				if(!entity.rendering)
					continue;
				
				GameSettings settings = mc.gameSettings;
				RenderGlobal renderBackup = mc.renderGlobal;
				Entity entityBackup = mc.getRenderViewEntity();
				int thirdPersonBackup = settings.thirdPersonView;
				boolean hideGuiBackup = settings.hideGUI;
				int mipmapBackup = settings.mipmapLevels;
				float fovBackup = settings.fovSetting;
				int widthBackup = mc.displayWidth;
				int heightBackup = mc.displayHeight;
				
				mc.renderGlobal = wormholeGlobalRenderer;
				mc.setRenderViewEntity(entity);
				settings.fovSetting = BHSConfigs.client_wormholeFov;
				settings.thirdPersonView = 0;
				settings.hideGUI = true;
				settings.mipmapLevels = 3;
				mc.displayWidth = quality;
				mc.displayHeight = quality;
				
				ClientProxy.rendering = true;
				ClientProxy.renderEntity = mc.player;
				
				int fps = BHSConfigs.client_seeThroughtWormholes;
				EntityRenderer entityRenderer = mc.entityRenderer;
				entityRenderer.renderWorld(event.renderTickTime, renderEndNanoTime + (1000000000 / fps));
				
				GlStateManager.bindTexture(registerWormholes.get(entity).intValue());
				GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 0, 0, quality, quality, 0);
				
				renderEndNanoTime = System.nanoTime();
				
				ClientProxy.renderEntity = null;
				ClientProxy.rendering = false;
				
				mc.renderGlobal = renderBackup;
				mc.setRenderViewEntity(entityBackup);
				settings.fovSetting = fovBackup;
				settings.thirdPersonView = thirdPersonBackup;
				settings.hideGUI = hideGuiBackup;
				settings.mipmapLevels = mipmapBackup;
				mc.displayWidth = widthBackup;
				mc.displayHeight = heightBackup;
				
				entity.rendering = false;
			}
		}
	}
}