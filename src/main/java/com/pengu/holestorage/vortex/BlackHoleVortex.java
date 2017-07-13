package com.pengu.holestorage.vortex;

import java.util.List;

import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.AxisAlignedBB;

import com.pengu.hammercore.math.MathHelper;
import com.pengu.hammercore.proxy.ParticleProxy_Client;
import com.pengu.holestorage.BlackHoleStorage;
import com.pengu.holestorage.client.Render3D;
import com.pengu.holestorage.client.particle.ParticleEnergyFX;
import com.pengu.holestorage.client.particle.ParticleGeneric;
import com.pengu.holestorage.client.particle.ParticleLiquid;
import com.pengu.holestorage.client.particle.ParticleLiquidTextured;
import com.pengu.holestorage.tile.TileBlackHole;

public class BlackHoleVortex extends Vortex
{
	public final TileBlackHole tile;
	private final AxisAlignedBB tileAB;
	
	public BlackHoleVortex(TileBlackHole tile)
	{
		super(tile.getPos().getX() + .5, tile.getPos().getY() + .5, tile.getPos().getZ() + .5, 8, false);
		tileAB = null;
		this.tile = tile;
	}
	
	@Override
	public void update()
	{
		if(tile == null || tile.getWorld().getTileEntity(tile.getPos()) != tile)
		{
			BlackHoleStorage.proxy.removeParticleVortex(this);
			return;
		}
		
		boolean isSlower = !tile.canPull;
		radius = isSlower ? 16 : tile.radius;
		
		x = tile.getPos().getX() + .5;
		y = tile.getPos().getY() + .5;
		z = tile.getPos().getZ() + .5;
		
		AxisAlignedBB dieAABB = new AxisAlignedBB(x - .25, y - .25, z - .25, x + .25, y + .25, z + .25);
		List<Particle> particles = Render3D.getParticlesInRange(getBoundingBox());
		for(Particle p : particles)
		{
			if(p instanceof ParticleLiquid || p instanceof ParticleLiquidTextured || p instanceof ParticleEnergyFX || p instanceof ParticleGeneric)
				continue;
			
			double mx = ParticleProxy_Client.getParticleMotionX(p);
			double my = ParticleProxy_Client.getParticleMotionY(p);
			double mz = ParticleProxy_Client.getParticleMotionZ(p);
			
			double px = ParticleProxy_Client.getParticlePosX(p);
			double py = ParticleProxy_Client.getParticlePosY(p);
			double pz = ParticleProxy_Client.getParticlePosZ(p);
			
			double div = isSlower ? 128D : 32D;
			mx += MathHelper.clip((x - px), -1, 1) / div;
			my += MathHelper.clip((y - py), -1, 1) / div;
			mz += MathHelper.clip((z - pz), -1, 1) / div;
			
			ParticleProxy_Client.setParticleMotionX(p, mx);
			ParticleProxy_Client.setParticleMotionY(p, my);
			ParticleProxy_Client.setParticleMotionZ(p, mz);
			
			double distSq = (tile.getPos().getX() - px + .5) * (tile.getPos().getX() - px + .5) + (tile.getPos().getY() - py + .5) * (tile.getPos().getY() - py + .5) + (tile.getPos().getZ() - pz + .5) * (tile.getPos().getZ() - pz + .5);
			if(distSq < Math.sqrt(tile.additionalMass) + Math.sqrt(tile.additionalMass) - 8)
				p.setExpired();
		}
	}
}