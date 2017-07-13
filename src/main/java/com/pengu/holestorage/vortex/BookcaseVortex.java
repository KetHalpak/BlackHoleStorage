package com.pengu.holestorage.vortex;

import java.util.List;

import net.minecraft.client.particle.Particle;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.hammercore.math.MathHelper;
import com.pengu.hammercore.proxy.ParticleProxy_Client;
import com.pengu.holestorage.BlackHoleStorage;
import com.pengu.holestorage.client.Render3D;

public class BookcaseVortex extends Vortex
{
	public BlockPos pos;
	public World world;
	
	public int ticksExisted = 0;
	
	public BookcaseVortex(World world, BlockPos pos)
	{
		super(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, 16, false);
		this.pos = pos;
		this.world = world;
	}
	
	@Override
	public void update()
	{
		if(pos == null || world.getBlockState(pos).getBlock() != Blocks.BOOKSHELF)
		{
			BlackHoleStorage.proxy.removeParticleVortex(this);
			return;
		}
		
		if(world.rand.nextInt(100) < 25)
		{
			double x = pos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * 8;
			double y = pos.getY() + (world.rand.nextDouble() - world.rand.nextDouble()) * 8;
			double z = pos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * 8;
			
			double mx = (pos.getX() - x + .5) / 8D;
			double my = (pos.getY() - y + .5) / 8D;
			double mz = (pos.getZ() - z + .5) / 8D;
			
			world.spawnParticle(EnumParticleTypes.END_ROD, x, y, z, mx, my, mz);
		}
		
		radius = 16;
		
		AxisAlignedBB dieAABB = new AxisAlignedBB(pos.getX() + .1, pos.getY() + .1, pos.getZ() + .1, pos.getX() + .9, pos.getY() + .9, pos.getZ() + .9);
		
		List<Particle> particles = Render3D.getParticlesInRange(getBoundingBox());
		for(Particle p : particles)
		{
			double mx = ParticleProxy_Client.getParticleMotionX(p);
			double my = ParticleProxy_Client.getParticleMotionY(p);
			double mz = ParticleProxy_Client.getParticleMotionZ(p);
			
			double px = ParticleProxy_Client.getParticlePosX(p);
			double py = ParticleProxy_Client.getParticlePosY(p);
			double pz = ParticleProxy_Client.getParticlePosZ(p);
			
			mx += MathHelper.clip((x - px), -1, 1) / 16D;
			my += MathHelper.clip((y - py), -1, 1) / 16D;
			mz += MathHelper.clip((z - pz), -1, 1) / 16D;
			
			ParticleProxy_Client.setParticleMotionX(p, mx);
			ParticleProxy_Client.setParticleMotionY(p, my);
			ParticleProxy_Client.setParticleMotionZ(p, mz);
			
			if(p.getBoundingBox().intersects(dieAABB))
				p.setExpired();
		}
	}
}