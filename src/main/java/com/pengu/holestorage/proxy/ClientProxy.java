package com.pengu.holestorage.proxy;

import java.util.HashSet;
import java.util.Set;

import com.pengu.hammercore.client.render.item.ItemRenderingHandler;
import com.pengu.hammercore.proxy.ParticleProxy_Client;
import com.pengu.holestorage.InfoBHS;
import com.pengu.holestorage.client.Render3D;
import com.pengu.holestorage.client.particle.ParticleEnergyFX;
import com.pengu.holestorage.client.particle.ParticleLiquid;
import com.pengu.holestorage.client.particle.ParticleLiquidTextured;
import com.pengu.holestorage.client.render.item.ItemRenderAntiMatter;
import com.pengu.holestorage.client.tesr.TileRenderAtomicTransformer;
import com.pengu.holestorage.client.tesr.TileRenderBlackHole;
import com.pengu.holestorage.client.tesr.TileRenderBlackHoleFormer;
import com.pengu.holestorage.client.tesr.TileRenderWormhole;
import com.pengu.holestorage.client.tesr.TileRenderWormholeFormer;
import com.pengu.holestorage.init.ItemsBHS;
import com.pengu.holestorage.tile.TileAtomicTransformer;
import com.pengu.holestorage.tile.TileBlackHole;
import com.pengu.holestorage.tile.TileBlackHoleFormer;
import com.pengu.holestorage.tile.TileWormhole;
import com.pengu.holestorage.tile.TileWormholeFormer;
import com.pengu.holestorage.vortex.Vortex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy
{
	public static boolean rendering = false;
	public static Entity renderEntity = null;
	public static Entity backupEntity = null;
	
	public static Set<Vortex> particleVortex = new HashSet<>();
	
	@Override
	public void init()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileBlackHoleFormer.class, new TileRenderBlackHoleFormer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAtomicTransformer.class, new TileRenderAtomicTransformer());
		{
			TileRenderWormhole w;
			ClientRegistry.bindTileEntitySpecialRenderer(TileWormhole.class, w = new TileRenderWormhole());
			MinecraftForge.EVENT_BUS.register(w);
		}
		ClientRegistry.bindTileEntitySpecialRenderer(TileWormholeFormer.class, new TileRenderWormholeFormer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileBlackHole.class, new TileRenderBlackHole());
	}
	
	@SubscribeEvent
	public void onClientWorldLoad(WorldEvent.Load event)
	{
		if(event.getWorld() instanceof WorldClient)
			TileRenderWormhole.wormholeGlobalRenderer.setWorldAndLoadRenderers((WorldClient) event.getWorld());
	}
	
	@SubscribeEvent
	public void onClientWorldUnload(WorldEvent.Unload event)
	{
		if(event.getWorld() instanceof WorldClient)
			TileRenderWormhole.clearRegisteredWormholes();
	}
	
	@SubscribeEvent
	public void onPrePlayerRender(RenderPlayerEvent.Pre event)
	{
		if(!rendering)
			return;
		
		if(event.getEntityPlayer() == renderEntity)
		{
			this.backupEntity = Minecraft.getMinecraft().getRenderManager().renderViewEntity;
			Minecraft.getMinecraft().getRenderManager().renderViewEntity = renderEntity;
		}
	}
	
	@SubscribeEvent
	public void onPostPlayerRender(RenderPlayerEvent.Post event)
	{
		if(!rendering)
			return;
		
		if(event.getEntityPlayer() == renderEntity)
		{
			Minecraft.getMinecraft().getRenderManager().renderViewEntity = backupEntity;
			renderEntity = null;
		}
	}
	
	@Override
	public void preInit()
	{
		OBJLoader.INSTANCE.addDomain(InfoBHS.MOD_ID);
		MinecraftForge.EVENT_BUS.register(new Render3D());
		ItemRenderingHandler.INSTANCE.setItemRender(ItemsBHS.ANTI_MATTER, new ItemRenderAntiMatter());
	}
	
	@Override
	public void spawnEnergyFX(World w, double x, double y, double z, double tx, double ty, double tz, int rf)
	{
		if(w == null)
			return;
		if(w.isRemote)
			new ParticleEnergyFX(Minecraft.getMinecraft().world, x, y, z, tx, ty, tz, rf).spawn();
		else
			super.spawnEnergyFX(w, x, y, z, tx, ty, tz, rf);
	}
	
	@Override
	public void spawnLiquidFX(World w, double x, double y, double z, double tx, double ty, double tz, int count, int color, float scale, int extend)
	{
		if(w == null)
			return;
		if(w.isRemote)
			ParticleProxy_Client.queueParticleSpawn(new ParticleLiquid(w, x, y, z, tx, ty, tz, count, color, scale, extend));
		else
			super.spawnLiquidFX(w, x, y, z, tx, ty, tz, count, color, scale, extend);
	}
	
	@Override
	public void spawnLiquidTexturedFX(World w, double x, double y, double z, double tx, double ty, double tz, int count, FluidStack stack, float scale, int extend)
	{
		if(w == null)
			return;
		if(w.isRemote)
			ParticleProxy_Client.queueParticleSpawn(new ParticleLiquidTextured(w, x, y, z, tx, ty, tz, count, stack, scale, extend));
		else
			super.spawnLiquidTexturedFX(w, x, y, z, tx, ty, tz, count, stack, scale, extend);
	}
	
	@Override
	public void addParticleVortex(Vortex vortex)
	{
		if(vortex == null || vortex.getVortexStrenght() == 0 || particleVortex.contains(vortex))
			return;
		Set particleVortex = new HashSet<>(this.particleVortex);
		particleVortex.add(vortex);
		this.particleVortex = particleVortex;
	}
	
	@Override
	public void removeParticleVortex(Vortex vortex)
	{
		if(vortex == null || vortex.getVortexStrenght() == 0 || !particleVortex.contains(vortex))
			return;
		Set particleVortex = new HashSet<>(this.particleVortex);
		particleVortex.remove(vortex);
		this.particleVortex = particleVortex;
	}
}