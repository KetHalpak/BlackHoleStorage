package com.pengu.holestorage.proxy;

import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.pengu.hammercore.net.HCNetwork;
import com.pengu.holestorage.net.PacketSpawnEnergyFX;
import com.pengu.holestorage.net.PacketSpawnLiquidFX;
import com.pengu.holestorage.net.PacketSpawnLiquidTexturedFX;
import com.pengu.holestorage.vortex.Vortex;

public class CommonProxy
{
	public void preInit()
	{
	}
	
	public void init()
	{
	}
	
	public void spawnEnergyFX(World w, double x, double y, double z, double tx, double ty, double tz, int rf)
	{
		TargetPoint tp = new TargetPoint(w.provider.getDimension(), x, y, z, 64D);
		HCNetwork.manager.sendToAllAround(new PacketSpawnEnergyFX(w.provider.getDimension(), x, y, z, tx, ty, tz, rf), tp);
	}
	
	public void spawnLiquidFX(World w, double x, double y, double z, double tx, double ty, double tz, int count, int color, float scale, int extend)
	{
		TargetPoint tp = new TargetPoint(w.provider.getDimension(), x, y, z, 64D);
		HCNetwork.manager.sendToAllAround(new PacketSpawnLiquidFX(w.provider.getDimension(), x, y, z, tx, ty, tz, count, color, scale, extend), tp);
	}
	
	public void spawnLiquidTexturedFX(World w, double x, double y, double z, double tx, double ty, double tz, int count, FluidStack stack, float scale, int extend)
	{
		TargetPoint tp = new TargetPoint(w.provider.getDimension(), x, y, z, 64D);
		HCNetwork.manager.sendToAllAround(new PacketSpawnLiquidTexturedFX(w.provider.getDimension(), x, y, z, tx, ty, tz, count, stack, scale, extend), tp);
	}
	
	public void addParticleVortex(Vortex vortex)
	{
		
	}
	
	public void removeParticleVortex(Vortex vortex)
	{
		
	}
}